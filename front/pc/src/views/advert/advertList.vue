<template>
	<div>
		<el-row class="title-box">
			广告管理
		</el-row>
		<el-row class="content-box">
		</el-row>
		<el-row class="table-box">
			<el-table :data="tableData" border style="width: 100%">
				<el-table-column label="展示图">
					<template scope="scope">
						<img :src="scope.row.imgSrc" width="40" height="40" class="head_pic" />
					</template>
				</el-table-column>
				<el-table-column prop="linkSrc" label="链接" width="200">
				</el-table-column>
				<el-table-column prop="remark" label="备注">
				</el-table-column>
				<el-table-column prop="createTime" label="创建日期" :formatter="timeFliter">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button @click="delBtn(scope.row)" type="text" size="small" v-if="scope.row.status == 0">删除</el-button>
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
		name: "advertList",
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
				this.$api.getAdverts(cnt, (res) => {
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
				
				this.$confirm('此操作将永久删除此数据, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					let cnt = {
						moduleId: this.$constData.module,
						id:info.id,
					};
					this.$api.delAdvert(cnt, (res) => {
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
