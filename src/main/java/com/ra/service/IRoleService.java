package com.ra.service;

import com.ra.constants.RoleName;
import com.ra.model.Roles;

public interface IRoleService {
	Roles findByRoleName(RoleName roleName);
}
