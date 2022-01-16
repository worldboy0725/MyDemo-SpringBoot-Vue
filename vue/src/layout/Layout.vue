<template>
  <div>
    <!--    頭部-->
    <Header :user="user"/>
    <!--    主體-->
    <div style="display: flex">
      <!--      側邊欄-->
      <Aside/>
      <!--      內容區域-->
      <router-view style="flex: 1"/>
    </div>
  </div>
</template>

<script>


import Header from "../components/Header";
import Aside from "../components/Aside";
import request from "../utils/request";

export default {
  name: "Layout",
  components: {
    Header,
    Aside,
  },
  data() {
    return {
      user: {}
    }
  },
  created() {
    this.refreshUser()
  },
  methods: {
    refreshUser() {
      let userJson = sessionStorage.getItem("user");
      if (!userJson) {
        return
      }
      let userId = JSON.parse(userJson).id
      // 從後台取出更新後的最新帳戶資訊
      request.get(this.$myGlobal.API + "user/" + userId).then(res => {
        this.user = res.data
      })
    }
  }
}
</script>

<style scoped>

</style>