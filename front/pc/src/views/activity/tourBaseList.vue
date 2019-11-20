<template>
	<div>
		<el-row class="title-box">
			基地管理
		</el-row>
		<el-row class="content-box">
			<el-row>
				<el-col :span="8">
					<el-button type="primary" @click="addtourbase">创建基地</el-button>
				</el-col>
			</el-row>
		</el-row>
		<el-row class="table-box">
			<el-table :data="tableData" border style="width: 100%">
				<el-table-column prop="name" label="名称" width="400">
				</el-table-column>
				<el-table-column prop="address" label="地址">
				</el-table-column>
				<el-table-column prop="buyTicketsLink" label="购票链接">
				</el-table-column>
				<el-table-column prop="createTime" label="发布日期" :formatter="timeFliter">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button @click="delBtn(scope.row)" style="color: red;" type="text" size="small">删除</el-button>
						<el-button @click="infoBtn(scope.row)" type="text" size="small">详情</el-button>
						<el-button @click="updateBtn(scope.row)" type="text" size="small">编辑</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-row>
		<el-row style="height: 80px;">
			<el-col :span="22" style="margin-left: 20px">
				当前页数：{{page}}
				<el-button type="primary" size="small" :disabled="page==1" @click="changePage(page-1)">上一页</el-button>
				<el-button type="primary" size="small" :disabled="pageOver" @click="changePage(page+1)">下一页</el-button>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	export default {
		name: "tourBaseList",
		data() {
			return {
				tableData: [],
				count: 10,
				page: 1,
				pageOver: true,
				searchData: {
					type: '',
					status: '',
					power: '',
					tags: '',
				},

				typeList: this.$constData.typeList,
				statusList: this.$constData.statusList,
				powerList: this.$constData.powerList,
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
			/*获取内容列表*/
			getContents(cnt) {
				this.$api.getTourBases(cnt, (res) => {
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
					moduleId: this.$constData.module,
					userCoordinate: '0,0',
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
			},
			/* 删除内容*/
			delBtn(info) {
				this.$confirm('此操作将永久删除该文件及其所属活动, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					let cnt = {
						moduleId: this.$constData.module,
						id: info.id,
					}
					this.$api.delupdateTourBase(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							this.$message({
								type: 'success',
								message: '删除成功!'
							});
						let cnt = {
							moduleId: this.$constData.module, // Long 模块编号
							userCoordinate: '0,0',
							count: this.count,
							offset: (this.page - 1) * this.count,
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
			infoBtn(info) {
				this.$router.push({
					path: '/tourBaseInfo',
					name: 'tourBaseInfo',
					params: {
						info: info
					}
				})
			},

			addtourbase() {
				this.$router.push({
					path: '/addTourBase',
					name: 'addTourBase',
				})
			},
			updateBtn(info){
				this.$router.push({
					path: '/editTourBase',
					name: 'editTourBase',
					params:{
						info:info
					}
				})
			}
		},
		mounted() {
			//获取内容列表
			let cnt = {
				moduleId: this.$constData.module, // Long 模块编号
				userCoordinate: '0,0',
				count: this.count,
				offset: (this.page - 1) * this.count,
			}
			this.getContents(cnt)
		},
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
