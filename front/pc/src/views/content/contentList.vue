<template>
	<div>
		<el-row class="title-box">
			图文管理
		</el-row>
		<el-row style="padding: 10px;">
			<el-col :span="18">
				<span style="font-size: 16px;">查询栏目：</span>
				<el-button size="mini" round v-for="item in vipList" :key="item.id" @click="getContentsByCheck(item.id)">{{item.title}}</el-button>
			</el-col>
			<el-col :span="6">
				 <el-input placeholder="请输入内容" v-model="keyword">
				    <el-button  slot="append" icon="el-icon-search" @click="search">搜索</el-button>
				  </el-input>
			</el-col>
		</el-row>
		<el-row style="padding: 10px;">
			<el-col :span="18">
				<span style="font-size: 16px;">查询专题：</span>
				<el-button size="mini" round v-for="item in channelList" :key="item.id" @click="getContentsByCheck(item.id)">{{item.title}}</el-button>
			</el-col>
			<el-col :span="6">
				<el-button plain @click="getContentsBtn">默认列表</el-button>
					<el-button type="primary" plain @click="createContent" style="float: right;">发布图文</el-button>
			</el-col> 
		</el-row>

		<el-row class="table-box">
			<el-table :data="tableData" border style="width: 100%">
				<el-table-column prop="title" label="标题" width="400">
				</el-table-column>
				<el-table-column prop="pageView" label="浏览量">
				</el-table-column>
				<el-table-column prop="createTime" label="发布日期" :formatter="timeFliter">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button @click="infoBtn(scope.row)" type="text" size="small">查看详情</el-button>
						<el-button @click="updateBtn(scope.row)" type="text" size="small">编辑</el-button>
						<el-button @click="delBtn(scope.row)" type="text" size="small" style="color: red;">删除</el-button>
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
		name: "contentList",
		data() {
			return {
				vipList:'',
				channelList:'',
				keyword:'',
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
			powerFliter(row, col, val) {
				let powerList = this.powerList
				for (let i = 0; i < powerList.length; i++) {
					if (powerList[i].value == val) {
						return powerList[i].name
					}
				}
			},
			/*获取内容列表*/
			getContents(cnt) {
				this.$api.getContents(cnt, (res) => {
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
					type: this.typeList[0].value,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
			},
			/* 查询数据*/
			searchBtn() {
				this.page = 1
				let cnt = {
					module: this.$constData.module,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				if (this.searchData.type) {
					cnt.type = this.searchData.type
				}
				if (this.searchData.status) {
					cnt.status = this.searchData.status
				}
				if (this.searchData.power) {
					cnt.power = this.searchData.power
				}
				if (this.searchData.tags) {
					cnt.tags = this.searchData.tags
				}
				this.getContents(cnt)
			},
			search(){
				let cnt = {
					module: this.$constData.module,
					type: this.typeList[0].value,
					count: this.count,
					offset: (this.page - 1) * this.count,
				}
				if(this.keyword == ''){
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
			/* 删除内容*/
			delBtn(info) {
				this.$confirm('此操作将永久删除该文件及其该内容下评论, 是否继续?', '提示', {
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
							this.$message({
								type: 'success',
								message: '删除成功!'
							});
							let cnt = {
								module: this.$constData.module,
								type: this.typeList[0].value,
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
					path: '/editContent',
					name: 'editContent',
					params: {
						info: info
					}
				})
			},
			//获取默认列表
			getContentsBtn() {
				this.searchData.type = 5
				this.searchData.status = ''
				this.searchData.power = ''
				this.searchData.tags = ''
				this.page = 1
				let cnt = {
					module: this.$constData.module,
					type: this.typeList[0].value,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
			},
			createContent() {
				this.$router.push('/addContent')
			},
			getSvip(){
				let cnt = {
					module: this.$constData.module,
					type:'0',
					count: 200,
					offset: '0'
				}
				this.$api.getChannels(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.vipList = this.$util.tryParseJson(res.data.c)
					}
				})
			},
			getChannel(){
				let cnt = {
					module: this.$constData.module,
					type:'1',
					count: 200,
					offset: '0'
				}
				this.$api.getChannels(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.channelList = this.$util.tryParseJson(res.data.c)
					}
				})
			},
			getContentsByCheck(id){
				let cnt = {
					module: this.$constData.module,
					type: this.typeList[0].value,
					upChannelId:id,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
			}
		},
		mounted() {
			this.getSvip()
			this.getChannel()
			//获取内容列表
			let cnt = {
				module: this.$constData.module,
				type: this.typeList[0].value,
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
