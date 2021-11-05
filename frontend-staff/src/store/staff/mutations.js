// 标志当前角色
export function setRole(state, name) {
	state.role = name;
}

// 登陆后在前端存储staff详细信息
export function setStaffInfo(state, info) {
	state.staffInfo = info;
}

export function setRolesAndPermissions(state, data) {
	state.roles = data.roleNames;
	state.permissions = data.permissionNames;
	state.uris = data.permissionUris;
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

// 登录框开关
export function openLoginForm(state) {
	state.showLogin = true;
}

export function closeLoginForm(state) {
	state.showLogin = false;
}
