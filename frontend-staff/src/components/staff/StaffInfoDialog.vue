<template>
  <q-dialog ref="staffInfoDialog">
    <q-card style="max-width:80vw">
      <q-card-section class="bg-grey-3">
        <div class="text-weight-bold">个人资料</div>
      </q-card-section>

      <q-form spellcheck="false" @submit="onSubmit">
        <q-card-section horizontal>
          <q-card-section class="q-gutter-lg">
            <q-card horizontal class="row no-wrap">
              <q-card-section class="col-4 bg-grey-2">账号</q-card-section>
              <q-input
                class="q-pl-md q-pb-none my-input-width"
                borderless
                v-model="usernameInput"
                type="text"
                readonly
              />
            </q-card>

            <q-card horizontal class="row no-wrap">
              <q-card-section class="col-4 bg-grey-2">昵称</q-card-section>
              <q-input
                class="q-px-md q-pb-none my-input-width"
                borderless
                v-model="nickNameInput"
                type="text"
                :rules="[
									val =>
										(val && val.length > 0) ||
										'昵称不能为空！'
								]"
              />
            </q-card>

            <q-card horizontal class="row no-wrap">
              <q-card-section class="col-4 bg-grey-2">年龄</q-card-section>
              <q-input
                borderless
                class="q-px-md q-pb-none my-input-width"
                v-model="ageInput"
                type="text"
                :rules="[
									val =>
										val == null ||
										val.length == 0 ||
										(Number.isSafeInteger(
											parseFloat(val)
										) &&
											val > 0) ||
										'请输入正确的年龄'
								]"
              />
            </q-card>

            <q-card horizontal class="row no-wrap">
              <q-card-section class="col-4 bg-grey-2">性别</q-card-section>
              <q-select
                class="col-8 q-pl-md q-pb-none"
                borderless
                bg-color="white"
                v-model="genderInput"
                :options="genderOptions"
                option-value="value"
                option-label="label"
                filled
                emit-value
                map-options
              />
            </q-card>

            <q-card horizontal class="row no-wrap">
              <q-card-section class="col-4 bg-grey-2">手机</q-card-section>
              <q-input
                class="q-px-md q-pb-none my-input-width"
                borderless
                v-model="phoneInput"
                type="tel"
                mask="### #### ####"
                unmasked-value
                :rules="[
									val =>
										val == null ||
										val.length == 0 ||
										(Number.isSafeInteger(parseInt(val)) &&
											val > 0) ||
										'请输入正确的手机号',
									val => val == null  ||
										val.length == 0 || val.length == 11 || '手机号应为11位'
								]"
              />
            </q-card>
          </q-card-section>

          <q-card-section class="q-gutter-lg">
            <div class="row no-wrap items-center q-mt-none">
              <q-card horizontal class="row no-wrap q-mr-md">
                <q-card-section class="col-4 bg-grey-2">简介</q-card-section>
                <q-input
                  class="q-px-md q-pb-none my-input-width"
                  borderless
                  v-model="resumeInput"
                  type="textarea"
                />
              </q-card>

              <!-- 头像组件 -->
              <image-uploader
                class="q-mt-md"
                :uploadUrl="imageUploadUrl"
                :previewUrl="imagePreviewUrl"
              ></image-uploader>
            </div>

            <q-card horizontal class="row no-wrap q-mt-sm">
              <q-card-section class="col-4 bg-grey-2">员工类型</q-card-section>
              <q-input
                class="q-px-md q-pb-none my-input-width"
                borderless
                v-model="typeInput"
                type="text"
                :readonly="true"
              >
                <q-tooltip v-if="true">您不可编辑该项</q-tooltip>
              </q-input>
            </q-card>

            <q-card horizontal class="row no-wrap">
              <q-card-section class="col-4 bg-grey-2">职务</q-card-section>
              <q-input
                class="q-px-md q-pb-none my-input-width"
                borderless
                v-model="jobInput"
                type="text"
                :readonly="true"
              >
                <q-tooltip v-if="true">您不可编辑该项</q-tooltip>
              </q-input>
            </q-card>

            <q-card horizontal class="row no-wrap">
              <q-card-section class="col-4 bg-grey-2">职称</q-card-section>
              <q-input
                class="q-px-md q-pb-none my-input-width"
                borderless
                v-model="postInput"
                type="text"
                :readonly="true"
              >
                <q-tooltip v-if="true">您不可编辑该项</q-tooltip>
              </q-input>
            </q-card>
          </q-card-section>
        </q-card-section>

        <q-card-actions align="right" class="q-gutter-md">
          <q-btn v-if="userSelf" label="修改密码" @click="changePassword" />

          <q-btn label="提交" type="submit" color="primary" :loading="submitting" />
          <q-btn label="取消" color="primary" flat class="q-ml-sm" @click="hide" />
        </q-card-actions>
      </q-form>
    </q-card>
  </q-dialog>
</template>

<script>
import StaffPasswordDialog from "./StaffPasswordDialog.vue";
import ImageUploader from "./ImageUploader.vue";
export default {
  name: "StaffInfoDialog",
  components: { ImageUploader },
  props: {
    username: { type: String, required: true },
    //staffInfo: { type: Object, required: true },
    userSelf: { type: Boolean, default: false } //是否是用户自己，用户自己才能改密码
  },
  data() {
    return {
      usernameInput: null,
      nickNameInput: null,
      ageInput: null,
      genderInput: null,
      genderOptions: [
        { value: 1, label: "男" },
        { value: 2, label: "女" }
      ],
      phoneInput: null,

      typeInput: null,
      jobInput: null, //职务
      postInput: null, //职称
      hireDateInput: null,
      employeeIdInput: null,
      resumeInput: null, //简介
      remarkInput: null, //备注

      submitting: false
    };
  },
  created() {
    this.$axios
      .get("/api/staff/" + this.username + "/get/staffInfo")
      .then(response => {
        let staffInfo = response.data;
        this.usernameInput = staffInfo.username;
        this.nickNameInput = staffInfo.nickname;
        this.ageInput = staffInfo.age;
        this.genderInput = staffInfo.gender;
        this.phoneInput = staffInfo.phone;
        this.typeInput = staffInfo.type;
        this.jobInput = staffInfo.job;
        this.postInput = staffInfo.post;
        this.hireDateInput = staffInfo.hireDate;
        this.employeeIdInput = staffInfo.employeeId;
        this.resumeInput = staffInfo.resume;
        this.remarkInput = staffInfo.remark;
      });
  },
  computed: {
    imageUploadUrl() {
      return "api/staff/" + this.usernameInput + "/image/update";
    },
    imagePreviewUrl() {
      return "api/staff/" + this.usernameInput + "/image/get";
    }
  },
  methods: {
    show() {
      this.$refs.staffInfoDialog.show();
    },

    hide() {
      this.$refs.staffInfoDialog.hide();
    },
    onSubmit() {
      this.submitting = true;
      const url = "api/staff/" + this.usernameInput + "/update";
      const data = {
        staffId: null,
        username: this.usernameInput,
        nickname: this.nickNameInput,
        age: this.ageInput,
        gender: this.genderInput,
        type: this.typeInput,
        hireDate: null,
        job: this.jobInput,
        post: this.postInput, //职称
        phone: this.phoneInput,
        employeeId: null,
        resume: this.resumeInput,
        remark: this.remarkInput
      };
      this.$axios
        .post(url, data)
        .then(response => {
          this.$q.notify({
            type: "positive",
            message: "更新成功！",
            timeout: "1500"
          });
          this.submitting = false;
          this.hide();
        })
        .catch(error => {
          this.submitting = false;
          this.$q.notify({
            type: "negative",
            message: "更新失败!",
            timeout: "1500"
          });
        });
    },
    changePassword() {
      this.$q.dialog({
        component: StaffPasswordDialog,
        parent: this,
        username: this.usernameInput
      });
    }
  }
};
</script>

<style scoped>
.my-input-width {
  width: 300px;
}
</style>
