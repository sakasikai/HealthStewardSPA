import { axiosInstance as axios } from "boot/axios.js";

export async function asyncSetUserInfo({ commit }, token) {
	return new Promise(async resolve => {
		// 更新token
		try {
			localStorage.setItem("jwtToken", token);
		} catch (e) {
			console.log(e);
		}

		// 设置http请求头
		axios.defaults.headers.common["Authorization"] = "Bearer " + token;
		// 存储userName在前端

		// 向后端请求 user 详细信息, 全局存储在前端中
		const res = await axios.get(
			"/api/staff/" +
				localStorage.getItem("staffUsername") +
				"/get/staffInfo"
		);

		commit("setStaffInfo", res.data);
		commit("isLogin");

		resolve();
	});
}

export async function userLogout({ commit }) {
	try {
		localStorage.removeItem("jwtToken");
	} catch (e) {
		console.log(e);
	}

	commit("setStaffInfo", null);
	commit("isLogout");
}
