var HealthStewardStaff = { 
    path: "layouts/health-steward-staff",
    name: "HealthStewardStaff",
    component: () => import("layouts/health-steward-staff.vue"),
    children: [
        {
          path: "",
          redirect: 'manage-clients',
        },
        {
          path: "manage-clients",
          component: () => import("pages/health-steward-staff/manage-client-list.vue"),
         },
        {
          path: "manage-apply-list",
          component: () => import("pages/health-steward-staff/manage-apply-list.vue"), 
        },
        {
          path: "manage-talk-list",
          component: () => import("pages/health-steward-staff/manage-talk-list.vue"), 
        },
    ]
  }
  
  export default HealthStewardStaff;