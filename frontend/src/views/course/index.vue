<template>
  <div>
    <el-row>
      <el-col :span="16" style="padding: 20px;">
        <el-button v-if="roles.indexOf('student') >= 0" type="danger" size="small" style="float: right" @click="quit">{{ $t('table.quit') }}</el-button>
        <div class="title">
          {{ title }}
        </div>
        <div v-if="roles.indexOf('student') >= 0" style="float: right;">课程成绩：<span style="color: red">{{ grade }}</span></div>
        <div>
          <el-switch
            v-if="roles.indexOf('teacher') >= 0"
            v-model="showGrades"
            active-text="查看全体成绩"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="changeValue"
          />
          <br>
          <el-button v-if="showGrades" style="margin-top: 10px;" @click="dialogTableVisible = true">查看全体成绩</el-button>
          <el-upload
            v-if="roles.indexOf('teacher') >= 0"
            :limit="1"
            :data="param"
            action="http://localhost:8000/upload/grades"
            style="margin-top: 10px;">
            <el-button size="small" type="primary">上传成绩</el-button>
            <div slot="tip" class="el-upload__tip">只能上传excel文件，格式：学号+成绩</div>
          </el-upload>
        </div>
        <div style="font-size: large; font-weight: bold; margin-top: 20px;">课件列表</div>
        <div>
          <a v-for="i in urlList" :key="i" :href="'http://localhost:8000/course/' + param.id + '/coursewares/' + i" class="el-icon-download download">{{ i }}</a>
        </div>
        <div style="font-size: large; font-weight: bold; margin-top: 20px; margin-bottom: 10px;">作业</div>
        <el-button v-if="roles.indexOf('teacher') >= 0" type="primary" size="small" style="float: right; margin-bottom: 20px;" @click="homeworkVisible = true">{{ $t('table.addHomework') }}</el-button>
        <el-table
          :data="homework"
          border
          fit
          highlight-current-row
          style="width: 100%;">
          <el-table-column :label="$t('table.desc')" min-width="150px">
            <template slot-scope="scope">
              <span @click="show(scope.row)">{{ scope.row.desc }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('table.status')" class-name="status-col" width="100">
            <template slot-scope="scope">
              <el-tag v-if="compare(scope.row.endDate)">正在进行</el-tag>
              <el-tag v-else type="danger">已结束</el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="$t('table.endTime')" class-name="status-col" width="100">
            <template slot-scope="scope">
              <span>{{ scope.row.endDate | parseTime('{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="roles.indexOf('student') >= 0" :label="$t('table.grade')" class-name="status-col" width="100">
            <template slot-scope="scope">
              <span v-if="scope.row.grade">{{ scope.row.grade }}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="roles.indexOf('teacher') >= 0" :label="$t('table.grade')" class-name="status-col" width="100">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="showGradesList(scope.row)">查看成绩</el-button>
            </template>
          </el-table-column>
          <el-table-column :label="$t('table.actions')" align="center" width="210" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-upload
                v-if="roles.indexOf('teacher') >= 0"
                :limit="1"
                :data="homeworkParam"
                :before-upload="paramChange(scope.row.id)"
                action="http://localhost:8000/upload/homeworkGrades"
                style="float: left">
                <el-button size="small" type="warning">上传作业成绩</el-button>
              </el-upload>
              <el-button v-if="roles.indexOf('student') >= 0 && compare(scope.row.endDate)" type="primary" size="small" @click="show(scope.row)">{{ $t('table.show') }}</el-button>
              <el-button v-if="roles.indexOf('teacher') >= 0" type="primary" size="small" @click="download(scope.row)">{{ $t('table.download') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span="8" style="padding: 20px">
        <el-button type="primary" size="small" style="float: right" @click="dialogFormVisible = true">{{ $t('table.post') }}</el-button>
        <el-table :data="list" style="width: 100%;padding-top: 15px;">
          <el-table-column label="主题" min-width="200">
            <template slot-scope="scope" >
              <div class="link-type" @click="forum(scope.row)">
                {{ scope.row.name }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建人" width="100" align="center">
            <template slot-scope="scope">
              {{ scope.row.username }}
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <el-dialog :visible.sync="dialogHomeworkVisible">
      <el-table
        :data="homeworkGradesList"
        border
        fit
        highlight-current-row
        style="width: 100%;">
        <el-table-column :label="$t('table.number')" min-width="150px">
          <template slot-scope="scope">
            <span>{{ scope.row.number }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.grades')" width="200">
          <template slot-scope="scope">
            <template v-if="scope.row.edit">
              <el-input v-model="scope.row.grades" type="number" class="edit-input" size="small" />
              <el-button class="cancel-btn" size="small" icon="el-icon-refresh" type="warning" @click="cancelGradesEdit(scope.row)">cancel</el-button>
            </template>
            <span v-else>{{ scope.row.grades }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="roles.indexOf('teacher') >= 0" :label="$t('table.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button v-if="scope.row.edit" type="success" size="small" icon="el-icon-circle-check-outline" @click="confirmGradesEdit(scope.row)">Ok</el-button>
            <el-button v-else type="primary" size="small" icon="el-icon-edit" @click="scope.row.edit=!scope.row.edit">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogHomeworkVisible = false">{{ $t('table.cancel') }}</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="temp" label-position="left" label-width="80px" style="width: 500px; margin-left:50px;">
        <el-form-item :label="$t('table.name')" prop="title">
          <el-input v-model="temp.name"/>
        </el-form-item>
        <el-form-item :label="$t('table.desc')" prop="title">
          <el-input
            :rows="2"
            v-model="temp.desc"
            type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button type="primary" @click="postData()">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="homeworkVisible">
      <el-form ref="homeworkForm" :model="tempWork" label-position="left" label-width="80px" style="width: 500px; margin-left:50px;">
        <el-form-item :label="$t('table.endTime')" prop="title">
          <el-date-picker
            v-model="tempWork.endDate"
            value-format="yyyy-MM-dd"
            type="date"
            placeholder="选择日"/>
        </el-form-item>
        <el-form-item :label="$t('table.desc')" prop="title">
          <el-input
            :rows="2"
            v-model="tempWork.desc"
            type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="homeworkVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button type="primary" @click="postHomework()">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogTableVisible">
      <el-table
        :data="gradesList"
        border
        fit
        highlight-current-row
        style="width: 100%;">
        <el-table-column :label="$t('table.number')" min-width="150px">
          <template slot-scope="scope">
            <span>{{ scope.row.number }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.grades')" width="300">
          <template slot-scope="scope">
            <template v-if="scope.row.edit">
              <el-input v-model="scope.row.grades" type="number" class="edit-input" size="small" />
              <el-button class="cancel-btn" size="small" icon="el-icon-refresh" type="warning" @click="cancelEdit(scope.row)">cancel</el-button>
            </template>
            <span v-else>{{ scope.row.grades }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="roles.indexOf('teacher') >= 0" :label="$t('table.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button type="primary" size="small" icon="el-icon-edit" @click="sendEmail(scope.row.number)">赠送邮件</el-button>
            <el-button v-if="scope.row.edit" type="success" size="small" icon="el-icon-circle-check-outline" @click="confirmEdit(scope.row)">Ok</el-button>
            <el-button v-else type="primary" size="small" icon="el-icon-edit" @click="scope.row.edit=!scope.row.edit">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTableVisible = false">{{ $t('table.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { sendMail, changeGrades, changeHomeworkGrades, getHomeworkGradesList, fetchCoursewares, fetchReleaseCourse, downloadHomework, quitCourse, postTopic, fetchPostTopic, fetchCourse, fetchGradesList, fetchGrades, createHomework, getHomework, changeGradesStatus } from '@/api/course'
import { parseTime } from '@/utils/index'

export default {
  name: 'Course',
  data() {
    return {
      title: 'J2EE与中间件',
      content: '',
      courseId: 0,
      showGrades: false,
      list: null,
      homework: null,
      homeworkId: 0,
      gradesList: null,
      homeworkGradesList: null,
      param: {
        id: 0
      },
      homeworkParam: {
        id: 0,
        courseId: 0
      },
      urlList: [
      ],
      dialogFormVisible: false,
      homeworkVisible: false,
      dialogTableVisible: false,
      dialogHomeworkVisible: false,
      temp: {
        name: '',
        desc: ''
      },
      tempWork: {
        id: 0,
        endDate: '',
        desc: ''
      },
      grade: 0,
      url: 'http://localhost:8000/'
    }
  },
  computed: {
    ...mapGetters([
      'name',
      'avatar',
      'roles',
      'number'
    ])
  },
  created() {
    this.param.id = this.$route.query.id
    this.tempWork.id = this.$route.query.id
    this.homeworkParam.courseId = this.$route.query.id
    if (this.roles.indexOf('student') >= 0) {
      this.getGrades()
      if (this.showGrades) {
        this.getGradesList()
      }
    } else {
      this.getGradesList()
    }
    fetchReleaseCourse(this.$route.query.id).then(response => {
      this.courseId = response.data.id
      this.showGrades = response.data.showGrades
      this.getCourse()
    })
    this.getHomeworkList()
  },
  methods: {
    getHomeworkList() {
      getHomework(this.$route.query.id).then(response => {
        this.homework = response.data
      })
    },
    getCourse() {
      fetchCourse(this.courseId).then(response => {
        this.title = response.data.name
      })
      fetchCoursewares(this.courseId).then(response => {
        this.urlList = response.data
      })
      fetchPostTopic(this.courseId).then(response => {
        this.list = response.data
      })
    },
    getGrades() {
      fetchGrades(this.$route.query.id).then(response => {
        this.grade = response.data
      })
    },
    getGradesList() {
      fetchGradesList(this.$route.query.id).then(response => {
        this.gradesList = response.data
        const items = response.data
        this.gradesList = items.map(v => {
          this.$set(v, 'edit', false) // https://vuejs.org/v2/guide/reactivity.html
          v.originalTitle = v.grades //  will be used when user click the cancel botton
          return v
        })
      })
    },
    forum(row) {
      this.$router.push({ name: 'Forum', query: { id: row.id }})
    },
    postData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          postTopic(this.temp.name, this.temp.desc, this.courseId).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            location.reload()
          })
        }
      })
    },
    postHomework() {
      this.$refs['homeworkForm'].validate((valid) => {
        if (valid) {
          createHomework(this.tempWork).then(() => {
            this.homeworkVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            location.reload()
          })
        }
      })
    },
    show(row) {
      this.$router.push({ name: 'Homework', query: { id: row.id }})
    },
    download(row) {
      downloadHomework(row.id).then(response => {
        window.open(this.url + response.data)
      })
    },
    quit() {
      quitCourse(this.$route.query.id).then(() => {
        this.$notify({
          title: '成功',
          message: '退课成功',
          type: 'success',
          duration: 2000
        })
        this.$router.push({ name: 'Dashboard' })
      })
    },
    compare(time) {
      return (new Date(parseTime(time, '{y}-{m}-{d}'))).getTime() > (new Date()).getTime()
    },
    changeValue(value) {
      changeGradesStatus(this.$route.query.id, value).then(response => {
        this.$notify({
          title: '成功',
          message: '修改成功',
          type: 'success',
          duration: 2000
        })
      })
    },
    paramChange(id) {
      this.homeworkParam.id = id
    },
    cancelEdit(row) {
      row.grades = row.originalTitle
      row.edit = false
      this.$message({
        message: '取消修改',
        type: 'warning'
      })
    },
    confirmEdit(row) {
      changeGrades(row.number, this.param.id, row.grades).then(response => {
        row.edit = false
        row.originalTitle = row.grades
        this.$message({
          message: '修改成功',
          type: 'success'
        })
      })
    },
    cancelGradesEdit(row) {
      row.grades = row.originalTitle
      row.edit = false
      this.$message({
        message: '取消修改',
        type: 'warning'
      })
    },
    confirmGradesEdit(row) {
      changeHomeworkGrades(row.number, this.param.id, this.homeworkId, row.grades).then(response => {
        row.edit = false
        row.originalTitle = row.grades
        this.$message({
          message: '修改成功',
          type: 'success'
        })
      })
    },
    showGradesList(row) {
      this.homeworkId = row.id
      getHomeworkGradesList(row.id, this.param.id).then(response => {
        this.homeworkGradesList = response.data
        const items = response.data
        this.homeworkGradesList = items.map(v => {
          this.$set(v, 'edit', false) // https://vuejs.org/v2/guide/reactivity.html
          v.originalTitle = v.grades //  will be used when user click the cancel botton
          return v
        })
        this.dialogHomeworkVisible = true
      })
    },
    sendEmail(number) {
      sendMail(number, this.content).then(response => {
        this.$message({
          message: '发送',
          type: 'success'
        })
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .title {
    font-size: 32px;
    line-height: 48px;
    font-weight: bold;
    margin-bottom: 20px;
    color: #212121;
  }
  .download {
    margin: 15px;
  }
  .download:hover{
    color: #13ce66;
  }
  .emptyGif {
    display: block;
    width: 45%;
    margin: 0 auto;
  }

  .dashboard-editor-container {
    background-color: #FFFFFF;
    min-height: 100vh;
    padding: 50px 60px 0px;
    .pan-info-roles {
      font-size: 12px;
      font-weight: 700;
      color: #333;
      display: block;
    }
    .info-container {
      position: relative;
      margin-left: 190px;
      height: 150px;
      line-height: 200px;
      .display_name {
        font-size: 48px;
        line-height: 48px;
        color: #212121;
        position: absolute;
        top: 25px;
      }
    }
  }
</style>
