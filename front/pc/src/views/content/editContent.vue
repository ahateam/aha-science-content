<template>
	<div>
		<el-row style="padding-left: 20px;font-size: 16px;">
		</el-row>
		<el-row style="padding: 20px;margin-bottom: 20px;">
			<el-col :span="20">
				<span class="title-box"> 标题：</span>
				<el-input placeholder="请输入标题" v-model="title" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>

		<el-row>
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="状态">
						<el-select v-model="status" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in statusList" :key="item.value" :label="item.name" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="显示">
						<el-select v-model="show" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in showList" :key="item.value" :label="item.name" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="栏目:">
						<el-select clearable v-model="vip" placeholder="请选择" style="margin-right: 10px;"@change="changevip" @clear="clearData">
							<el-option v-for="item in vipList" :key="item.id" :label="item.title" :value="item.id">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="专题">
						<el-select v-model="upChannelId" placeholder="请选择" style="margin-right: 10px;" @clear="clearData" @change="changeChannel">
							<el-option v-for="item in channelList" :key="item.id" :label="item.title" :value="item.id">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="13">
				<el-form label-width="80px">
					<el-form-item label="关键词:">
						<el-select v-model="value" multiple default-first-option placeholder="请选择文章标签-提示:可多选，可删除" style="width: 100%;">
							<el-option v-for="item in keywordList" :key="item.id" :label="item.keyword" :value="item.keyword">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
		</el-row>
		<el-row style="margin-bottom: 10px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<div id="editor">
				</div>
			</el-col>
		</el-row>
		<el-row style="margin-top: 20px">
			<el-button type="primary" @click="subBtn" style="margin: 0 auto 100px auto;display: block;padding: 15px 50px">提交修改
			</el-button>
		</el-row>

	</div>
</template>

<script>
	import wangEditor from 'wangeditor'
	export default {
		name: "addContent",
		data() {
			return {
				keywordList: [{
					keyword: '',
					id: ''
				}],
				value: [],


				vipList: '',
				vip: '',
				channelName: '',
				tag: '',
				cotentHtml: '',
				id: '',
				channelTag: '',
				editor: {},
				imgList: [],
				tag: '',
				paidIndex: false,
				videoSrc: [],
				// paid: 0,
				upChannelId: '',
				channelList: [{
					title: '',
					id: 0,
				}],
				show: 0,
				title: '',
				status: '',
				userId:this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
				statusList: this.$constData.statusList,
				showList: this.$constData.showList,
			}
		},
		methods: {
			channelListFilter(val) {
				let channelList = this.channelList
				console.log("q" + channelList[0].id)
				for (let i = 0; i < channelList.length; i++) {
					console.log("44")
					if (channelList[i].id == val) {
						return channelList[i].title
					}
				}
			},
			subBtn() {
				let that = this
				let a = this.editor.txt.getJSON()
				for (let i = 0; i < a.length; i++) {
					let b = a[i].children
					for (let n = 0; n < b.length; n++) {
						if (b[n] instanceof Object && b[n].tag == 'img') {
							let imgSrc = {
								src: b[n].attrs[0].value
							}
							that.imgList.push(imgSrc)
						}
					}
				}
				this.editorBtn()
			},
			editorBtn() {
				let that = this
				let text = this.editor.txt.html()
				let data = {
					editor: [{
						type: 'textarea',
						value: text
					}],
					show: this.show,
					imgList: this.imgList
				}
				let tags = {
					homeCotent: this.value
				}
				if (that.upChannelId != '') {
					cnt.upChannelId = parseInt(that.upChannelId)
				}
				let cnt = {
					id: this.id,
					module: this.$constData.module,
					type: this.contentType,
					status: this.status,
					power: this.power,
					upChannelId: this.upChannelId == '' ? this.vip : this.upChannelId,
					upUserId: this.userId,
					tags: JSON.stringify(tags),
					title: this.title,
					data: JSON.stringify(data),
				}
				if (that.upChannelId != '') {
					cnt.upChannelId = parseInt(that.upChannelId)
				}
				that.$api.editContent(cnt, (res => {
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '修改成功',
							type: 'success'
						});
						that.$router.push('/contentList')
					} else {
						this.$message({
							message: res.data.c,
							type: 'warning'
						});
						that.imgList = []
					}
				}))
			},
			getChannels(info) {
				let cnt = {
					module: this.$constData.module,
					status: 0,
					count: 20,
					type: '1',
					offset: 0,
				};
				this.$api.getChannels(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.channelList = this.$util.tryParseJson(res.data.c)
						console.log(this.channelList)
					}
					this.channelName = this.channelListFilter(info.upChannelId)
				})
			},
			getVips() {
				let cnt = {
					module: this.$constData.module,
					type: '0',
					status: 0,
					count: 100,
					offset: 0,
				};
				this.$api.getChannels(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.vipList = this.$util.tryParseJson(res.data.c)
					}
				})
			},
			clearData() {
				this.upChannelId = ''
				this.vip = ''
			},
			changeChannel(){
				this.vip=''
			},
			changevip(){
				this.upChannelId=''
			},
			getKeyword() {
				let cnt = {
					count: 200,
					offset: '0',
				}
				this.$api.getKeywords(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.keywordList = this.$util.tryParseJson(res.data.c)
					}
				})
			}
		},
		mounted() {
			let info = this.$route.params.info
			this.getChannels(info)
			this.getVips()
			this.getKeyword()
			console.log(info)
			this.id = info.id
			this.title = info.title
			this.status = info.status
			this.power = info.power
			this.show = JSON.parse(info.data).show
			this.contentType = info.type
			this.editor = new wangEditor('#editor')
			this.editor.customConfig.zIndex = 1
			this.editor.create()
			this.cotentHtml = JSON.parse(info.data).editor[0].value
			this.editor.txt.html(this.cotentHtml)
			this.value = info.tags.homeCotent
			// this.upChannelId = info.upChannelId
			this.vip = info.upChannelId
		}
	}
</script>

<style lang="scss" scoped>
	.title-box {
		font-size: 1.6rem;
		line-height: 40px;
		letter-spacing: 2px;
		font-weight: 600;
		color: #666;
		text-align: right;
	}
</style>
