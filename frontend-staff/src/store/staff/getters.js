//通过返回函数使得这个getter可以传参
export function hasPermi(state) {
	return param => {
		if (param && param.length > 0) {
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
