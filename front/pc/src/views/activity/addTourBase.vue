<template>
	<div>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 基地名称：</span>
				<el-input placeholder="请输入名称" v-model="title" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>

		<baidu-map class="bm-view" :ak="ak" :center="center" :zoom="14" @ready="handler" @click="clickEvent"
		 :scroll-wheel-zoom="true">
			<bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :showAddressBar="true" :autoLocation="true"></bm-geolocation>
		</baidu-map>

		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 基地地点：</span>
				<v-distpicker :province="province" :city="city" :area="area" @selected="onSelected" style="display: inline-block;"></v-distpicker>
				<el-input placeholder="详细地址:如道路街道,小区" v-model="detail_address" style="width: 300px;margin-left: 5px;"></el-input>
			</el-col>
		</el-row>

		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 营业时间：<el-button style="margin-right: 8px;" @click="timeStr">长期有效</el-button></span>
				<el-time-picker is-range arrow-control v-model="time" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间"
				 placeholder="选择时间范围">
				</el-time-picker>
			</el-col>
		</el-row>

		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 基地封面图：</span>
				<img height="100" :src="imgSrc" v-if="imgSrc">
				<input @change="getMechData1($event)" type="file" class="upload" />
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 购票链接：</span>
				<el-input placeholder="请输入购票链接" v-model="shopLink" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<el-row style="margin-top: 10px">
			<el-col :span="24">
				<p style="font-size: 15px;"><span style="color: red;font-size: 15px;">(注：</span>1.如是从word里复制的文本请 <span style="color: red;">"鼠标右键"</span>>选择
					<span style="color: red;">"粘贴为纯文本"</span>,避免APP端无法正常显示。2.从网页复制的文本可直接粘贴，图片请点击"图片图标">选择"100%"</p>
				<p style="font-size: 15px;">3.如没有鼠标"右键">选择 "粘贴为纯文本"功能，推荐使用谷歌，360,QQ,搜狗等浏览器...</p>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 基地简介：</span>
				<el-row style="margin-bottom: 10px">
					<el-col :span="2" style="min-height: 20px"></el-col>
					<el-col :span="20">
						<div id="toolbar"></div>
						<div id="editor"></div>
					</el-col>
				</el-row>
			</el-col>
		</el-row>

		<el-row style="margin-top: 20px;padding-bottom: 10px">
			<el-col :span="4" style="min-height: 20px"></el-col>
			<el-button type="primary" @click="createBtn" style="margin-bottom: 100px;padding: 15px 50px">提交
			</el-button>
		</el-row>

	</div>
</template>
<script>
	import wangEditor from 'wangeditor'
	import ossAuth from '@/commen/oss/ossAuth.js'
	import VDistpicker from 'v-distpicker'
	let client = ossAuth.client

	export default {
		components: {
			VDistpicker
		},
		name: "addTourBase",
		data() {
			return {
				textimgSrc: '',
				detail_address: '',
				editor: {},
				homeTagName: '',
				homeTag: '',
				imgSrc: '',
				imgList: [],
				tag: '',
				time: [new Date(2019, 10, 20, 8, 30), new Date(2019, 10, 20, 17, 30)],
				workTime: '',
				shopLink: '',
				address: '',
				show: Math.round(Math.random()),
				title: '',
				info: '',
				power: 0,
				status: this.$constData.statusList[3].value,
				userId: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,

				contentType: this.$constData.typeList[3].value,

				ak: 'SOOpwhK5pwWkiC6X0TWgQY5RHGdlQGgt',
				center: {
					lng: 0,
					lat: 0
				},
				zoom: 3,

				locData: {
					longitude: '',
					latitude: '',
					address: ''
				},
				province: '',
				city: '',
				area: ''
			}
		},

		methods: {
			clickEvent(e) {
				map.clearOverlays()
				let Icon_0 = new BMap.Icon("http://weapp-xhj.oss-cn-hangzhou.aliyuncs.com/zskp/image/20191110/positioin.png", new BMap
					.Size(64, 64), {
						anchor: new BMap.Size(18, 32),
						imageSize: new BMap.Size(30, 30)
					})
				var myMarker = new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat), {
					icon: Icon_0
				})
				map.addOverlay(myMarker)
				//用所定位的经纬度查找所在地省市街道等信息
				var point = new BMap.Point(e.point.lng, e.point.lat)
				var gc = new BMap.Geocoder()
				let _this = this
				gc.getLocation(point, function(rs) {
					var addComp = rs.addressComponents
					//console.log(rs.address);//地址信息
					_this.locData.address = rs.address
					_this.province = rs.addressComponents.province
					_this.city = rs.addressComponents.city
					_this.area = rs.addressComponents.district
					_this.detail_address = rs.addressComponents.street + rs.addressComponents.streetNumber
					if (_this.province != _this.city) {
						_this.address = _this.province + _this.city + _this.area
					} else {
						_this.address = _this.province + _this.area
					}
					console.log(rs)
				})
				this.locData.longitude = e.point.lng
				this.locData.latitude = e.point.lat
				console.log(this.locData)
			},
			timeStr() {
				this.time = '长期'
				this.$message({
					message: '已选择长期有效',
					type: 'success'
				})
			},
			getMechData1() {
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

			getTime(date) {
				let h = date.getHours();
				let m = date.getMinutes();
				let s = date.getSeconds();
				return `${h}:${m<10?'0'+m:m}:${s<10?'0'+s:s}`
			},

			createBtn() {
				if (this.title == '') {
					this.$message({
						message: '请填写名称',
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

				if (this.time == '') {
					this.$message({
						message: '请选择营业时间',
						type: 'warning'
					})
					return
				}
				if (this.shopLink == '') {
					this.$message({
						message: '请输入购票链接',
						type: 'warning'
					})
					return
				}
				this.editorBtn()
			},
			editorBtn() {
				let that = this
				this.imgList.push(this.imgSrc)
				if (this.time != '长期') {
					let time1 = new Date(this.time[0])
					let time2 = new Date(this.time[1])
					let newTime = this.getTime(time1) + '至' + this.getTime(time2)
					this.newTime = newTime
				}
				let data = {
					info: this.editor.txt.html(),
					img: this.imgList,
					workTime: this.time == '长期' ? this.time : this.newTime,
				}
				let cnt = {
					moduleId: this.$constData.module,
					name: this.title,
					address: this.address + '' + this.detail_address,
					data: JSON.stringify(data),
					buyTicketsLink: this.shopLink,
				}
				if (this.address == '') {
					cnt.address = this.province + '' + this.city + '' + this.area + '' + this.detail_address
				}
				console.log(cnt)
				that.$api.createTourBase(cnt, (res => {
					console.log(res)
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '创建成功',
							type: 'success'
						});
						that.$router.push('/tourBaseList')
					} else {
						this.$message({
							message: res.data.rm,
							type: 'warning'
						});
						that.imgList = []
					}
				}))
			},
			onSelected(data) {
				this.province = data.province.value
				this.city = data.city.value
				this.area = data.area.value
			},

			handler({
				BMap,
				map
			}) {
				console.log(BMap, map)
				this.center.lng = 116.404
				this.center.lat = 39.915
				this.zoom = 15
				window.map = map
			},
		},
		mounted() {
			this.editor = new wangEditor('#toolbar','#editor')
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

	.disabled-select>>>select {
		background-color: #f5f7fa;
		border-color: #e4e7ed;
		color: #c0c4cc;
		cursor: not-allowed;
	}

	#container {
		width: 300px;
		height: 300px;
	}

	.bm-view {
		margin: 0 auto;
		padding: 10px 0;
		width: 80%;
		height: 300px;
		box-shadow: 0 1px 3px rgba(0, 0, 0, .2)
	}
</style>
