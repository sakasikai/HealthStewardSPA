<template>
	<q-dialog ref="userInfoDialog">
		<q-card style="height: 95%; max-width:80%">
			<q-card-section class="bg-grey-3">
				<div class="text-weight-bold">个人资料</div>
			</q-card-section>

			<q-form @submit="onSubmit">
				<q-card-section horizontal>
					<q-card-section class="q-gutter-md">

						<q-card horizontal class="row no-wrap">
							<q-card-section class="col-4 bg-grey-2">
								昵称
							</q-card-section>
							<q-input 
								v-show="!isEdit" 
								class="q-px-md my-input-width"
								disable
								v-model="nicknameInput"
							>
							</q-input>
							<q-input
								v-show="isEdit"
								class="q-px-md q-pb-none my-input-width"
								borderless
								v-model="nicknameInput"
								type="text"
								:rules="[
									val =>
										(!!val && val.length > 0) ||
										'昵称不能为空！'
								]"
							/>
						</q-card>

						<q-card horizontal class="row no-wrap">
							<q-card-section class="col-4 bg-grey-2">
								年龄
							</q-card-section>
							<q-input 
								v-show="!isEdit" 
								class="q-px-md my-input-width"
								disable
								v-model="ageInput"
							/>
							<q-input
								v-show="isEdit"
								borderless
								class="q-px-md q-pb-none my-input-width"
								v-model="ageInput"
								type="text"
								:rules="[
									val =>
										(Number.isSafeInteger(
											parseFloat(val)
										) &&
											val > 0) ||
										'请输入正确的年龄'
								]"
							/>
						</q-card>

						<q-card horizontal class="row no-wrap">
							<q-card-section class="col-4 bg-grey-2">
								手机
							</q-card-section>
							<q-input 
								v-show="!isEdit" 
								class="q-px-md my-input-width"
								disable
								v-model="phoneInput"
							/>
							<q-input
								v-show="isEdit"
								class="q-px-md q-pb-none my-input-width"
								borderless
								v-model="phoneInput"
								mask="### #### ####"
								:rules="[
									val => !val || val.length == 13 || '手机号应为11位'
								]"
							/>
						</q-card>

                        <q-card horizontal class="row no-wrap">
							<q-card-section class="col-4 bg-grey-2">
								身高
							</q-card-section>
							<q-input 
								v-show="!isEdit" 
								class="q-px-md my-input-width"
								disable
								v-model="heightInput"
							/>
							<q-input
								v-show="isEdit"
								class="q-px-md q-pb-none my-input-width"
								borderless
								v-model="heightInput"
								type="text"
								mask="###.#"
								hint="单位cm，默认保留一位小数"
								hide-hint
							/>
						</q-card>

                        <q-card horizontal class="row no-wrap">
							<q-card-section class="col-4 bg-grey-2">
								体重
							</q-card-section>
							<q-input 
								v-show="!isEdit" 
								class="q-px-md my-input-width"
								disable
								v-model="weightInput"
							/>
							<q-input
								v-show="isEdit"
								class="q-px-md q-pb-none my-input-width"
								borderless
								v-model="weightInput"
								type="text"
								mask="##.#"
								hint="单位kg，默认保留一位小数"
								hide-hint
							/>
						</q-card>

						<q-card horizontal class="row no-wrap">
							<q-card-section class="col-4 bg-grey-2">
								血型
							</q-card-section>
							<q-input 
								v-show="!isEdit" 
								class="q-px-md my-input-width"
								disable
								v-model="bloodTypeInput"
							/>
							<q-input
								v-show="isEdit"
								class="q-px-md q-pb-none my-input-width"
								borderless
								v-model="bloodTypeInput"
								type="text"
							/>
						</q-card>

						<q-card horizontal class="row no-wrap">
							<q-card-section class="col-4 bg-grey-2">
								出生日期
							</q-card-section>
							<q-input 
								v-show="!isEdit" 
								class="q-px-md my-input-width"
								disable
								v-model="birthdayInput"
							/>
							<q-input
								v-show="isEdit"
								class="q-px-md q-pb-none my-input-width"
								borderless
								v-model="birthdayInput"
								type="date"
								mask="YYYY-MM-DD"
							/>
						</q-card>
					</q-card-section>

					<q-card-section class="q-gutter-md">
						<div class="row no-wrap items-center q-mt-none" style="height: 170px">
							<div class="q-gutter-lg q-mr-md">
								<q-card horizontal class="row no-wrap">
									<q-card-section class="col-4 bg-grey-2">
										账号
									</q-card-section>
									<q-input
										v-show="!isEdit"
										class="q-px-md q-pb-none q-mr-md my-input-width"
										v-model="usernameInput"
										disable
									/>
									<q-input
										v-show="isEdit"
										class="q-pl-md q-pb-none my-input-width"
										borderless
										v-model="usernameInput"
										type="text"
										readonly
									>
										<q-tooltip v-show=true>
											您不可编辑该项
										</q-tooltip>
									</q-input>
								</q-card>

								<q-card horizontal class="row no-wrap">
									<q-card-section class="col-4 bg-grey-2">
										性别
									</q-card-section>
									<q-input
										class="q-px-md my-input-width q-mr-md"
										v-show="!isEdit"	
										v-model="genderView"
										disable
									></q-input>
									<q-input
										v-show="isEdit"
										class="q-pl-md q-pb-none my-input-width"
										borderless
										v-model="genderView"
										type="text"
										readonly
									>
										<q-tooltip v-show=true>
											您不可编辑该项
										</q-tooltip>
									</q-input>
								</q-card>
							</div>
							<!-- 头像组件 -->
							<!-- <img :src="imagePreviewUrl" v-show="!isEdit" /> -->
							<q-img 
								:src="imagePreviewUrl"
								height=100px
								width=100px
								v-show="!isEdit"
								ratio="1"
								spinner-colorr="primary"
								spinner-size="82px"
							></q-img>
							<image-uploader
								v-show="isEdit"
								class="q-mt-md"
								:uploadUrl="imageUploadUrl"
								:previewUrl="imagePreviewUrl"
							></image-uploader>
						</div>

						<q-card horizontal class="row no-wrap q-mt-sm">
							<q-card-section class="col-4 bg-grey-2">
								档案号
							</q-card-section>
							<q-input
								v-show="!isEdit"
								class="q-px-md q-pb-none my-input-width"
								v-model="fileNumberInput"
								disable
							/>
							<q-input
								v-show="isEdit"
								class="q-px-md q-pb-none my-input-width"
								borderless
								v-model="fileNumberInput"
								type="text"
								:readonly=true
							>
								<q-tooltip v-show=true>
									您不可编辑该项
								</q-tooltip>
							</q-input>
						</q-card>

						<q-card horizontal class="row no-wrap">
							<q-card-section class="col-4 bg-grey-2">
								身份证号
							</q-card-section>
							<q-input
								v-show="!isEdit"
								class="q-px-md q-pb-none my-input-width"
								v-model="idNumberInput"
								disable
							/>
							<q-input
								v-show="isEdit"
								class="q-px-md q-pb-none my-input-width"
								borderless
								v-model="idNumberInput"
								type="text"
								:readonly=true
							>
								<q-tooltip v-show=true>
									您不可编辑该项
								</q-tooltip>
							</q-input>
						</q-card>

						<q-card horizontal class="row no-wrap">
							<q-card-section class="col-4 bg-grey-2">
								老人类型
							</q-card-section>
							<q-input
								v-show="!isEdit"
								class="q-px-md q-pb-none my-input-width"
								v-model="typeInput"
								disable
							/>
							<q-input
								v-show="isEdit"
								class="q-px-md q-pb-none my-input-width"
								borderless
								v-model="typeInput"
								type="text"
								:readonly=true
							>
								<q-tooltip v-show=true>
									您不可编辑该项
								</q-tooltip>
							</q-input>
						</q-card>

                        <q-card horizontal class="row no-wrap">
							<q-card-section class="col-4 bg-grey-2">
								备注
							</q-card-section>
							<q-input
								v-show="!isEdit"
								class="q-px-md q-pb-none my-input-width"
								v-model="remarkInput"
								type="textarea"
								disable
							/>
							<q-input
								v-show="isEdit"
								class="q-px-md q-pb-none my-input-width"
								borderless
								v-model="remarkInput"
								type="textarea"
                                :readonly=true
                            >
                                <q-tooltip v-show=true>
									您不可编辑该项
								</q-tooltip>
							</q-input>
						</q-card>
					</q-card-section>
				</q-card-section>

				<q-card-actions align="right" class="q-gutter-lg q-mr-md" v-show="!onlyShow">
                    <q-btn label="修改" v-show="!isEdit" @click="edit"></q-btn>
					<q-btn label="提交" v-show="isEdit" type="submit" color="primary" />
					<q-btn
						label="取消"
						color="primary"
                        v-show="isEdit"
						flat
						class="q-ml-sm"
						@click="cancelEdit"
					/>
				</q-card-actions>
			</q-form>
		</q-card>
	</q-dialog>
</template>

<script>
import ImageUploader from "./ImageUploader.vue";
export default {
	name: "ClientInfoDialog",
	components: { ImageUploader },
	props: {
		userInfo: { type: Object, required: true },
		onlyShow: { type: Boolean,required: true }
	},
	data() {
		return {
            isEdit: false,
            clientInfo: '',
			usernameInput: null,
			nicknameInput: null,
			ageInput: null,
			genderInput: null,
			genderOptions: [
				{ value: 1, label: "男" },
				{ value: 2, label: "女" }
			],
			phoneInput: null,
			typeInput: null,
			fileNumberInput: null,
			idNumberInput: null, 
			birthdayInput: null,
			heightInput: null,
			weightInput: null,
			bloodTypeInput: null,
			remarkInput: null,
			genderView: null,
		};
	},
	computed: {
		imageUploadUrl() {
			return "api/client/image/update/" + this.userInfo.userId;
		},
		imagePreviewUrl() {
			return "api/client/image/get/" + this.userInfo.userId;
		}
    },
    mounted () {
        this.loadClientInfo();
    },
	methods: {
		show() {
			this.$refs.userInfoDialog.show();
		},

		hide() {
			this.$refs.userInfoDialog.hide();
		},

		cancelEdit() {
			this.isEdit=false;
        },

        edit () {
            this.isEdit=true;
        },
		onSubmit() {
			const url = "api/client/"+this.userInfo.userName+"/update";
			const data = {
				userId: this.clientInfo.userId,
				username: this.usernameInput,
				nickname: this.nicknameInput,
				age: this.ageInput,
				gender: this.genderView,
				type: this.typeInput,
				phone: this.phoneInput.split(' ').join(''),
				fileNumber: this.fileNumberInput,
				idNumber: this.idNumberInput,
				birthday: this.birthdayInput,
				height: this.heightInput,
				weight: this.weightInput,
				bloodType: this.bloodTypeInput,
				remark: this.remarkInput
			};
			this.$axios
				.post(url, data)
				.then(response => {
					if(response.status === 200){
						this.$q.notify({
							position: 'top',
							type: "positive",
							message: "更新成功！",
							timeout: "1500"
						});
						this.cancelEdit();
					}else if(response.status === 304){
						this.$q.notify({
							position: 'top',
							type: "negative",
							message: "更新失败！",
							timeout: "1500"
						});
					}
					// update 方法基本不会出现0行被影响
				})
				.catch(error => {
					this.$q.notify({
						position: 'top',
						type: "negative",
						message: "出错了",
						timeout: "1500"
					});
				});
		},
        loadClientInfo () {
            this.$axios.get("api/client/"+ this.userInfo.userName+"/get/clientInfo")
                .then(res => {
					this.clientInfo = res.data;
					this.usernameInput = this.clientInfo.username;
					this.nicknameInput = this.clientInfo.nickname;
					this.ageInput = this.clientInfo.age;
					this.genderInput = this.clientInfo.gender;
					this.typeInput = this.clientInfo.type;
					this.fileNumberInput = this.clientInfo.fileNumber;
					this.idNumberInput = this.clientInfo.idNumber;
					this.birthdayInput = this.clientInfo.birthday;
					this.heightInput = this.clientInfo.height;
					this.weightInput = this.clientInfo.weight;
					this.bloodTypeInput = this.clientInfo.bloodType;
					this.remarkInput = this.clientInfo.remark; 
					
					if(!!this.genderInput && this.genderInput == 1){
						console.log('ok')
						this.genderView = '男'
					}else if(!!this.genderInput && this.genderInput == 2){
						this.genderView = '女'
					}
					if(!!this.clientInfo.phone){
						this.phoneInput = this.clientInfo.phone.toString().substring(0,3) 
						+ ' ' + this.clientInfo.phone.toString().substring(3,7) + ' ' + this.clientInfo.phone.toString().substring(7)
					}
					
                })
                .catch(e => {
					this.$q.notify({
						position: 'top',
						message: '获取用户信息失败',
						timeout: 1500,
						type: 'negative'
					});
				});
        }
	}
};
</script>

<style>
.my-input-width {
	width: 300px;
}
</style>
