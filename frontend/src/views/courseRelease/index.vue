<template>
  <div class="dashboard-editor-container">
    <el-table
      v-loading="listLoading"
      :key="tableKey"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;">
      <el-table-column :label="$t('table.title')" min-width="150px">
        <template slot-scope="scope">
          <span class="link-type" @click="show(scope.row)">{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.startTime')" width="100px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.endTime')" width="100px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.endTime }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.time')" width="150px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.time }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.author')" width="110px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.status')" width="110px" align="center">
        <template slot-scope="scope">
          <div v-if="scope.row.status === 'CHECKED'">
            <el-tag>{{ courseFilter(scope.row.status) }}</el-tag>
          </div>
          <div v-if="scope.row.status === 'RUNNING'">
            <el-tag type="success">{{ courseFilter(scope.row.status) }}</el-tag>
          </div>
          <div v-if="scope.row.status === 'DRAFT'">
            <el-tag type="info">{{ courseFilter(scope.row.status) }}</el-tag>
          </div>
          <div v-if="scope.row.status === 'END'">
            <el-tag type="warning">{{ courseFilter(scope.row.status) }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" width="120" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="show(scope.row)">{{ $t('table.show') }}</el-button>
          <el-button v-if="scope.row.course === 'RUNNING' || scope.row.course === 'CHECKED'" size="small" type="danger" @click="quit(scope.row)">{{ $t('table.quit') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
  </div>
</template>

<script>
import PanThumb from '@/components/PanThumb'
import { fetchCourseRelease } from '@/api/course'
import waves from '@/directive/waves' // Waves directive
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

const courseTypeOptions = [
  { key: 'DRAFT', display_name: '草稿中' },
  { key: 'CHECKED', display_name: '未开课' },
  { key: 'RUNNING', display_name: '开课中' },
  { key: 'END', display_name: '已结束' }

]

const courseTypeKeyValue = courseTypeOptions.reduce((acc, sch) => {
  acc[sch.key] = sch.display_name
  return acc
}, {})

const semesterTypeOptions = [
  { key: 'SPRING', display_name: '春季' },
  { key: 'SUMMER', display_name: '夏季' },
  { key: 'AUTUMN', display_name: '秋季' },
  { key: 'WINTER', display_name: '冬季' },
  { key: '暂未开始', display_name: '暂未开始' }
]

const semesterTypeKeyValue = semesterTypeOptions.reduce((acc, sch) => {
  acc[sch.key] = sch.display_name
  return acc
}, {})

export default {
  name: 'CourseRelease',
  components: { PanThumb, Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        title: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchCourseRelease().then(response => {
        this.list = response.data
        // this.total = response.data.total
        // Just to simulate the time of the request
        this.listLoading = false
      })
    },
    courseFilter(type) {
      return courseTypeKeyValue[type]
    },
    semesterFilter(type) {
      return semesterTypeKeyValue[type]
    },
    show(row) {
      this.$router.push({ name: 'Course', query: { id: row.id }})
    }
  }
}
</script>

<style scoped>
  .dashboard-editor-container {
    background-color: #FFFFFF;
    min-height: 100vh;
    padding: 50px 60px 0px;
  }
</style>
