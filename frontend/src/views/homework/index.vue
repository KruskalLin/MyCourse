<template>
  <div class="components-container">
    <code>
      {{ intro }}
    </code>
    <span style="font-size: 18px; font-weight: bold;">作业说明文件</span>
    <div style="margin-top: 20px;">
      <a v-for="i in urlList" :key="i" :href="'http://localhost:8000/homeworkHandout/' + + param.id + '/' + i" class="el-icon-download download">{{ i }}</a>
    </div>
    <el-upload
      v-if="roles.indexOf('teacher') >= 0"
      :data="param"
      style="margin-top: 10px; margin-bottom: 10px;"
      drag
      action="http://localhost:8000/upload/homeworkHandout"
      multiple>
      <i class="el-icon-upload"/>
      <div class="el-upload__text">将作业说明文件拖到此处，或<em>点击上传</em></div>
    </el-upload>
    <el-form ref="homework" :model="homework" class="form-container" style="margin-top: 20px;">
      <el-row>
        <el-col :span="8">
          <el-form-item label-width="80px" label="截至日期:" class="postInfo-container-item">
            {{ homework.time }}
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item v-if="roles.indexOf('student') >= 0" label-width="80px" label="当前状态:" class="postInfo-container-item">
            {{ homework.status }}
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div class="editor-container">
      <el-upload
        v-if="roles.indexOf('student') >= 0"
        :data="param"
        :headers="headers"
        :file-list="fileList"
        :on-preview="preview"
        :on-success="success"
        :on-remove="remove"
        accept=".zip"
        style="margin-top: 10px; margin-bottom: 10px;"
        drag
        action="http://localhost:8000/upload/homework">
        <i class="el-icon-upload"/>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip">只允许.zip，不超过20MB，不同文件名都会改成您的学号</div>
      </el-upload>
    </div>
  </div>
</template>

<script>
import Dropzone from '@/components/Dropzone'
import { getHomeworkDetail, getHomeworkFile, removeHomeworkFile, fetchHomeworkHandout } from '@/api/course'
import { getToken } from '@/utils/auth'
import { mapGetters } from 'vuex'

export default {
  name: 'Homework',
  components: { Dropzone },
  data() {
    return {
      homework: {
        time: '2018-9-10',
        status: '未提交'
      },
      param: {
        id: 0
      },
      intro: '作业XXX',
      fileList: [],
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      url: 'http://localhost:8000/',
      urlList: null,
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
    this.initHomework()
    this.getHomeworkHandout()
  },
  methods: {
    getHomeworkHandout() {
      fetchHomeworkHandout(this.$route.query.id).then(response => {
        this.urlList = response.data
      })
    },
    initHomework() {
      getHomeworkDetail(this.$route.query.id).then(response => {
        this.homework.time = response.data.endDate
        this.intro = response.data.desc
      })
      getHomeworkFile(this.$route.query.id).then(response => {
        if (response.data !== null && response.data !== '') {
          this.fileList.push({
            name: response.data.substring(response.data.lastIndexOf('/') + 1),
            url: this.url + response.data
          })
          this.homework.status = '已提交'
        }
      })
    },
    preview(file) {
      window.open(file.url)
    },
    success(response, file, fileList) {
      this.homework.status = '已提交'
    },
    remove(file, fileList) {
      removeHomeworkFile(this.$route.query.id).then(response => {
        this.$message({
          message: '删除成功',
          type: 'success'
        })
        this.homework.status = '未提交'
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
