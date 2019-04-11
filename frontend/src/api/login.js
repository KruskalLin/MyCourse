import request from '@/utils/request'

export function loginByUsername(username, password) {
  const data = {
    username,
    password
  }
  console.log(data)
  return request({
    url: '/signin',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get',
    params: { }
  })
}

export function updateUserInfo(name) {
  return request({
    url: '/user/update',
    method: 'get',
    params: { name }
  })
}

export function sendEmail(username, password) {
  return request({
    url: '/user/sendEmail',
    method: 'get',
    params: { username, password }
  })
}

export function signUp(username, password, code) {
  const data = {
    username,
    password,
    code
  }
  return request({
    url: '/signup',
    method: 'post',
    data
  })
}

export function cancel() {
  return request({
    url: '/user/cancel',
    method: 'get',
    params: { }
  })
}
