import Mock from 'mockjs'

const List = []
const count = 20

for (let i = 0; i < count; i++) {
  List.push(Mock.mock({
    id: '@increment',
    order_no: '@guid()',
    timestamp: +Mock.Random.date('T'),
    startTime: +Mock.Random.date('T'),
    endTime: +Mock.Random.date('T'),
    title: '@title(5, 10)',
    name: '@first',
    username: '@name()',
    price: '@float(1000, 15000, 0, 2)',
    'status|1': ['success', 'pending'],
    'operation|1': ['QUIT', 'SELECT']
  }))
}

export default {
  getList: () => {
    return {
      total: List.length,
      items: List
    }
  }
}
