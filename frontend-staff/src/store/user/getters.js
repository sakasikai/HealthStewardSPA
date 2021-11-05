import { Notify } from "quasar";

export function getUserName(state) {
	return state.userName;
}

export function getUserInfo(state) {
	return state.userInfo;
}

//通过返回函数使得这个getter可以传参
export function hasPerm(state) {
	return param => {
		if (param && param instanceof Array && param.length > 0) {
			return state.permissions.some(permission => {
				const perms = permission.split(":");
				return param.some(v => {
					const input = v.split(":");
					for (let i = 0; i < 3; i++) {
						if (input[i] == "*" || perms[i] == "*") continue;
						if (input[i] != perms[i]) return false;
					}
					return true;
				});
			});
		} else if (param && typeof param === "string" && param.length > 0) {
			const input = param.split(":");
			if (input.length != 3) throw new Error("权限参数错误！");
			return state.permissions.some(permission => {
				const perms = permission.split(":");
				for (let i = 0; i < 3; i++) {
					if (input[i] == "*" || perms[i] == "*") continue;
					if (input[i] != perms[i]) return false;
				}
				return true;
			});
		} else throw new Error("权限参数错误！");
	};
}

export function hasRole(state) {
	return param => {
		if (param && param instanceof Array && param.length > 0) {
			const super_admin = "admin";
			const roles = state.roles;

			const hasRole = roles.some(role => {
				return super_admin === role || param.includes(role);
			});
			return hasRole;
		} else throw new Error("角色参数错误！");
	};
}

export function hasUri(state) {
	return param => {
		if (param && param.length > 0) {
			return state.uris.includes(param);
		} else throw new Error("uri参数错误！");
	};
}

export function alertPerm(state) {
	return param => {
		let result = true;
		if (param && param instanceof Array && param.length > 0) {
			result = state.permissions.some(permission => {
				const perms = permission.split(":");
				return param.some(v => {
					const input = v.split(":");
					for (let i = 0; i < 3; i++) {
						if (input[i] == "*" || perms[i] == "*") continue;
						if (input[i] != perms[i]) return false;
					}
					return true;
				});
			});
		} else if (param && typeof param === "string" && param.length > 0) {
			const input = param.split(":");
			if (input.length != 3) throw new Error("权限参数错误！");
			result = state.permissions.some(permission => {
				const perms = permission.split(":");
				for (let i = 0; i < 3; i++) {
					if (input[i] == "*" || perms[i] == "*") continue;
					if (input[i] != perms[i]) return false;
				}
				return true;
			});
		} else throw new Error("权限参数错误！");
		if (!result) {
			Notify.create({
				type: "warning",
				message: "权限不足！",
				position: "top",
				timeout: 1500
			});
		}
	};
}

export function alertRole(state) {
	return param => {
		if (param && param instanceof Array && param.length > 0) {
			const super_admin = "admin";
			const roles = state.roles;

			const hasRole = roles.some(role => {
				return super_admin === role || param.includes(role);
			});
			if (!hasRole) {
				Notify.create({
					type: "warning",
					message: "权限不足！",
					position: "top",
					timeout: 1500
				});
			}
		} else throw new Error("角色参数错误！");
	};
}

export function alertUri(state) {
	return param => {
		if (param && param.length > 0) {
			if (!state.uris.includes(param)) {
				Notify.create({
					type: "warning",
					message: "权限不足！",
					position: "top",
					timeout: 1500
				});
			}
		} else throw new Error("uri参数错误！");
	};
}
