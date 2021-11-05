<template>
  <q-dialog ref="signupDialog">
    <q-card flat bordered class="signupCard">
      <q-card-section>
        <div class="text-h6 q-pa-lg q-gutter-md" color="purple">
          注册
        </div>

        <q-form @submit="onSubmit" class="q-gutter-md">
          <q-input
            filled
            v-model="model.userName"
            label="设置账号"
            hint="一经注册便不可更改"
            color="purple"
            debounce="300"
            :rules="[
              val => (val && val.length >= 4) || '长度至少为4',
              checkUsernameExists,
              val =>
                val.match(/^(\w|[\u4E00-\u9FA5])*$/) ||
                '账号只允许为英文，数字,下划线和汉字的组合'
            ]"
          >
            <template v-slot:append>
              <q-icon v-show="userCheckState" name="done" color="green" />
            </template>
          </q-input>
          <q-input
            filled
            v-model="model.nickName"
            label="输入昵称"
            hint="个性化称呼"
            color="purple"
            lazy-rules
            :rules="[
              val => (val && val.length > 0) || '昵称不能为空！',
              val =>
                val.match(/^(\w|[\u4E00-\u9FA5])*$/) ||
                '昵称只允许为英文，数字,下划线和汉字的组合'
            ]"
          />

          <q-input
            v-model="model.password"
            filled
            label="输入密码"
            :type="isPwd ? 'password' : 'text'"
            hint="切换密码可见性"
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

          <q-input
            v-model="password2"
            filled
            label="确认密码"
            :type="isPwd2 ? 'password' : 'text'"
            hint="切换密码可见性"
            debounce="500"
            :rules="[val => val === model.password || '请输入相同密码']"
          >
            <template v-slot:append>
              <q-icon
                :name="isPwd2 ? 'visibility_off' : 'visibility'"
                class="cursor-pointer"
                @click="isPwd2 = !isPwd2"
              />
            </template>
          </q-input>

          <div class="q-gutter-sm" align="right">
            <q-btn
              label="注册"
              type="submit"
              color="purple"
              :loading="onSubmitLoading"
            >
              <template v-slot:loading>
                <q-spinner-hourglass />
              </template>
            </q-btn>
            <q-btn flat label="取消" color="purple" @click="closeSignupForm" />
          </div>
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
export default {
  data() {
    return {
      model: {
        userName: null,
        nickName: null,
        password: null
      },
      myOnSignupDialog: null,
      isServerValidating: true,
      userNameValidated: true,
      isPwd: true,
      isPwd2: true,
      password2: null, // 再次输入的密码
      onSubmitLoading: false, //提交申请时指示loading...
      userCheckState: false //检查用户名
    };
  },
  methods: {
    show() {
			this.$refs.signupDialog.show();
			this.myOnSignupDialog = true;
		},

		hide() {
			this.$refs.signupDialog.hide();
			this.myOnSignupDialog = false;
		},
    validateUserName() {
      // 调用server API 进行验证, http.get(/validateUserName, model.userName)
      // success: 通行
      // erroe: 要求重新输入
    },
    onSubmit() {
      this.onSubmitLoading = true;
      this.$axios
        .post("api/signup/staff", this.model)
        .then(res => {
          if (res.status === 201) {
            // 存储userName在前端
            localStorage.setItem("userName", this.model.userName);
            // 自动登录
            this.$store.dispatch("user/asyncSetUserInfo", res.data);
            this.closeSignupForm(); // 登录成功后自动关闭SignupForm

            this.onSubmitLoading = false;
            this.$q.notify({
              message: "注册成功",
              color: "green",
              type: "positive",
              position: "top"
            });
          } else {
            // do nothing
          }
        })
        .catch(err => {
          this.onSubmitLoading = false;
          this.$q.notify({
            message: "注册失败",
            color: "red",
            type: "negative",
            position: "top"
          });
          console.log(err);
        });
    },
    closeSignupForm() {
      this.hide();

    },
    checkUsernameExists(username) {
      if (this.username) return true;
      return new Promise((resolve, reject) => {
        this.userCheckState = false;
        this.$axios
          .get("api/checkExists/" + this.model.userName)
          .then(response => {
            resolve(true);
            this.userCheckState = true;
          })
          .catch(error => {
            resolve("用户名已存在！");
          });
      });
    }
  }
};
</script>

<style>
.signupCard {
  width: 500px;
}
</style>
