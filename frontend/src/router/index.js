import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/views/layout/Layout'

/* Router Modules */

/** note: Submenu only appear when children.length>=1
 *  detail see  https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 **/

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    roles: ['admin','editor']    will control the page roles (you can set multiple roles)
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: true                if true, the page will no be cached(default is false)
    breadcrumb: false            if false, the item will hidden in breadcrumb(default is true)
  }
**/
export const constantRouterMap = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/signup',
    component: () => import('@/views/signup/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/authredirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/errorPage/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/errorPage/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: 'dashboard', icon: 'dashboard', noCache: true }
      }
    ]
  }

]

export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  { path: '*', redirect: '/404', hidden: true },
  {
    path: '/course',
    component: Layout,
    redirect: 'list',
    meta: {
      title: 'course',
      icon: 'nested'
    },
    children: [
      {
        path: 'release',
        component: () => import('@/views/courseRelease/index'),
        name: 'CourseRelease',
        meta: { title: 'courseRelease', icon: 'clipboard', noCache: true, roles: ['teacher'] }
      },
      {
        path: 'list',
        component: () => import('@/views/courseList/index'),
        name: 'CourseList',
        meta: { title: 'courseList', icon: 'form', noCache: true, roles: ['teacher'] }
      },
      {
        path: 'check',
        component: () => import('@/views/check/index'),
        name: 'Check',
        meta: { title: 'check', icon: 'form', noCache: true, roles: ['admin'] }
      },
      {
        path: 'checkCourse',
        component: () => import('@/views/checkCourse/index'),
        name: 'CheckCourse',
        meta: { title: 'checkCourse', icon: 'form', noCache: true, roles: ['admin'] }
      },
      {
        path: 'course',
        component: () => import('@/views/course/index'),
        name: 'Course',
        hidden: true,
        meta: { noCache: true }
      },
      {
        path: 'detail',
        component: () => import('@/views/courseDetail/index'),
        name: 'CourseDetail',
        hidden: true,
        meta: { noCache: true }
      },
      {
        path: 'releaseCourse',
        component: () => import('@/views/release/index'),
        name: 'Release',
        hidden: true,
        meta: { noCache: true }
      },
      {
        path: 'homework',
        component: () => import('@/views/homework/index'),
        name: 'Homework',
        hidden: true,
        meta: { noCache: true }
      },
      {
        path: 'forum',
        component: () => import('@/views/forum/index'),
        name: 'Forum',
        hidden: true,
        meta: { noCache: true }
      },
      {
        path: 'choose',
        component: () => import('@/views/courseChoose/index'),
        name: 'CourseChoose',
        meta: { title: 'courseChoose', icon: 'table', noCache: true, roles: ['student'] }
      },
      {
        path: 'record',
        component: () => import('@/views/courseRecord/index'),
        name: 'CourseRecord',
        meta: { title: 'courseRecord', icon: 'documentation', noCache: true, roles: ['student'] }
      },
      {
        path: 'courseRecord',
        component: () => import('@/views/teacherCourseRecord/index'),
        name: 'TeacherCourseRecord',
        meta: { title: 'courseRecord', icon: 'documentation', noCache: true, roles: ['teacher'] }
      },
      {
        path: 'myCourse',
        component: () => import('@/views/myCourse/index'),
        name: 'MyCourse',
        meta: { title: 'myCourse', icon: 'edit', noCache: true, roles: ['student'] }
      }
    ]
  }
]
