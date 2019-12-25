<template>
	<div>
		<el-row class="title-box">
			图文管理
		</el-row>
		<el-row style="padding: 10px;">
			<el-col :span="18">
				<span style="font-size: 16px;">查询栏目：</span>
				<el-button :type="vipCurr == -1?'primary':'text'" size="mini" round @click="getContentsBtn">全部</el-button>
				<el-button :type="vipCurr == index?'primary':'text'" size="mini" round v-for="(item,index) in vipList" :key="index"
				 @click="getContentsByCheck(item.id,0,index)">{{item.title}}</el-button>
			</el-col>
			<el-col :span="6">
				<el-input placeholder="请输入内容" v-model="keyword" @input="changeSearch">
					<el-button slot="append" icon="el-icon-search" @click="search">搜索</el-button>
				</el-input>
			</el-col>
		</el-row>

		<el-row style="padding: 10px;">
			<el-col :span="18">
				<span style="font-size: 16px;">查询专题：</span>
				<el-button :type="channelCurr == -1?'primary':'text'" size="mini" round @click="getContentsBtn">全部</el-button>
				<el-button :type="channelCurr == index?'primary':'text'" size="mini" round v-for="(item,index) in channelList" :key="index"
				 @click="getContentsByCheck(item.id,1,index)">{{item.title}}</el-button>
			</el-col>
			<el-col :span="6">
				<el-button plain @click="getContentsBtn">默认列表</el-button>
				<el-button type="primary" plain @click="createContent" style="float: right;">发布图文</el-button>
				<el-button plain @click="draftContent" style="float: right;">草稿箱</el-button>
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
			<el-table :data="tableData" style="width: 100%" @sort-change="sortChange">
				<el-table-column prop="title" label="标题" width="400">
				</el-table-column>
				<el-table-column prop="pageView" label="显示浏览量" sortable='custom'>
				</el-table-column>
				<el-table-column prop="truePageView" label="浏览量">
				</el-table-column>
				<el-table-column prop="createTime" label="发布日期" :formatter="timeFliter" sortable='custom'>
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

		<el-row style="height: 80px;margin-bottom: 80px;">
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
		name: "contentList",
		data() {
			return {
				tag: [],

				channelId: '',

				vipList: [],
				vipCurr: -1,

				channelList: [],
				channelCurr: -1,
				keyword: '',
				keywordList: [],
				keyWordCurr: -1,
				sortInfo: '',
				tableData: [],
				count: 8,
				page: 1,
				pageOver: true,
				searchData: {
					type: '',
					status: '',
					power: '',
					tags: '',
				},
				userId: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
				typeList: this.$constData.typeList,
				statusList: this.$constData.statusList,
				powerList: this.$constData.powerList,
			}
		},
		methods: {
			sortChange(column, prop, order) {
				let cnt = {
					module: this.$constData.module,
					type: this.typeList[0].value,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				if (this.channelId != '') {
					cnt.upChannelId = this.channelId
				}
				if ('ascending' == column.order && 'pageView' == column.prop) {
					this.sortInfo = 'ORDER BY page_view asc'
				} else if ('descending' == column.order && 'pageView' == column.prop) {
					this.sortInfo = 'ORDER BY page_view desc'
				} else if ('descending' == column.order && 'createTime' == column.prop) {
					this.sortInfo = 'ORDER BY create_time desc'
				} else if ('ascending' == column.order && 'createTime' == column.prop) {
					this.sortInfo = 'ORDER BY create_time asc'
				} else {
					this.sortInfo = ''
				}
				this.getContents(cnt)
			},
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
				cnt.re = 'admin'
				cnt.status = this.$constData.statusList[3].value
				cnt.upUserId = this.userId
				if (this.sortInfo != '') {
					cnt.sort = this.sortInfo
				}
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
				localStorage.setItem("page_contentList", this.page)
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
			search() {
				this.tag = ''
				this.channelId = ''
				this.vipCurr = -1
				this.channelCurr = -1
				this.keyWordCurr = -1
				this.page = 1
				let cnt = {
					module: this.$constData.module,
					type: this.typeList[0].value,
					count: this.count,
					offset: (this.page - 1) * this.count,
				}
				if (this.keyword == '') {
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
			changeSearch() {
				if (this.keyword == '') {
					let cnt = {
						module: this.$constData.module,
						type: this.typeList[0].value,
						count: this.count,
						offset: (this.page - 1) * this.count
					}
					this.getContents(cnt)
				}
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
				this.vipCurr = -1
				this.channelCurr = -1
				this.keyWordCurr = -1
				this.channelId = ''
				this.tag = []
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
			draftContent() {
				this.$router.push('/contentListByDraft')
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
			//获取关键词
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
			//查询专题
			getContentsByCheck(id, e, index) {
				this.page = 1
				this.channelId = id
				this.tag = ''

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
					type: this.typeList[0].value,
					upChannelId: id,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
			},
			//关键词查询
			getTags(info, index) {
				this.keyWordCurr = index
				this.vipCurr = -1
				this.channelCurr = -1
				this.channelId = ''

				this.page = 1
				let tag = {
					homeCotent: [info]
				}
				this.tag = tag
				let cnt = {
					module: this.$constData.module,
					type: this.typeList[0].value,
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
			this.getKeywords()
			let page = Number(localStorage.getItem('page_contentList'))
			if (page) {
				this.page = page
			}
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
