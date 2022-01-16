<template>
  <div style="padding: 10px">
    <!--    功能區域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add" v-if="user.role ===1">新增</el-button>
      <el-button type="primary">導入</el-button>
      <el-button type="primary">導出</el-button>
    </div>

    <!--    搜索區域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="書名搜尋" style="width: 20% " clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查詢</el-button>
    </div>

    <el-table size="small" :data="tableData" stripe style="width: 100%">
      <el-table-column
          prop="id" label="書籍編號" sortable>
      </el-table-column>
      <el-table-column
          prop="name" label="書名">
      </el-table-column>
      <el-table-column
          prop="price" label="價格">
      </el-table-column>
      <el-table-column
          prop="author" label="作者">
      </el-table-column>
      <el-table-column
          prop="createTime" label="出版時間">
      </el-table-column>
      <el-table-column label="封面">
        <template slot-scope="scope">
          <el-image
              style="width: 100px; height: 100px"
              :src="scope.row.cover"
              :preview-src-list="[scope.row.cover]"></el-image>
        </template>
      </el-table-column>
      <el-table-column
          label="操作" align="center">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)" v-if="user.role ===1">編輯</el-button>
          <el-popconfirm title="確認刪除？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button style="margin-left: 10px" size="mini" type="danger" v-if="user.role ===1">刪除
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

      <el-dialog title="新增" :visible.sync="dialogVisible" width="30%">
        <el-form ref="form" :model="form" label-width="120px">
          <el-form-item label="書名">
            <el-input v-model="form.name" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="價格">
            <el-input v-model="form.price" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="作者">
            <el-input v-model="form.author" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="出版時間">
            <el-date-picker v-model="form.createTime" value-format="yyyy-MM-dd" type="date" style="width: 80%"
                            clearable></el-date-picker>
          </el-form-item>
          <el-form-item label="封面">
            <el-upload ref="upload" action="http://localhost:9090/files/upload" :on-success="filesUploadSuccess">
              <el-button style="width: 300%" size="small" type="primary">上傳</el-button>
              <div slot="tip" class="el-upload__tip">只能上傳jpg/png文件，且不超過500kb</div>
            </el-upload>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">確 定</el-button>
        </span>
      </el-dialog>

    </div>
  </div>
</template>

<style>

</style>

<script>

import request from "../utils/request";

export default {
  name: 'Books',
  components: {},
  data() {
    return {
      user: {},
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData: []
    }
  },
  created() { //頁面加載的方法
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
    // 請求服務端，確認當前登入用戶的狀態
    request.get(this.$myGlobal.API + "user/" + this.user.id).then(res => {
      if (res.code === '0') {
        this.user = res.data
      }
    })
    this.load()

  },
  methods: {
    filesUploadSuccess(res) {
      console.log(res)
      this.form.cover = res.data
    },
    load() {
      request.get(this.$myGlobal.API + "books", {
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
      if (this.$refs['upload']) { // 防止彈窗報錯
        this.$refs['upload'].clearFiles() // 清除上傳文字歷史
      }
    },
    save() {
      if (this.form.id) { // 更新
        request.put(this.$myGlobal.API + "books", this.form).then(res => {
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
        request.post(this.$myGlobal.API + "books", this.form).then(res => {
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
      this.$nextTick(() => { // 回調延遲下次DOM更新循環之後執行 解決彈窗後找不到清除的元件
        this.$refs['upload'].clearFiles() // 清除上傳文字歷史
      })
    },
    handleDelete(id) {
      console.log(id)
      request.delete(this.$myGlobal.API + "books/" + id).then(res => {
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
