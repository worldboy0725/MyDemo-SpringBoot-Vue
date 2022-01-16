<template>
  <div style="width: 100%; height: 100vh; overflow: hidden">
    <div style="width: 400px; margin: 100px auto">
      <el-card>
        <div style="color: #cccccc; font-size: 30px; text-align: center; padding: 30px 0">註冊</div>
        <el-form ref="form" :model="form" size="normal" :rules="rules">
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user-solid" v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" v-model="form.password" show-password></el-input>
          </el-form-item>
          <el-form-item prop="confirm">
            <el-input prefix-icon="el-icon-lock" v-model="form.confirm" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 100%" type="primary" @click="register">註冊</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="text" @click="$router.push('/login')">&lt;&lt; 返回登入</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
import request from "../utils/request";

export default {
  name: "Register",
  data() {
    return {
      form: {},
      rules: {
        username: [
          {required: true, message: '請輸入帳號', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '請輸入密碼', trigger: 'blur'}
        ],
        confirm: [
          {required: true, message: '請確認密碼', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    register() {
      if (this.form.password !== this.form.confirm) {
        this.$message({
          type: "error",
          message: '兩次密碼輸入不一致'
        })
        return
      }

      this.$refs['form'].validate((valid) => { // 滿足效驗規則
        if (valid) {
          request.post(this.$myGlobal.API + "user/register", this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "註冊成功"
              })
              this.$router.push("/login") // 登入成功後跳回主頁
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        } else {
          this.$message({
            type: "error",
            message: '請輸入帳號及密碼'
          })
          return
        }
      })
    }
  }
}
</script>

<style scoped>

</style>