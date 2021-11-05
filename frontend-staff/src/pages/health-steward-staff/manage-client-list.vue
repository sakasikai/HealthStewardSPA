<template>
	<div>
		<div class="q-pa-md">
			<q-table
				:data="data"
				:columns="columns"
				title="管理用户"
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
						<q-td key="SignedTimeStamp" :props="props">
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
						<q-td key="RepealContract" :props="props">
							<q-btn
								flat
								rounded
								color="red"
								label="解除合约"
								@click="
									showContractPannel(props.row.clientName)
								"
							/>
						</q-td>
					</q-tr>
				</template>
			</q-table>
		</div>


		<repeal-contract
			:myOnRepealContract="onRepealContract"
			:userName="currentUserName"
			@myCloseContractPannel="closeContractPannel"
			@myDeleteItem="deleteItem"
		/>
	</div>
</template>

<script>
import StaffNotes from "components/health-steward-tools/staff-notes";
import RepealContract from "components/health-steward-tools/repeal-contract";
import ClientInfoDialog from "components/client/ClientInfoDialog";
import { date } from "quasar";

const columns = [
	{
		name: "UserName",
		align: "left",
		label: "用户账号",
		field: "clientName",
		sortable: true
	},
	{
		name: "NickName",
		align: "center",
		label: "用户昵称",
		field: "nickName",
		sortable: true
	},
	{
		name: "SignedTimeStamp",
		label: "合同生效时间",
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
		name: "RepealContract",
		label: "解除合约",
		field: "repealContract"
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
			onRepealContract: false,
			filter: "",
			columns,
			data: []
		};
	},
	async mounted() {
		let url =
			"/api/" +
			localStorage.getItem("userName") +
			"/HealthSteward/SignedClients";

		let res = await this.$axios.get(url);
		this.data = res.data;
	},
	components: {
		// ClientDetails,
		RepealContract
	},
	computed: {
		imageUrl: function() {
			return (
				"api/staff/" + localStorage.getItem("userName") + "/image/get"
			);
		}
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
		showStaffNote(userName, contractId) {
			// 子组件通过v-bind绑定将下面参数传入
			// this.onStaffNote = true;

			this.$q.dialog({
				component: StaffNotes,
				parent: this,
				userName: userName,
				type: "signedNote",
				contractId: contractId
			});
		},
		showStaffNoteAll() {
			this.$q.dialog({
				component: StaffNotes,
				parent: this,
				userName: "",
				type: "signedNote"
			});
		},
		closeStaffNote() {
			this.onStaffNote = false;
		},
		showContractPannel(userName) {
			// 子组件通过v-bind绑定将下面参数传入
			this.onRepealContract = true;
			this.currentUserName = userName;
		},
		closeContractPannel() {
			this.onRepealContract = false;
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
