<template>
	<q-dialog ref="ApplyNote">
		<q-card style="width:450px">
			<q-card-section>
				<div class="text-subtitle1 text-grey">
					客户<font class="text-h5" color="black">
						{{ userName }}</font
					>
					的申请要求
				</div>
			</q-card-section>

			<q-card-section
				class="text-body1 q-pa-xl text-left"
				v-html="applyNote"
			>
				{{ applyNote }}
			</q-card-section>

			<q-card-actions align="right">
				<q-btn flat label="OK" color="primary" @click="close" />
			</q-card-actions>
		</q-card>
	</q-dialog>
</template>

<script>
export default {
	props: {
		userName: {
			type: String,
			required: true
		},
		applyNote: {
			type: String,
			required: false
		}
	},
	data() {
		return {
			onApplyNote: null
		};
	},
	methods: {
		// 以下方法是必需的
		// (不要改变它的名称 --> "show")
		show() {
			this.$refs.ApplyNote.show();
			this.onApplyNote = true;
		},

		hide() {
			this.$refs.ApplyNote.hide();
			this.onApplyNote = false;
		},
		close() {
			this.hide();
		}
	}
};
</script>
