<template>
	<div>
		<el-row style="padding: 20px">
			<el-col :span="20">
				<span class="title-box"> 标题：</span>
				<el-input placeholder="请输入标题" v-model="title" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<el-row>
			<!-- <el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="状态:">
						<el-select v-model="status" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in statusList" :key="item.value" :label="item.name" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col> -->
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="显示:">
						<el-select v-model="show" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in showList" :key="item.value" :label="item.name" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="类型:">
						<el-select v-model="type" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in typeListTemp" :key="item.value" :label="item.name" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="8" v-if="this.upChannelId == ''">
				<el-form label-width="80px">
					<el-form-item label="栏目:">
						<el-select clearable v-model="vip" placeholder="请选择" style="margin-right: 10px;" @clear="clearData">
							<el-option v-for="item in vipList" :key="item.id" :label="item.title" :value="item.id">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="8" v-if="this.vip == ''">
				<el-form label-width="80px">
					<el-form-item label="专题:">
						<el-select clearable v-model="upChannelId" placeholder="请选择" style="margin-right: 10px;" @clear="clearData">
							<el-option v-for="item in channelList" :key="item.id" :label="item.title" :value="item.id">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="8" v-if="this.isShowpage">
				<el-form label-width="80px">
					<el-form-item label="浏览量:">
						<el-input v-model="pageView" placeholder="请输入要显示的浏览量" style="width:217px;"></el-input>
					</el-form-item>
				</el-form>
				<el-radio v-model="viewradio" label="1">浏览量可见</el-radio>
				<el-radio v-model="viewradio" label="0">浏览量不可见</el-radio>
			</el-col>
			<el-col :span="13">
				<el-form label-width="80px">
					<el-form-item label="关键词:">
						<el-select v-model="value" filterable multiple default-first-option placeholder="请选择文章标签-提示:可多选,可搜索,可删除" style="width: 100%;">
							<el-option v-for="item in keywordList" :key="item.id" :label="item.keyword" :value="item.keyword">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
		</el-row>
		<el-row style="margin-top: 10px">
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="作者">
						<el-input v-model="contentAuthor" placeholder="请输入" style="width:217px;"></el-input>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="来源">
						<el-input v-model="contentSource" placeholder="请输入" style="width:217px;"></el-input>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="摘要">
						<el-input type="textarea" maxlength="100" show-word-limit v-model="contentRemark" placeholder="请输入(最大100个字符)"></el-input>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="20">
				<span class="title-box"> 口播MP3：</span>
				<input @change="getMP3($event)" ref="mp3pathClear" type="file" class="upload" />
				<el-button size="mini" round @click="delmp4src('mp3')">取消mp3</el-button>
				</el-input>
				<audio :src="mp3Src" controls="controls" ref='audio'></audio>
			</el-col>
			<el-col :span="20">
				<span class="title-box"> 上传视频：</span>
				<input @change="getMP4($event)" ref="mp4pathClear" type="file" class="upload" />
				<el-button size="mini" round @click="delmp4src('mp4')">取消视频</el-button>
				</el-input>
				<video :src="mp4Src" controls="controls" style="width: 20%;"></video>
			</el-col>
		</el-row>
		<el-row style="margin-top: 10px">
			<el-col :span="24">
				<p style="font-size: 15px;"><span style="color: red;font-size: 15px;">(注：</span>1.如是从word里复制的文本请 <span style="color: red;">"鼠标右键"</span>>选择  <span style="color: red;">"粘贴为纯文本"</span>,避免APP端无法正常显示。2.从网页复制的文本可直接粘贴，图片请点击"图片图标">选择"100%"</p>
				<p style="font-size: 15px;">3.如没有鼠标"右键">选择  "粘贴为纯文本"功能，推荐使用谷歌，360,QQ,搜狗等浏览器...</p>
			</el-col>
		</el-row>
		<el-row style="margin-top: 10px">
			<el-col :span="2" style="min-height: 20px">
			</el-col>
			<el-col :span="20">
				<div id="editor"></div>
			</el-col>
		</el-row>
		<el-row style="margin-top: 20px">
			<el-col :span="12">
				<el-button type="primary" @click="subBtn" style="margin: 0 auto 100px auto;display: block;padding: 15px 50px">提交
				</el-button>
			</el-col>
			<el-col :span="12">
				<el-button @click="subBtnByDraft" style="margin: 0 auto 100px auto;display: block;padding: 15px 50px">存入草稿箱
				</el-button>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	import wangEditor from 'wangeditor'
	import ossAuth from '@/commen/oss/ossAuth.js'

	let client = ossAuth.client
	export default {
		name: "addContent",
		data() {
			return {
				tempDate: {},
				mp4Src: '',
				mp3Src: '',
				contentRemark: '',
				contentSource: '',
				contentAuthor: '',
				viewradio: '1',
				isShowpage: false,
				pageView: '',
				keywordList: [{
					keyword: '',
					id: ''
				}],
				value: [],


				vipList: '',
				vip: '',
				imgSrc: '',
				imgList: [],
				address: '',
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
				show: 1,
				title: '',
				type: 5,
				status: this.$constData.statusList[3].value,
				userId: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
				statusList: this.$constData.statusList,
				showList: this.$constData.showList,
				typeListTemp: this.$constData.typeListTemp,
			}
		},
		methods: {
			delmp4src(e) {
				if (e == 'mp3') {
					this.mp3Src = ''
					this.$refs.mp3pathClear.value = ''
				}
				if (e == 'mp4') {
					this.mp4Src = ''
					this.$refs.mp4pathClear.value = ''
				}
			},
			getMP3() {
				if (event.target.files[0].type != 'audio/mp3') {
					this.$message({
						message: '请上传MP3格式的文件',
						type: 'error'
					});
					return;
				}
				this.mechGrantImg = event.target.files[0]
				this.doUpload2(this.mechGrantImg)
			},
			getMP4() {
				// if(event.target.files[0].type != 'audio/mp3'){
				// 	this.$message({
				// 		message: '请上传MP4格式的文件',
				// 		type: 'error'
				// 	});
				// 	return;
				// }
				this.mechGrantImg = event.target.files[0]
				this.doUpload(this.mechGrantImg)
			},
			doUpload2(file) {
				let date = new Date()
				this.size = file.size
				let tmpName = 'zskp/MP3/' + date.getFullYear() + '' + (1 * date.getMonth() + 1) + '' + date.getDate() + '/' +
					encodeURIComponent(file.name)
				this.multipartUpload(tmpName, file, 1)
			},
			doUpload(file) {
				let date = new Date()
				this.size = file.size
				let tmpName = 'zskp/video/' + date.getFullYear() + '' + (1 * date.getMonth() + 1) + '' + date.getDate() + '/' +
					encodeURIComponent(file.name)
				this.multipartUpload(tmpName, file, 0)
			},
			multipartUpload(upName, upFile, val) {
				//Vue中封装的分片上传方法（详见官方文档）
				let _this = this
				try {
					let result = client.multipartUpload(upName, upFile, {
						meta: {
							year: 2017,
							people: 'test'
						}
					}).then(res => {
						//取出存好的url
						let address = res.res.requestUrls[0]
						let _index = address.indexOf('?')
						let src = ''
						if (_index == -1) {
							src = address
						} else {
							src = address.substring(0, _index)
						}
						if (val == 0) {
							this.mp4Src = src
						} else if (val == 1) {
							this.mp3Src = src
						}
					}).catch(err => {
						console.log(err)
					})
				} catch (e) {
					// 捕获超时异常
					if (e.code === 'ConnectionTimeoutError') {
						console.log("Woops,超时啦!");
					}
					console.log(e)
				}
			},
			subBtn() {
				if (this.title == '') {
					this.$message({
						message: '请填写标题',
						type: 'warning'
					});
					return
				}
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
			subBtnByDraft() {
				this.status = 1
				this.editorBtn()
			},
			editorBtn() {
				if (this.title == '') {
					this.$message({
						message: "请填入标题",
						type: 'warning'
					});
					return
				}
				let that = this
				let text = this.editor.txt.html()
				let data = {
					editor: [{
						type: 'textarea',
						value: text
					}],
					show: this.show,
					imgList: this.imgList,
					video: this.mp4Src,
				}
				let tags = {
					homeCotent: this.value
				}
				let cnt = {
					module: this.$constData.module,
					type: 5,
					status: this.status,
					power: 0,
					upUserId: this.userId,
					upChannelId: this.upChannelId == '' ? this.vip : this.upChannelId,
					tags: JSON.stringify(tags),
					title: this.title,
					data: JSON.stringify(data),
					pageView: this.pageView == '' ? 0 : this.pageView,
					isPageView: this.viewradio,
					mp3Src: this.mp3Src,
					contentRemark: this.contentRemark,
					contentSource: this.contentSource,
					contentAuthor: this.contentAuthor,
				}
				if (that.upChannelId != '') {
					cnt.upChannelId = parseInt(that.upChannelId)
				}
				that.$api.addContent(cnt, (res => {
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '添加成功',
							type: 'success'
						});
						this.title = ''
						this.pageView = ''
						this.contentRemark = ''
						this.contentSource = ''
						this.contentAuthor = ''
						localStorage.setItem('tempContent', '');
						localStorage.setItem("tempContentHtml", '');
						that.$router.push('/contentList')
					} else {
						this.$message({
							message: "创建失败，发生未知错误",
							type: 'error'
						});
						that.imgList = []
					}
				}))
			},
			getChannels() {
				let cnt = {
					module: this.$constData.module,
					type: '1',
					status: 0,
					count: 100,
					offset: 0,
				};
				this.$api.getChannels(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.channelList = this.$util.tryParseJson(res.data.c)
					}
				})
			},
			//获取栏目
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
			this.getVips()
			this.getChannels()
			this.getKeyword()
			this.getUser()
			this.editor = new wangEditor('#editor')
			this.editor.customConfig.zIndex = 1
			let _this = this
			this.editor.customConfig.customUploadImg = function(files, insert) {
				try {
					let date = new Date()
					let tmpName = 'zskp/image/' + date.getFullYear() + '' + (1 * date.getMonth() + 1) + '' + date.getDate() + '/' +
						encodeURIComponent(files[0].name)
					console.log('---------tmpName--------------------')
					console.log(tmpName)
					client.multipartUpload(tmpName, files[0], {
						meta: {
							year: 2017,
							people: 'test'
						}
					}).then(res => {
						console.log('--------res-------------------')
						console.log(res)
						//取出存好的url
						let address = res.res.requestUrls[0]
						console.log(address)
						let _index = address.indexOf('?')
						if (_index == -1) {
							_this.imgSrc = address
						} else {
							_this.imgSrc = address.substring(0, _index)
						}
						insert(_this.imgSrc)
					}).catch(err => {
						console.log(err)
					})

				} catch (e) {
					// 捕获超时异常
					if (e.code === 'ConnectionTimeoutError') {
						console.log("Woops,超时啦!");
					}
					console.log(e)
				}
			}
			this.editor.customConfig.onchange = function(html) {
				// html 即变化之后的内容
				localStorage.setItem("tempContentHtml", html);
			}
			this.editor.create();
			this.editor.txt.html(localStorage.getItem('tempContentHtml'))
			let temp = JSON.parse(localStorage.getItem('tempContent'))
			this.title = temp.title
			this.pageView = temp.pageView
			this.contentRemark = temp.contentRemark
			this.contentSource = temp.contentSource
			this.contentAuthor = temp.contentAuthor
		},
		beforeDestroy() {
			this.tempDate = {
				title: this.title,
				pageView: this.pageView,
				contentRemark: this.contentRemark,
				contentSource: this.contentSource,
				contentAuthor: this.contentAuthor,
			}
			localStorage.setItem("tempContent", JSON.stringify(this.tempDate));
		},
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
