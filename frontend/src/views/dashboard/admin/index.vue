<template>
  <div class="dashboard-editor-container">

    <github-corner style="position: absolute; top: 0px; border: 0; right: 0;"/>
    <el-row :gutter="40" class="panel-group">
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-people">
            <svg-icon icon-class="peoples" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">使用人数</div>
            <count-to :start-val="0" :end-val="student + teacher" :duration="1000" class="card-panel-num"/>
          </div>
        </div>
      </el-col>
    </el-row>
    <ve-histogram :data="chartData"/>
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
      <el-table-column :label="$t('table.name')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.author')" min-width="150px">
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
import GithubCorner from '@/components/GithubCorner'
import CountTo from 'vue-count-to'
import 'v-charts/lib/style.css'
import { getStudents, getTeachers, fetchRecordList } from '@/api/data'
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
  name: 'DashboardAdmin',
  directives: { waves },
  components: {
    GithubCorner,
    CountTo
  },
  data() {
    return {
      student: 0,
      teacher: 0,
      chartData: {
        columns: ['学生/老师', '学生', '老师'],
        rows: [
        ]
      },
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        title: '',
        type: 'TEACHER',
        time: []
      },
      operationTypeOptions,
      typeOptions: [{ value: 'NAME', label: '搜索课程' },
        { value: 'TEACHER', label: '搜索老师' }]
    }
  },
  created() {
    this.getUsers()
    this.getList()
  },
  methods: {
    getUsers() {
      getStudents().then(response => {
        this.student = response.data
        getTeachers().then(response => {
          this.teacher = response.data
          this.chartData.rows.push({ '学生/老师': '使用人数', '学生': this.student, '老师': this.teacher })
        })
      })
    },
    getList() {
      fetchRecordList(this.listQuery).then(response => {
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

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
}
.panel-group {
  margin-top: 18px;
  .card-panel-col{
    margin-bottom: 32px;
  }
  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
      .icon-people {
        background: #40c9c6;
      }
      .icon-message {
        background: #36a3f7;
      }
      .icon-money {
        background: #f4516c;
      }
      .icon-shopping {
        background: #34bfa3
      }
    }
    .icon-people {
      color: #40c9c6;
    }
    .icon-message {
      color: #36a3f7;
    }
    .icon-money {
      color: #f4516c;
    }
    .icon-shopping {
      color: #34bfa3
    }
    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }
    .card-panel-icon {
      float: left;
      font-size: 48px;
    }
    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;
      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}
</style>
