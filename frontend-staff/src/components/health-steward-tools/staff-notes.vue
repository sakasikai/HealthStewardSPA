<template>
	<div>
		<q-dialog ref="staffNote">
			<q-card
				style="height: 75%; width: 70%;"
				class="column justify-between no-wrap"
			>
				<!-- 标题 -->
				<q-card-section style="height:15% min-height:88px">
					<q-toolbar>
						<q-toolbar-title
							><span class="text-weight-bold text-grey"
								>备注对象：{{ staffNoteModel.clientName }}</span
							></q-toolbar-title
						>
						<q-btn
							flat
							round
							dense
							icon="close"
							@click="closeNote"
						/>
					</q-toolbar>
					<q-separator class="q-pb-none" />
				</q-card-section>

				<!-- 交叉备注列表 -->
				<q-card-section style="max-height: 73%" class="scroll">
					<div class="q-pa-md flex justify-center" v-if="hsaData">
						<div style="width: 400px;">
							<q-intersection
								v-for="(note, index) in staffNoteList"
								:key="index"
								transition="flip-right"
								class="staffNote"
							>
								<div class="row justify-start ">
									<q-item clickable v-ripple>
										<q-item-section avatar>
											<q-btn round>
												<q-avatar size="32px">
													<q-icon
														color="primary"
														name="eva-file-text-outline"
													>
														<q-tooltip
															content-class="bg-grey"
															:delay="600"
															>点击右侧消息，进行更多操作</q-tooltip
														>
													</q-icon>
												</q-avatar>
											</q-btn>
										</q-item-section>

										<q-item-section
											style="width:210px"
											@click="itemSelected(note)"
										>
											<q-item-label
												v-html="
													getNoteOverview(
														note.staffNote
													)
												"
												>{{
													getNoteOverview(
														note.staffNote
													)
												}}/
												{{
													note.clientName
												}}</q-item-label
											>
											<q-item-label caption lines="1"
												>{{ note.timeStamp }}
											</q-item-label>
										</q-item-section>
										<span class="q-px-lg" />
										<q-item-section side>
											<q-icon
												name="chat_bubble"
												color="green"
												@click="
													displaySpecificNote(
														note,
														index
													)
												"
												class="q-px-md"
											/>
										</q-item-section>
									</q-item>
								</div>
							</q-intersection>
						</div>
					</div>
					<div v-else class="flex justify-center">
						<font class="text-h6 text-grey text-bold"
							>没有笔记:(</font
						>
					</div>
				</q-card-section>

				<!-- 增加按钮 -->
				<q-card-section
					class="row justify-end content-end q-pb-sm"
					style="height:10% max-height:48px"
				>
					<q-btn
						flat
						round
						dense
						icon="add"
						color="purple"
						class="q-mr-sm"
						v-if="isClientSpecified"
						@click="openAddNoteDialog"
					/>
				</q-card-section>
			</q-card>
		</q-dialog>

		<!-- display Specific Note -->
		<q-dialog
			v-model="onSpercificNote"
			persistent
			transition-show="scale"
			transition-hide="scale"
		>
			<q-card style="max-height: 70%; width: 44%">
				<q-card-section class="bg-teal text-white">
					<div class="text-h6">备注</div>
				</q-card-section>

				<q-card-section class="q-pa-md">
					<q-editor
						v-if="editEnabled"
						v-model="noteContent"
						flat
						content-class="bg-teal-1"
						toolbar-text-color="dark"
						toolbar-toggle-color="yellow-8"
						toolbar-bg="primary"
						:toolbar="[
							['bold', 'italic', 'underline'],
							[
								{
									label: $q.lang.editor.formatting,
									icon: $q.iconSet.editor.formatting,
									list: 'no-icons',
									options: ['p', 'h3', 'h4', 'h5', 'h6']
								}
							]
						]"
						@paste.native="evt => pasteCapture(evt)"
					/>
					<div v-else v-html="staffNoteModel.staffNote">
						{{ staffNoteModel.staffNote }}
					</div>
				</q-card-section>
				<q-card-section class="q-py-none">
					<q-toggle
						v-model="editEnabled"
						color="green"
						label="启用编辑"
					/>
				</q-card-section>

				<q-card-actions
					align="right"
					class="bg-white text-teal q-pt-none"
				>
					<q-btn
						flat
						label="更新"
						@click="submitEditedNote"
						:disable="!editEnabled"
					/>
					<q-btn flat label="删除" @click="deleteSpecificNote" />
					<q-btn flat label="关闭" @click="closeSpecificNote" />
				</q-card-actions>
			</q-card>
		</q-dialog>

		<!-- add note -->
		<q-dialog
			v-model="onAddNote"
			persistent
			transition-show="scale"
			transition-hide="scale"
		>
			<q-card class="bg-blue-grey-1" style="max-height: 70%;width: 44%">
				<q-card-section>
					<div class="text-h6">
						为 {{ staffNoteModel.clientName }} 新填备注
					</div>
				</q-card-section>

				<q-card-section class="q-pt-none">
					<div class="q-pa-none q-gutter-sm">
						<q-editor
							v-model="addNoteContent"
							flat
							content-class="bg-teal-1"
							toolbar-text-color="dark"
							toolbar-toggle-color="yellow-8"
							toolbar-bg="primary"
							:toolbar="[
								['bold', 'italic', 'underline'],
								[
									{
										label: $q.lang.editor.formatting,
										icon: $q.iconSet.editor.formatting,
										list: 'no-icons',
										options: ['p', 'h3', 'h4', 'h5', 'h6']
									}
								]
							]"
							@paste.native="evt => pasteCapture(evt)"
						/>
					</div>
				</q-card-section>

				<q-card-actions
					align="right"
					class="q-pt-none bg-blue-grey-1 text-teal"
				>
					<q-btn flat label="关闭" @click="closeAddNoteDialog" />
					<q-btn flat label="提交" @click="commitAddNote" />
				</q-card-actions>
			</q-card>
		</q-dialog>
	</div>
</template>

<script>
export default {
	props: {
		userName: {
			// 2 situations: staff-user-note, staff-note(default)
			type: String,
			required: true
		},
		contractId: {
			// when userName sepcified, this parameter is provided
			type: Number,
			required: false
		},
		type: {
			// 2 types: talkingNote, signedNote
			type: String,
			required: true
		}
	},
	data() {
		return {
			staffNoteList: [],
			staffNoteModel: {
				noteId: null,
				staffName: null,
				clientName: null,
				staffNote: null,
				timeStamp: null
			},
			index: null,
			myOnStaffNote: null,
			onSpercificNote: false,
			onAddNote: false,
			isClientSpecified: false,
			editEnabled: false,
			noteContent: null,
			addNoteContent: "请编辑新备注"
		};
	},
	computed: {
		imageUrl: function() {
			return (
				"api/staff/" + localStorage.getItem("userName") + "/image/get"
			);
		},
		noteBaseUrl: function() {
			let url =
				"/api/" + localStorage.getItem("userName") + "/HealthSteward";
			url += this.userName ? "/" + this.userName + "/" : "/";
			url += this.regularizeTypeString(this.type);
			return url;
		},
		hsaData: function() {
			if (this.staffNoteList.length === 0) {
				return false;
			}
			return true;
		}
	},
	watch: {
		myOnStaffNote: async function(val, oldVal) {
			this.initData(this.userName);
		},
		editEnabled: function(val, oldVal) {
			if (val) {
				this.noteContent = this.staffNoteModel.staffNote;
			}
		}
	},
	methods: {
		// 以下方法是必需的
		// (不要改变它的名称 --> "show")
		show() {
			this.$refs.staffNote.show();
			this.myOnStaffNote = true;
		},

		hide() {
			this.$refs.staffNote.hide();
			this.myOnStaffNote = false;
		},
		closeNote() {
			this.hide();
		},

		async initData(val) {
			// 清空data
			this.staffNoteList = [];
			// 打开NoteDialog, 加载note数据
			let res = await this.$axios.get(this.noteBaseUrl);
			this.staffNoteList = res.data;

			// 为staffNoteModel的userName设置默认值
			this.staffNoteModel = {
				clientName: val != "" ? val : "全部用户"
			};

			// 显示addNote浮标
			if (this.userName != "") {
				this.isClientSpecified = true;
			} else {
				this.isClientSpecified = false;
			}
		},

		itemSelected(note) {
			if (this.userName == "") {
				this.staffNoteModel = note;
			}
		},
		displaySpecificNote(note, index) {
			this.onSpercificNote = true;
			this.staffNoteModel = note;
			this.index = index;
		},
		async deleteSpecificNote() {
			let url = "";
			if (this.userName == "") {
				let cut = this.noteBaseUrl.lastIndexOf("/");
				url =
					this.noteBaseUrl.substring(0, cut + 1) +
					this.staffNoteModel.clientName +
					this.noteBaseUrl.substring(cut) +
					"/" +
					this.staffNoteModel.noteId;
			} else {
				url = this.noteBaseUrl + "/" + this.staffNoteModel.noteId;
			}

			await this.$axios
				.delete(url)
				.then(res => {
					this.staffNoteList.splice(this.index, 1);
					this.$q.notify({
						message: "删除成功",
						type: "positive",
						position: "center",
						timeout: 2000
					});
				})
				.catch(err => {
					console.log(err);
				});
			this.closeSpecificNote();
		},
		async submitEditedNote() {
			if (this.noteContent === this.staffNoteModel.staffNote) {
				this.$q.notify({
					message: "没有做出更改",
					type: "warning",
					position: "center",
					timeout: 2000
				});
				return;
			}

			let url = "";
			if (this.userName == "") {
				let cut = this.noteBaseUrl.lastIndexOf("/");
				url =
					this.noteBaseUrl.substring(0, cut + 1) +
					this.staffNoteModel.clientName +
					this.noteBaseUrl.substring(cut) +
					"/" +
					this.staffNoteModel.noteId;
			} else {
				url = this.noteBaseUrl + "/" + this.staffNoteModel.noteId;
			}

			await this.$axios
				.put(url, this.noteContent, {
					headers: {
						"Content-Type": "application/json"
					}
				})
				.then(res => {
					this.staffNoteList[this.index].staffNote = this.noteContent;
					this.editEnabled = !this.editEnabled;
					this.onSpercificNote = false;
					this.$q.notify({
						message: "修改成功",
						type: "positive",
						position: "center",
						timeout: 2000
					});
				});
		},
		closeSpecificNote() {
			this.onSpercificNote = false;
			this.index = null;
			this.editEnabled = false;
		},
		openAddNoteDialog() {
			this.onAddNote = true;
			this.addNoteContent = "请编辑新备注";
		},
		closeAddNoteDialog() {
			// do sth
			this.onAddNote = false;
		},
		async commitAddNote() {
			let cutPoint = this.noteBaseUrl.lastIndexOf("/");
			let urlWithContractId =
				this.noteBaseUrl.substring(0, cutPoint) +
				"/Contract/" +
				this.contractId +
				this.noteBaseUrl.substring(cutPoint);

			let res = await this.$axios
				.post(urlWithContractId, this.addNoteContent, {
					headers: {
						"Content-Type": "application/json"
					}
				})
				.then(res => {
					if (res.status === 201) {
						this.staffNoteList.push(res.data);
					}
				});
			this.closeAddNoteDialog();
		},
		regularizeTypeString(str) {
			switch (str.toLowerCase()) {
				case "signednote":
					return "SignedNote";
				case "talkingnote":
					return "TalkingNote";
				default:
					return;
			}
		},
		getNoteOverview(note) {
			if (note && note.length > 15) return note.substr(0, 15) + "... ";
			else return note + " ";
		},
		pasteCapture(evt) {
			/**
			 * Capture the <CTL-V> paste event, only allow plain-text, no images.
			 *
			 */
			// let text, onPasteStripFormattingIEPaste;
			// evt.preventDefault();
			// if (evt.originalEvent && evt.originalEvent.clipboardData.getData) {
			// 	text = evt.originalEvent.clipboardData.getData("text/plain");
			// 	this.$refs.editor_ref.runCmd("insertText", text);
			// } else if (evt.clipboardData && evt.clipboardData.getData) {
			// 	text = evt.clipboardData.getData("text/plain");
			// 	this.$refs.editor_ref.runCmd("insertText", text);
			// } else if (window.clipboardData && window.clipboardData.getData) {
			// 	if (!onPasteStripFormattingIEPaste) {
			// 		onPasteStripFormattingIEPaste = true;
			// 		this.$refs.editor_ref.runCmd("ms-pasteTextOnly", text);
			// 	}
			// 	onPasteStripFormattingIEPaste = false;
			// }
		}
	}
};
</script>

<style>
.staffNote {
	height: 56px;
}
</style>
