<template>
	<div>
		<el-row class="row-box">
			<el-col :span="24">
				{{contentInfo.title}}
			</el-col>
		</el-row>
		<el-row class="row-title">
			<h1>基本信息：</h1>
			<p>内容id：{{id}}</p>
			<p>浏览量：{{pageView}}</p>
			<p v-if="this.isShowpage">实际浏览量：{{truePageView}}</p>
			<p>关键词：<el-tag v-for="item in tags" type="info" style="margin-right: 5px;">{{item}}</el-tag>
			</p>
			<p>类型：{{type}}</p>
			<p>当前状态：{{status}}</p>
			<p>栏目/专题：<el-button @click="infoChannel" v-if="orShow">查看所属栏目/专题</el-button><span>{{upChannelId}}</span></p>
			<p>创建者id：{{upUserId}}</p>
			<p>创建者用户名：{{user}}</p>
			<p>修改时间：{{updateTime}}</p>
			<p>创建时间：{{createTime}}</p>
			<p>口播mp3：<audio :src="mp3Src" controls="controls" ref='audio'></audio></p>
			<p>视频：<video :src="mp4Src" controls="controls" style="width: 20%;"></video></p>
			<p>作者：{{contentAuthor}}</p>
			<p>来源：{{contentSource}}</p>
			<p>摘要：{{contentRemark}}</p>
			<hr />
		</el-row>

		<div v-if="contentInfo.type != '3'">
			<div v-for="(item,index) in textList" :key="index">
				<div v-html="item.value" v-if="item.type == 'textarea'" style="padding: 20px;" class="textInfo"></div>
				<img :src="item.value" v-else>
			</div>
		</div>

		<div v-else style="width: 100%">
			<video width="576" height="324" style="margin: 0 auto;display: block" controls>
				<source :src="JSON.parse(contentInfo.data).url" type="video/mp4">
				您的浏览器不支持html5的video标签
			</video>
			<div style="font-size: 20px;font-weight: bold;margin-left:50px">
				封面图：
				<img :src="JSON.parse(contentInfo.data).imgSrc">
			</div>
		</div>

		<el-col :span="24" class="row-box1">
			<el-button @click="goBack()">返回上一层</el-button>
		</el-col>
	</div>
</template>

<script>
	export default {
		name: "contetnInfo",
		data() {
			return {
				mp4Src:'',
				mp3Src:'',
				contentRemark: '',
				contentSource: '',
				contentAuthor: '',
				isShowpage: false,
				truePageView: '',
				orShow: true,
				id: '',
				pageView: '',
				status: '',
				tags: '',
				type: '',
				upChannelId: '',
				upUserId: '',
				user: '',
				updateTime: '',
				createTime: '',
				channel: '',
				contentInfo: {},
				textList: [],
				typeList: this.$constData.typeList,
				statusList: this.$constData.statusList,
				userId: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
			}
		},
		methods: {
			goBack() {
				this.$router.go(-1)
			},
			timeFliter(val) {
				let timer = new Date(val)
				let dataTime = timer.toLocaleDateString() + ' ' + timer.toLocaleTimeString('chinese', {
					hour12: false
				})
				return dataTime
			},
			typeFliter(val) {
				let typeList = this.typeList
				for (let i = 0; i < typeList.length; i++) {
					if (typeList[i].value == val) {
						return typeList[i].name
					}
				}
			},
			statusFliter(val) {
				let statusList = this.statusList
				for (let i = 0; i < statusList.length; i++) {
					if (statusList[i].value == val) {
						return statusList[i].name
					}
				}
			},
			channelFliter(val) {
				let statusList = this.channel
				for (let i = 0; i < statusList.length; i++) {
					if (statusList[i].id == val) {
						return statusList[i].title
					}
				}
			},
			getChannel() {
				let cnt = {
					module: this.$constData.module,
					count: 200,
					offset: 0,
				}
				this.$api.getChannels(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.channel = this.$util.tryParseJson(res.data.c)
					}
				})
			},
			infoChannel() {
				this.orShow = false
				if (this.contentInfo.upChannelId == '') {
					this.upChannelId = '本内容没有设置栏目或专题'
					return;
				}
				this.upChannelId = this.channelFliter(this.contentInfo.upChannelId)
			},
			getUser() {
				let cnt = {
					moduleId: this.$constData.module,
					id: this.userId,
				}
				this.$api.getUser(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let userInfo = this.$util.tryParseJson(res.data.c)
						if (userInfo.authority == 4) {
							console.log(userInfo)
							this.isShowpage = true
						}
					}
				})
			}
		},
		mounted() {
			this.getChannel()
			this.getUser()
			this.contentInfo = this.$route.params.info
			console.log(this.$route.params.info)
			let a = JSON.parse(this.contentInfo.data).editor
			if (a != '' && a != undefined) {
				this.textList = a
			}
			this.id = this.contentInfo.id
			this.tags = this.contentInfo.tags.homeCotent
			this.pageView = this.contentInfo.pageView
			this.status = this.statusFliter(this.contentInfo.status)
			this.type = this.typeFliter(this.contentInfo.type)
			this.upUserId = this.contentInfo.upUserId
			this.user = this.contentInfo.user.name
			this.truePageView = this.contentInfo.truePageView
			this.updateTime = this.timeFliter(this.contentInfo.updateTime)
			this.createTime = this.timeFliter(this.contentInfo.createTime)
			this.mp3Src = this.contentInfo.mp3Src
			this.mp4Src = JSON.parse(this.contentInfo.data).video
			this.contentRemark = this.contentInfo.contentRemark
			this.contentSource = this.contentInfo.contentSource
			this.contentAuthor = this.contentInfo.contentAuthor
		}
	}
</script>

<style scoped>
	.row-box {
		background: #fff;
		padding: 15px;
		font-size: 3rem;
		color: #666;
		border-left: 4px solid #67C23A;
	}

	.row-title {
		background: #fff;
		padding-left: 15px;
		font-size: 16px;
		color: #666;
		border-left: 4px solid #67C23A;
	}

	.row-box1 {
		margin-top: 40px;
		text-align: center;
		padding-bottom: 80px;
	}

	.textInfo>>>table {
		border-top: 1px solid #ccc;
		border-left: 1px solid #ccc;
	}

	.textInfo>>>table td,
	table th {
		border-bottom: 1px solid #ccc;
		border-right: 1px solid #ccc;
		padding: 3px 5px;
	}

	.textInfo>>>table th {
		border-bottom: 2px solid #ccc;
		text-align: center;
	}

	/* blockquote 样式 */
	.textInfo>>>blockquote {
		display: block;
		border-left: 8px solid #d0e5f2;
		padding: 5px 10px;
		margin: 10px 0;
		line-height: 1.4;
		font-size: 100%;
		background-color: #f1f1f1;
	}

	/* code 样式 */
	.textInfo>>>code {
		display: inline-block;
		*display: inline;
		*zoom: 1;
		background-color: #f1f1f1;
		border-radius: 3px;
		padding: 3px 5px;
		margin: 0 3px;
	}

	.textInfo>>>pre code {
		display: block;
	}

	/* ul ol 样式 */
	.textInfo>>>ul,
	ol {
		margin: 10px 0 10px 20px;
	}
</style>
