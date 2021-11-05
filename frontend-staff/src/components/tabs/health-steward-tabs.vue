<template>
  <q-tabs class="bg-grey-2 shadow-2 rounded-borders" align="left">
    <q-route-tab
      to="/layouts/health-steward-staff/manage-clients"
      replace
      class="text-dark"
      label="管理签约客户"
    />
    <q-route-tab
      to="/layouts/health-steward-staff/manage-apply-list"
      replace
      class="text-dark"
      label="处理新申请"
    />
    <q-route-tab
      to="/layouts/health-steward-staff/manage-talk-list"
      replace
      class="text-dark"
      label="管理洽谈"
    />
  </q-tabs>
</template>

<script>
export default {
  name: "HealthStewardTabs"
};
</script>
