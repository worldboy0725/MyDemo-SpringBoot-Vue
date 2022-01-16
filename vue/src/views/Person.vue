<template>
  <div>
    <el-card style="width: 40%; margin: 10px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item style="text-align: center" label-width="0">
          <!--          <el-upload-->
          <!--              class="avatar-uploader"-->
          <!--              action="http://localhost:9090/files/upload"-->
          <!--              :show-file-list="false"-->
          <!--              :on-success="handleAvatarSuccess">-->
          <!--            <img v-if="form.avatar" :src="form.avatar" class="avatar">-->
          <!--            <i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
          <!--          </el-upload>-->
        </el-form-item>
        <el-form-item label="帳號">
          <el-input v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="暱稱">
          <el-input v-model="form.nickName"></el-input>
        </el-form-item>
        <el-form-item label="年齡">
          <el-input v-model="form.age"></el-input>
        </el-form-item>
        <el-form-item label="性別">
          <el-input v-model="form.sex"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address"></el-input>
        </el-form-item>
        <!--        <el-form-item label="密码">-->
        <!--          <el-input v-model="form.password" show-password></el-input>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="余额(￥)">-->
        <!--          <el-input v-model="form.account" show-password></el-input>-->
        <!--        </el-form-item>-->
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="update">保存</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "../utils/request";

export default {
  name: "Person",
  data() {
    return {
      form: {}
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}"
    this.form = JSON.parse(str)
  },
  methods: {
    update() {
      request.put(this.$myGlobal.API + "user", this.form).then(res => {
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "更新成功"
          })
          sessionStorage.setItem("user", JSON.stringify(this.form))
          // 觸發Layout更新用户信息
          this.$emit("userInfo")
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>