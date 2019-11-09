<template>
	<div>
		<el-row style="padding: 20px">
			<el-col :span="20">
				<span class="title-box"> 标题：</span>
				<el-input placeholder="请输入标题" v-model="title" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="状态:">
						<el-select v-model="status" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in statusList" :key="item.value" :label="item.name" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
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
			<el-col :span="13">
				<el-form label-width="80px">
					<el-form-item label="关键词:">
						<el-select v-model="value" multiple   default-first-option placeholder="请选择文章标签-提示:可多选，可删除" style="width: 100%;">
							<el-option v-for="item in keywordList" :key="item.id" :label="item.keyword" :value="item.keyword">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
		</el-row>
		<el-row style="margin-top: 10px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<div id="editor"></div>
			</el-col>
		</el-row>
		<el-row style="margin-top: 20px">
			<el-button type="primary" @click="subBtn" style="margin: 0 auto;display: block;padding: 15px 50px">提交
			</el-button>
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
				show: 0,
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
					homeCotent:this.value
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
						that.$router.push('/contentList')
					} else {
						this.$message({
							message: res.data.rm,
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
			getKeyword(){
				let cnt = {
					count:200,
					offset:'0',
				}
				this.$api.getKeywords(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.keywordList = this.$util.tryParseJson(res.data.c)
					}
				})
			}
			
		},
		mounted() {
			this.getVips()
			this.getChannels()
			this.getKeyword()
			this.editor = new wangEditor('#editor')
			this.editor.customConfig.zIndex = 1
			let _this = this
			this.editor.customConfig.customUploadImg = function(files, insert) {
				try {
					let date = new Date()
					let tmpName = 'zskp/image/' + date.getFullYear() + '' + (1 * date.getMonth() + 1) + '' + date.getDate() + '/' +
						encodeURIComponent(files[0].name)
					client.multipartUpload(tmpName, files[0], {
						meta: {
							year: 2017,
							people: 'test'
						}
					}).then(res => {
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
			this.editor.create();
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
