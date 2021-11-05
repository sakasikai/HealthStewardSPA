<template>
	<q-layout view="hHr lpr fFr">
		<health-steward-tabs class="bg-blue-6 text-white" align="left" />
		<q-page-container>
			<transition
				enter-active-class="animated fadeIn"
				leave-active-class="animated fadeOut"
				mode="out-in"
			>
				<router-view />
			</transition>
		</q-page-container>
	</q-layout>
</template>

<script>
import HealthStewardTabs from "components/tabs/health-steward-tabs";

export default {
	components: {
		HealthStewardTabs
	}
};
</script>
