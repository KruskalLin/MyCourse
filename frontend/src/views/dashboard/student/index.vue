<template>
  <div class="dashboard-editor-container">
    <div class=" clearfix">
      <pan-thumb :image="avatar" style="float: left"> 您的身份:
        <span v-for="item in roles" :key="item" class="pan-info-roles">{{ item }}</span>
      </pan-thumb>
      <div class="info-container">
        <span class="display_name">{{ name }}</span>
        <span>{{ degreeFilter(degree) }}     </span>
        <span>{{ grade }}级    </span>
        <span style="font-size:20px;padding-top:20px;display:inline-block;">{{ number }}</span>
      </div>
      <el-button type="primary" style="float: right" @click="dialogFormVisible = true">修改个人信息</el-button>
    </div>
    <el-dialog :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="temp" label-position="left" label-width="80px" style="width: 500px; margin-left:50px;">
        <el-form-item :label="$t('table.name')" prop="title">
          <el-input v-model="temp.name"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button type="primary" @click="updateData()">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import PanThumb from '@/components/PanThumb'
import waves from '@/directive/waves' // Waves directive
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { updateUserInfo } from '@/api/login'

const degreeTypeOptions = [
  { key: 'BACHELOR', display_name: '本科生' },
  { key: 'MASTER', display_name: '硕士生' },
  { key: 'DOCTER', display_name: '博士生' }
]

const degreeTypeKeyValue = degreeTypeOptions.reduce((acc, sch) => {
  acc[sch.key] = sch.display_name
  return acc
}, {})

export default {
  name: 'DashboardStudent',
  components: { PanThumb, Pagination },
  directives: { waves },
  data() {
    return {
      dialogFormVisible: false,
      temp: {
        name: ''
      }
    }
  },
  computed: {
    ...mapGetters([
      'name',
      'grade',
      'degree',
      'avatar',
      'roles',
      'number'
    ])
  },
  created() {
  },
  methods: {
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateUserInfo(tempData.name).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
            location.reload()
          })
        }
      })
    },
    degreeFilter(type) {
      return degreeTypeKeyValue[type]
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
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

