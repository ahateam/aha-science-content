<template>
	<div>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 基地名称：</span>
				<el-input placeholder="请输入名称" v-model="title" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 基地简介：</span>
				<el-row style="margin-bottom: 10px">
					<el-col :span="2" style="min-height: 20px"></el-col>
					<el-col :span="20">
						<div id="editor"></div>
					</el-col>
				</el-row>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 购票链接：</span>
				<el-input placeholder="请输入购票链接" v-model="shopLink" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<el-row style="margin-top: 20px;padding-bottom: 10px">
			<el-col :span="4" style="min-height: 20px"></el-col>
			<el-button type="primary" @click="editorBtn" style="margin-bottom: 100px;padding: 15px 50px">提交
			</el-button>
		</el-row>

	</div>
</template>

<script>
	import wangEditor from 'wangeditor'
	import ossAuth from '@/commen/oss/ossAuth.js'
	let client = ossAuth.client

	export default {
		name: "editTourBase",
		data() {
			return {
				id:'',
				title:'',
				shopLink:'',
				address:'',
				editor:{},
				imgList:[],
				workTime:'',
				status: this.$constData.statusList[3].value,
				userId: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
				contentType: this.$constData.typeList[3].value,
			}
		},
		methods: {
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
			createBtn() {
			},
			editorBtn() {
				let that = this
				let data = {
					info: this.editor.txt.html(),
					img: this.imgList,
					workTime: this.workTime,
				}
				let cnt = {
					id:this.id,
					moduleId: this.$constData.module,
					name: this.title,
					address:this.address,
					data: JSON.stringify(data),
					buyTicketsLink: this.shopLink,
				}
				console.log(cnt)
				that.$api.updateTourBase(cnt, (res => {
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '修改成功',
							type: 'success'
						});
						that.$router.push('/tourBaseList')
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
			this.editor = new wangEditor('#editor')
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
			let info = this.$route.params.info
			console.log(info)
			this.id = info.id
			this.title = info.name;
			this.editor.txt.html(JSON.parse(info.data).info)
			this.shopLink = info.buyTicketsLink
			this.address = info.address
			this.imgList = JSON.parse(info.data).img
			this.workTime = JSON.parse(info.data).workTime
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
</style>
