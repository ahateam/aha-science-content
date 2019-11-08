<template>
	<div>
		<el-row class="title-box">
			标签管理
		</el-row>
		<el-row class="table-box">
			<el-table :data="tableData" border style="width: 100%">
				<el-table-column prop="keyword" label="关键词" width="400">
				</el-table-column>
				<el-table-column prop="pageView" label="浏览量" width="400">
				</el-table-column>
				<el-table-column label="操作" width="100">
					<template slot-scope="scope">
						<el-button @click="delBtn(scope.row)" type="text" size="small" style="color: red">删除</el-button>
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
		name: "homeTag",
		data() {
			return {
				options: '',
				tagGroup: '',
				showTagGroup: false,
				tagTitle: '',
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
				tagstatus: this.$constData.tagstatus,
			}
		},
		methods: {
			getContents(cnt) {
				this.$api.getKeywords(cnt, (res) => {
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
					count: this.count,
					offset: (this.page - 1) * this.count,
				}
				this.getContents(cnt)
			},
			delBtn(info) {
				this.$confirm('将会永久删除此数据，将会影响该标签下内容的显示，是否继续？', '警告', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'error'
				}).then(async () => {
					let cnt = {
						id:info.id
					}
					this.$api.delKeyword(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							this.$message({
								type: 'success',
								message: '操作成功!'
							})
							let cnt = {
								count: this.count,
								offset: (this.page - 1) * this.count,
							}
							this.getContents(cnt)
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
		},
		mounted() {
			let cnt = {
				count: this.count,
				offset: (this.page - 1) * this.count,
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
