package xhj.cn.start;

import io.vertx.core.Vertx;
import zyxhj.cms.service.AppraiseService;
import zyxhj.cms.service.ChannelService;
import zyxhj.cms.service.ChannelTagService;
import zyxhj.cms.service.ContentService;
import zyxhj.cms.service.ContentTagService;
import zyxhj.cms.service.ReplyService;
import zyxhj.core.controller.TestController;
import zyxhj.utils.Singleton;
import zyxhj.utils.ZeroVerticle;
import zyxhj.zskp.service.SmsServer;
import zyxhj.zskp.service.ZskpOtherContent;
import zyxhj.zskp.service.ZskpUserService;

public class ZSKPVerticle extends ZeroVerticle {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new ZSKPVerticle());
	}

	public String name() {
		return "zskp";
	}

	public int port() {
		return 8067;
	}

	protected void init() throws Exception {


		initCtrl(ctrlMap, Singleton.ins(AppraiseService.class, "appraise"));
		
		initCtrl(ctrlMap, Singleton.ins(ChannelService.class, "channel"));
		initCtrl(ctrlMap, Singleton.ins(ChannelTagService.class, "channelTag"));
		
		initCtrl(ctrlMap, Singleton.ins(ContentService.class, "content"));
		initCtrl(ctrlMap, Singleton.ins(ContentTagService.class, "contentTag"));

		initCtrl(ctrlMap, Singleton.ins(ReplyService.class, "reply"));
		
		initCtrl(ctrlMap, Singleton.ins(ZskpOtherContent.class, "ZskpOtherContent"));
		initCtrl(ctrlMap, Singleton.ins(ZskpUserService.class, "ZskpUserService"));
		initCtrl(ctrlMap, Singleton.ins(SmsServer.class, "sms"));
	}

}
