<template>
	<q-btn
		:color="cltCfmColor"
		:disabled="ifDisabled"
		size="sm"
		:label="state"
		rounded
	>
		<q-tooltip content-class="bg-grey" :delay="300"
			>点击删除该申请项</q-tooltip
		>
	</q-btn>
</template>

<script>
export default {
	props: {
		clientName: {
			type: String,
			required: true
		},
		contractState: {
			type: Number,
			required: true
		}
	},
	data() {
		return {
			timer: null,
			model: {
				state: this.contractState
			}
		};
	},
	watch: {
		clientName: function(val, oldVal) {
			// 刷新组件的局部数据
			this.model.state = this.contractState;
		}
	},
	computed: {
		cltCfmColor: function() {
			switch (this.model.state) {
				case -1:
					return "grey";
				case 1:
					return "green";
				case 0:
					return "red";
				default:
					return null;
			}
		},
		ifDisabled: function() {
			// 只有 未定义状态 按钮不可用，其他可点
			switch (this.model.state) {
				case -1:
					return true;
				case 1:
				case 0:
					return false;
				default:
					return null;
			}
		},
		state: function() {
			switch (this.model.state) {
				case -1:
					return "未知";
				case 1:
					return "成功";
				case 0:
					return "失败";
				default:
					return null;
			}
		}
	},
	created() {
		if (this.model.state !== -1) {
			// this.$store.commit(
			// 	"healthSteward/addContractModel",
			// 	this.clientName,
			// 	this.model.state
			// );
			return;
		}

		// 未知状态，要间断查询后端以更新this.model.state
		if (this.timer) {
			clearInterval(this.timer);
		}
		this.timer = setInterval(async () => {
			let url =
				"/api/" +
				localStorage.getItem("userName") +
				"/HealthSteward/" +
				this.clientName +
				"/ContractState";

			await this.$axios
				.get(url)
				.then(res => {
					this.model.state = res.data;
				})
				.catch(err => {
					console.log(err);
				});

			if (this.model.state === 1 || this.model.state === 0) {
				this.$store.commit("healthSteward/addContractModel", {
					contractClient: this.clientName,
					contractState: this.model.state
				});
				clearInterval(this.timer);
			}
		}, 8000);
	},
	destroyed() {
		if (this.timer) {
			clearInterval(this.timer);
		}
	}
};
</script>
