<template>
  <div class="dashboard-container">
    <component :is="currentRole"/>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import adminDashboard from './admin'
import teacherDashboard from './teacher'
import studentDashboard from './student'

export default {
  name: 'Dashboard',
  components: { adminDashboard, teacherDashboard: teacherDashboard, studentDashboard: studentDashboard },
  data() {
    return {
      currentRole: 'adminDashboard'
    }
  },
  computed: {
    ...mapGetters([
      'roles'
    ])
  },
  created() {
    if (this.roles.includes('teacher')) {
      this.currentRole = 'teacherDashboard'
    } else if (this.roles.includes('student')) {
      this.currentRole = 'studentDashboard'
    } else {
      this.currentRole = 'adminDashboard'
    }
  }
}
</script>
