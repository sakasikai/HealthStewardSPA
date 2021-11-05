<template>
	<!-- 自动刷新的状态按钮，有1:可以申请 0:不可申请两态, 前者可点击，后者不可以-->
	<q-btn
		:color="cltCfmColor"
		size="sm"
		:label="labelMsg"
		:disabled="ifDisabled"
		rounded
	>
		<q-tooltip content-class="bg-grey" :delay="300" v-if="onHint">
			"该客户已有医生服务"
		</q-tooltip>
	</q-btn>
</template>

<script>
export default {
	props: {
		userName: {
			type: String,
			required: true
		},
		applyState: {
			type: Number,
			required: true
		}
	},
	data() {
		return {
			timer: null,
			applyLevel: 0
		};
	},
	computed: {
		state: function() {
			// applyDialog那边触发了事件，改变全局变量 (userName, applyState),
			// 当前组件应当匹配下，如果userName匹配，就改变applyLevel
			switch (this.applyLevel) {
				case 2:
					return 1;
				case 3:
				case 4:
				case 5:
					return 0;
				default:
					return 0;
			}
		},
		cltCfmColor: function() {
			switch (this.state) {
				case 1:
					return "green";
				case 0:
					return "grey";
				default:
					return null;
			}
		},
		labelMsg: function() {
			switch (this.state) {
				case 1:
					return "可以申请";
				case 0:
					return "不可申请";
				default:
					return null;
			}
		},
		ifDisabled: function() {
			switch (this.state) {
				case 1:
					return false;
				case 0:
					return true;
				default:
					return null;
			}
		},
		onHint: function() {
			switch (this.state) {
				case 1:
					return false;
				case 0:
					return true;
				default:
					return null;
			}
		},
		ifSeclected: function() {
			let nm = this.$store.state.healthSteward.applyModel.applyClient;
			return nm == this.userName;
		}
	},
	watch: {
		ifSeclected: function(val, oldVal) {
			if (val === true) {
				this.applyLevel = this.$store.state.healthSteward.applyModel.applyState;
				this.$store.commit("healthSteward/setApplyModel", {
					applyClient: "",
					applyState: -1
				});
			}
		},
		userName: function(val, oldVal) {
			// 刷新组件的局部数据
			this.applyLevel = this.applyState;
		}
	},
	created() {
		this.applyLevel = this.applyState;

		if (this.state === 1) {
			// 定时更新表示当前client是否可以申请的状态
			var url = "/api/" + localStorage.getItem("userName") + "/HealthSteward/"+ this.userName + "/ApplyFlowLevel";

			// 间断测试状态
			if (this.timer) {
				clearInterval(this.timer);
			}
			this.timer = setInterval(async () => {
				await this.$axios
					.get(url)
					.then(res => {
						this.applyLevel = res.data;
					})
					.catch(err => {
						console.log(err);
					});

				if (this.state === 0) {
					clearInterval(this.timer);
				}
			}, 10000);
		}
	},
	destroyed() {
		if (this.timer) {
			clearInterval(this.timer);
		}
	}
};
</script>
