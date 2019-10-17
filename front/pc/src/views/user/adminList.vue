<template>
	<div>
		<el-row class="title-box">
			用户管理
		</el-row>
		<el-row class="content-box">
			<el-button @click="adduser">创建普通管理员</el-button>
		</el-row>
		<el-row class="table-box">
			<el-table :data="tableData" border style="width: 100%">
				<el-table-column label="头像">
					<template scope="scope">
						<img :src="scope.row.head" width="40" height="40" class="head_pic" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="用户名" width="200">
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
				</el-table-column>
				<el-table-column prop="createTime" label="注册日期" :formatter="timeFliter">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button @click="delBtn(scope.row)" type="text" size="small" v-if="scope.row.status == 0">封禁</el-button>
						<el-button @click="delBtn(scope.row)" type="text" size="small" v-if="scope.row.status != 0">解封</el-button>
						<el-button @click="infoBtn(scope.row)" type="text" size="small">详情</el-button>
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
	</div>
</template>

<script>
	export default {
		name: "adminList",
		data() {
			return {
				tableData: [],
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
			/*获取评论列表*/
			getContents(cnt) {
				this.$api.getUsers(cnt, (res) => {
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
			delBtn(info) {
				let msg =''
				if(info.state == 0){
					msg = '封禁'
				}else{
					msg = '解封'
				}
				this.$confirm('此操作将'+msg+'用户的评论功能, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					let cnt = {
						moduleId: this.$constData.module,
						id:info.id,
						bool:true
					};
					if(info.state == 1){
						cnt.bool = false
					}
					this.$api.closeUser(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							this.$message({
								type: 'success',
								message: '操作成功!'
							});
						} else {
							this.$message({
								type: 'error',
								message: '操作失败!'
							});
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消'
					});
				});
			},
			//查看 详情
			infoBtn(info) {
				this.$router.push({
					path: '/userInfo',
					name: 'userInfo',
					params: {
						info: info
					}
				})
			},
			adduser(){
				this.$router.push({
					path: '/addUser',
					name: 'addUser',
				})
			}
		},
		mounted() {
			let cnt = {
				moduleId: this.$constData.module,
				authority:3,
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
