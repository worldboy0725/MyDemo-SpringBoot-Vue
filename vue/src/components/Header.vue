<template>
  <div style="height: 50px; line-height: 50px; border-bottom: 1px solid #ccc; display: flex">
    <div style="width: 200px; padding-left: 30px; font-weight: bold; color: dodgerblue">後台管理</div>
    <div style="flex: 1"></div>
    <div style="width: 100px">
      <el-dropdown>
        <span class="el-dropdown-link">
<!--          管理員-->
<!--       <el-avatar :size="30" :src="user.avatar" style="position: relative; top: 10px"></el-avatar>-->
           {{ name }}
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <template #dropdown>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="$router.push('/person')">個人中心</el-dropdown-item>
            <!--          <el-dropdown-item @click="$router.push('/password')">修改密碼</el-dropdown-item>-->
            <el-dropdown-item @click.native="$router.push('/login')">登出</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>

export default {
  name: "Header",
  data() {
    return {
      name: {}
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}"
    let strName = JSON.parse(str)
    this.name = strName.username
  }
}
</script>

<style scoped>

</style>