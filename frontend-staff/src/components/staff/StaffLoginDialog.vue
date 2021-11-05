<template>
	<q-dialog v-model="showDialog">
		<q-card bordered>
			<q-card-section>
				<div class="row q-mt-sm justify-center">
					<div class="text-h5">健康管家登录</div>
				</div>
			</q-card-section>
			<q-card-section>
				<q-form spellcheck="false" @submit="login" class="q-gutter-md">
					<div class="row q-my-md justify-center">
						<q-input
							class="my-input-width"
							filled
							v-model="username"
							label="用户名"
							:rules="[
								val =>
									(val !== null && val !== '') ||
									'用户名不能为空！'
							]"
						>
							<template v-slot:prepend>
								<q-icon name="person_outline" />
							</template>
						</q-input>
					</div>
					<div class="row q-my-md justify-center">
						<q-input
							class="my-input-width"
							v-model="password"
							filled
							:type="isPwd ? 'password' : 'text'"
							label="密码"
							:rules="[
								val =>
									(val !== null && val !== '') ||
									'密码不能为空！'
							]"
						>
							<template v-slot:prepend>
								<q-icon name="vpn_key" />
							</template>
							<template v-slot:append>
								<q-icon
									:name="
										isPwd ? 'visibility_off' : 'visibility'
									"
									class="cursor-pointer"
									@click="isPwd = !isPwd"
								/>
							</template>
						</q-input>
					</div>

					<div class="row q-my-md">
						<q-checkbox v-model="remember" label="记住密码" />
					</div>
					<div class="row q-my-md justify-evenly">
						<q-btn
							class="col-4"
							type="submit"
							color="white"
							text-color="black"
							label="登入"
							:loading="submitting"
						/>
						<q-btn
							class="col-4"
							label="取消"
							@click="closeLoginForm"
						/>
					</div>
				</q-form>
			</q-card-section>
		</q-card>
	</q-dialog>
</template>

<script>
export default {
	name: "StaffLoginDialog",
	data() {
		return {
			username: "",
			password: "",
			isPwd: true,
			remember: false,
			submitting: false
		};
	},
	computed: {
		showDialog: {
			get() {
				return this.$store.state.user.onLoginForm;
			},
			set() {
				this.$store.commit("user/closeLoginForm");
			}
		}
	},
	mounted() {
		this.username = localStorage.getItem("staffUsername");
		this.password = localStorage.getItem("staffPassword");
	},
	methods: {
		closeLoginForm() {
			this.$store.commit("user/closeLoginForm");
		},
		mapUrl(url) {
			if (url == "/") return "/index";
			else return url;
		},
		login() {
			//后端dto里N大写。
			let data = { userName: this.username, password: this.password };
			this.submitting = true;
			this.$axios
				.post("api/login", data)
				.then(async response => {
					console.log(response);
					if (response.status == 200) {
						localStorage.setItem("userName", this.username); //
						localStorage.setItem("staffUsername", this.username);
						
						await this.$store.dispatch(
							"user/asyncSetUserInfo",
							response.data
						); //
						if (this.remember) {
							localStorage.setItem(
								"staffPassword",
								this.password
							);
						}
						this.submitting = false;
						this.closeLoginForm();
						if (
							this.$route.path !=
							this.mapUrl(this.$store.state.user.targetUrl)
						)
							this.$router.push(this.$store.state.user.targetUrl);
						this.$q.notify({
							type: "positive",
							message: "登录成功！",
							timeout: "1500"
						});
					} else {
						this.submitting = false;
						this.$q.notify({
							type: "warning",
							message: "登录失败,请检查用户名和密码",
							timeout: "1500"
						});
					}
				})
				.catch(error => {
					console.log(error);
					this.submitting = false;
					this.$q.notify({
						type: "warning",
						message: "登录失败,请检查用户名和密码",
						timeout: "1500"
					});
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
