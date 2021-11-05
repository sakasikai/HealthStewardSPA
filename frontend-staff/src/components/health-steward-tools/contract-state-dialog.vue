<template>
	<q-dialog v-model="middleValue" v-close-popup>
		<q-card>
			<q-card-section class="row items-center">
				<q-avatar
					icon="eva-alert-triangle-outline"
					size="lg"
					color="warning"
					text-color="white"
				/>
				<span class="q-px-md text-h5">注意</span>
				<q-space />
				<q-btn
					flat
					round
					dense
					color="red"
					icon="close"
					@click="close"
				/>
			</q-card-section>

			<q-separator class="q-mb-md" />

			<q-card-section class="q-mx-md">
				<div v-html="notification" />
			</q-card-section>

			<q-card-actions align="right">
				<q-btn
					flat
					label="确认"
					color="primary"
					class="q-mr-sm"
					v-if="contractState === 1"
					@click="setContractManaged(userName)"
				/>
				<q-btn
					flat
					label="确认"
					color="primary"
					class="q-mr-sm"
					v-else-if="contractState === 0"
					@click="removeContract(userName)"
				/>
			</q-card-actions>
		</q-card>
	</q-dialog>
</template>

<script>
export default {
	props: {
		myOnContractDialog: {
			type: Boolean,
			required: true
		},
		userName: {
			type: String,
			required: true
		},
		contractState: {
			type: Number,
			required: true
		}
	},
	computed: {
		middleValue: {
			get: function() {
				return this.myOnContractDialog;
			},
			set: function(newValue) {
				return newValue;
			}
		},
		// contractState: function() {
		// 	console.log(
		// 		"contractState: " + this.$store.state.healthSteward.contractState
		// 	);
		// 	return this.$store.state.healthSteward.contractState;
		// },
		notification: function() {
			if (this.contractState === 1) {
				return "请同时进行合约的线下确认!<br>点击确认后本合约将正式生效, 之后请到管理签约客户界面进行查看";
			} else if (this.contractState === 0) {
				return "请同时进行合约的线下确认!点击确认后将删除本洽谈项";
			}
			return null;
		}
	},
	methods: {
		setContractManaged(userName) {
			let url =
				"/api/" +
				localStorage.getItem("userName") +
				"/HealthSteward/" +
				userName +
				"/Contract";
			this.$axios
				.put(url)
				.then(res => {
					// console.log(res);
				})
				.catch(err => {
					console.log(err);
				});

			this.close();
			this.$emit("myDeleteItem", userName); // 前端删除
		},
		removeContract(userName) {
			let url =
				"/api/" +
				localStorage.getItem("userName") +
				"/HealthSteward/" +
				userName +
				"/Contract";
			this.$axios
				.delete(url)
				.then(res => {
					// console.log(res);
				})
				.catch(err => {
					console.log(err);
				});

			this.close();
			this.$emit("myDeleteItem", userName); // 前端删除
		},
		close() {
			this.$emit("myCloseContractDialog");
		}
	}
};
</script>
