<template>
	<div>
		<el-row class="title-box">
			<el-col :span="18">评论管理</el-col>
			<el-col :span="3"> <el-button type="warning" plain @click="goSenssitivewordReply">敏感评论审核</el-button></el-col>
			<el-col :span="2"> <el-button type="danger" plain @click="goSenssitivewordList">敏感词词库</el-button></el-col>
		</el-row>
		<el-row style="padding: 10px;">
			<el-col :span="18">
				<span style="font-size: 16px;">查询栏目：</span>
				<el-button :type="vipCurr == -1?'primary':'text'" size="mini" round @click="getContentsBtn">全部</el-button>
				<el-button :type="vipCurr == index?'primary':'text'" size="mini" round v-for="(item,index) in vipList" :key="index"
				 @click="getContentsByCheck(item.id,0,index)">{{item.title}}</el-button>
			</el-col>
			<el-col :span="6">
				<el-input placeholder="请输入内容" v-model="keyword"  @input = "changeSearch">
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
				<el-table-column prop="title" label="原文标题" width="400">
				</el-table-column>
				<el-table-column prop="createTime" label="发布日期" :formatter="timeFliter">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button @click="infoBtn(scope.row)" type="text" size="small">查看评论</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-row>
		<el-row style="height: 80px;margin-bottom: 100px;">
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

				tableData: [],
				count: 10,
				page: 1,
				pageOver: true,
				typeList: this.$constData.typeList,
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
			goSenssitivewordReply(){
				this.$router.push({
					path: '/SenssitivewordReply',
					name: 'SenssitivewordReply',
				})
			},
			goSenssitivewordList(){
				this.$router.push({
					path: '/StringFilterList',
					name: 'StringFilterList',
				})
			},
			/*获取评论列表*/
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
			search() {
				this.page = 1
				let cnt = {
					module: this.$constData.module,
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
			changeSearch(){
				if(this.keyword == ''){
					let cnt = {
						module: this.$constData.module,
						count: this.count,
						offset: (this.page - 1) * this.count
					}
					this.getContents(cnt)
				}
			},
			/* 分页*/
			changePage(e) {
				if (e) {
					this.page += 1
				} else {
					this.page -= 1
				}
				localStorage.setItem("page_replyList", this.page)
				//获取内容列表
				let cnt = {
					module: this.$constData.module,
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
			//查看 详情
			infoBtn(info) {
				this.$router.push({
					path: '/replyInfo',
					name: 'replyInfo',
					params: {
						info: info
					}
				})
			},
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
					tags: tag,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				if (tag.homeCotent == '') {
					cnt.tags = ''
				}
				this.getContents(cnt)
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
			//获取默认列表
			getContentsBtn() {
				this.vipCurr = -1
				this.channelCurr = -1
				this.keyWordCurr = -1
				this.channelId = ''
				this.tag = []
				this.page = 1
				let cnt = {
					module: this.$constData.module,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
			}
		},
		mounted() {
			this.getSvip()
			this.getChannel()
			this.getKeywords()
			let page = Number(localStorage.getItem('page_replyList'))
			if (page) {
				this.page = page
			}
			let cnt = {
				module: this.$constData.module,
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
