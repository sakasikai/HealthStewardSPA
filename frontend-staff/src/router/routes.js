import HealthStewardStaff from "./healthStewardStaff";

const routes = [
	{
		path: "/",
		component: () => import("layouts/MainLayout.vue"),
		children: [
			{
				path: "",
				redirect: "index"
			},
			{
				path: "index",
				component: () => import("pages/Index.vue"),
				meta: { isPublic: true }
			}
		]
	},
	{
		path: "*",
		component: () => import("pages/Error404.vue"),
		meta: { isPublic: true }
	}
];

routes[0].children.push(HealthStewardStaff);

export default routes;
