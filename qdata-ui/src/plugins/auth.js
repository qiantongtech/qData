/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import useUserStore from '@/store/system/user'

function authPermission(permission) {
  const all_permission = "*:*:*";
  const permissions = useUserStore().permissions
  if (permission && permission.length > 0) {
    return permissions.some(v => {
      return all_permission === v || v === permission
    })
  } else {
    return false
  }
}

function authRole(role) {
  const super_admin = "admin";
  const roles = useUserStore().roles
  if (role && role.length > 0) {
    return roles.some(v => {
      return super_admin === v || v === role
    })
  } else {
    return false
  }
}

export default {
  // Verify whether the user has certain permissions
  hasPermi(permission) {
    return authPermission(permission);
  },
  // Verify whether the user has the specified permissions, only need to include one of them
  hasPermiOr(permissions) {
    return permissions.some(item => {
      return authPermission(item)
    })
  },
  // Verify whether the user has the specified permissions, which must all be owned
  hasPermiAnd(permissions) {
    return permissions.every(item => {
      return authPermission(item)
    })
  },
  // Verify whether the user has a certain role
  hasRole(role) {
    return authRole(role);
  },
  // Verify whether the user has the specified role, only one of them needs to be included
  hasRoleOr(roles) {
    return roles.some(item => {
      return authRole(item)
    })
  },
  // Verify whether the user has the specified role, which must all be owned
  hasRoleAnd(roles) {
    return roles.every(item => {
      return authRole(item)
    })
  }
}
