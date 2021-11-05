<template>
	<div class="q-pa-md">
		<q-table
			:data="data"
			:columns="columns"
			title="查看新申请项"
			:rows-per-page-options="[10, 15, 20]"
			row-key="userName"
			:filter="filter"
		>
			<template v-slot:top>
				<div class="row no-wrap items-center q-gutter-md">
					<q-btn
						rounded
						outline
						style="color: goldenrod"
						icon="update"
						label="更新"
						@click="updateAll_notify()"
					/>
				</div>

				<q-space />
				<q-input
					dense
					debounce="300"
					color="primary"
					label="按用户名查找..."
					v-model="filter"
				>
					<template v-slot:append>
						<q-icon name="search" />
					</template>
				</q-input>
			</template>

			<template v-slot:body="props">
				<q-tr :props="props">
					<q-td key="UserName" :props="props">
						{{ props.row.userName }}
					</q-td>
					<q-td key="NickName" :props="props">
						{{ props.row.nickName }}
					</q-td>
					<q-td key="TimeStamp" :props="props">
						{{ props.row.timeStamp }}
					</q-td>
					<q-td key="ApplyNote" :props="props">
						<q-btn
							v-html="getNoteOverview(props.row.applyNote)"
							flat
							:label="getNoteOverview(props.row.applyNote)"
							color="primary"
							@click="
								showApplyNote(
									props.row.userName,
									props.row.applyNote
								)
							"
						/>
					</q-td>
					<q-td key="UserInfo" :props="props">
						<q-btn
							flat
							rounded
							color="primary"
							label="详细信息"
							@click="showDetailsPannel(props.row.userName)"
						/>
					</q-td>

					<q-td key="ApplyState" :props="props">
						<apply-state
							:userName="props.row.userName"
							:applyState="props.row.applyState"
							@click.native="
								showApplyTalkDialog(props.row.userName)
							"
						/>
					</q-td>
				</q-tr>
			</template>
		</q-table>

		<!-- 预约洽谈 -->
		<apply-talk-dialog
			:myOnApplyTalkDialog="onApplyTalkDialog"
			:userName="currentUserName"
			@myCloseApplyTalkDialog="closeApplyTalkDialog"
		/>
	</div>
</template>

<script>
import { date } from "quasar";
import ApplyState from "components/health-steward-tools/apply-state";
import ApplyTalkDialog from "components/health-steward-tools/apply-talk-dialog";
import ApplyNote from "components/health-steward-tools/apply-note";
import ClientInfoDialog from "components/client/ClientInfoDialog";

const columns = [
	{
		name: "UserName",
		align: "left",
		label: "账号",
		field: "userName", // this is clientName
		sortable: true
	},
	{
		name: "NickName",
		align: "center",
		label: "用户名",
		field: "nickName",
		sortable: true
	},
	{
		name: "TimeStamp",
		label: "申请创建时间",
		field: "timeStamp",
		sortable: true
	},
	{
		name: "ApplyNote",
		label: "申请要求",
		field: "applyNote"
	},
	{
		name: "UserInfo",
		label: "详细信息",
		field: "userInfo"
	},
	{
		// 包含两个状态： 可申请(等待医生选择)，不可申请(已有医生选择)
		// 后端要提供 原子性 API 使用注解：@Transactional
		// 申请时，先向后台测试，有没有锁，有的话申请失败，没有的话对该申请项上锁，申请成功
		name: "ApplyState",
		label: "申请状态",
		field: "applyState"
	}
];

export default {
	data() {
		return {
			currentUserName: "",
			onDetailsPannel: false,
			currentUserNote: "",
			onApplyTalkDialog: false,
			filter: "",
			columns,
			data: []
		};
	},
	components: {
		// ClientDetails,
		ApplyState,
		ApplyTalkDialog
	},
	async mounted() {
		let url =
			"/api/" +
			localStorage.getItem("userName") +
			"/HealthSteward/ApplyItems";
		let res = await this.$axios.get(url);
		this.data = res.data;
	},
	methods: {
		async showDetailsPannel(clientName) {
			let url = "/api/client/" + clientName + "/get/simpleUserInfo";
			let res = await this.$axios.get(url);
			this.$q.dialog({
				component: ClientInfoDialog,
				parent: this,
				userInfo: res.data,
				onlyShow: true
			});
		},
		showApplyNote(userName, note) {
			// 查看医生所有TalkingNote
			this.$q.dialog({
				component: ApplyNote,
				parent: this,
				userName: userName,
				applyNote: note
			});
		},
		closeApplyNote() {
			// 查看医生所有TalkingNote
			this.onApplyNote = false;
		},
		getNoteOverview(note) {
			if (note && note.length > 10) return note.substr(0, 10) + "...";
			else return note;
		},
		showApplyTalkDialog(userName) {
			this.onApplyTalkDialog = true;
			this.currentUserName = userName;
		},
		closeApplyTalkDialog() {
			this.onApplyTalkDialog = false;
		},
		updateAll_notify() {
			let url =
				"/api/" +
				localStorage.getItem("userName") +
				"/HealthSteward/ApplyItems";

			this.$axios
				.get(url)
				.then(res => {
					this.data = res.data;
					this.$q.notify({
						type: "positive",
						message: "更新成功！",
						timeout: 1500,
						position: "top"
					});
				})
				.catch(err => {
					console.log(err);
					this.$q.notify({
						type: "negative",
						message: "更新失败！",
						timeout: 1500,
						position: "top"
					});
				});
		}
	}
};
</script>
