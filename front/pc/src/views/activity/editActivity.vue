<template>
	<div>
		<el-row style="padding: 20px" class="titleInfo">
			<h1 style="font-size: 16px;">基本信息</h1>
			<p>活动id:{{id}}</p>
			<p>活动地点:{{address}}</p>
			<p>活动时间:{{time}}</p>
			<p>直播地址:{{live}}</p>
			<p>科普基地:{{place}}</p>
			<p>当前直播开始时间:{{liveStartTime}}</p>
			<p>当前直播结束时间:{{liveEndTime}}</p>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 活动标题：</span>
				<el-input placeholder="请输入标题" v-model="title" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="10">
				<span class="title-box"> 活动地点：</span>
				<el-input placeholder="请输入活动地点" v-model="address" style="display: inline-block;width: 350px"></el-input>
			</el-col>

			<el-col :span="10">
				<span class="title-box"> 科普基地：</span>
				<el-select filterable v-model="place" placeholder="请选择科普基地" style="margin-right: 10px;">
					<el-option v-for="(item,index) in placeList" :key="index" :label="item.name" :value="item.id"></el-option>
				</el-select>
			</el-col>
		</el-row>

		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="10">
				<span class="title-box"> 直播链接：</span>
				<el-input placeholder="请输入直播链接" v-model="live" style="display: inline-block;width: 350px"></el-input>
			</el-col>

			<el-col :span="10">
				<span class="title-box"> 活动时间：<el-button @click="timeStr">长期有效</el-button></span>
				<el-date-picker v-model="time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
				</el-date-picker>
			</el-col>
		</el-row>

		<el-row>
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="10">
				<span class="title-box"> 活动封面图：</span>
				<input @change="getMechData1($event)" type="file" class="upload" />
			</el-col>
			<el-col :span="10">
				<span class="title-box"> 直播时间：</span>(可暂时不设置)
				<el-date-picker v-model="liveTime" value-format="timestamp" type="datetimerange" range-separator="至"
				 start-placeholder="开始日期" end-placeholder="结束日期">
				</el-date-picker>
			</el-col>
		</el-row>
		<el-row style="margin-top: 50px;">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="18">
				<span class="title-box"> 活动封面图：</span>
				原封面图：<img height="100" :src="imgSrcTemp" />
				修改后封面图：<img height="100" :src="imgSrc" />
			</el-col>
		</el-row>
		<el-row style="margin-top: 10px">
			<el-col :span="24">
				<p style="font-size: 15px;"><span style="color: red;font-size: 15px;">(注：</span>1.如是从word里复制的文本请 <span style="color: red;">"鼠标右键"</span>>选择  <span style="color: red;">"粘贴为纯文本"</span>,避免APP端无法正常显示。2.从网页复制的文本可直接粘贴，图片请点击"图片图标">选择"100%"</p>
				<p style="font-size: 15px;">3.如没有鼠标"右键">选择  "粘贴为纯文本"功能，推荐使用谷歌，360,QQ,搜狗等浏览器...</p>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<div class="title-box" style="float: left;"> 活动简介：</div>
				<el-col :span="20">
					<div id="toolbar"></div>
					<div id="editor"></div>
				</el-col>
			</el-col>
		</el-row>
		<el-row style="margin-top: 20px;padding-bottom: 10px">
			<el-col :span="4" style="min-height: 20px"></el-col>
			<el-button type="primary" @click="subBtn()" style="margin-bottom: 150px;">提交修改</el-button>
		</el-row>
	</div>
</template>

<script>
	import wangEditor from 'wangeditor'
	import ossAuth from '@/commen/oss/ossAuth.js'

	let client = ossAuth.client
	export default {
		name: "editActivity",
		data() {
			return {
				imgSrcTemp: '',
				imgSrc: '',
				uptime: '',
				place: '',
				placeList: '',
				liveStartTime: '',
				liveEndTime: '',
				liveTime: '',
				live: '',
				address: '',
				cotentHtml: '',
				id: '',
				time: '',
				homeTagName: '',
				channelTag: '',
				homeTag: '',
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
				power: 0,
				show: 0,
				title: '',
				status: '',
				userId: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
				statusList: this.$constData.statusList,
				showList: this.$constData.showList,
			}
		},
		methods: {
			timeStr() {
				this.time = '长期'
				this.$message({
					message: '已选择长期有效',
					type: 'success'
				})
			},
			timeFilter(str) {
				let date = new Date(parseInt(str));
				let Y = date.getFullYear() + '-';
				let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
				let D = date.getDate() + ' ';
				let h = date.getHours();
				let m = date.getMinutes();
				let s = date.getSeconds();
				return Y + M + D + this.timeparse(h) + ':' + this.timeparse(m) + ':' + this.timeparse(s);
			},
			getMechData1() {
				this.imgList = []
				this.mechGrantImg = event.target.files[0]
				this.doUpload(this.mechGrantImg)
			},
			
			doUpload(file) {
				let date = new Date()
				this.size = file.size
				let tmpName = 'zskp/image/' + date.getFullYear() + '' + (1 * date.getMonth() + 1) + '' + date.getDate() + '/' +(new Date()).valueOf()+"_"+
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
						let _index = address.indexOf('?')
						console.log(_index)
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
			timeparse(str) {
				if (str < 10) {
					str = '0' + str;
				}
				return str
			},
			subBtn() {
				console.log(this.imgSrc)
				if (this.imgSrc != '') {
					let imgSrc = {
						src: this.imgSrc
					}
					this.imgList.push(imgSrc)
				}
				if (this.time == '') {
					this.time = '长期'
				}
				let that = this
				let imgSrc = {
					src: this.imgSrc
				}
				this.editorBtn()
			},
			getTime(time) {
				let y = time.getFullYear()
				let m = time.getMonth() * 1 + 1
				let d = time.getDate()
				return `${y}-${m}-${d}`
			},
			getPlace() {
				let cnt = {
					moduleId: this.$constData.module, // Long 模块编号
					userCoordinate: '0,0',
					count: 300, // int
					offset: 0, // int
				}
				this.$api.getTourBases(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.placeList = this.$util.tryParseJson(res.data.c)
					} else {
						this.addressList = []
					}
				})
			},
			editorBtn() {
				if (this.time != '长期') {
					let time1 = new Date(this.time[0])
					let time2 = new Date(this.time[1])
					let newTime = this.getTime(time1) + '至' + this.getTime(time2)
					this.newTime = newTime
				}
				let that = this
				let text = this.editor.txt.html()
				let time1 = new Date(this.uptime[0])
				let time2 = new Date(this.uptime[1])
				let newTime = this.getTime(time1) + '至' + this.getTime(time2)
				let data = {
					show: this.show,
					imgList: this.imgList,
					place: this.place,
					address: this.address,
					time: this.time == '长期' ? this.time : this.newTime,
					info: text,
					live: this.live,
				}
				let cnt = {
					id: this.id,
					module: this.$constData.module,
					status: this.status,
					title: this.title,
					data: JSON.stringify(data),
				}
				if (this.liveTime != '') {
					cnt.liveStartTime = this.liveTime[0];
					cnt.liveEndTime = this.liveTime[1];
				}
				that.$api.editContent(cnt, (res => {
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '修改成功',
							type: 'success'
						});
						that.$router.push('/activityList')
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
			this.getPlace()
			let info = this.$route.params.info
			this.id = info.id
			this.title = info.title
			this.status = info.status
			this.power = info.power
			this.show = JSON.parse(info.data).show
			this.contentType = info.type
			this.editor = new wangEditor('#toolbar', '#editor')
			this.editor.customConfig.zIndex = 1
			let _this = this
			this.editor.customConfig.customUploadImg = function(files, insert) {
				try {
					let date = new Date()
					let tmpName = 'zskp/image/' + date.getFullYear() + '' + (1 * date.getMonth() + 1) + '' + date.getDate() + '/' +(new Date()).valueOf()+"_"+
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
							_this.textimgSrc = address
						} else {
							_this.textimgSrc = address.substring(0, _index)
						}
						insert(_this.textimgSrc)
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
			this.editor.create()
			this.cotentHtml = JSON.parse(info.data).info
			this.editor.txt.html(this.cotentHtml)
			this.imgList = JSON.parse(info.data).imgList
			console.log(JSON.parse(info.data).imgList)
			this.imgSrcTemp = JSON.parse(info.data).imgList[0].src
			this.address = JSON.parse(info.data).address
			this.time = JSON.parse(info.data).time
			this.live = JSON.parse(info.data).live
			this.place = JSON.parse(info.data).place
			this.liveStartTime = info.liveStartTime == undefined ? '暂未设置' : this.timeFilter(info.liveStartTime)
			this.liveEndTime = info.liveStartTime == undefined ? '暂未设置' : this.timeFilter(info.liveEndTime)
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

	.titleInfo p {
		font-size: 16px;
	}
</style>
