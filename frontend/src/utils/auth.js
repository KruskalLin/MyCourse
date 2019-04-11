import Cookies from 'js-cookie'

const TokenKey = 'AccessToken'

const Roles = 'Roles'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getRoles() {
  if (Cookies.get(Roles) == null) {
    return ''
  }
  const role_str = Cookies.get(Roles)
  const roles = role_str.split(',')
  roles.splice(roles.length - 1, 1)
  return roles
}

export function setRoles(roles) {
  let role_str = ''
  for (const role in roles) {
    role_str += roles[role]
    role_str += ','
  }
  return Cookies.set(Roles, role_str)
}

export function removeRoles() {
  return Cookies.remove(Roles)
}
