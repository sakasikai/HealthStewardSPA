
const routes = [
  {
    path: "/",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { 
        path: "", 
        redirect: 'layouts/client-home-welcome', 
        meta:{isPublic:true} 
      },
      {
        path: "layouts/client-home-welcome",
        component: () => import("layouts/client-home-welcome.vue"), 
        meta:{isPublic:true}
      },
      { 
        path: "layouts/health-steward-client",
        component: () => import("layouts/health-steward-client.vue"), 
      },

    ]
  }
];

// 模块化路由

routes.push({
  path: "/unauthorized",
  component: () => import("pages/unauthorized401.vue")
});
routes.push({
  path: "*",
  component: () => import("pages/Error404.vue")
});

export default routes;
