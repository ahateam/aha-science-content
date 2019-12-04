<template>
	<div>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 广告类别：</span>
				<el-select v-model="advertType" placeholder="请选择" style="margin-right: 10px;">
					<el-option v-for="item in advertTypeList" :key="item.id" :label="item.name" :value="item.id">
					</el-option>
				</el-select>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 链接：</span>
				<el-input placeholder="请输入" v-model="title" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 备注：</span>
				<el-input type="textarea" placeholder="请输入" autosize v-model="info" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 排序权重：</span>
				<el-input-number v-model="level" label="请输入"></el-input-number>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 广告banner图：</span>
				<img width="100" :src="imgSrc" v-if="imgSrc">
				<input @change="getMechData1($event)" type="file" class="upload" v-if="imgSrc == ''" />
			</el-col>
		</el-row>
		<el-row style="padding: 20px" v-if="this.advertType == '0' ">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<h2> 你选择的广告类别:【APP主页广告】</h2>
				<h1 style="font-size: 16px;">APP主页广告图片建议：（横屏图片）比如：高度固定为固定高度150px,长度建议最低320px</h1>
			</el-col>
		</el-row>
		<el-row style="padding: 20px" v-if="this.advertType == '1' ">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<h2> 你选择的广告类别:【科普栏目广告】，请你选择一个栏目：</h2>
				<el-select v-model="upChannelId" placeholder="请选择" style="margin-right: 10px;">
					<el-option v-for="item in channelList" :key="item.id" :label="item.title" :value="item.id">
					</el-option>
				</el-select>
				<h1 style="font-size: 16px;">科普栏目广告图片建议：（横屏图片）比如：高度185px左右,长度建议400px左右</h1>
			</el-col>
		</el-row>
		<el-row style="padding: 20px" v-if="this.advertType == '2' ">
			<el-col :span="2" style="min-height: 20px">
			</el-col>
			<el-col :span="22">
				<h2> 你选择的广告类别:【APP启动页广告】，请你选择是否启用当前广告：</h2>
				<el-radio v-model="radio" label="0">设置当前广告为启动页广告</el-radio>
				<el-radio v-model="radio" label="1">仅添加，以后设置</el-radio>
				<h1 style="font-size: 16px;">启动图分辨率建议：（手机竖屏图片）1920px*1080px</h1>
			</el-col>
		</el-row>
		<el-row style="margin-top: 20px;padding-bottom: 10px">
			<el-col :span="4" style="min-height: 20px"></el-col>
			<el-button type="primary" @click="createBtn" style=";padding: 15px 50px">提交
			</el-button>
		</el-row>

	</div>
</template>

<script>
	import ossAuth from '@/commen/oss/ossAuth.js'

	let client = ossAuth.client

	export default {
		name: "addAdvert",
		data() {
			return {
				uploadImgInfo:{},
				channelList: '',
				upChannelId: '',
				radio: '',
				advertTypeList: this.$constData.advertTypeList,
				advertType: '',
				imgSrc: '',
				imgList: [],
				address: '',
				title: '',
				info: '',
				level: '',
			}
		},
		methods: {
			getChannels() {
				let cnt = {
					module: this.$constData.module,
					status: 0,
					type: '0',
					count: 20,
					offset: 0,
				};
				this.$api.getChannels(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.channelList = this.$util.tryParseJson(res.data.c)
					}
				})
			},
			getMechData1() {
				this.mechGrantImg = event.target.files[0]
				this.doUpload(this.mechGrantImg)
			},
			doUpload(file) {
				let date = new Date()
				this.size = file.size
				let tmpName = 'zskp/image/' + date.getFullYear() + '' + (1 * date.getMonth() + 1) + '' + date.getDate() + '/' +
					encodeURIComponent(file.name)
				this.multipartUpload(tmpName, file)
			},
			multipartUpload(upName, upFile) {
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
						// this.uploadImgInfo = address+'?x-oss-process=image/info'
						// console.log(this.uploadImgInfo)
						let _index = address.indexOf('?')
						if (_index == -1) {
							_this.imgSrc = address
						} else {
							_this.imgSrc = address.substring(0, _index)
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

			createBtn() {
				if (this.title == '') {
					this.$message({
						message: '请填写链接',
						type: 'warning'
					})
					return
				}
				if (this.imgSrc == '') {
					this.$message({
						message: '请上传封面图',
						type: 'warning'
					})
					return
				}
				if (this.advertType == '') {
					this.$message({
						message: '请选择广告类别',
						type: 'warning'
					})
					return
				}
				if (this.advertType == '1' && this.upChannelId == '') {
					this.$message({
						message: '因为你选择了栏目广告，请选择一个栏目',
						type: 'warning'
					})
					return
				}
				if (this.advertType == '2' && this.radio == '') {
					this.$message({
						message: '因为你选择了APP启动页广告，请选择一个是否启用状态',
						type: 'warning'
					})
					return
				}
				this.editorBtn()
			},
			editorBtn() {
				let that = this
				// level
				let cnt = {
					moduleId: this.$constData.module,
					imgSrc: this.imgSrc,
					linkSrc: this.title,
					remake: this.info,
					sortSize: this.level,
					type: this.advertType,
				}
				if (this.advertType == '1') {
					cnt.channelId = this.upChannelId
				}
				if (this.advertType == '2') {
					cnt.status = this.radio
				}
				console.log(cnt)
				that.$api.createAdvert(cnt, (res => {
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '创建成功',
							type: 'success'
						});
						that.$router.push('/advertList')
					} else {
						this.$message({
							message: res.data.c,
							type: 'warning'
						});
						that.imgList = []
					}
				}))
			},
		},
		mounted() {
			this.getChannels()
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
