<template>
	<div class="q-pa-md">
		<q-table
			:data="data"
			:columns="columns"
			title="管理洽谈项"
			:rows-per-page-options="[10, 15, 20]"
			row-key="userName"
			:filter="filter"
		>
			<template v-slot:top>
				<div class="row no-wrap items-center q-gutter-md">
					<q-img
						style="width: 50px"
						ratio="1"
						class="rounded-borders"
						:src="imageUrl"
					/>
					<q-btn
						rounded
						outline
						style="color: goldenrod"
						label="查看所有备注"
						@click="showStaffNoteAll()"
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
						{{ props.row.clientName }}
					</q-td>
					<q-td key="NickName" :props="props">
						{{ props.row.nickName }}
					</q-td>
					<q-td key="TimeStamp" :props="props">
						{{ props.row.creationTimeStamp }}
					</q-td>

					<q-td key="UserInfo" :props="props">
						<q-btn
							flat
							rounded
							color="primary"
							label="详细信息"
							@click="showDetailsPannel(props.row.clientName)"
						/>
					</q-td>

					<q-td key="StaffNote" :props="props">
						<q-btn
							flat
							rounded
							color="primary"
							label="查看备注"
							@click="
								showStaffNote(
									props.row.clientName,
									props.row.contractId
								)
							"
						/>
					</q-td>

					<q-td key="StaffConfirm" :props="props">
						<staff-confirm-btn
							:clientName="props.row.clientName"
							:staffConfirm="props.row.staffConfirm"
						/>
					</q-td>

					<q-td key="ClientConfirm" :props="props">
						<client-confirm-btn
							:clientName="props.row.clientName"
							:clientConfirm="props.row.clientConfirm"
						/>
					</q-td>
					<!-- 每10s刷新，查询数据库判断状态，点击后要弹出通知并显示相关信息，提醒staff手动删除 -->
					<q-td key="ContractState" :props="props">
						<contract-state-btn
							:clientName="props.row.clientName"
							:contractState="props.row.contractState"
							@click.native="
								showContractDialog(
									props.row.clientName,
									props.row.contractState
								)
							"
						/>
					</q-td>
				</q-tr>
			</template>
		</q-table>

		<!-- <staff-notes
			:myOnStaffNote="onStaffNote"
			:userName="currentUserName"
			:type="noteType"
			:contractId="contractId"
			@myCloseStaffNote="closeStaffNote"
		/> -->
		<!-- ContractState notify and delete -->
		<contract-state-dialog
			:myOnContractDialog="onContractDialog"
			:userName="currentUserName"
			:contractState="contractState"
			@myCloseContractDialog="closeContractDialog"
			@myDeleteItem="deleteItem"
		/>
	</div>
</template>

<script>
// import ClientDetails from "components/health-steward-tools/clientInfo-details";
import StaffNotes from "components/health-steward-tools/staff-notes";
import StaffConfirmBtn from "components/health-steward-tools/staff-confirm-btn";
import ClientConfirmBtn from "components/health-steward-tools/client-confirm-btn";
import ContractStateBtn from "components/health-steward-tools/contract-state-btn";
import ContractStateDialog from "components/health-steward-tools/contract-state-dialog";
import ClientInfoDialog from "components/client/ClientInfoDialog";
import { date } from "quasar";

const columns = [
	{
		name: "UserName",
		align: "left",
		label: "账号",
		field: "clientName", // this is clientName
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
		field: "creationTimeStamp",
		sortable: true
	},
	{
		name: "UserInfo",
		label: "详细信息",
		field: "userInfo"
	},
	{
		name: "StaffNote",
		label: "备注",
		field: "contractId"
	},
	{
		name: "StaffConfirm",
		label: "医生签约",
		field: "staffConfirm"
	},
	{
		name: "ClientConfirm",
		label: "客户签约",
		field: "clientConfirm"
	},
	{
		name: "ContractState",
		label: "合约状态",
		field: "contractState"
	}
];

export default {
	data() {
		return {
			currentUserName: "",
			onDetailsPannel: false,
			onStaffNote: false,
			noteType: "",
			contractId: -1,
			onContractDialog: false,
			contractState: -1,
			filter: "",
			columns,
			data: []
		};
	},
	async mounted() {
		let url =
			"/api/" +
			localStorage.getItem("userName") +
			"/HealthSteward/TalkingItems";

		let res = await this.$axios.get(url);
		this.data = res.data;
	},
	components: {
		// StaffNotes,
		StaffConfirmBtn,
		ClientConfirmBtn,
		ContractStateBtn,
		ContractStateDialog
	},
	computed: {
		imageUrl: function() {
			return (
				"api/staff/" + localStorage.getItem("userName") + "/image/get"
			);
		},
		mutatedContractStateList: function() {
			return this.$store.state.healthSteward.mutatedStateList;
		}
	},
	methods: {
		async showDetailsPannel(clientName) {
			let url = "/api/client/" + clientName + "/get/simpleUserInfo";
			// console.log("check!")
			// console.log(url)
			let res = await this.$axios.get(url);
			this.$q.dialog({
				component: ClientInfoDialog,
				parent: this,
				userInfo: res.data,
				onlyShow: true
			});
		},
		showStaffNote(userName, contractId) {
			this.$q.dialog({
				component: StaffNotes,
				parent: this,
				userName: userName,
				type: "talkingNote",
				contractId: contractId
			});
		},
		showStaffNoteAll() {
			this.$q.dialog({
				component: StaffNotes,
				parent: this,
				userName: "",
				type: "talkingNote"
			});
		},
		closeStaffNote() {
			this.onStaffNote = false;
		},
		showContractDialog(userName, contractState) {
			this.onContractDialog = true;
			this.currentUserName = userName;
			// 这个contractState是created()时从后端获取的，
			// 但是contractState会变！页面不会刷新！
			// 变化后的contractState只会更新到Store里
			if (contractState === -1 && this.isContractStateMutated(userName)) {
				// contractState有变化！更新为store里的state
				this.contractState = this.getMutatedContractState(userName);
			} else {
				this.contractState = contractState;
			}
		},
		isContractStateMutated(userName) {
			return this.mutatedContractStateList.some(model => {
				return model.contractClient === userName;
			});
		},
		getMutatedContractState(userName) {
			for (let i = 0; i < this.mutatedContractStateList.length; i++) {
				if (
					userName === this.mutatedContractStateList[i].contractClient
				) {
					return this.mutatedContractStateList[i].contractState;
				}
			}
			return null;
		},
		closeContractDialog() {
			this.onContractDialog = false;
		},
		deleteItem(userName) {
			var startIndex = 0;
			for (let index = 0; index < this.data.length; index++) {
				if (this.data[index].clientName === userName) {
					startIndex = index;
					break;
				}
			}
			this.data.splice(startIndex, 1);
		}
	}
};
</script>
