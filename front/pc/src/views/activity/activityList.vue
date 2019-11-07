<template>
	<div>
		<el-row class="content-box">
			<el-row>
				<el-col :span="18">
					<el-form label-width="80px">
						<el-form-item label="状态查询:">
							<el-select v-model="searchData.status" placeholder="请选择状态">
								<el-option v-for="(item,index) in statusList" :key="index" :label="item.name" :value="item.value"></el-option>
							</el-select>
						</el-form-item>
					</el-form>
					<el-button type="primary" @click="searchBtn">查询</el-button>
					<el-button type="primary" @click="getContentsBtn">默认列表</el-button>
				</el-col>
				<el-col :span="6">
					<el-input placeholder="请输入内容" v-model="keyword">
						<el-button slot="append" icon="el-icon-search" @click="search">搜索</el-button>
					</el-input>
				</el-col>
			</el-row>
		</el-row>
		<el-row class="table-box">
			<el-table :data="tableData" border style="width: 100%">
				<el-table-column prop="title" label="标题" width="400">
				</el-table-column>
				<el-table-column prop="type" label="类型" :formatter="typeFliter">
				</el-table-column>
				<el-table-column prop="createTime" label="发布日期" :formatter="timeFliter">
				</el-table-column>
				<el-table-column prop="status" label="状态" :formatter="statusFliter">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<!-- <el-button @click="infoBtn(scope.row)" type="text" size="small">查看详情</el-button> -->
						<el-button @click="updateBtn(scope.row)" type="text" size="small">编辑</el-button>
						<el-button @click="delBtn(scope.row)" type="text" size="small">删除</el-button>
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
		name: "contentList",
		data() {
			return {
				keyword: '',
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
			/** 过滤器*/
			typeFliter(row, col, val) {
				let typeList = this.typeList
				for (let i = 0; i < typeList.length; i++) {
					if (typeList[i].value == val) {
						return typeList[i].name
					}
				}
			},
			timeFliter(row, col, val) {
				let timer = new Date(val)
				let dataTime = timer.toLocaleDateString() + ' ' + timer.toLocaleTimeString('chinese', {
					hour12: false
				})
				return dataTime
			},
			statusFliter(row, col, val) {
				let statusList = this.statusList
				for (let i = 0; i < statusList.length; i++) {
					if (statusList[i].value == val) {
						return statusList[i].name
					}
				}
			},
			/*获取内容列表*/
			getContents(cnt) {
				this.$api.getContents(cnt, (res) => {
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
					offset: (this.page - 1) * this.count,
					type: this.typeList[3].value, // Byte <选填> 类型
				}
				this.getContents(cnt)
			},
			/* 查询数据*/
			searchBtn() {
				this.page = 1
				let cnt = {
					module: this.$constData.module,
					count: this.count,
					offset: (this.page - 1) * this.count,
					type: this.typeList[3].value, // Byte <选填> 类型
				}
				if (this.searchData.status) {
					cnt.status = this.searchData.status
				}
				this.getContents(cnt)
			},
			/* 删除内容*/
			delBtn(info) {
				this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					let cnt = {
						moduleId: this.$constData.module,
						id: info.id,
					}
					this.$api.delContentById(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							let cnt = {
								module: this.$constData.module,
								count: this.count,
								offset: (this.page - 1) * this.count,
								type: this.typeList[3].value, // Byte <选填> 类型
							}
							this.getContents(cnt)
							this.$message({
								type: 'success',
								message: '删除成功!'
							})
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
			//查看 详情
			infoBtn(info) {
				this.$router.push({
					path: '/contentInfo',
					name: 'contentInfo',
					params: {
						info: info
					}
				})
			},
			//编辑修改
			updateBtn(info) {
				this.$router.push({
					path: '/editActivity',
					name: 'editActivity',
					params: {
						info: info
					}
				})
			},
			search() {
				let cnt = {
					module: this.$constData.module,
					count: this.count,
					offset: (this.page - 1) * this.count,
					type: this.typeList[3].value, // Byte <选填> 类型
				}
				if (this.keyword == '') {
					this.getContents(cnt)
					return
				}
				cnt.keyword = this.keyword
				this.$api.searchContents(cnt, (res) => {
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
			//获取默认列表
			getContentsBtn() {
				this.searchData.status = ''
				this.page = 1
				let cnt = {
					module: this.$constData.module,
					count: this.count,
					offset: (this.page - 1) * this.count,
					type: this.typeList[3].value, // Byte <选填> 类型
				}
				this.getContents(cnt)
			}
		},
		mounted() {
			//获取内容列表
			let cnt = {
				module: this.$constData.module,
				count: this.count,
				offset: (this.page - 1) * this.count,
				type: this.typeList[3].value, // Byte <选填> 类型
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
