<template>
	<div>
		<el-row class="title-box" style="color: #0077AA;">
			敏感回复审核管理
		</el-row>
		<el-row class="content-box">
		</el-row>
		<el-row class="table-box">
			<el-table :data="tableData" style="width: 100%;margin-bottom: 20px;">
				<el-table-column prop="text" label="评论">
				</el-table-column>
				<el-table-column prop="createTime" label="评论日期" :formatter="timeFliter">
				</el-table-column>
				<el-table-column prop="name" label="发布者">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button @click="delBtn(scope.row)" type="text" style="color: red;" size="small">不通过</el-button>
						<el-button @click="infoBtn(scope.row)" type="text" size="small">通过</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-row>
		<el-row style="height: 80px;">
			<el-col :span="24">
				当前页数：{{page}}
				<el-button type="primary" size="small" :disabled="page==1" @click="changePage(page-1)">上一页</el-button>
				<el-button type="primary" size="small" :disabled="pageOver" @click="changePage(page+1)">下一页</el-button>
			</el-col>
		</el-row>
		
		
		<el-dialog title="回复" :visible.sync="dialogTableVisible">
		  <el-table :data="tableData1" style="width: 100%;margin-bottom: 20px;">
		  	<el-table-column prop="name" label="用户名" width="200">
		  	</el-table-column>
		  	<el-table-column prop="text" label="评论">
		  	</el-table-column>
		  	<el-table-column prop="appraiseCount" label="点赞数">
		  	</el-table-column>
		  	<el-table-column prop="createTime" label="评论日期" :formatter="timeFliter">
		  	</el-table-column>
		  	<el-table-column label="操作" width="200">
		  		<template slot-scope="scope">
		  			<el-button @click="delCommentBtn(scope.row,scope.$index)" type="text" style="color: red;" size="small">删除</el-button>
		  		</template>
		  	</el-table-column>
		  </el-table>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		name: "SenssitivewordComment",
		data() {
			return {
				dialogTableVisible:false,
				tableData: [],
				tableData1:[],
				count: 10,
				page: 1,
				pageOver: true,
			}
		},
		methods: {
			timeFliter(row, col, val) {
				let timer = new Date(val)
				let dataTime = timer.toLocaleDateString() + ' ' + timer.toLocaleTimeString('chinese', {
					hour12: false
				})
				return dataTime
			},
			getContents(cnt) {
				cnt.type = 1
				this.$api.getCommentListByStatus(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableData = this.$util.tryParseJson(res.data.c)
						console.log(this.tableData)
					} else {
						this.tableData = []
					}
					if (this.tableData.length < this.count) {
						this.pageOver = true
					} else {
						this.pageOver = false
					}
				})
			},
			/* 分页*/
			changePage(page) {
				this.page = page
				//获取内容列表
				let cnt = {
					module: this.$constData.module,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
			},
			infoBtn(info){
				let cnt = {
					id:info.sequenceId,
				}
				this.$api.updateComment(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.$message({
							type: 'success',
							message: '操作成功!'
						});
						let cnt = {
							type:1,
							count: this.count,
							offset: (this.page - 1) * this.count
						}
						this.getContents(cnt)
					} else {
						this.$message({
							type: 'error',
							message: '操作失败!'
						});
					}
				})
			},
			//删除回复
			delCommentBtn(info,index){
					this.$confirm('此操作将永久删除, 是否继续?', '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(async () => {
						let cnt = {
							ownerId: info.replyId,
							sequenceId: info.sequenceId,
							isReply:"comment",
						}
						this.$api.delReply(cnt, (res) => {
							if (res.data.rc == this.$util.RC.SUCCESS) {
								this.$message({
									type: 'success',
									message: '删除成功!'
								});
								this.tableData1.splice(index,1)
							} else {
								this.$message({
									type: 'error',
									message: '删除失败!'
								});
							}
						})
					}).catch(() => {
						this.$message({
							type: 'info',
							message: '已取消删除'
						});
					});
			},
			/* 删除内容*/
			delBtn(info) {
				this.$confirm('永久删除该条评论, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					let cnt = {
						ownerId: info.replyId,
						sequenceId: info.sequenceId,
						isReply:"comment",
					}
					this.$api.delReply(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							this.$message({
								type: 'success',
								message: '删除成功!'
							});
							let cnt = {
								count: this.count,
								offset: (this.page - 1) * this.count
							}
							this.getContents(cnt)
						} else {
							this.$message({
								type: 'error',
								message: '删除失败!'
							});
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
		},
		mounted() {
			let cnt = {
				type:1,
				count: this.count,
				offset: (this.page - 1) * this.count
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
</style>
