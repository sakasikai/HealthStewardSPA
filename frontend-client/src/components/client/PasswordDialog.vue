<template>
	<q-dialog ref="PasswordDialog">
		<q-card>
			<q-card-section class="bg-grey-3">
				<div class="text-weight-bold">修改密码</div>
			</q-card-section>

			<q-card-section>
				<q-form
					spellcheck="false"
					@submit="onSubmit"
					class="q-gutter-lg"
				>
					<q-card horizontal class="row">
						<q-card-section class="col-4 bg-grey-2">
							账号
						</q-card-section>
						<div class="q-pl-md">
							<q-input
								class="q-pb-none "
								borderless
								v-model="usernameInput"
								type="text"
								disable
							/>
						</div>
					</q-card>

					<q-card horizontal class="row no-wrap">
						<q-card-section class="col-4 bg-grey-2">
							当前密码
						</q-card-section>
						<div class="q-px-md">
							<q-input
								class="q-pb-none "
								debounce="300"
								v-model="passwordInput"
								borderless
								:type="isPwd ? 'password' : 'text'"
								:rules="[...passwordRules, passwordValidate]"
							>
								<template v-slot:append>
									<q-icon
										:name="
											isPwd
												? 'visibility_off'
												: 'visibility'
										"
										class="cursor-pointer"
										@click="isPwd = !isPwd"
									/>
								</template>
							</q-input>
						</div>
					</q-card>

					<q-card horizontal class="row no-wrap">
						<q-card-section class="col-4 bg-grey-2">
							新密码
						</q-card-section>
						<div class="q-px-md">
							<q-input
								class="q-pb-none "
								v-model="newPassword"
								borderless
								hint="密码长度应在6~20位之间"
								:type="isPwd ? 'password' : 'text'"
								:rules="passwordRules"
							>
								<template v-slot:append>
									<q-icon
										:name="
											isPwd
												? 'visibility_off'
												: 'visibility'
										"
										class="cursor-pointer"
										@click="isPwd = !isPwd"
									/>
								</template>
							</q-input>
						</div>
					</q-card>

					<q-card horizontal class="row no-wrap">
						<q-card-section class="col-4 bg-grey-2">
							确认密码
						</q-card-section>
						<div class="q-px-md">
							<q-input
								class="q-pb-none "
								v-model="confirmPassword"
								borderless
								:type="isPwd ? 'password' : 'text'"
								:rules="[
									...passwordRules,
									val =>
										val == newPassword ||
										'两次密码输入不一致！'
								]"
							>
								<template v-slot:append>
									<q-icon
										:name="
											isPwd
												? 'visibility_off'
												: 'visibility'
										"
										class="cursor-pointer"
										@click="isPwd = !isPwd"
									/>
								</template>
							</q-input>
						</div>
					</q-card>

					<div class="row q-gutter-md justify-end">
						<q-btn
							label="提交"
							type="submit"
							color="primary"
							:loading="submitting"
						/>
						<q-btn
							label="取消"
							color="primary"
							flat
							class="q-ml-sm"
							@click="hide"
						/>
					</div>
				</q-form>
			</q-card-section>
		</q-card>
	</q-dialog>
</template>

<script>
export default {
	name: "PasswordDialog",
	props: { username: {type:String,required:true} },
	data() {
		return {
			isPwd: false,
			usernameInput: this.username,
			passwordInput: null,
			newPassword: null,
			confirmPassword: null,

			passwordRules: [
				val => (val && val.length > 0) || "密码不能为空！",
				val => val.length >= 3 || "密码长度必须大于等于3位！",
				val => val.length <= 20 || "密码长度不能超过20位！"
			],
			submitting: false
		};
	},
	computed: {},
	methods: {
		show() {
			this.$refs.PasswordDialog.show();
		},

		hide() {
			this.$refs.PasswordDialog.hide();
		},
		passwordValidate(val) {
			return new Promise((resolve, reject) => {
				this.$axios
					.get(
						"/api/" +
							this.usernameInput +
							"/checkPassword/" +
							this.passwordInput
					)
					.then(response => {
						resolve(true);
					})
					.catch(error => {
						resolve("当前密码输入错误！");
					});
			});
		},
		onSubmit() {
			this.submitting = true;
			this.$axios
				.get(
					"/api/" +
						this.usernameInput +
						"/changePassword/" +
						this.confirmPassword
				)
				.then(response => {
					this.submitting = false;
					this.$q.notify({
						type: "positive",
						message: "修改密码成功！",
						timeout: "1500"
					});
					this.hide();
				})
				.catch(error => {
					this.submitting = false;
					this.$q.notify({
						type: "negative",
						message: "修改密码失败！",
						timeout: "1500"
					});
				});
		}
	}
};
</script>

<style></style>
