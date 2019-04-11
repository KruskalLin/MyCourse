<template>
  <div class="dashboard-editor-container">
    <span style="font-weight: bold; font-size: 35px;">论坛</span>
    <el-table
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-top: 20px;">
      <el-table-column :label="$t('table.user')" min-width="100px">
        <template slot-scope="scope">
          <div><pan-thumb :image="avatar" class="panThumb"/></div>
          <div style="margin: 10px">{{ scope.row.username }}</div>
          <div style="margin: 10px">{{ scope.row.createdAt | parseTime('{y}-{m}-{d}') }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.info')" width="900px" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <code v-if="scope.row.parentName === null"><div style="font-weight: bold">主题:</div> {{ scope.row.name }}</code>
          <code v-if="scope.row.parentName != null"><div style="font-weight: bold">回复:</div> {{ scope.row.parentName }}</code>
          <div v-html="scope.row.desc"/>
          <el-button style="background:#1890ff;borderColor:#1890ff; margin-top: 5px;" icon="el-icon-edit" size="mini" type="primary" @click="parentId = scope.row.id">回复</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="parentId !== null" style="margin-top: 40px;">
      <el-button style="background:#1890ff;borderColor:#1890ff" icon="el-icon-upload" size="mini" type="primary" @click="post">发布帖子</el-button>
      <Tinymce ref="editor" :height="400" v-model="content" style="margin-top: 5px;"/>
    </div>
    <el-tooltip placement="top" content="回到顶部">
      <back-to-top :custom-style="myBackToTopStyle" :visibility-height="300" :back-position="50" transition-name="fade"/>
    </el-tooltip>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import PanThumb from '@/components/PanThumb'
import { fetchPosts, createPost } from '@/api/course'
import waves from '@/directive/waves' // Waves directive
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import Tinymce from '@/components/Tinymce'
import BackToTop from '@/components/BackToTop'

export default {
  name: 'Forum',
  components: { PanThumb, Pagination, Tinymce, BackToTop },
  directives: { waves },
  data() {
    return {
      list: null,
      content: '',
      parentId: null,
      myBackToTopStyle: {
        right: '50px',
        bottom: '50px',
        width: '40px',
        height: '40px',
        'border-radius': '4px',
        'line-height': '45px', // 请保持与高度一致以垂直居中 Please keep consistent with height to center vertically
        background: '#e7eaf1'// 按钮的背景颜色 The background color of the button
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
      fetchPosts(this.$route.query.id).then(response => {
        this.list = response.data
      })
    },
    post() {
      createPost(this.parentId, this.content).then(response => {
        this.$notify({
          title: '成功',
          message: '创建成功',
          type: 'success',
          duration: 2000
        })
        location.reload()
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss"  scoped>
  .panThumb {
    height: 70px!important;
    width: 70px!important;
    border: 5px solid #ffffff;
    background-color: #fff;
    box-shadow: none!important;
  /deep/ .pan-info {
    box-shadow: none!important;
  }
  }
  .dashboard-editor-container {
    background-color: #FFFFFF;
    min-height: 100vh;
    padding: 50px 60px 0px;
  }
</style>
