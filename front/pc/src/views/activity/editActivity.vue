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
			<span class="title-box"> 直播时间：</span>(可设置)
			<el-date-picker v-model="liveTime" value-format="timestamp" type="datetimerange" range-separator="至"
			 start-placeholder="开始日期" end-placeholder="结束日期">
			</el-date-picker>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="10">
				<span class="title-box"> 标题：</span>
				<el-input placeholder="请输入标题" v-model="title" style="display: inline-block;width: 400px"></el-input>
			</el-col>
			<el-col :span="10">
				<span class="title-box"> 科普基地：</span>
				<el-select filterable v-model="place" placeholder="请选择科普基地" style="margin-right: 10px;">
					<el-option v-for="(item,index) in placeList" :key="index" :label="item.name" :value="item.id"></el-option>
				</el-select>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="10">
				<span class="title-box"> 活动时间：</span>
				<el-date-picker v-model="uptime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
				</el-date-picker>
			</el-col>
		</el-row>
		<el-row style="margin-bottom: 10px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<div id="editor">
				</div>
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
		</el-row>
		<el-row style="margin-top: 20px">
			<el-button type="primary" @click="subBtn" style="margin:10px auto 100px auto;display: block;padding: 15px 50px">提交修改
			</el-button>
		</el-row>

	</div>
</template>

<script>
	import wangEditor from 'wangeditor'
	export default {
		name: "editActivity",
		data() {
			return {
				uptime:'',
				place:'',
				placeList:'',
				liveStartTime: '',
				liveEndTime: '',
				liveTime: '',
				place: '',
				live: '',
				address: '',
				cotentHtml: '',
				id: '',
				time:'',
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
			timeFilter(str) {
				let date = new Date(parseInt(str));
				let Y = date.getFullYear() + '-';
				let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
				let D = date.getDate() + ' ';
				let h = date.getHours();
				let m = date.getMinutes();
				let s = date.getSeconds();
				return Y + M + D + this.timeparse(h) +':'+ this.timeparse(m) +':'+this.timeparse(s);
			},
			timeparse(str) {
				if (str < 10) {
					str = '0' + str;
				}
				return str
			},
			subBtn() {
				let that = this
				let a = this.editor.txt.getJSON()
				for (let i = 0; i < a.length; i++) {
					let b = a[i].children
					if (b) {
						for (let n = 0; n < b.length; n++) {
							if (b[n] instanceof Object && b[n].tag == 'img') {
								let imgSrc = {
									src: b[n].attrs[0].value
								}
								that.imgList.push(imgSrc)
							}
						}
					}

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
					time: this.uptime == ''?this.time:newTime,
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
			this.editor = new wangEditor('#editor')
			this.editor.customConfig.zIndex = 1
			this.editor.create()
			this.cotentHtml = JSON.parse(info.data).info
			this.editor.txt.html(this.cotentHtml)
			this.imgList = JSON.parse(info.data).imgList
			this.address = JSON.parse(info.data).address
			this.time = JSON.parse(info.data).time
			this.live = JSON.parse(info.data).live
			this.place = JSON.parse(info.data).place
			this.liveStartTime = info.liveStartTime==undefined ?'暂未设置':this.timeFilter(info.liveStartTime)
			this.liveEndTime = info.liveStartTime ==undefined ?'暂未设置':this.timeFilter(info.liveEndTime)
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
