<template>
	<!-- 按完变状态，有通知 -->
	<div>
		<q-btn-dropdown
			flat
			color="primary"
			:label="state"
			dropdown-icon="change_history"
			v-if="!ended"
		>
			<q-list>
				<q-item clickable v-close-popup @click="StfCfmClick(1)">
					<q-item-section>
						<q-item-label>确定</q-item-label>
					</q-item-section>
				</q-item>

				<q-item clickable v-close-popup @click="StfCfmClick(0)">
					<q-item-section>
						<q-item-label>拒绝</q-item-label>
					</q-item-section>
				</q-item>
			</q-list>
		</q-btn-dropdown>
		<q-btn
			:color="stfCfmColor"
			disabled
			size="sm"
			:label="state"
			rounded
			v-else
		/>
	</div>
</template>
<script>
export default {
	props: {
		clientName: {
			type: String,
			required: true
		},
		staffConfirm: {
			type: Number,
			required: true
		}
	},
	data() {
		return {
			model: {
				stfConfirm: this.staffConfirm
			}
		};
	},
	computed: {
		stfCfmColor: function() {
			switch (this.state) {
				case "已确定":
					return "green";
				case "已拒绝":
					return "red";
				default:
					return null;
			}
		},
		state: function() {
			// 显示状态标签
			switch (this.model.stfConfirm) {
				case -1:
					return "待选择";
				case 1:
					return "已确定";
				case 0:
					return "已拒绝";
				default:
					return null;
			}
		},
		ended: function() {
			// stfConfirm为1/2时，代表出结果确定，这相当于一个标志
			switch (this.model.stfConfirm) {
				case -1:
					return false;
				case 1:
				case 0:
					return true;
				default:
					return null;
			}
		}
	},
	methods: {
		StfCfmClick(num) {
			if (num === 1 || num === 0) {
				let url =
					"/api/" +
					localStorage.getItem("userName") +
					"/HealthSteward/" +
					this.clientName +
					"/StaffContractConfirm?num=" +
					num;

				this.$axios
					.put(url)
					.then(res => {
						this.model.stfConfirm = num;
					})
					.catch(err => {
						console.log(err);
					});
			}
		}
	},
	created() {
		let url =
			"/api/" +
			localStorage.getItem("userName") +
			"/HealthSteward/" +
			this.clientName +
			"/StaffContractConfirm";

		this.$axios
			.get(url)
			.then(res => {})
			.catch(err => {});
	},
	watch: {
		clientName: function(val, oldVal) {
			// 刷新组件的局部数据
			this.model.stfConfirm = this.staffConfirm;
		}
	}
};
</script>
