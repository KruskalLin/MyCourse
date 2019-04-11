import request from '@/utils/request'

export function fetchCourseList(query) {
  const data = JSON.stringify(query)
  return request({
    url: '/course/list',
    method: 'post',
    data
  })
}

export function fetchCourse(id) {
  return request({
    url: '/course/detail',
    method: 'get',
    params: { id }
  })
}

export function fetchReleaseCourse(id) {
  return request({
    url: '/course/releaseDetail',
    method: 'get',
    params: { id }
  })
}

export function fetchGradesList(id) {
  return request({
    url: '/course/getGradesList',
    method: 'get',
    params: { id }
  })
}

export function fetchGrades(id) {
  return request({
    url: '/course/getGrades',
    method: 'get',
    params: { id }
  })
}

export function changeGradesStatus(id, status) {
  return request({
    url: '/course/grades',
    method: 'get',
    params: { id, status }
  })
}

export function fetchCoursewares(id) {
  return request({
    url: '/course/coursewaresList',
    method: 'get',
    params: { id }
  })
}

export function postTopic(name, desc, id) {
  return request({
    url: '/course/post',
    method: 'get',
    params: { name, desc, id }
  })
}

export function fetchPostTopic(id) {
  return request({
    url: '/course/postList',
    method: 'get',
    params: { id }
  })
}

export function fetchPosts(id) {
  return request({
    url: '/course/allPost',
    method: 'get',
    params: { id }
  })
}

export function fetchReleasedCourse(id) {
  return request({
    url: '/course/releasedCourse',
    method: 'get',
    params: { id }
  })
}

export function createPost(id, content) {
  return request({
    url: '/course/postPost',
    method: 'get',
    params: { id, content }
  })
}

export function createCourse(data) {
  data = JSON.stringify(data)
  return request({
    url: '/course/create',
    method: 'post',
    data
  })
}

export function createHomework(data) {
  data = JSON.stringify(data)
  return request({
    url: '/course/homework',
    method: 'post',
    data
  })
}

export function getHomework(id) {
  return request({
    url: '/course/homeworkList',
    method: 'get',
    params: { id }
  })
}

export function getHomeworkDetail(id) {
  return request({
    url: '/course/homework',
    method: 'get',
    params: { id }
  })
}

export function getHomeworkFile(id) {
  return request({
    url: '/course/homeworkFile',
    method: 'get',
    params: { id }
  })
}

export function removeHomeworkFile(id) {
  return request({
    url: '/upload/removeHomework',
    method: 'get',
    params: { id }
  })
}

export function downloadHomework(id) {
  return request({
    url: '/course/downloadHomework',
    method: 'get',
    params: { id }
  })
}

export function fetchHomeworkHandout(id) {
  return request({
    url: '/course/homeworkHandout',
    method: 'get',
    params: { id }
  })
}

export function release(data) {
  data = JSON.stringify(data)
  return request({
    url: '/course/release',
    method: 'post',
    data
  })
}

export function fetchCourseRelease() {
  return request({
    url: '/course/courseRelease',
    method: 'get',
    params: {}
  })
}

export function check(id, state) {
  return request({
    url: '/course/check',
    method: 'get',
    params: { id, state }
  })
}

export function checkCourse(id, state) {
  return request({
    url: '/course/checkCourse',
    method: 'get',
    params: { id, state }
  })
}

export function startClass(id) {
  return request({
    url: '/course/start',
    method: 'get',
    params: { id }
  })
}

export function endClass(id) {
  return request({
    url: '/course/end',
    method: 'get',
    params: { id }
  })
}

export function fetchMyCourse() {
  return request({
    url: '/course/myCourse',
    method: 'get',
    params: { }
  })
}

export function fetchReleaseList(query) {
  const data = JSON.stringify(query)
  return request({
    url: '/course/releaseList',
    method: 'post',
    data
  })
}

export function fetchCourses(query) {
  const data = JSON.stringify(query)
  return request({
    url: '/course/courseList',
    method: 'post',
    data
  })
}

export function fetchRecordList(query) {
  const data = JSON.stringify(query)
  return request({
    url: '/course/recordList',
    method: 'post',
    data
  })
}

export function fetchTeacherRecordList(query) {
  const data = JSON.stringify(query)
  return request({
    url: '/course/teacherRecordList',
    method: 'post',
    data
  })
}

export function fetchChooseList(query) {
  const data = JSON.stringify(query)
  return request({
    url: '/course/chooseList',
    method: 'post',
    data
  })
}

export function chooseCourse(id) {
  return request({
    url: '/course/choose',
    method: 'get',
    params: { id }
  })
}

export function quitCourse(id) {
  return request({
    url: '/course/quit',
    method: 'get',
    params: { id }
  })
}

export function changeGrades(number, id, grades) {
  return request({
    url: '/course/changeGrades',
    method: 'get',
    params: { number, id, grades }
  })
}

export function changeHomeworkGrades(number, id, homeworkId, grades) {
  return request({
    url: '/course/changeHomeworkGrades',
    method: 'get',
    params: { number, id, homeworkId, grades }
  })
}

export function getHomeworkGradesList(id, courseId) {
  return request({
    url: '/course/homeworkGradesList',
    method: 'get',
    params: { id, courseId }
  })
}

export function sendMail(number, content) {
  return request({
    url: '/course/sendMail',
    method: 'get',
    params: { number, content }
  })
}
