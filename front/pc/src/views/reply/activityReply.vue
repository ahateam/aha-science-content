<template>
	<div>
		<el-row class="title-box">
			活动管理-问答中心
		</el-row>
		<el-row class="content-box">
		</el-row>
		<el-row>
			<el-col :span="12" class="title-box">
				<el-button type="primary" @click=" isReply = true">我要发表评论
				</el-button>
			</el-col>
		</el-row>
		<el-row class="table-box">
			<el-table :data="tableData" style="width: 100%;margin-bottom: 20px;">
				<el-table-column prop="name" label="用户名" width="200">
				</el-table-column>
				<el-table-column prop="text" label="评论">
				</el-table-column>
				<el-table-column prop="appraiseCount" label="点赞数">
				</el-table-column>
				<el-table-column prop="createTime" label="发布日期" :formatter="timeFliter">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button @click="delBtn(scope.row)" type="text" style="color: red;" size="small">删除</el-button>
						<el-button @click="infoBtn(scope.row)" type="text" size="small">查看回复</el-button>
						<el-button @click="showCommentBtn(scope.row)" type="text" size="small">我要回复ta</el-button>
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
				<el-table-column prop="createTime" label="发布日期" :formatter="timeFliter">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button @click="delCommentBtn(scope.row,scope.$index)" type="text" style="color: red;" size="small">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-input v-model="inputComment" placeholder="请输入要回复的内容">
			</el-input>
			<el-button type="primary" @click="createCommentBtn2" style="margin-top: 5px;">提交回复</el-button>
		</el-dialog>

		<el-dialog title="发表评论" :visible.sync="isReply" width="30%">
			<el-input v-model="inputReply" placeholder="请输入要评论的内容">
			</el-input>
			<span slot="footer" class="dialog-footer">
				<el-button @click="isReply = false">取 消</el-button>
				<el-button type="primary" @click="createReplyBtn">确 定</el-button>
			</span>
		</el-dialog>

		<el-dialog title="发表回复" :visible.sync="isComment" width="30%">
			<el-input v-model="inputComment" placeholder="请输入要回复的内容">
			</el-input>
			<span slot="footer" class="dialog-footer">
				<el-button @click="isComment = false">取 消</el-button>
				<el-button type="primary" @click="createCommentBtn">确 定</el-button>
			</span>
		</el-dialog>

	</div>
</template>

<script>
	export default {
		name: "activityReply",
		data() {
			return {
				sequenceId: '',
				isReply: false,
				isComment: false,
				inputComment: '',
				inputReply: '',
				replyInfo: '',
				dialogTableVisible: false,
				tableData: [],
				tableData1: [],
				count: 10,
				page: 1,
				pageOver: true,
				userId: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
				userHead: this.$util.tryParseJson(localStorage.getItem('loginUser')).head,
				userName: this.$util.tryParseJson(localStorage.getItem('loginUser')).name,
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
			/*获取评论列表*/
			getContents(cnt) {
				this.$api.getReplyList(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableData = this.$util.tryParseJson(res.data.c)
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
			infoBtn(info) {
				this.tableData1 = info.comment
				this.dialogTableVisible = true
				this.sequenceId = info.sequenceId
			},
			createReplyBtn() {
				let cnt = {
					ownerId: this.replyInfo.id,
					upUserId: this.userId, // Long 提交者编号
					atUserId: '1', // Long @对象编号
					atUserName: '无', // String @对象名称
					title: '无', // String 标题
					text: this.inputReply, // String 正文
					ext: 'ext', // String 扩展
				};
				this.$api.createReply(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.inputReply = ''
						this.$message({
							type: 'success',
							message: '发表成功'
						});
						let cnt = {
							ownerId: this.replyInfo.id,
							orderDesc: true,
							count: this.count,
							offset: (this.page - 1) * this.count
						}
						this.getContents(cnt)
					}
					this.isReply = false
				})
			},
			showCommentBtn(info) {
				this.isComment = true
				this.sequenceId = info.sequenceId
			},
			createCommentBtn(info) {
				let cnt = {
					replyId: this.sequenceId, // Long 一级评论id
					upUserId: this.userId, // Long 提交者编号
					upUserHead: this.userHead, // String 提交者头像
					upUserName: this.userName, // String 提交者昵称
					text: this.inputComment, // String 正文
					toUserId: '1', // Long <选填> 目标用户编号
					toUserName: '无', // String <选填> 目标用户昵称
				};
				this.$api.createComment(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.inputComment = ''
						this.$message({
							type: 'success',
							message: '发表成功'
						});
						let cnt = {
							ownerId: this.replyInfo.id,
							orderDesc: true,
							count: this.count,
							offset: (this.page - 1) * this.count
						}
						this.getContents(cnt)
					}
					this.isComment = false
				})
			},
			createCommentBtn2() {
				let cnt = {
					replyId: this.sequenceId, // Long 一级评论id
					upUserId: this.userId, // Long 提交者编号
					upUserHead: this.userHead, // String 提交者头像
					upUserName: this.userName, // String 提交者昵称
					text: this.inputComment, // String 正文
					toUserId: '1', // Long <选填> 目标用户编号
					toUserName: '无', // String <选填> 目标用户昵称
				};
				this.$api.createComment(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.inputComment = ''
						this.$message({
							type: 'success',
							message: '发表成功'
						});
						let back = this.$util.tryParseJson(res.data.c)
						back.name = this.userName
						this.tableData1.push(back)
					}
					this.isComment = false
					let cnt = {
						ownerId: this.replyInfo.id,
						orderDesc: true,
						count: this.count,
						offset: (this.page - 1) * this.count
					}
					this.getContents(cnt)
				})
			},
			//删除回复
			delCommentBtn(info, index) {
				this.$confirm('此操作将永久删除, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					let cnt = {
						ownerId: info.replyId,
						sequenceId: info.sequenceId,
						isReply: "comment",
					}
					this.$api.delReply(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							this.$message({
								type: 'success',
								message: '删除成功!'
							});
							this.tableData1.splice(index, 1)
							let cnt = {
								ownerId: this.replyInfo.id,
								orderDesc: true,
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
			/* 删除内容*/
			delBtn(info) {
				this.$confirm('此操作将永久删除, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					let cnt = {
						ownerId: info.ownerId,
						sequenceId: info.sequenceId,
						isReply: "reply",
					}
					this.$api.delReply(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							this.$message({
								type: 'success',
								message: '删除成功!'
							});
							let cnt = {
								ownerId: this.replyInfo.id,
								orderDesc: true,
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
			let info = this.$route.params.info
			this.replyInfo = info
			let cnt = {
				ownerId: info.id,
				orderDesc: true,
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
