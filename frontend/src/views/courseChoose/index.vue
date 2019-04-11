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
      <el-table-column :label="$t('table.time')" width="150px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.time }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.limit')" width="110px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.choosed }} / {{ scope.row.limit }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.status')" width="110px" align="center">
        <template slot-scope="scope">
          <div v-if="scope.row.courseState === 'CHECKED'">
            <el-tag>{{ courseFilter(scope.row.courseState) }}</el-tag>
          </div>
          <div v-if="scope.row.courseState === 'RUNNING'">
            <el-tag type="success">{{ courseFilter(scope.row.courseState) }}</el-tag>
          </div>
          <div v-if="scope.row.courseState === 'DRAFT'">
            <el-tag type="info">{{ courseFilter(scope.row.courseState) }}</el-tag>
          </div>
          <div v-if="scope.row.courseState === 'END'">
            <el-tag type="warning">{{ courseFilter(scope.row.courseState) }}</el-tag>
          </div>
          <div v-if="scope.row.courseState === 'REJECTED'">
            <el-tag type="danger">{{ courseFilter(scope.row.courseState) }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <div v-if="scope.row.hasChoosed === false">
            <el-button type="primary" @click="select(scope.row)">{{ $t('table.select') }}</el-button>
          </div>
          <div v-if="scope.row.hasChoosed === true">
            <el-tag type="primary">{{ $t('table.selected') }}</el-tag>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import PanThumb from '@/components/PanThumb'
import { fetchChooseList, chooseCourse } from '@/api/course'
import waves from '@/directive/waves' // Waves directive
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

const courseTypeOptions = [
  { key: 'DRAFT', display_name: '待审核' },
  { key: 'CHECKED', display_name: '未开课' },
  { key: 'RUNNING', display_name: '开课中' },
  { key: 'END', display_name: '已结束' },
  { key: 'REJECTED', display_name: '被拒绝' }
]

const courseTypeKeyValue = courseTypeOptions.reduce((acc, sch) => {
  acc[sch.key] = sch.display_name
  return acc
}, {})

const semesterTypeOptions = [
  { key: 'SPRING', display_name: '春季' },
  { key: 'SUMMER', display_name: '夏季' },
  { key: 'AUTUMN', display_name: '秋季' },
  { key: 'WINTER', display_name: '冬季' }

]

const semesterTypeKeyValue = semesterTypeOptions.reduce((acc, sch) => {
  acc[sch.key] = sch.display_name
  return acc
}, {})

export default {
  name: 'ChooseCourse',
  components: { PanThumb, Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      courseTypeOptions,
      listQuery: {
        page: 0,
        limit: 20,
        title: ''
      }
    }
  },
  computed: {
    ...mapGetters([
      'name',
      'avatar',
      'roles',
      'introduction'
    ])
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchChooseList(this.listQuery).then(response => {
        this.list = response.data.items
        this.total = response.data.total
        this.listLoading = false
      })
    },
    select(row) {
      chooseCourse(row.id).then(response => {
        this.$message({
          message: '选课成功',
          type: 'success'
        })
        location.reload()
      })
    },
    courseFilter(type) {
      return courseTypeKeyValue[type]
    },
    semesterFilter(type) {
      return semesterTypeKeyValue[type]
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
