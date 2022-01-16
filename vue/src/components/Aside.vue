<template>
  <div>
    <el-menu
        style="width: 200px; min-height: calc(100vh - 50px)"
        :default-active="path" router>
      <!-- active屬性根據按鈕點擊變藍 router屬性根據index內容進行跳轉-->

      <el-submenu index="1" v-if="user.role === 1">
        <template slot="title">系統管理</template>
        <el-menu-item index="/user">用戶管理</el-menu-item>
      </el-submenu>
      <el-menu-item index="/books">圖書管理</el-menu-item>
      <el-menu-item index="/news">新聞管理</el-menu-item>

    </el-menu>
  </div>
</template>

<script>
import request from "../utils/request";

export default {
  name: "Aside",
  data() {
    return {
      user: {},
      path: this.$route.path
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)

    // 請求服務端，確認當前登入用戶的狀態
    request.get(this.$myGlobal.API + "user/" + this.user.id).then(res => {
      if (res.code === '0') {
        this.user = res.data
      }
    })
  }
}
</script>

<style scoped>

</style>