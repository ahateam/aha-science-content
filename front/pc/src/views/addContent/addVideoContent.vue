<template>
	<div>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 输入标题：</span>
				<el-input placeholder="请输入标题" v-model="title" style="display: inline-block;width: 400px">
				</el-input>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box">输入简介：</span>
				<el-input placeholder="请输入简介" v-model="text" style="display: inline-block;width: 400px">
				</el-input>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 视频地址：</span>
				<input @change="getMechData1($event)" type="file" class="upload" v-if="src == ''" />
				</el-input>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 封面地址：</span>
				<input @change="getMechData2($event)" type="file" class="upload" v-if="imgSrc == ''" />
				</el-input>
			</el-col>
		</el-row>
		<el-row style="margin-bottom: 10px">
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="展示样式">
						<el-select v-model="show" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in showList" :key="item.value" :label="item.name" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="初始状态">
						<el-select v-model="status" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in statusList" :key="item.value" :label="item.name" :value="item.value">
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
		<el-row style="margin-top: 20px">
			<el-button type="primary" @click="addContent" style="margin: 0 auto;display: block;padding: 15px 50px">提交
			</el-button>
		</el-row>
		<div style="text-align: center;margin-top: 50px;">
				<video :src="src" controls="controls" style="width: 20%; margin-bottom: 150px;"></video>
				<img :src="imgSrc"  style="width: 20%; margin-bottom: 150px;" />
		</div>
	</div>
</template>

<script>
	import ossAuth from '@/commen/oss/ossAuth.js'

	let client = ossAuth.client
	export default {
		name: "addVideoContent",
		data() {
			return {
				keywordList:[{
					keyword: '',
					id: ''
				}],
				value:'',
				vip:'',
				vipList:'',
				channelList: [{
					title: '',
					id: 0,
				}],
				status: 4,
				statusList: this.$constData.statusList,
				conType: '',
				text: '',
				title: '',
				userId: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
				//展示样式
				showList: [{
						name: '右侧',
						value: 1,
					},
					{
						name: '底部',
						value: 0,
					}
				],
				show: 0,
				upChannelId: '',
				paid: 0,
				tag: '',
				paidIndex: false,
				typeList: this.$constData.typeList,
				videoSrc: [],
				src: '',
				imgSrc: '',
			}
		},
		methods: {
			getMechData1() {
				this.mechGrantImg = event.target.files[0]
				this.doUpload(this.mechGrantImg)
			},
			getMechData2() {
				this.mechGrantImg = event.target.files[0]
				this.doUpload2(this.mechGrantImg)
			},
			doUpload(file) {
				let date = new Date()
				this.size = file.size
				let tmpName = 'zskp/video/' + date.getFullYear() + '' + (1 * date.getMonth() + 1) + '' + date.getDate() + '/' +
					encodeURIComponent(file.name)
				this.multipartUpload(tmpName, file, 0)
			},
			doUpload2(file) {
				let date = new Date()
				this.size = file.size
				let tmpName = 'zskp/img/' + date.getFullYear() + '' + (1 * date.getMonth() + 1) + '' + date.getDate() + '/' +
					encodeURIComponent(file.name)
				this.multipartUpload(tmpName, file, 1)
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
							this.src = src
						} else if (val == 1) {
							this.imgSrc = src
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
			//--------
			addContent() {
				if(this.title == ''){
					this.$message({
						message: "请输入标题",
						type: 'warning'
					});
					return;
				}if(this.text == ''){
					this.$message({
						message: "请输入简介",
						type: 'warning'
					});
					return;
				}
				if(this.src == ''){
					this.$message({
						message: "请上传视频",
						type: 'warning'
					});
					return;
				}
				let that = this
				let dataUrl = {
					url: this.src,
					text: this.text,
					show: this.show,
					imgSrc: this.imgSrc
				}
				let tags = {
					homeCotent:this.value
				}
				let cnt = {
					module: this.$constData.module,
					type: 3,
					status: this.status,
					power: 0,
					upUserId: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
					upChannelId:  this.upChannelId == '' ? this.vip : this.upChannelId,
					tags: JSON.stringify(tags),
					title: this.title,
					data: dataUrl,
				}
				if (this.upChannelId != '') {
					cnt.upChannelId = this.upChannelId
				}
				this.$api.addContent(cnt, (res => {
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '添加成功',
							type: 'success'
						});
						that.$router.push('/videoContentList')
					} else {
						this.$message({
							message: res.data.c,
							type: 'warning'
						});
					}
				}))
			},
			getChannels() {
				let cnt = {
					module: this.$constData.module,
					status: 0,
					type: '1',
					count: 20,
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
			let info = this.$route.params.info
			if (info != undefined) {
				this.upChannelId = info.id
				this.paidIndex = true
			}
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
