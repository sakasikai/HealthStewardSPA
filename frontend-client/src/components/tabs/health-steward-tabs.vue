<template>
  <q-tabs class="q-pt-sm" align="left" dense>
    <q-route-tab
      to="/layouts/health-steward-staff/manage-clients"
      replace
      label="管理签约客户"
    />
    <q-route-tab
      to="/layouts/health-steward-staff/manage-apply-list"
      replace
      label="处理新申请"
    />
    <q-route-tab
      to="/layouts/health-steward-staff/manage-talk-list"
      replace
      label="管理洽谈"
    />
  </q-tabs>
</template>

<script>
export default {
  name: "HealthStewardTabs"
};
</script>
