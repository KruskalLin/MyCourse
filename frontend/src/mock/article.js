import Mock from 'mockjs'
import { param2Obj } from '@/utils'

const List = []
const count = 100

const url = 'https://wpimg.wallstcn.com/e4558086-631c-425c-9430-56ffb46e70b3'

for (let i = 0; i < count; i++) {
  List.push(Mock.mock({
    id: '@increment',
    startTime: +Mock.Random.date('T'),
    endTime: +Mock.Random.date('T'),
    year: '@integer(2019, 2024)',
    name: '@first',
    title: '@title(5, 10)',
    desc: '我是测试数据',
    'course|1': ['DRAFT', 'CHECKED', 'RUNNING', 'END'],
    'operation|1': ['QUIT', 'SELECT'],
    'semester|1': ['SPRING', 'SUMMER', 'AUTUMN', 'WINTER'],
    'choose|1': [true, false],
    time: '仙I202 周四 第5-7节\n仙I201 周五 第5-7节',
    grades: '@integer(0, 100)',
    limit: '@integer(1, 50)',
    number: '@integer(1, 50)',
    url: url
  }))
}

export default {
  getList: config => {
    const { title, page = 1, limit = 10 } = param2Obj(config.url)

    const mockList = List.filter(item => {
      if (title && item.title.indexOf(title) < 0) return false
      return true
    })

    const pageList = mockList.filter((item, index) => index < limit * page && index >= limit * (page - 1))

    return {
      total: mockList.length,
      items: pageList
    }
  },
  getPv: () => ({
    pvData: [{ key: 'PC', pv: 1024 }, { key: 'mobile', pv: 1024 }, { key: 'ios', pv: 1024 }, { key: 'android', pv: 1024 }]
  }),
  getArticle: (config) => {
    const { id } = param2Obj(config.url)
    for (const article of List) {
      if (article.id === +id) {
        return article
      }
    }
  },
  createArticle: () => ({
    data: 'success'
  }),
  updateArticle: () => ({
    data: 'success'
  })
}
