import request from '@/utils/request'

export function getStudents() {
  return request({
    url: '/data/student',
    method: 'get',
    params: {}
  })
}

export function getTeachers() {
  return request({
    url: '/data/teacher',
    method: 'get',
    params: {}
  })
}

export function fetchRecordList(query) {
  const data = JSON.stringify(query)
  return request({
    url: '/data/records',
    method: 'post',
    data
  })
}
