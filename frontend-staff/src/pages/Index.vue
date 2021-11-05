<template>
	<!-- <q-page class="flex flex-center"> -->
	<q-page class="my-bg">
		<div class="row justify-evenly">
			<module
				class="col-5"
				:head="prop('健康管家', '/layouts/health-steward-staff')"
				:body="[
					props(
						'管理签约用户',
						'/layouts/health-steward-staff/manage-clients',
						'gradient',
						'对用户已经签约的客户进行管理'
					),
					props(
						'处理新申请',
						'/layouts/health-steward-staff/manage-apply-list',
						'event_note',
						'寻找新的用户'
					),
					props(
						'管理洽谈',
						'/layouts/health-steward-staff/manage-talk-list',
						'perm_contact_calendar',
						'管理和新用户的洽谈，方便和客户进行双向选择'
					)
				]"
			/>
			<module
				class="col-5"
				:head="prop('健康管家', '/layouts/health-steward-staff')"
				:body="[
					props(
						'管理签约用户',
						'/layouts/health-steward-staff/manage-clients',
						'gradient',
						'对用户已经签约的客户进行管理'
					),
					props(
						'处理新申请',
						'/layouts/health-steward-staff/manage-apply-list',
						'event_note',
						'寻找新的用户'
					),
					props(
						'管理洽谈',
						'/layouts/health-steward-staff/manage-talk-list',
						'perm_contact_calendar',
						'管理和新用户的洽谈，方便和客户进行双向选择'
					)
				]"
			/>
		</div>

		
	</q-page>
</template>

<script>
import Module from "components/index/Module.vue";

export default {
	name: "PageIndex",
	components: { Module },
	data() {
		return {
			img:
				"http://img.zcool.cn/community/01c4cb58d8dc5fa801219c774ce351.gif"
			//"http://www.desktx.com/d/file/wallpaper/construction/20160728/69329dd6b335786970961023be3100d3.jpg"
		};
	},
	methods: {
		prop(title, to) {
			return { title: title, to: to };
		},
		props(title, to, icon, description) {
			return {
				title: title,
				to: to,
				icon: icon,
				description: description
			};
		}
	}
};
</script>

<style scoped>
.my-bg {
	background-image: url("http://img.zcool.cn/community/01c4cb58d8dc5fa801219c774ce351.gif");
	background-repeat: repeat-y;
	background-position: top;
	background-size: 90%
}
</style>
