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
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="标签分组:">
						<el-select v-model="tagGroup" placeholder="请选择" style="margin-right: 10px;" @change="changeTagGroup">
							<el-option v-for="item in tagGroupList" :key="item.groupName" :label="item.groupName" :value="item.groupName">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="选择标签">
						<el-select v-model="homeTagName" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in homeTag" :key="item.name" :label="item.title" :value="item.name">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="选择专题">
						<el-select v-model="upChannelId" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in channelList" :key="item.id" :label="item.title" :value="item.id">
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
		<div style="text-align: center;margin-top: 20px;">
			<iframe :src="src" width="500px" height="300px"></iframe>
			<iframe :src="imgSrc" width="500px" height="300px"></iframe>
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
				tagGroup:'',
				tagGroupList:'',
				homeTagName: '',
				homeTag: '',
				channelList: [{
					title: '',
					id: 0,
				}],
				status: '',
				statusList: this.$constData.statusList,
				conType: '',
				text: '',
				title: '',
				userId: 400795534052038,
				//展示样式
				showList: [{
						name: '竖屏',
						value: 2,
					},
					{
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
				this.doUpload(this.mechGrantImg,1)
			},
			getMechData2() {
				this.mechGrantImg = event.target.files[0]
				this.doUpload(this.mechGrantImg,2)
			},
			doUpload(file,val) {
				let date = new Date()
				this.size = file.size
				let tmpName = 'zskp/image/' + date.getFullYear() + '' + (1 * date.getMonth() + 1) + '' + date.getDate() + '/' +
					encodeURIComponent(file.name)
				this.multipartUpload(tmpName, file,val)
			},
			multipartUpload(upName, upFile,val) {
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
						if (_index == -1) {
							_this.imgSrc = address
						} else {
							_this.imgSrc = address.substring(0, _index)
						}
						if(val == 1){
							this.src = _this.imgSrc 
						}else if(val ==2){
							this.imgSrc = _this.imgSrc
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
				let that = this
				let dataUrl = {
					url: this.src,
					text: this.text,
					show: this.show,
					imgSrc: this.imgSrc
				}
				let cid = `{"homeCotent":["${this.homeTagName}"]}`
				let cnt = {
					module: this.$constData.module,
					type: 3,
					status: this.status,
					power: 0,
					upUserId: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
					upChannelId: this.upChannelId,
					tags: JSON.parse(cid),
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
						that.$router.push('/contentList')
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
					//tags: tags, 
					count: 20,
					offset: 0,
				};
				this.$api.getChannels(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.channelList = this.$util.tryParseJson(res.data.c)
					}
				})
			},
			getTagGroup() {
				let cnt = {
						moduleId: this.$constData.module,
						count: 200,
						offset: 0,
					};
				this.$api.getContentTagGroup(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tagGroupList = this.$util.tryParseJson(res.data.c)
					}
				})
			},
			changeTagGroup(){
				let cnt = {
						moduleId: this.$constData.module,
						group: this.tagGroup, 
						status: 1,
						count: 200, 
						offset: 0,  
					};
				this.$api.getContentTag(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.homeTag = this.$util.tryParseJson(res.data.c)
					}
				})
			}
		},
		mounted() {
			this.getTagGroup()
			this.getChannels()
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
