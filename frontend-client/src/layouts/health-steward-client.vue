<template>
  <div class="q-pt-md row justify-center">
    <div class="q-pa-sm col-xl-9 col-lg-9 col-md-10 col-sm-12 col-xs-12">
      <q-stepper
        v-model="step"
        v-show="!isLoadingStepper"
        ref="stepper"
        color="primary"
        animated
        class="q-py-md q-gutter-col-md"
      >
        <q-step
          :name="1"
          title="业务介绍"
          icon="settings"
          :done="step > 1"
          style="min-height: 200px"
        >
          <p>业务介绍！</p>
        </q-step>

        <q-step
          :name="2"
          title="提交申请"
          icon="create_new_folder"
          :done="step > 2"
          style="min-height: 300px"
        >
          <apply-note @updateNoteValue="updateNote" />
        </q-step>

        <q-step
          :name="3"
          title="等待医生预约洽谈"
          icon="assignment"
          :done="step > 3"
          style="min-height: 200px"
        >
          预计等待时间:1天
        </q-step>

        <q-step
          :name="4"
          title="确认签约"
          icon="add_comment"
          :done="step > 4"
          style="min-height: 200px"
        >
          <p class="text-grey">
            请尽快联系
            <font class="text-bold text-dark">{{ staffInfo.nickName }}</font>
            医生并完成约谈，仔细协商后再做出选择！
          </p>
          <p class="text-grey">
            医生的联系方式：<font class="text-bold text-dark">{{
              staffInfo.phone
            }}</font>
          </p>
        </q-step>

        <q-step
          :name="5"
          title="等待医生确认签约"
          icon="add_comment"
          :done="step > 5"
          style="min-height: 200px"
        >
          <p class="text-grey">感谢您选择我们的健康管家服务</p>
        </q-step>

        <q-step
          :name="6"
          title="查看申请结果"
          icon="add_comment"
          :done="step > 6"
          style="min-height: 200px"
          class="row justify-center"
        >
          <q-card v-show="!isLoadingStep6" style="width:600px">
            <q-card-section
              class="bg-light-blue-1 row no-wrap justify-center content-center"
            >
              <q-icon
                size="md"
                name="eva-checkmark-circle-2"
                color="green-10"
                class="q-pr-sm"
              />
              <div class="text-teal-8 text-subtitle1">
                您已于
                <font class="text-bold text-teal-8">{{
                  contractResultTime
                }}</font>
                完成了与
                <font class="text-bold text-teal-8">{{
                  staffInfo.nickName
                }}</font>
                医生(工号:<font class="text-bold text-teal-8">{{
                  staffInfo.userName
                }}</font
                >)的签约
              </div>
            </q-card-section>
          </q-card>
          <q-inner-loading :showing="isLoadingStep6">
            <q-spinner-gears size="50px" color="primary" />
          </q-inner-loading>
        </q-step>

        <template v-slot:navigation>
          <q-stepper-navigation>
            <q-btn
              v-if="step === 1 || step === 2"
              @click="goFoward"
              color="primary"
              :label="step === 1 ? '提出申请' : '提交申请'"
            />

            <q-btn
              v-if="step === 2"
              flat
              color="primary"
              @click="goBack"
              label="上一步"
              class="q-ml-sm"
            />

            <div v-if="step === 4" class="q-gutter-sm">
              <q-btn @click="goFoward" color="primary" label="确认签约" />
              <q-btn @click="rejectCurrentApply" color="red" label="拒绝签约" />
            </div>
          </q-stepper-navigation>
        </template>

        <template v-slot:message>
          <q-banner v-if="step === 1" class="bg-purple-8 text-white q-px-lg">
            签约前请向工作人员仔细了解"健康管家业务"
          </q-banner>
          <q-banner
            v-else-if="step === 2"
            class="bg-orange-8 text-white q-px-lg"
          >
            提交后会创建一个申请项
          </q-banner>
          <q-banner
            v-else-if="step === 3"
            class="bg-green-8 text-white q-px-lg"
          >
            等待医生选择您的申请。。。
          </q-banner>
          <q-banner
            v-else-if="step === 4"
            class="bg-orange-8 text-white q-px-lg"
          >
            已有医生选择了您的申请
          </q-banner>
          <q-banner
            v-else-if="step === 5"
            class="bg-green-8 text-white q-px-lg"
          >
            等待<font class="text-subtitle1 text-bold">{{
              staffInfo.nickName
            }}</font
            >医生确认。。。
          </q-banner>
          <q-banner
            v-else-if="step === 6"
            class="bg-green-8 text-white q-px-lg"
          >
            恭喜您！您已完成健康管家签约程序！
          </q-banner>
          <q-banner v-else class="bg-green-8 text-white q-px-lg">
            请等待...
          </q-banner>
        </template>
      </q-stepper>

      <q-inner-loading :showing="isLoadingStepper">
        <q-spinner color="primary" size="50px" />
      </q-inner-loading>
    </div>
  </div>
</template>

<script>
import ApplyNote from "components/health-steward-tools/apply-note";
import { date, QSpinnerCube } from "quasar";

export default {
  data() {
    return {
      step: -1, // q-step\navigation-slot\message-slot 的指针， 从1开始(开始界面), -1是等待页面
      mountedOnExecuted: false, // 驱动client的step3,5的一个入口，为true代表了首次加载则step为3或5的时候，这个入口会触发watcher; 另一个入口是step的watcher
      loading1: false,
      applyNote: null, // 客户申请说明
      applyItem: {
        timeStamp: null,
        applyNote: null,
        flowLevel: null // 0-5
      },
      staffInfo: {
        userId: null,
        userName: null,
        nickName: null,
        gender: null,
        job: null,
        post: null,
        phone: null,
        resume: null
      },
      timer: null,
      contractResultTime: null,
      isLoadingStepper: true,
      isLoadingStep6: true
    };
  },
  components: {
    ApplyNote,
    QSpinnerCube
  },
  async created() {
    // 初始化 step 和 applyItem.flowLevel
    let url =
      "/api/" +
      localStorage.getItem("userName") +
      "/HealthSteward/ApplyFlowLevel";

    await this.$axios
      .get(url)
      .then(res => {
        this.$store.commit("healthSteward/setFlowLevel", res.data);
      })
      .catch(err => {
        console.log(err);
      });

    this.step = this.level + 1;
    this.$refs.stepper.goTo(this.step);

    this.isLoadingStepper = false;

    if (this.step !== 3 && this.step !== 5) {
      // JS作用域的缘故，只能这样触发下面的setTimeOut函数
      return;
    }

    setTimeout(() => {
      this.mountedOnExecuted = true;
    }, 1000);
  },
  methods: {
    async goFoward() {
      // 只有step:1,2,4时使用， step:3,5为等待医生操作界面，由监听事件驱动
      if (this.step === 2) {
        // 创建一个 apply item
        this.applyItem = {
          timeStamp: date.formatDate(Date.now(), "YYYY-MM-DD-HH:mm:ss"),
          applyNote:
            this.applyNote != null && this.applyNote != ""
              ? this.applyNote
              : "请详细填写...",
          flowLevel: this.level
        };

        let url =
          "/api/" +
          localStorage.getItem("userName") +
          "/HealthSteward/ApplyItem";
        var backUpTransactionTriggered = false;

        await this.$axios.post(url, this.applyItem).catch(err => {
          console.log(err);

          backUpTransactionTriggered = true;

          this.$q
            .dialog({
              title: "<div color='red'>申请失败!</div>",
              message: "您还有与医生的合同关联未删除<br>请尽快联系相关医生",
              html: true,
              color: "primary",
              progress: {
                spinner: QSpinnerCube,
                color: "primary"
              },
              cancel: {
                flat: true
              },
              ok: {
                flat: true
              },
              focus: "none"
            })
            .onDismiss(() => {
              this.$store.commit("healthSteward/setFlowLevel", 0);
              this.step = this.level + 1;
              this.$refs.stepper.goTo(this.step);
            });
        });

        if (backUpTransactionTriggered) {
          return;
        }
      }

      if (this.step === 4) {
        // 确认签约按钮触发！
        let url =
          "/api/" +
          localStorage.getItem("userName") +
          "/HealthSteward/" +
          this.staffInfo.userName +
          "/ClientContractConfirm?num=1";
        await this.$axios.put(url);
      }

      this.$store.commit("healthSteward/setFlowLevel", this.level + 1);
      this.$refs.stepper.next();
    },
    goBack() {
      this.$store.commit("healthSteward/setFlowLevel", this.level - 1);
      this.$refs.stepper.previous();
    },
    async rejectCurrentApply() {
      var inBackUpFlow = false;
      // 备选事件流
      this.$q
        .dialog({
          title: "<div class='text-h5 text-orange-9'>警告</div>",
          message: "您是否想彻底退出申请流程？",
          html: true,
          color: "primary",
          ok: {
            flat: true
          },
          cancel: {
            flat: true
          },
          focus: "cancel"
        })
        .onOk(async () => {
          // 客户拒绝签约，进入备选事件流
          inBackUpFlow = true;
          let url =
            "/api/" +
            localStorage.getItem("userName") +
            "/HealthSteward/" +
            this.staffInfo.userName +
            "/ClientContractConfirm?num=0";

          await this.$axios.put(url);

          this.$store.commit("healthSteward/setFlowLevel", 0);
          this.step = this.level + 1;
          this.$refs.stepper.goTo(this.step);
        })
        .onDismiss(() => {
          if (!inBackUpFlow) {
            return;
          }
          this.$store.commit("healthSteward/setFlowLevel", 0);
          this.step = this.level + 1;
          this.$refs.stepper.goTo(this.step);
        });
    },
    updateNote(val) {
      // step 2; updateNoteValue event
      this.applyNote = val;
    }
  },
  computed: {
    level: function() {
      return this.$store.state.healthSteward.flowLevel;
    }
  },
  watch: {
    step: async function(val, oldVal) {
      if (oldVal != -1 && (val === 3 || val === 5)) {
        // 间断询问后端
        if (this.timer) {
          clearInterval(this.timer);
        }

        this.timer = setInterval(async () => {
          let url = "/api/";
          if (val === 3) {
            url += localStorage.getItem("userName") + "/HealthSteward/TalkLink";
          } else {
            url +=
              localStorage.getItem("userName") +
              "/HealthSteward/staff/" +
              this.staffInfo.userName +
              "/StaffContractConfirm";
          }

          await this.$axios
            .get(url)
            .then(res => {
              if (val === 3 && res.data === true) {
                // 有医生选择了
                this.$store.commit("healthSteward/setFlowLevel", 4);
                this.$refs.stepper.next();
                clearInterval(this.timer);
              }

              if (val === 5 && res.data === 1) {
                // 医生确认签约，进行到下一流程
                this.$store.commit("healthSteward/setFlowLevel", 6);
                this.$refs.stepper.next();
                clearInterval(this.timer);
              }

              if (val === 5 && res.data === 0) {
                // 医生拒绝了，进入备选事件流
                this.$q
                  .dialog({
                    title: "<div color='text-h5 text-red-9'>申请流程失败</div>",
                    message: "医生拒绝与您签约",
                    html: true,
                    progress: {
                      spinner: QSpinnerCube,
                      color: "primary"
                    },
                    cancel: {
                      flat: true
                    },
                    ok: {
                      flat: true
                    },
                    focus: "none"
                  })
                  .onDismiss(() => {
                    this.$store.commit("healthSteward/setFlowLevel", 0);
                    this.step = this.level + 1;
                    this.$refs.stepper.goTo(this.step);
                  });
                clearInterval(this.timer);
              }
            })
            .catch(err => {
              console.log(err);
            });
        }, 5000);
      }

      // staff的详细信息
      if (
        val >= 4 &&
        (this.staffInfo === null ||
          (this.staffInfo != null && this.staffInfo.userId === null))
      ) {
        let res = await this.$axios.get(
          "/api/" +
            localStorage.getItem("userName") +
            "/HealthSteward/StaffInfo"
        );
        this.staffInfo = res.data;
      }

      // 最后签约时间
      if (val === 6 && this.contractResultTime === null) {
        let res = await this.$axios.get(
          "/api/" +
            localStorage.getItem("userName") +
            "/HealthSteward/ContractResultTime"
        );
        this.contractResultTime = res.data;
        setTimeout(() => {
          this.isLoadingStep6 = false;
        }, 5);
      }
    },
    mountedOnExecuted: function(val, oldVal) {
      if (this.timer) {
        clearInterval(this.timer);
      }

      this.timer = setInterval(async () => {
        let url = "/api/";
        if (this.step === 3) {
          url += localStorage.getItem("userName") + "/HealthSteward/TalkLink";
        } else {
          url +=
            localStorage.getItem("userName") +
            "/HealthSteward/staff/" +
            this.staffInfo.userName +
            "/StaffContractConfirm";
        }

        await this.$axios
          .get(url)
          .then(res => {
            if (this.step === 3 && res.data === true) {
              // 有医生选择了
              this.$store.commit("healthSteward/setFlowLevel", 4);
              this.$refs.stepper.next();
              clearInterval(this.timer);
            }

            if (this.step === 5 && res.data === 1) {
              // 医生确认签约，进行到下一流程
              this.$store.commit("healthSteward/setFlowLevel", 6);
              this.$refs.stepper.next();
              clearInterval(this.timer);
            }

            if (this.step === 5 && res.data === 0) {
              // 医生拒绝了，进入备选事件流
              this.$q
                .dialog({
                  title: "<div color='text-h5 text-red-9'>申请流程失败</div>",
                  message: "医生拒绝与您签约",
                  html: true,
                  progress: {
                    spinner: QSpinnerCube,
                    color: "primary"
                  },
                  cancel: {
                    flat: true
                  },
                  ok: {
                    flat: true
                  },
                  focus: "none"
                })
                .onDismiss(() => {
                  this.$store.commit("healthSteward/setFlowLevel", 0);
                  this.step = this.level + 1;
                  this.$refs.stepper.goTo(this.step);
                });
              clearInterval(this.timer);
            }
          })
          .catch(err => {
            console.log(err);
          });
      }, 5000);
    }
  },
  destroyed() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  }
};
</script>
