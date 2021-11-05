export default function() {
	return {
		userName: null,
		userInfo: {
			userId: null,
			userName: null,
			nickName: null,
			age: null,
			gender: null
		},
		isLogin: false, // 用户登录状态
		onLoginForm: false, // 登录框显示状态
		onSignupForm: false,
		targetUrl: "/",

		roles: [],
		permissions: [],
		uris: []
		//暂存
		// staffInfo: {
		// 	staffId: null,
		// 	username: null,
		// 	nickname: null,
		// 	age: null,
		// 	gender: null,
		// 	type: null,
		// 	hireDate: null,
		// 	job: null,
		// 	post: null, //职称
		// 	phone: null,
		// 	employeeId: null,
		// 	resume: null,
		// 	remark: null
		// }
	};
}
