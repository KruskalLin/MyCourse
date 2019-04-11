<template>
  <div class="createPost-container">
    <el-form ref="postForm" :model="postForm" class="form-container">

      <sticky :class-name="'sub-navbar'">
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">发布
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-row>
          <el-col :span="24">
            <div class="postInfo-container">
              <el-row>
                <el-col :span="8">
                  <el-form-item label-width="80px" label="限制人数:" class="postInfo-container-item">
                    <el-input-number v-model="postForm.limit"/>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-col>
          <el-button type="primary" size="tiny" @click="dialogFormVisible = true">创建时间</el-button>
          <el-form-item style="margin-top: 10px;" prop="time">
            <el-table
              :data="postForm.time"
              border
              fit
              highlight-current-row
              style="width: 100%;">
              <el-table-column :label="$t('table.week')" min-width="150px">
                <template slot-scope="scope">
                  <span>{{ scope.row.week }}</span>
                </template>
              </el-table-column>
              <el-table-column :label="$t('table.weekTime')" width="150px">
                <template slot-scope="scope">
                  <span>{{ scope.row.weekStart }}-{{ scope.row.weekEnd }}周</span>
                </template>
              </el-table-column>
              <el-table-column :label="$t('table.classroom')" width="150px">
                <template slot-scope="scope">
                  <span>教室{{ scope.row.classroom }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-row>
      </div>
    </el-form>
    <el-dialog :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="temp" label-position="left" label-width="80px" style="width: 500px; margin-left:50px;">
        <el-form-item :label="$t('table.week')" prop="title">
          <el-select v-model="temp.week">
            <el-option
              v-for="item in week"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('table.weekStart')" prop="title">
          <el-input-number v-model="temp.weekStart"/>
        </el-form-item>
        <el-form-item :label="$t('table.weekEnd')" prop="title">
          <el-input-number v-model="temp.weekEnd"/>
        </el-form-item>
        <el-form-item :label="$t('table.classroom')" prop="title">
          <el-input v-model="temp.classroom"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button type="primary" @click="createData">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import Sticky from '@/components/Sticky' // 粘性header组件
import { release } from '@/api/course'

export default {
  name: 'Release',
  components: { Tinymce, Sticky },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      postForm: {
        id: 0,
        limit: 0,
        time: []
      },
      temp: {
        week: '',
        weekStart: 0,
        weekEnd: 0,
        classroom: ''
      },
      loading: false,
      userListOptions: [],
      dialogFormVisible: false,
      week: [{ value: '周一', label: '周一' }, { value: '周二', label: '周二' }, { value: '周三', label: '周三' },
        { value: '周四', label: '周四' }, { value: '周五', label: '周五' },
        { value: '周六', label: '周六' }, { value: '周日', label: '周日' }],
      semester: [{ value: 'SPRING', label: '春季' }, { value: 'SUMMER', label: '夏季' }, { value: 'AUTUMN', label: '秋季' },
        { value: 'WINTER', label: '冬季' }]
    }
  },
  created() {
    this.postForm.id = this.$route.query.id
  },
  methods: {
    submitForm() {
      this.$refs.postForm.validate(valid => {
        if (valid) {
          if (this.postForm.limit <= 0 || this.postForm.time.length === 0){
            this.$notify({
              title: '失败',
              message: '请填写完整',
              type: 'error',
              duration: 2000
            })
            return false
          }
          this.loading = true
          const tempData = Object.assign({}, this.postForm)
          const time = []
          for (let i = 0; i < tempData.time.length; i++) {
            time.push(tempData.time[i].week + ' ' + '周' + tempData.time[i].weekStart + '-' + tempData.time[i].weekEnd + ' ' + tempData.time[i].classroom)
          }
          tempData.time = time
          release(tempData).then(() => {
            this.$notify({
              title: '成功',
              message: '发布文章成功',
              type: 'success',
              duration: 2000
            })
            this.$router.push({ name: 'CourseRelease' })
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    createData() {
      this.postForm.time.push(this.temp)
      this.dialogFormVisible = false
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "~@/styles/mixin.scss";
  .createPost-container {
    position: relative;
    .createPost-main-container {
      padding: 40px 45px 20px 50px;
      .postInfo-container {
        position: relative;
        @include clearfix;
        margin-bottom: 10px;
        .postInfo-container-item {
          float: left;
        }
      }
    }
    .word-counter {
      width: 40px;
      position: absolute;
      right: -10px;
      top: 0px;
    }
  }
</style>
