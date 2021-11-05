<template>
	<q-uploader
		auto-upload
		flat
		:factory="factoryFn"
		color="white"
		style="max-width: 120px"
		field-name="file"
		accept=".jpg, .png"
		max-file-size="2097152"
		@rejected="onRejected"
		@uploaded="imageUploaded"
		@failed="imageFailed"
	>
		<template v-slot:header="scope">
			<div class="row no-wrap justify-center">
				<!-- 这是从本地选的新图片 -->
				<q-item v-for="file in scope.files" :key="file.name">
					<q-item-section v-if="file.__img" avatar class="q-pa-none">
						<q-avatar size="100px">
							<q-img
								:src="file.__img.src"
								ratio="1"
								height="100px"
								width="100px"
								spinner-color="primary"
								spinner-size="82px"
							/>
						</q-avatar>
					</q-item-section>
				</q-item>
				<!-- 以下为原本的头像，当从本地选择图片后该项消失 -->
				<q-avatar
					v-if="
						scope.queuedFiles.length == 0 &&
							scope.uploadedFiles.length == 0 &&
							!scope.isUploading
					"
					size="100px"
				>
					<q-img
						:src="previewUrl"
						ratio="1"
						height="100px"
						width="100px"
						spinner-color="primary"
						spinner-size="82px"
					/>
				</q-avatar>
			</div>
		</template>
		<template v-slot:list="scope">
			<div class="row no-wrap justify-center">
				<q-btn
					flat
					v-if="scope.canAddFiles"
					type="a"
					label="更改头像"
					text-color="black"
				>
					<q-uploader-add-trigger />
					<q-tooltip>选择图片</q-tooltip>
				</q-btn>
			</div>
		</template>
	</q-uploader>
</template>

<script>
export default {
	name: "ImageUploader",
	props: {
		previewUrl: {
			type: String,
			default: "https://cdn.quasar.dev/img/avatar.png"
		}, //头像预览链接
		uploadUrl: { type: String, required: true } //头像上传链接
	},
	data() {
		return {};
	},
	methods: {
		factoryFn() {
			return {
				url: this.uploadUrl,
				method: "POST"
			};
		},
		onRejected(rejectedEntries) {
			let reason = "";
			for (let i of rejectedEntries) {
				reason += i.failedPropValidation;
			}
			this.$q.notify({
				type: "negative",
				message: reason,
				timeout: "1500"
			});
		},
		imageUploaded() {
			this.$q.notify({
				type: "positive",
				message: "图片上传成功!",
				timeout: "1500"
			});
		},
		imageFailed() {
			this.$q.notify({
				type: "negative",
				message: "图片上传失败!",
				timeout: "1500"
			});
		}
	}
};
</script>

<style></style>
