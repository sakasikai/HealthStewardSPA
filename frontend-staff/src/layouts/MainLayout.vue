<template>
	<q-layout view="hHh lpR fFf">
		<q-header elevated class="bg-grey-10 text-white" height-hint="98">
			<q-tabs v-model="mainTab" class="shadow-2">
				<q-route-tab
					class="col-md-1"
					to="/index"
					label="首页"
					name="首页"
				/>
				
				<q-route-tab
					class="col-md-1"
					to="/layouts/health-steward-staff"
					label="健康管家"
				/>
				<q-space />


				<q-btn
					v-if="!$store.state.user.isLogin"
					class="col-md-1 q-mr-sm"
					label="注册账号"
					@click="signup_staff"
				/>
				<q-btn
					v-if="!$store.state.user.isLogin"
					class="col-md-1 q-mr-sm"
					label="登录"
					@click="login"
				/>
				<div v-else>
					<q-btn flat no-wrap>
						<!-- <q-avatar rounded>
						<img :src="imageUrl" />
						</q-avatar> -->
						欢迎您&nbsp;~&nbsp;&nbsp;{{ nickname }}
						<q-menu
							transition-show="jump-down"
							transition-hide="jump-up"
							fit
						>
							<div class="q-pa-md text-center">
								<font class="text-grey-9 text-bold"
									>用户名：{{ username }}</font
								>
							</div>
							<q-separator />
							<q-list style="min-width:140px">
								<q-item clickable>
									<q-item-section class="row no-wrap">
										<font
											class="text-center text-grey-9 text-bold text-grey-9"
											>昵称：{{ nickname }}</font
										></q-item-section
									>
								</q-item>
								<q-item clickable>
									<q-item-section
										class="text-center text-bold text-grey-9"
										@click="changeStaffInfo"
										>详细信息</q-item-section
									>
								</q-item>
								<q-item clickable @click="logout">
									<q-item-section
										class="text-center text-red-8 text-bold"
										>注销</q-item-section
									>
								</q-item>
							</q-list>
						</q-menu>
					</q-btn>
				</div>
			</q-tabs>
		</q-header>

		<q-page-container>
			<staff-login-dialog></staff-login-dialog>
			<router-view />
		</q-page-container>
	</q-layout>
</template>

<script>
import StaffInfoDialog from "components/staff/StaffInfoDialog.vue";
import StaffLoginDialog from "components/staff/StaffLoginDialog.vue";
import SignupStaffDialog from "components/staff/SignupStaffDialog.vue";


export default {
	name: "MainLayout",
	components: { StaffLoginDialog },
	data() {
		return {
			mainTab: "首页"
		};
	},
	computed: {
		label() {
			if (this.$store.state.user.isLogin) {
				return "注销";
			} else return "登录";
		},
		username() {
			return this.$store.state.user.userInfo.userName;
		},
		nickname() {
			return this.$store.state.user.userInfo.nickName;
		}
		// imageUrl: function() {
		// 	return "api/staff/" + this.userInfo.userName + "/image/get";
		// }
	},
	methods: {
		changeStaffInfo() {
			if (this.$store.state.user.isLogin) {
				this.$q.dialog({
					component: StaffInfoDialog,
					parent: this,
					username: localStorage.getItem("userName"),
					userSelf: true
				});
			} else {
				this.$store.commit("user/openLoginForm");
			}
		},
		login() {
			this.$store.commit("user/openLoginForm");
		},
		logout() {
			this.$q.notify({
				type: "positive",
				message: "注销成功",
				timeout: 1500
			});
			this.$store.dispatch("user/userLogout").then(() => {
				if (this.$route.path != "/index") {
					this.$router.push("/");
				}
			});
		},
		signup_staff(){
			this.$q.dialog({
					component: SignupStaffDialog,
					parent: this
				});
		}
	}
};
</script>
