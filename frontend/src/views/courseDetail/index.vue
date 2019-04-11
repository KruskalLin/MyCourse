<template>
  <div>
    <el-row>
      <el-col :span="16" style="padding: 20px;">
        <div class="title">
          {{ title }}
        </div>
        <div style="font-size: large; font-weight: bold">课件列表</div>
        <el-upload
          :on-success="success"
          :data="param"
          style="margin-top: 10px; margin-bottom: 10px;"
          drag
          action="http://localhost:8000/upload/coursewares"
          multiple>
          <i class="el-icon-upload"/>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div slot="tip" class="el-upload__tip">不超过20MB</div>
        </el-upload>
        <div>
          <a v-for="i in urlList" :key="i" :href="'http://localhost:8000/course/' + param.id + '/coursewares/' + i" class="el-icon-download download">{{ i }}</a>
        </div>
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

  </div>
</template>

<script>

import { fetchCoursewares, postTopic, fetchPostTopic, fetchCourse } from '@/api/course'

export default {
  name: 'CourseDetail',
  data() {
    return {
      title: 'J2EE与中间件',
      list: null,
      param: {
        id: 0
      },
      urlList: [
      ],
      dialogFormVisible: false,
      temp: {
        name: '',
        desc: ''
      }
    }
  },
  created() {
    this.param.id = this.$route.query.id
    this.getCourse()
    this.getList()
    this.getPostList()
  },
  methods: {
    getCourse() {
      fetchCourse(this.$route.query.id).then(response => {
        console.log(response.data)
        this.title = response.data.name
      })
    },
    getList() {
      fetchCoursewares(this.$route.query.id).then(response => {
        this.urlList = response.data
        console.log(this.urlList)
      })
    },
    getPostList() {
      fetchPostTopic(this.$route.query.id).then(response => {
        this.list = response.data
        console.log(this.list)
      })
    },
    forum(row) {
      this.$router.push({ name: 'Forum', query: { id: row.id }})
    },
    postData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          postTopic(this.temp.name, this.temp.desc, this.$route.query.id).then(() => {
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
    success() {
      location.reload()
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

