// 登录框开关
export function openLoginForm(state) {
	state.onLoginForm = true;
}

export function closeLoginForm(state) {
	state.onLoginForm = false;
}

// 注册框开关
export function openSignupForm(state) {
	state.onSignupForm = true;
}

export function closeSignupForm(state) {
	state.onSignupForm = false;
}

// 标志当前用户名
export function setUserName(state, name) {
	state.userName = name;
}

// 登陆后在前端存储User详细信息
export function setUserInfo(state, userInfo) {
	state.userInfo = userInfo;
}

// 标识当前用户是否登录
export function isLogin(state) {
	state.isLogin = true;
}

export function isLogout(state) {
	state.isLogin = false;
}

// 路由守卫 登录成功后 自动导向目标url
export function setTargetUrl(state, path) {
	state.targetUrl = path;
}

//设置角色和权限
export function setRolesAndPermissions(state, data) {
	state.roles = data.roleNames;
	state.permissions = data.permissionNames;
	state.uris = data.permissionUris;
}

// 登陆后在前端存储staff详细信息
export function setStaffInfo(state, info) {
	state.staffInfo = info;
}
