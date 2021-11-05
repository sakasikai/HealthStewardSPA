<template>
  <!-- 登录框 -->
  <q-dialog v-model="$store.state.user.onLoginForm" persistent>
    <q-card bordered class="loginCard">
      <q-card-section class="items-center">
        <div class="text-h6 q-pa-lg q-gutter-md" color="purple">
          登录
        </div>
        <q-form @submit="onSubmit" class="q-gutter-md">
          <q-input
            filled
            v-model="model.userName"
            label="输入账号"
            hint="注册的邮箱/用户名"
            color="purple"
            lazy-rules
            :rules="[val => (val && val.length > 0) || '用户名不能为空！']"
          />
          <q-input
            v-model="model.password"
            filled
            label="输入密码"
            hint="切换密码可见性"
            :type="isPwd ? 'password' : 'text'"
            lazy-rules
            :rules="[val => (val && val.length > 0) || '密码不能为空！']"
          >
            <template v-slot:append>
              <q-icon
                :name="isPwd ? 'visibility_off' : 'visibility'"
                class="cursor-pointer"
                @click="isPwd = !isPwd"
              />
            </template>
          </q-input>

          <div class="justify-start q-gutter-sm">
            <q-btn
              label="登录"
              type="submit"
              color="primary"
              :loading="onSubmitLoading"
            >
              <template v-slot:loading>
                <q-spinner-hourglass />
              </template>
            </q-btn>
            <q-btn flat label="取消" color="primary" @click="closeLoginForm" />
          </div>
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import mapUrl from "../../router/router-utils/map-target-url.js";

export default {
  // name: 'ComponentName',
  data() {
    return {
      isPwd: true,
      password: null,
      onLogin: false,
      model: {
        userName: null,
        password: null
      },
      onSubmitLoading: false // true时指示loading...
    };
  },
  methods: {
    onSubmit() {
      this.onSubmitLoading = true;
      // 提交表单
      this.$axios
        .post("/api/login", this.model)
        .then(async res => {
          console.log(res);
          if (res.status === 200) {
            // 存储userName在前端
            localStorage.setItem("userName", this.model.userName);
            // 从后端拿到用户数据后，存储在前端
            await this.$store.dispatch("user/asyncSetUserInfo", res.data);
            this.closeLoginForm(); // 登录成功后自动关闭LoginForm

            this.onSubmitLoading = false;
            // 前进到目标页面, mapUrl用于“默认路由”的映射
            if (this.$route.path != mapUrl(this.$store.state.user.targetUrl))
              this.$router.push(this.$store.state.user.targetUrl);

            this.$q.notify({
              message: "登陆成功",
              color: "green",
              type: "positive",
              position: "top"
            });
          } else {
            this.onSubmitLoading = false;
            this.$q.notify({
              message: "登陆失败，请检查用户名和密码",
              color: "red",
              type: "negative",
              position: "top"
            });
          }
        })
        .catch(err => {
          console.log(err);
          this.onSubmitLoading = false;
          this.$q.notify({
            message: "登陆失败，请检查用户名和密码",
            color: "red",
            type: "negative",
            position: "top"
          });
          console.log(err);
        });
    },
    closeLoginForm() {
      this.$store.commit("user/closeLoginForm");
    }
  }
};
</script>

<style>
.loginCard {
  width: 400px;
}
</style>
