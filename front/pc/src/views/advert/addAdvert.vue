<template>
	<div>
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
				<!-- <el-input type="number" placeholder="请输入" autosize v-model="" style="display: inline-block;width: 400px"></el-input> -->
				<el-input-number v-model="level" label="请输入"></el-input-number>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 广告banner图：</span>
				<img width="500" :src="imgSrc" v-if="imgSrc">
				<input @change="getMechData1($event)" type="file" class="upload" v-if="imgSrc == ''" />
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
				imgSrc: '',
				imgList: [],
				address: '',
				title: '',
				info: '',
				level:'',
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
						console.log(address)
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
					sortSize:this.level,
				}
				that.$api.createAdvert(cnt, (res => {
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '创建成功',
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
