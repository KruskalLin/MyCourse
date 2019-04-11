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
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.name')" width="110px" align="center">
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
          <div v-if="scope.row.status === 'REJECTED'">
            <el-tag type="warning">{{ courseFilter(scope.row.status) }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <div v-if="scope.row.status === 'DRAFT'">
            <el-button type="primary" size="mini" @click="approve(scope.row)">{{ $t('table.approve') }}</el-button>
            <el-button type="danger" size="mini" @click="reject(scope.row)">{{ $t('table.reject') }}</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
  </div>
</template>

<script>

import PanThumb from '@/components/PanThumb'
import { fetchCourses, checkCourse } from '@/api/course'
import waves from '@/directive/waves' // Waves directive
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

const courseTypeOptions = [
  { key: 'DRAFT', display_name: '待审核' },
  { key: 'CHECKED', display_name: '已通过' },
  { key: 'RUNNING', display_name: '开课中' },
  { key: 'END', display_name: '已结束' },
  { key: 'REJECTED', display_name: '被拒绝' }
]

const courseTypeKeyValue = courseTypeOptions.reduce((acc, sch) => {
  acc[sch.key] = sch.display_name
  return acc
}, {})

export default {
  name: 'CheckCourse',
  components: { PanThumb, Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 0,
        limit: 20,
        title: ''
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchCourses(this.listQuery).then(response => {
        this.list = response.data.items
        this.total = response.data.total
        // Just to simulate the time of the request
        this.listLoading = false
      })
    },
    courseFilter(type) {
      return courseTypeKeyValue[type]
    },
    approve(row) {
      checkCourse(row.id, 'CHECKED').then(response => {
        this.$notify({
          title: '成功',
          message: '已通过',
          type: 'success',
          duration: 2000
        })
        location.reload()
      })
    },
    reject(row) {
      checkCourse(row.id, 'REJECTED').then(response => {
        this.$notify({
          title: '成功',
          message: '已拒绝',
          type: 'success',
          duration: 2000
        })
        location.reload()
      })
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
