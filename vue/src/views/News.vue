<template>
  <div style="padding: 10px">
    <!--    功能區域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
    </div>

    <!--    搜索區域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="書名搜尋" style="width: 20% " clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查詢</el-button>
    </div>

    <el-table size="small" :data="tableData" stripe style="width: 100%">
      <el-table-column
          prop="id" label="編號" sortable>
      </el-table-column>
      <el-table-column
          prop="title" label="標題">
      </el-table-column>
      <el-table-column
          prop="author" label="作者">
      </el-table-column>
      <el-table-column
          prop="time" label="時間">
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button size="mini" @click="handleDetails(scope.row)">內容</el-button>
          <el-button size="mini" @click="handleEdit(scope.row)">編輯</el-button>
          <el-popconfirm title="確認刪除？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button style="margin-left: 6px" size="mini" type="danger">刪除</el-button>
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

    <el-dialog title="新增" v-model="dialogVisible" width="50%">
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="標題">
          <el-input v-model="form.title" style="width: 50%;"></el-input>
        </el-form-item>
        <!--        <el-form-item label="內容">-->
        <!--          <el-input v-model="form.content" style="width: 80%;"></el-input>-->
        <!--        </el-form-item>-->
        <div id="wangEditor"></div>

      </el-form>
      <template #footer>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">確 定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog title="內容" v-model="vis" width="50%">
      <el-card>
        <div v-html="detail.content" style="min-height: 100px"/>
      </el-card>
    </el-dialog>
  </div>
</template>

<style>

</style>

<script>

// import E from 'wangeditor'
import request from "../utils/request";

let editor

export default {
  name: 'News',
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
      vis: false,
      detail: {},
      // editor: null
    }
  },
  created() { // 頁面加載的方法
    this.load()
  },
  // mounted() {
  //   this.init()
  // },
  methods: {
    init() {
      this.editor = new E('#wangEditor')
      // 配置後端server接口地址
      this.editor.config.uploadImgServer = 'http://' + window.server.filesUploadUrl + ':9090/files/editor/upload';
      this.editor.config.uploadFileName = 'file'; // 設置文件上傳的名字
      this.editor.create(); // 創建
    },
    handleDetails(row) {
      this.detail = row
      this.vis = true
    },
    load() {
      request.get(this.$myGlobal.API + "news", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then((res) => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add() {
      this.form = {}
      this.dialogVisible = true
      // 關聯彈窗裡面的div, new一個editor對象, 如果報錯是因為 彈窗是異步加載，在執行方法時editor元素可能還不存在
      this.$nextTick(() => { // 回調延遲下次DOM更新循環之後執行 解決彈窗後找不到元件
        // if (!editor) {
        //   editor = new E('#div1')
        //   // 配置後端server接口地址
        //   this.editor.config.uploadImgServer = 'http://' + window.server.filesUploadUrl + ':9090/files/editor/upload';
        //   this.editor.config.uploadFileName = 'file'; // 設置文件上傳的名字
        //   this.editor.create(); // 創建
        // }
        // editor.txt.html("")
      })
    },
    save() {
      this.form.content = editor.txt.html() // 獲取文本編輯器 賦予到form實體中在推到後端

      if (this.form.id) { // 更新
        request.put(this.$myGlobal.API + "news", this.form).then((res) => {
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
        let userStr = sessionStorage.getItem("user") || "{}" // 儲存的sessionStorage的對象user有值，沒有為空對象
        let user = JSON.parse(userStr)
        this.form.author = user.nickName

        request.post(this.$myGlobal.API + "news", this.form).then((res) => {
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
      // 關聯彈窗裡面的div, new一個editor對象, 如果報錯是因為 彈窗是異步加載，在執行方法時editor元素可能還不存在
      this.$nextTick(() => { // 回調延遲下次DOM更新循環之後執行 解決彈窗後找不到元件
        // if (!editor) {
        //   editor = new E('#div1')
        //   // 配置後端server接口地址
        //   this.editor.config.uploadImgServer = 'http://' + window.server.filesUploadUrl + ':9090/files/editor/upload';
        //   this.editor.config.uploadFileName = 'file'; // 設置文件上傳的名字
        //   this.editor.create(); // 創建
        // }
        // editor.txt.html(row.content)
      })
    },
    handleDelete(id) {
      console.log(id)
      request.delete(this.$myGlobal.API + "news/" + id).then((res) => {
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
