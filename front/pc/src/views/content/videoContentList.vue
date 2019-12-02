<template>
	<div>
		<el-row class="title-box">
			视频管理
		</el-row>

		<el-row style="padding: 10px;">
			<el-col :span="18">
				<span style="font-size: 16px;">查询栏目：</span>
				<el-button :type="vipCurr == index?'primary':'text'" size="mini" round v-for="(item,index) in vipList" :key="item.id"
				 @click="getContentsByCheck(item.id,0,index)">{{item.title}}</el-button>
			</el-col>
			<el-col :span="6">
				<el-input placeholder="请输入内容" v-model="keyword">
					<el-button slot="append" icon="el-icon-search" @click="search">搜索</el-button>
				</el-input>
			</el-col>
		</el-row>

		<el-row style="padding: 10px;">
			<el-col :span="18">
				<span style="font-size: 16px;">查询专题：</span>
				<el-button :type="channelCurr == index?'primary':'text'" size="mini" round v-for="(item,index) in channelList" :key="item.id"
				 @click="getContentsByCheck(item.id,1,index)">{{item.title}}</el-button>
			</el-col>
			<el-col :span="6">
				<el-button type="primary" @click="getContentsBtn">默认列表</el-button>
				<el-button type="primary" @click="createContent" style="float: right;">发布视频</el-button>
			</el-col>
		</el-row>

		<el-row style="padding: 10px;">
			<el-col :span="18">
				<span style="font-size: 16px;">查询关键词：</span>
				<el-button :type="keyWordCurr == index?'primary':'text'" size="mini" round v-for="(item,index) in keywordList" :key="index"
				 @click="getTags(item.keyword,index)">{{item.keyword}}</el-button>
			</el-col>
		</el-row>

		<el-row class="table-box">
			<el-table :data="tableData" border style="width: 100%">
				<el-table-column prop="title" label="标题" width="400">
				</el-table-column>
				<el-table-column prop="pageView" label="显示浏览量">
				</el-table-column>
				<el-table-column prop="truePageView" label="浏览量">
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
				<el-button type="primary" size="small" :disabled="page==1" @click="changePage(0)">上一页</el-button>
				<el-button type="primary" size="small" :disabled="pageOver" @click="changePage(1)">下一页</el-button>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	export default {
		name: "videoContentList",
		data() {
			return {
				vipList: [],
				vipCurr: -1,

				channelList: [],
				channelCurr: -1,
				channelId: '',

				keyword: '',
				keywordList: [],
				keyWordCurr: -1,
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
			changePage(e) {
				if (e) {
					this.page += 1
				} else {
					this.page -= 1
				}
				localStorage.setItem("page_videoContentList", this.page)
				//获取内容列表
				let cnt = {
					module: this.$constData.module,
					type: this.typeList[0].value,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				if (this.tag) {
					cnt.tags = this.tag
				}
				if (this.channelId) {
					cnt.upChannelId = this.channelId
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
							this.$message({
								type: 'success',
								message: '删除成功!'
							});
							let cnt = {
								module: this.$constData.module,
								count: this.count,
								type: this.typeList[1].value,
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
					path: '/editVideo',
					name: 'editVideo',
					params: {
						info: info
					}
				})
			},
			//获取默认列表
			getContentsBtn() {
				this.vipCurr = -1
				this.channelCurr = -1
				this.keyWordCurr = -1
				this.tag = ''
				this.channelId = ''

				this.page = 1
				let cnt = {
					module: this.$constData.module,
					count: this.count,
					type: this.typeList[1].value,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
			},
			createContent() {
				this.$router.push('/addVideoContent')
			},
			getSvip() {
				let cnt = {
					module: this.$constData.module,
					type: '0',
					count: 200,
					offset: '0'
				}
				this.$api.getChannels(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.vipList = this.$util.tryParseJson(res.data.c)
					}
				})
			},
			getChannel() {
				let cnt = {
					module: this.$constData.module,
					type: '1',
					count: 200,
					offset: '0'
				}
				this.$api.getChannels(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.channelList = this.$util.tryParseJson(res.data.c)
					}
				})
			},
			getContentsByCheck(id, e, index) {
				this.channelId = id

				if (e == 0) {
					this.vipCurr = index
					this.channelCurr = -1
				} else {
					this.channelCurr = index
					this.vipCurr = -1
				}
				this.keyWordCurr = -1

				let cnt = {
					module: this.$constData.module,
					type: this.typeList[1].value,
					upChannelId: id,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
			},
			search() {
				this.vipCurr = -1
				this.channelCurr = -1
				this.keyWordCurr = -1
				this.tag = ''
				this.channelId = ''


				let cnt = {
					module: this.$constData.module,
					type: this.typeList[1].value,
					count: this.count,
					offset: (this.page - 1) * this.count,
				}
				if (cnt.offset != 0) {
					cnt.offset = 0;
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

			getKeywords() {
				let cnt = {
					count: 200,
					offset: '0'
				}
				this.$api.getKeywords(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.keywordList = this.$util.tryParseJson(res.data.c)
					}
				})
			},

			//关键词查询
			getTags(info, index) {
				this.keyWordCurr = index
				this.vipCurr = -1
				this.channelCurr = -1

				this.page = 1
				let tag = {
					homeCotent: [info]
				}
				this.tag = tag
				let cnt = {
					module: this.$constData.module,
					type: this.typeList[1].value,
					tags: tag,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				if (tag.homeCotent == '') {
					cnt.tags = ''
				}
				this.getContents(cnt)
			}
		},
		mounted() {
			this.getSvip()
			this.getChannel()
			let page = Number(localStorage.getItem('page_videoContentList'))
			if (page) {
				this.page = page
			}
			//获取内容列表
			let cnt = {
				module: this.$constData.module,
				count: this.count,
				type: this.typeList[1].value,
				offset: (this.page - 1) * this.count
			}
			this.getContents(cnt)
			this.getKeywords()
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
