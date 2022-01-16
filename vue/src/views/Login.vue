<template>
  <div style="width: 100%; height: 100vh; overflow: hidden">
    <div style="width: 400px; margin: 100px auto">
      <el-card>
        <div style="color: #cccccc; font-size: 30px; text-align: center; padding: 30px 0">歡迎登入</div>
        <el-form ref="form" :model="form" size="normal" :rules="rules">
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user-solid" v-model="form.username">請輸入帳號</el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" v-model="form.password" show-password>請輸入密碼</el-input>
          </el-form-item>
          <el-form-item>
            <div style="display: flex">
              <el-input prefix-icon="el-icon-key" v-model="form.validCode" style="width: 50%"
                        placeholder="請輸入驗證碼"></el-input>
              <ValidCode v-on:input="createValidCode"/>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 100%" type="primary" @click="login">登入</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="text" @click="$router.push('/register')">立即註冊 >></el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <!--    登入-->
  </div>
</template>

<script>
import request from "../utils/request";
import ValidCode from "../components/ValidCode";

export default {
  name: "Login",
  components: {ValidCode},
  component: {
    ValidCode
  },
  data() {
    return {
      form: {},
      rules: {
        username: [
          {required: true, message: '請輸入帳號', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '請輸入密碼', trigger: 'blur'}
        ]
      },
      validCode: ''
    }
  },
  created() {
    sessionStorage.removeItem("user")
  },
  methods: {
    // 接收驗證碼組件提交的 4位驗證碼
    createValidCode(data) {
      this.validCode = data
    },
    login() {
      this.$refs['form'].validate((valid) => { // 判斷效驗規則
        if (valid) {
          if (!this.form.validCode) {
            this.$message.error("請填寫驗證碼")
            return;
          }
          if (this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()) { // 處理驗證碼會有小寫的問題
            this.$message.error("驗證碼錯誤")
            return;
          }
          request.post(this.$myGlobal.API + "user/login", this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "登入成功"
              })
              sessionStorage.setItem("user", JSON.stringify(res.data))  // 缓存用户信息
              // 登录成功的时候更新当前路由
              // activeRouter()
              this.$router.push("/").catch(()=>{
                console.info(error.message)
              }) // 登入成功後跳回主頁
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