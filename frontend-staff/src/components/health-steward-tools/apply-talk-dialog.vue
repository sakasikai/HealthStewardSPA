<template>
	<q-dialog v-model="middleValue">
		<q-card>
			<q-card-section>
				<div class="row row items-center">
					<q-avatar
						icon="eva-alert-triangle-outline"
						size="lg"
						color="warning"
						text-color="white"
					/>
					<span class="q-px-md text-h5">注意</span>
					<q-space />
					<q-btn
						color="red-4"
						round
						flat
						icon="eva-close-outline"
						@click="close"
					/>
				</div>
			</q-card-section>
			<q-separator class="q-mb-md" />
			<q-card-section class="q-pt-md">
				由于有其他医生抢先申请的可能，您的提交可能会失败，
				<p>确认后请留意反馈信息</p>
			</q-card-section>

			<q-card-actions align="right" class="q-pa-sm q-mr-sm">
				<q-btn
					flat
					color="primary"
					label="申请洽谈"
					@click="createApplyTalk"
					:loading="onSubmitLoading"
				>
					<template v-slot:loading>
						<q-spinner-hourglass />
					</template>
				</q-btn>
			</q-card-actions>
		</q-card>
	</q-dialog>
</template>

<script>
import { date } from "quasar";

export default {
	props: {
		myOnApplyTalkDialog: {
			type: Boolean,
			required: true
		},

		userName: {
			type: String,
			required: true
		}
	},
	computed: {
		middleValue: {
			get: function() {
				return this.myOnApplyTalkDialog;
			},
			set: function(newValue) {
				return newValue;
			}
		}
	},
	data() {
		return {
			onSubmitLoading: false
		};
	},
	methods: {
		async createApplyTalk() {
			this.onSubmitLoading = true;

			let url =
				"/api/" +
				localStorage.getItem("userName") +
				"/HealthSteward/" +
				this.userName +
				"/TalkLink?timeStamp=" +
				date.formatDate(Date.now(), "YYYY-MM-DD-HH:mm:ss");

			await this.$axios
				.put(url)
				.then(res => {
					this.$q.notify({
						message: "创建成功",
						type: "positive",
						color: "green",
						position: "top"
					});

					this.onSubmitLoading = false;
					this.close();
					// 要立即改变applyState
					this.$store.commit("healthSteward/setApplyModel", {
						applyClient: this.userName,
						applyState: 2
					});
				})
				.catch(err => {
					console.log(err);
				});
		},
		close() {
			this.$emit("myCloseApplyTalkDialog");
		}
	}
};
</script>
