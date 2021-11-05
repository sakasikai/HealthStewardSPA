export default function() {
	return {
		isLogin: false,
		// staffUsername: "le",
		// staffNickName: "倒吊人",
		staffInfo: {
			staffId: null,
			username: null,
			nickname: null,
			age: null,
			gender: null,
			type: null,
			hireDate: null,
			job: null,
			post: null, //职称
			phone: null,
			employeeId: null,
			resume: null,
			remark: null
		},
		// role: "admin",
		roles: [],
		permissions: [],
		uris: [],

		targetUrl: "/",

		showLogin: false
	};
}
