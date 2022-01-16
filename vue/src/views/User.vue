<template>
  <div style="padding: 10px">
    <!--    功能區域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary">導入</el-button>
      <el-button type="primary">導出</el-button>
    </div>

    <!--    搜索區域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="姓名搜尋" style="width: 20% " clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查詢</el-button>
    </div>

    <el-table size="small" :data="tableData" stripe style="width: 100%">
      <el-table-column
          prop="id" label="ID" sortable>
      </el-table-column>
      <el-table-column
          prop="username" label="姓名">
      </el-table-column>
      <el-table-column
          prop="nickName" label="暱稱">
      </el-table-column>
      <el-table-column
          prop="age" label="年齡">
      </el-table-column>
      <el-table-column
          prop="sex" label="性別">
      </el-table-column>
      <el-table-column
          prop="address" label="地址">
      </el-table-column>
      <el-table-column
          prop="address" label="狀態">
        <template #default="scope">
          <span v-if="scope.row.role ===1">管理員</span>
          <span v-else-if="scope.row.role ===2">使用者</span>
          <span v-else="">使用者</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
<!--          <el-button size="mini" type="success" plain @click="showBooks(scope.row.bookList)">查看圖書列表</el-button>-->
          <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)" >編輯</el-button>
          <el-popconfirm title="確認刪除？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger">刪除
              </el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!--    分頁-->
    <div style="margin: 10px 0">
      <el-pagination
          align="center"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="已擁有的書籍" v-model="bookVis" width="30%">
      <el-table :data="bookList" stripe border>
        <el-table-column prop="id" label="編號"></el-table-column>
        <el-table-column prop="name" label="書名"></el-table-column>
        <el-table-column prop="price" label="價格"></el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog title="新增" :visible.sync="dialogVisible" width="30%">
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="姓名">
          <el-input v-model="form.username" style="width: 80%;"></el-input>
        </el-form-item>
        <el-form-item label="暱稱">
          <el-input v-model="form.nickName" style="width: 80%;"></el-input>
        </el-form-item>
        <el-form-item label="年齡">
          <el-input v-model="form.age" style="width: 80%;"></el-input>
        </el-form-item>
        <el-form-item label="性別">
          <el-radio-group v-model="form.sex">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
            <el-radio label="未知">未知</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="地址">
          <el-input type="textarea" v-model="form.address" style="width: 80%;"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">確 定</el-button>
        </span>
    </el-dialog>

  </div>
</template>

<style>

</style>

<script>

import request from "../utils/request";
import router from "../router";

export default {
  name: 'User',
  components: {},
  data() {
    return {
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      bookList: [],
      bookVis: false,
      user: [],
      role: ''
    }
  },
  created() { //頁面加載的方法
    this.load()
    let str = sessionStorage.getItem("user") || "{}"
    let strName = JSON.parse(str)
    this.role = strName.role
    if (this.role !== 1) {
      router.push("/books")
    }
  },
  methods: {
    showBooks(books) {
      this.bookList = books
      console.log("安安")
      this.bookVis = true
    },
    load() {
      request.get(this.$myGlobal.API + "user", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add() {
      this.form = {}
      this.dialogVisible = true
    },
    save() {
      if (this.form.id) { // 更新
        request.put(this.$myGlobal.API + "user", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load()
          this.dialogVisible = false
        })
      } else { // 新增
        request.post(this.$myGlobal.API + "user", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load()
          this.dialogVisible = false
        })
      }
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row)) // 避免影響表單數據，使用深拷貝，獨立出來
      this.dialogVisible = true
    },
    handleDelete(id) {
      console.log(id)
      request.delete(this.$myGlobal.API + "user/" + id).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "刪除成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load()
      })
    },
    handleSizeChange(pageSize) { // 當前每頁個數改變觸發
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) { // 改變當前頁碼觸發
      this.currentPage = pageNum
      this.load()
    }
  }
}

</script>
