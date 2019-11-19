package zyxhj.zskp.service;
import io.netty.util.internal.StringUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerFileUpload;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
 
 
/**
 * Windows 版本
 * Created by sweet on 17-8-31.
 */
public class UploadVerticleForWindows extends AbstractVerticle {
 
  private static final String rootPath = "C:/xiaoniu_doc/fileserver-version-3/vertx-timer/";
 
  private static final String url = "http://127.0.0.1:7778/file?path=";
  private static final Logger log = LogManager.getLogger(UploadVerticleForWindows.class);
 
  private FileSystem fileSystem;
//  public static void main(String[] args) {
//	    Runner.runExample(UploadVerticleForWindows.class);
//	 }

 
  @Override
  public void start(Future<Void> startFuture) throws Exception {
 
    fileSystem = vertx.fileSystem();
 
    Future<Boolean> initFuture = Future.future();
 
    initFuture.setHandler(init -> {
      if (init.succeeded() && init.result()) {
        Future uploadFuture = Future.future();
        Future downloadFuture = Future.future();
 
        upload(uploadFuture);
        download(downloadFuture);
 
        CompositeFuture.all(uploadFuture, downloadFuture).setHandler(server -> {
          if (server.succeeded()) {
            log.debug("server 全部部署成功");
            startFuture.complete();
          } else {
            server.cause().printStackTrace();
            log.error(server.cause().getMessage());
            startFuture.fail(server.cause());
          }
        });
      } else {
        init.cause().printStackTrace();
        log.error(init.cause().getMessage());
        startFuture.fail(init.cause());
      }
    });
  }
 
  private void saveFile(HttpServerFileUpload upload, String savePath,
                        String name, String uuid, String nameDb, String time, String urlPath) {
    upload.streamToFileSystem(savePath).endHandler(end -> {
      long size = upload.size();
      System.out.println("size : " + size + " bytes");
      String type = type(name);
 
      JsonObject json = new JsonObject()
              .put("id", uuid)
              .put("name", nameDb)
              .put("realName", upload.filename())
              .put("path", savePath)
              .put("type", type)
              .put("createTime", time)
              .put("url", urlPath)
              .put("size", size);
 
      System.out.println(Json.encodePrettily(json));
 
    });
  }
 
 
  private void upload(Future uploadFuture) {
    vertx.createHttpServer().requestHandler(req -> {
      req.setExpectMultipart(true);
      req.exceptionHandler(th -> {
        th.printStackTrace();
        log.error(th.getLocalizedMessage());
        respError(req, 500, th.getLocalizedMessage());
      });
 
      HttpMethod method = req.method();
      String reqPath = req.path();
      System.out.println("method: " + method + " , reqPath: " + reqPath);
      if (method.equals(HttpMethod.POST) && "/api/fileUpload".equals(reqPath)) {
 
        String name = req.getParam("name"); // 默认 yyyy/MM/dd 文件夹位置 name=xxxx.jpg
        String path = req.getParam("path"); // 带 path 路径 path=wang/xue/hello.jpg
        String fileSavePath = rootPath + dir();
        System.out.println("name: " + name + " path >> " + fileSavePath);
 
        String time = time(); // 当前时间戳
        String uuid = uuid(); //  UUID 做 key
 
        if (!StringUtil.isNullOrEmpty(name)) { // 处理 name 参数 上传文件
          req.uploadHandler(upload -> {
            if (!checkPath(fileSavePath))
              fileSystem.mkdirsBlocking(fileSavePath);
 
            System.out.println("fileName : " + upload.filename());
            String nameDb = time + "-" + name;
            String savePath = fileSavePath + nameDb;
            String urlPath = url + uuid;
            System.out.println("path ==> " + savePath);
 
            saveFile(upload, savePath, name, uuid, nameDb, time, urlPath);
            resp(req, new JsonObject().put("url", urlPath));
          });
        } else if (!StringUtil.isNullOrEmpty(path)) { // 处理 path 路径 文件上传
          String[] pathArray = path(path);
          // 需要检查 该路径是否存在，不存在创建(并检查是否存在 ..) TODO 测试 6级文件夹深度
          String pathItem = pathArray[0];     // 参数传入的 路径
          String reqFileName = pathArray[1]; // 参数传入的 fileName
 
          if (pathItem.indexOf(".") > 0) {
            log.error("非法路径 >> " + path);
            respError(req, 404, "");
          } else {
            String savePath = rootPath + pathItem;
            if (!checkPath(savePath))
              fileSystem.mkdirsBlocking(savePath);
            req.uploadHandler(upload -> {
              System.out.println("fileName : " + upload.filename());
              String nameDb = time + "-" + reqFileName;
              String urlPath = url + uuid;
              String savePathAndFileName = savePath + nameDb;
              System.out.println("path ==> " + savePathAndFileName);
              saveFile(upload, savePathAndFileName, nameDb, uuid, nameDb, time, urlPath);
              resp(req, new JsonObject().put("url", urlPath));
            });
          }
        } else {
          respError(req, 404, "");
        }
      } else {
        respError(req, 404, "");
      }
    }).listen(7777, res -> {
      if (res.succeeded()) {
        log.debug("Upload Server 7777");
        uploadFuture.complete();
      } else {
        res.cause().printStackTrace();
        log.error(res.cause().getMessage());
        uploadFuture.fail(res.cause());
      }
    });
  }
 
  private void download(Future downloadFuture) {
    vertx.createHttpServer().requestHandler(req -> {
 
      req.exceptionHandler(th -> {
        th.printStackTrace();
        log.error(th.getLocalizedMessage());
        respError(req, 500, th.getLocalizedMessage());
      });
 
      HttpMethod method = req.method();
      String urlPath = req.path();
      System.out.println("method: " + method + " , url: " + urlPath);
      if (method.equals(HttpMethod.GET) && "/file".equals(urlPath)) {
      } else {
        respError(req, 404, "路径不对");
      }
    }).listen(7778, res -> {
      if (res.succeeded()) {
        log.debug("Download Server 7778");
        downloadFuture.complete();
      } else {
        res.cause().printStackTrace();
        log.error(res.cause().getMessage());
        downloadFuture.fail(res.cause());
      }
    });
  }
 
  private static void resp(HttpServerRequest request, JsonObject ret) {
    request.response()
            .putHeader("content-type", "application/json;charset=utf-8")
            .putHeader("Access-Control-Allow-Origin", "*")
            .putHeader("Access-Control-Allow-Credentials", "true")
            .putHeader("Content-Disposition", "attachment")
            .end(Json.encodePrettily(ret));
    return;
  }
 
  private static void respError(HttpServerRequest request, int code, String error) {
    request.response()
            .putHeader("content-type", "application/json;charset=utf-8")
            .putHeader("Access-Control-Allow-Origin", "*")
            .putHeader("Access-Control-Allow-Credentials", "true")
            .putHeader("Content-Disposition", "attachment")
            .setStatusCode(code)
            .end(Json.encodePrettily(new JsonObject().put("error", error)));
    return;
  }
 
  public static void main(String[] args) {
	    String[] path = path("/xxxx/xxxx/111.jpg");
	    for (String s: path) {
	      System.out.println(s);
	    }
	 
	    boolean exists = Files.exists(Paths.get("C:\\xiaoniu_doc\\fileserver-version-3\\vertx-timer\\xxxx\\"));
	    System.out.println(exists);
	 
	    System.out.println(path[0].indexOf("."));
	    System.out.println("xxxxx/xxxx/xx.jpg".indexOf("."));
	  }
	 
	  /**
	   * 生成 2017/10/01 路径
	   * @return
	   */
	  public static String dir() {
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/");
	    return simpleDateFormat.format(new Date());
	  }
	 
	  /**
	   * 当前时间戳
	   * @return
	   */
	  public static String time() {
	    return System.currentTimeMillis()+"";
	  }
	 
	  /**
	   * 大写 uuid
	   * @return
	   */
	  public static String uuid() {
	    return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	  }
	 
	  /**
	   * 获取文件的类型
	   * @param name
	   * @return
	   */
	  public static String type(String name) {
	    return name.substring(name.lastIndexOf(".")+1, name.length());
	  }
	 
	  /**
	   * 处理文件 path /xxxx/xxxx/111.jpg
	   * @param path
	   * @return [0] 文件路径
	   *          [1] 文件名
	   *
	   */
	  public static String[] path(String path) {
	    String[] s = new String[3];
	 
	    int i = path.lastIndexOf("/");
	    String fileName = path.substring(i + 1, path.length());
	    s[0] = path.substring(0, i+1);
	    s[1] = fileName;
	 
	    return s;
	  }
	 
	  /**
	   * 路径存在 返回 true，如果不存在 返回 false
	   * @param path
	   * @return
	   */
	  public static boolean checkPath(String path) {
	    return Files.exists(Paths.get(path));
	  }

}