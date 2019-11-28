<template>
  <div>
    <el-container>
      <el-aside width="200px" class="slide-box" :style="sideBoxStyle">
        <div class="logo-box">
          <i class="iconfont icon-logo"></i>
        </div>
        <div class="menu-box" :style="menuBoxStyle">
          <el-menu
            :default-active="this.$store.state.navDefaultActive"
            class="el-menu-vertical-demo ul-box"
            background-color="#545c64"
            text-color="#fff"
            active-text-color="#ffd04b"
          >
            <span v-for="(item,key) in menuList" :key="key">
              <el-menu-item
                v-if="item.child.length ==0"
                :index="''+key"
                @click="navActive(item,key)"
              >
                <i class="iconfont icon-box" :class="item.icon"></i>
                <span slot="title">{{item.title}}</span>
              </el-menu-item>
              <el-submenu :index="''+key" v-if="item.child.length >0">
                <template slot="title">
                  <i class="iconfont icon-box" :class="item.icon"></i>
                  <span>{{item.title}}</span>
                </template>
                <el-menu-item
                  :index="key+'-'+key1"
                  v-for="(item1,key1) in item.child"
                  @click="navActive1(key,item1,key1)"
                  :key="key1"
                >{{item1.title}}</el-menu-item>
              </el-submenu>
            </span>
          </el-menu>
        </div>
      </el-aside>
      <el-container>
        <el-header class="header-box">
          <div class="system-box">掌上科普后台管理系统</div>

          <div class="system-mech">
            <div style="float: right">
              <span class="userName">{{userName}}</span>
              <el-button type="danger" plain size="mini" @click="outLogin">注销登录</el-button>
            </div>
          </div>
        </el-header>
        <el-main :style="mainBoxStyle">
          <div class="main">
            <router-view v-if="isRouterActive"></router-view>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import menu from "@/commen/menu";
export default {
  name: "home",
  components: {},
  data() {
    return {
      userName: this.getUserName(),

      showActive: false,
      showActive1: true,
      tableData: [],
      orgName: "",
      isRouterActive: true,
      menuList: [],
      mainBoxStyle: {
        background: "#f0f2f5",
        height: parseInt(document.documentElement.clientHeight) + "px"
      },
      sideBoxStyle: {
        height: parseInt(document.documentElement.clientHeight) + "px"
      },
      menuBoxStyle: {
        height: parseInt(document.documentElement.clientHeight) - 70 + "px"
      }
    };
  },
  methods: {
    getUserName() {
      let userData = localStorage.getItem("loginUser");
      userData = this.$util.tryParseJson(userData);
      return userData.name;
    },

    //事件层
    //一级菜单选中事件
    navActive(item, key) {
      this.$store.state.navDefaultActive = "" + key;
      this.$router.push(item.path);
    },
    //二级菜单选中事件
    navActive1(key, item1, key1) {
      this.$store.state.navDefaultActive = key + "-" + key1;
      this.$router.push(item1.path);
    },
    outLogin() {
      localStorage.clear();
      this.$router.push("/login");
    }
  },
  mounted() {
    const loading = this.$loading({
      lock: true,
      text: "拼命加载中...",
      spinner: "el-icon-loading"
    });
    let loginUser = JSON.parse(localStorage.getItem("loginUser"));
	if (loginUser.authority == 4) {
	  this.menuList = menu.supermenu;
	}else if (loginUser.authority == 3) {
      this.menuList = menu.menu;
    } else if (loginUser.authority == 2) {
      this.menuList = menu.insiderMenu;
    } else if (loginUser.authority == 1) {
      this.menuList = menu.userMenu;
    }
    this.$router.push("/contentList");
    this.showActive = true;
    this.$store.state.navDefaultActive = "0-0";
    loading.close();
  }
};
</script>

<style scoped lang="scss">
.main {
  width: 100%;
  background: #fff;
}
.slide-box {
  background: #545c64;
  overflow: hidden;
}
.menu-box {
  width: 215px;
  overflow-y: auto;
}

.logo-box {
  width: 200px;
  height: 60px;
  text-align: center;
  line-height: 60px;
  font-size: 40px;
  color: #fff;
}

.logo-box i {
  width: 200px;
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 40px;
}

.header-box {
  background: #545c64;
  font-size: 1.8rem;
  line-height: 60px;
  color: #fff;
}

.icon-box {
  display: block;
  float: left;
  width: 2rem;
  height: 56px;
  margin-right: 10px;
  font-size: 2rem;
}

.system-box {
  float: left;
  width: 250px;
  height: 60px;
  letter-spacing: 5px;
  font-weight: 600;
  text-align: center;
}

.system-mech {
  position: relative;
  width: auto;
  height: 60px;
  margin-left: 250px;
  letter-spacing: 5px;
  font-weight: 600;
  text-align: center;
}

.system-center {
  position: absolute;
  width: 300px;
  height: 60px;
  left: 50%;
  margin-left: -250px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.system-right {
  position: absolute;
  right: 100px;
  cursor: pointer;
}

.system-right span {
  color: #fff;
  border: 1px solid #ddd;
  background: rgba(255, 255, 255, 0.3);
}

.userName {
  font-size: 14px;
  padding: 6px;
}
</style>
