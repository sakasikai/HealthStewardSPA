<template>
	<q-timeline class="q-py-sm q-px-xl my-border" color="secondary" layout="dense">
		<q-timeline-entry heading tag="h4">
			<q-item :to="head.to">
				{{ head.title }}
			</q-item>
		</q-timeline-entry>

		<q-timeline-entry v-for="b in body" :key="b.title" :icon="b.icon">
			<template v-slot:title>
				<q-item :to="b.to">
					{{ b.title }}
				</q-item>
			</template>
			<template v-slot:default>
				<q-item>
					{{ b.description }}
				</q-item>
			</template>
		</q-timeline-entry>
	</q-timeline>
</template>

<script>
export default {
	name: "Module",
	props: {
		head: Object,
		body: Array
	}
};
</script>

<style scoped>
.module {
	width: 40vw;
	height: 25vh;
}
.my-border {
	border-style: solid;
	border-width: 2px;
	border-radius: 15px;
	box-shadow: 10px 10px 5px #888888;
}
</style>
