import { loginByUsername, logout, getUserInfo } from '@/api/login'
import { getToken, setToken, removeToken, getRoles, setRoles, removeRoles } from '@/utils/auth'

const user = {
  state: {
    user: '',
    degree: '',
    grade: '',
    status: '',
    code: '',
    token: getToken(),
    name: '',
    number: '',
    avatar: getRoles(),
    introduction: '',
    roles: '',
    setting: {
      articlePlatform: []
    }
  },

  mutations: {
    SET_CODE: (state, code) => {
      state.code = code
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_USER: (state, user) => {
      state.user = user
    },
    SET_INTRODUCTION: (state, introduction) => {
      state.introduction = introduction
    },
    SET_SETTING: (state, setting) => {
      state.setting = setting
    },
    SET_STATUS: (state, status) => {
      state.status = status
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_NUMBER: (state, number) => {
      state.number = number
    },
    SET_GRADE: (state, grade) => {
      state.grade = grade
    },
    SET_DEGREE: (state, degree) => {
      state.degree = degree
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    }
  },

  actions: {
    // 用户名登录
    LoginByUsername({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        loginByUsername(username, userInfo.password).then(response => {
          const data = response.data
          commit('SET_TOKEN', data.token)
          commit('SET_USER', userInfo.username)
          setToken(response.data.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo().then(response => {
          // 由于mockjs 不支持自定义状态码只能这样hack
          if (!response.data) {
            reject('Verification failed, please login again.')
          }
          const data = response.data

          if (data.roles && data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            const roles = []
            for (let i = 0; i < data.roles.length; i++) {
              if (data.roles[i].name === 'ROLE_STUDENT') {
                roles.push('student')
              } else if (data.roles[i].name === 'ROLE_TEACHER') {
                roles.push('teacher')
              } else if (data.roles[i].name === 'ROLE_ADMIN') {
                roles.push('admin')
              }
            }
            console.log(roles)
            response.data.roles = roles
            setRoles(roles)
            commit('SET_ROLES', roles)
            commit('SET_AVATAR', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif')
            commit('SET_NUMBER', data.number)
            commit('SET_NAME', data.name)
            commit('SET_DEGREE', data.degree)
            commit('SET_GRADE', data.grade)
          } else {
            reject('getInfo: roles must be a non-null array!')
          }
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 第三方验证登录
    // LoginByThirdparty({ commit, state }, code) {
    //   return new Promise((resolve, reject) => {
    //     commit('SET_CODE', code)
    //     loginByThirdparty(state.status, state.email, state.code).then(response => {
    //       commit('SET_TOKEN', response.data.token)
    //       setToken(response.data.token)
    //       resolve()
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // },

    // 登出
    LogOut({ commit, state }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        removeRoles()
        resolve()
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        removeRoles()
        resolve()
      })
    }
  }
}

export default user
