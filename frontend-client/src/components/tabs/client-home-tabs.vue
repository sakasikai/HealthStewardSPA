<template>
  <q-tabs align="left">
    <q-route-tab
      icon="pin_drop"
      to="/layouts/client-home-welcome"
      replace
      label="主页"
    />
    <q-route-tab
      icon="play_circle_outline"
      to="/layouts/health-steward-client"
      replace
      label="健康管家"
    />
  </q-tabs>
</template>

<script>
export default {
  name: "ClientHomeTabs"
};
</script>
