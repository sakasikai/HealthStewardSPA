<template>
  <q-layout view="hHr lpR fFf">
    <q-header elevated class="bg-light-blue-14 text-white" height-hint="98">
      <!-- 顶部工具栏 -->
      <q-toolbar class="bg-indigo-6 shadow-2">
        <q-toolbar-title class="row justify-start">
          <q-avatar>
            <img src="https://cdn.quasar.dev/logo/svg/quasar-logo.svg" />
          </q-avatar>
          <div class="q-px-xs q-pt-xs">
            客户端
          </div>
        </q-toolbar-title>

        <!-- 注册、登录、个人资料 -->
        <div class="q-gutter-sm">
          <q-btn
            flat
            color="white"
            label="注册"
            @click="openSignupForm"
            v-if="!$store.state.user.isLogin"
          />
          <q-btn
            flat
            color="white"
            label="登录"
            v-if="!$store.state.user.isLogin"
            @click="openLoginForm"
          />
          <q-btn dense flat no-wrap v-else>
            <q-avatar rounded>
              <img :src="imageUrl" />
            </q-avatar>
            <q-menu transition-show="jump-down" transition-hide="jump-up" fit>
              <div class="q-pa-md text-center">
                <font class="text-grey-9 text-bold"
                  >登录为：{{ userInfo.userName }}</font
                >
              </div>
              <q-separator />
              <q-list style="min-width:140px">
                <q-item clickable>
                  <q-item-section class="row no-wrap">
                    <font class="text-center text-grey-9 text-bold text-grey-9"
                      >昵称：{{ userInfo.nickName }}</font
                    ></q-item-section
                  >
                </q-item>
                <q-item clickable>
                  <q-item-section 
                    class="text-center text-bold text-grey-9"
                    @click="clientInfo"
                  >详细信息</q-item-section
                  >
                </q-item>
                <q-item clickable>
                  <q-item-section 
                    class="text-center text-bold text-grey-9"
                    @click="changePassword"
                  >修改密码</q-item-section
                  >
                </q-item>
                <q-item clickable @click="logout">
                  <q-item-section class="text-center text-red-8 text-bold"
                    >注销</q-item-section
                  >
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>
        </div>
      </q-toolbar>

      <!-- 头部导航栏 -->
      <client-home-tabs />
    </q-header>

    <!-- 注册框、登录框 -->
    <signup-card />
    <login-card />

    <q-page-container>
      <transition
        enter-active-class="animated fadeIn"
        leave-active-class="animated fadeOut"
        mode="out-in"
      >
        <router-view />
      </transition>
    </q-page-container>
  </q-layout>
</template>

<script>
import mapUrl from "../router/router-utils/map-target-url.js";
import ClientHomeTabs from "components/tabs/client-home-tabs";
import LoginCard from "components/auth/login-card";
import SignupCard from "components/auth/signup-card";
import ClientInfoDialog from "components/client/ClientInfoDialog"
import PasswordDialog from "components/client/PasswordDialog"

export default {
  components: {
    ClientHomeTabs,
    LoginCard,
    SignupCard,
    ClientInfoDialog
  },
  methods: {
    openLoginForm() {
      this.$store.commit("user/openLoginForm");
    },
    logout() {
      // vuex 里使用 router 的另一种解决办法：在vue实例里使用router
      this.$store.dispatch("user/userLogout").then(() => {
        if (this.$route.path != mapUrl("/")) {
          this.$router.push("/");
        }
      });
    },
    openSignupForm() {
      this.$store.commit("user/openSignupForm");
    },
    clientInfo () {
      this.$q.dialog({
        component: ClientInfoDialog,
        parent: this,
        userInfo: this.$store.state.user.userInfo,
        onlyShow: false
      })
    },
    changePassword() {
			this.$q.dialog({
				component: PasswordDialog,
				parent: this,
				username: localStorage.getItem('userName')
			});
		}
  },
  computed: {
    userInfo: function() {
      return this.$store.state.user.userInfo;
    },
    imageUrl: function() {
      return "api/client/image/get/" + this.userInfo.userId;
    },
  }
};
</script>
