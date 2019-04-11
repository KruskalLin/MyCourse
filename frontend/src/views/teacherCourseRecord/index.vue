<template>
  <div class="dashboard-editor-container">
    <div class="filter-container">
      <el-input :placeholder="$t('table.title')" v-model="listQuery.title" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-select v-model="listQuery.type" class="filter-item" placeholder="请选择">
        <el-option
          v-for="item in typeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"/>
      </el-select>
      <el-date-picker
        v-model="listQuery.time"
        type="daterange"
        class="filter-item"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="yyyy-MM-dd"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;">
      <el-table-column :label="$t('table.course')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.name')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.teacherName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.time')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.time | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.operation')" class-name="status-col" width="150px">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.operation === 'QUIT'" type="warning">{{ operationFilter(scope.row.operation) }}</el-tag>
          <el-tag v-if="scope.row.operation === 'SELECT'" type="primary">{{ operationFilter(scope.row.operation) }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { fetchTeacherRecordList } from '@/api/course'
import waves from '@/directive/waves' // Waves directive

const operationTypeOptions = [
  { key: 'QUIT', display_name: '退课' },
  { key: 'SELECT', display_name: '选课' }
]

const operationTypeKeyValue = operationTypeOptions.reduce((acc, sch) => {
  acc[sch.key] = sch.display_name
  return acc
}, {})

export default {
  name: 'TeacherCourseRecord',
  directives: { waves },
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        title: '',
        type: '16',
        time: []
      },
      operationTypeOptions,
      typeOptions: [{ value: '16', label: '16级学生' },
        { value: '15', label: '15级学生' }]
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
      fetchTeacherRecordList(this.listQuery).then(response => {
        this.list = response.data
        this.listLoading = false
      })
    },
    operationFilter(type) {
      return operationTypeKeyValue[type]
    },
    handleFilter() {
      this.getList()
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
