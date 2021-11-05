<template>
	<div>
		<q-btn
			:color="cltCfmColor"
			:disabled="ifDisabled"
			size="sm"
			:label="state"
			rounded
		>
			<q-tooltip content-class="bg-grey" :delay="300"
				>等待客户进行选择</q-tooltip
			>
		</q-btn>
	</div>
</template>

<script>
export default {
	props: {
		clientName: {
			type: String,
			required: true
		},
		clientConfirm: {
			type: Number,
			required: true
		}
	},
	data() {
		return {
			timer: null,
			model: {
				cltConfirm: this.clientConfirm
			}
		};
	},
	computed: {
		cltCfmColor: function() {
			switch (this.model.cltConfirm) {
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
			switch (this.model.cltConfirm) {
				case -1:
					return false;
				case 1:
				case 0:
					return true;
				default:
					return null;
			}
		},
		state: function() {
			switch (this.model.cltConfirm) {
				case -1:
					return "未定义";
				case 1:
					return "已确定";
				case 0:
					return "已拒绝";
				default:
					return null;
			}
		}
	},
	watch: {
		clientName: function(val, oldVal) {
			// 刷新组件的局部数据
			this.model.cltConfirm = this.clientConfirm;
		}
	},
	created() {
		if (this.model.cltConfirm === -1) {
			// clientConfirm 为-1时，需要一直刷新请求 直到检测到客户做出选择，ended为true
			if (this.timer) {
				clearInterval(this.timer);
			}
			this.timer = setInterval(async () => {
				let url =
					"/api/" +
					localStorage.getItem("userName") +
					"/HealthSteward/" +
					this.clientName +
					"/ClientContractConfirm";

				await this.$axios
					.get(url)
					.then(res => {
						// console.log(res);
						this.model.cltConfirm = res.data;
					})
					.catch(err => {
						console.log(err);
					});

				if (
					this.model.cltConfirm === 1 ||
					this.model.cltConfirm === 0
				) {
					clearInterval(this.timer);
				}
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
