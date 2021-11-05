<template>
	<q-card style="min-height: inherit;">
		<q-card-section class="bg-grey-1">
			<div class="text-h6 text-center">{{ title }}</div>
		</q-card-section>
		<q-separator />
		<!-- <q-card-section horizontal>
			<q-card-section
				class="bg-blue-grey-5"
				style="width:1%;"
			></q-card-section>
			<q-card-section
				class="bg-grey-1"
				style="width:98%;"
			></q-card-section>
			<q-card-section
				class="bg-blue-grey-5"
				style="width:1%;"
			></q-card-section>
		</q-card-section> -->

		<!-- style="height:68vh" -->
		<q-card-section>
			<slot name="content"></slot>
		</q-card-section>

		<!-- <q-separator />
		<q-card-section horizontal>
			<q-card-section
				class="bg-blue-grey-5"
				style="width:1%;"
			></q-card-section>
			<q-card-section
				class="bg-grey-1"
				style="width:98%;"
			></q-card-section>
			<q-card-section
				class="bg-blue-grey-5"
				style="width:1%;"
			></q-card-section>
		</q-card-section> -->
	</q-card>
</template>

<script>
export default {
	name: "card",
	props: {
		title: String
	}
};
</script>

<style></style>
