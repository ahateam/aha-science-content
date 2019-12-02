<template>
	<div>
		<el-row class="title-box">
			敏感词管理
		<el-button type="danger" plain @click="dialogVisible = true">添加敏感词</el-button>
		<el-button type="danger" plain @click="dialogVisibleDel = true">删除敏感词</el-button>
		</el-row>
		<el-row class="content-box">
			{{tableData}}
		</el-row>
		<el-dialog title="提示" :visible.sync="dialogVisible" width="30%">
			<el-input v-model="input" placeholder="请输入内容"></el-input>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="add()">确 定</el-button>
			</span>
		</el-dialog>
		<el-dialog title="提示" :visible.sync="dialogVisibleDel" width="30%">
			<el-input v-model="inputDel" placeholder="请输入要删除的内容"></el-input>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisibleDel = false">取 消</el-button>
				<el-button type="primary" @click="del()">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		name: "StringFilterList",
		data() {
			return {
				input:'',
				inputDel:'',
				dialogVisible:false,
				dialogVisibleDel:false,
				tableData: '',
			}
		},
		methods: {
			getContents(cnt) {
				this.$api.getFilterText(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableData = this.$util.tryParseJson(res.data.c)
					}
				})
			},
			add(){
				let cnt = {
					txt:this.input
				}
				this.$api.readFilterText(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableData = this.$util.tryParseJson(res.data.c)
						this.$message({
							type: 'success',
							message: '操作成功!'
						});
						this.input = ''
					}
					this.getContents(1)
				})
			},
			del(){
				let cnt = {
					txt:this.inputDel
				}
				this.$api.delFilterText(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableData = this.$util.tryParseJson(res.data.c)
						this.$message({
							type: 'success',
							message: '操作成功!'
						});
						this.input = ''
					}else{
						this.$message({
							message: res.data.rm,
							type: 'error'
						});
					}
					this.getContents(1)
				})
			}
		},
		mounted() {
			let cnt = {

			}
			this.getContents(cnt)
		}
	}
</script>

<style scoped lang="scss">
	.title-box {
		font-size: 16px;
		color: #666;
		line-height: 50px;
		padding-left: 15px;
	}

	.content-box {
		margin-top: 20px;
		padding: 20px;
	}

	.table-box {
		padding: 20px;

	}

	.divdate {
		width: 120px;
		height: 20px;
		margin-left: 50px;
	}
</style>
