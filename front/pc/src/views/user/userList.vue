<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="2" style="min-height: 20px"></el-col>
      <el-col :span="16">
        <el-table
          :data="tableData"
          stripe
          style="width: 100%">
          <el-table-column
            prop="id"
            label="用户ID"
            width="180">
          </el-table-column>
          <el-table-column
            prop="name"
            label="用户名"
            width="180">
          </el-table-column>
          <el-table-column
            prop="title"
            label="简介"
            width="auto">
          </el-table-column>

          <!--              fixed="right"-->
          <el-table-column
            label="操作"
            width="100">
            <template slot-scope="scope">
              <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
              <!--            <el-button type="text" size="small">编辑</el-button>-->
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: "list",
    data(){
      return{
        tableData: [{
          id: '2016-05-02',
          name: '王小虎',
          title: '上海市普陀区金沙江路 1518 弄'
        }, {
          id: '2016-05-04',
          name: '王小虎',
          title: '上海市普陀区金沙江路 1517 弄'
        }, {
          id: '2016-05-01',
          name: '王小虎',
          title: '上海市普陀区金沙江路 1519 弄'
        }, {
          id: '2016-05-03',
          name: '王小虎',
          title: '上海市普陀区金沙江路 1516 弄'
        }],

      }
    },
    methods:{
      handleClick(e){
        console.log(e)
        this.$router.push({
          name: `userInfo`,
          params: {
            id:e.id
          }
        })
      }
    }
  }
</script>

<style scoped lang="scss">
  .box{
    background: #0086b3;
    width: 500px;
    height: 50px;
  }
</style>
