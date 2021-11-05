import Vue from "vue";
import VueRouter from "vue-router";

import routes from "./routes";
import { Dialog } from "quasar";
import { Notify } from "quasar";

Vue.use(VueRouter);

/*
 * If not building with SSR mode, you can
 * directly export the Router instantiation;
 *
 * The function below can be async too; either use
 * async/await or return a Promise which resolves
 * with the Router instance.
 */

export default function({ store }) {
	const Router = new VueRouter({
		scrollBehavior: () => ({ x: 0, y: 0 }),
		routes,

		// Leave these as they are and change in quasar.conf.js instead!
		// quasar.conf.js -> build -> vueRouterMode
		// quasar.conf.js -> build -> publicPath
		mode: process.env.VUE_ROUTER_MODE,
		base: process.env.VUE_ROUTER_BASE
	});

	Router.beforeEach(async (to, from, next) => {
		// 1. 没有登录，但本地有token => 自动登录, 跳过了component里的认证
		if (
			!store.state.user.isLogin &&
			localStorage.getItem("jwtToken") != null
		) {
			await store.dispatch(
				"user/asyncSetUserInfo",
				localStorage.getItem("jwtToken")
			);
		}

		// 2. 访问受限制url，且还未登录 => 要求登录
		if (!to.meta.isPublic && !store.state.user.isLogin) {
			Dialog.create({
				title: "当前页面的访问需要登录",
				message: "是否登录？",
				style: "text-align:center",
				position: "top"
			})
				.onOk(() => {
					store.commit("user/openLoginForm");
					store.commit("user/setTargetUrl", to.path);
				})
				.onDismiss(() => {
					if (from.meta.isPublic) {
						// do nothing
					} else {
						store.$router.push("/");
					}
				});
			return false;
		}

		// 3. 访问受限制url，有登录标志，没有token => 要求重新登录
		if (
			!to.meta.isPublic &&
			store.state.user.isLogin &&
			!localStorage.getItem("jwtToken")
		) {
			Dialog.create({
				title: "身份过期,请重新登录!",
				message: "是否登录？",
				style: "text-align:center",
				position: "top"
			})
				.onOk(() => {
					store.commit("user/openLoginForm");
					store.commit("user/setTargetUrl", to.path);
				})
				.onDismiss(() => {
					if (from.meta.isPublic) {
						// do nothing
					} else {
						store.$router.push("/");
					}
				});
			return false;
		}

		if (
			to.meta.required &&
			store.state.user.isLogin &&
			localStorage.getItem("jwtToken") &&
			!store.getters["user/hasUri"](to.path)
		) {
			Notify.create({
				type: "warning",
				position: "top",
				message: "权限不足！",
				timeout: 1500
			});
			return false;
		}

		next();
	});

	return Router;
}
