<template>
	<div>
		<el-row class="row-box1">
			<img :src="imgSrc" width="80" class="head_pic" />
			<el-col :span="24" style="margin-bottom: 10px">
				<el-col :span="4">
					<div class="title-box">标题:</div>
				</el-col>
				<el-col :span="14">
					<div class="text-box">
						<el-input v-model="title" placeholder="标题"></el-input>
					</div>
				</el-col>
			</el-col>
		</el-row>
		<el-row class="row-box1">
			<el-col :span="24" style="margin-bottom: 10px">
				<el-col :span="4">
					<div class="title-box">简介:</div>
				</el-col>
				<el-col :span="14">
					<div class="text-box">
						<el-input v-model="info" placeholder="简介"></el-input>
					</div>
				</el-col>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="4">
				<div class="title-box">专题图片:</div>
			</el-col>
			<el-col :span="18">
				<img width="200" :src="imgSrc" v-if="imgSrc">
				<input @change="getMechData1($event)" type="file" class="upload" />
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="4">
				<div class="title-box">边框色:</div>
			</el-col>
			<el-col style="position: relative;z-index: 99;line-height: 40px;" :span="18">
				<colorPicker v-model="colors" />
			</el-col>
		</el-row>
		<!-- <el-row>
			<el-col :span="4">
				<div class="title-box">你可输入的标签:</div>
			</el-col>
			<el-col :span="18">
				<el-tag type="info" v-for="tag in vipTagList" :key="tag.name">{{tag.name}}</el-tag>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="4">
				<div class="title-box">当前标签:</div>
			</el-col>
			<el-col :span="18">
				<el-tag type="info" v-for="tag in nowVipTagList" :key="tag.name">{{tag}}</el-tag>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="4">
				<div class="title-box">修改后的标签:</div>
			</el-col>
			<el-col :span="18" class="text-box">
				<el-tag :key="tag" v-for="tag in tagList" closable :disable-transitions="false" @close="handleClose(tag)">
					{{tag}}
				</el-tag>
				<el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput" size="small"
				 @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
				</el-input>
				<el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
			</el-col>
		</el-row> -->
		<el-col :span="24" style="text-align: center">
			<el-button style="padding: 1em 4em" type="primary" @click="createChannel">提交修改</el-button>
		</el-col>
	</div>
</template>

<script>
	import ossAuth from '@/commen/oss/ossAuth.js'
	let client = ossAuth.client
	export default {
		name: "addSvip",
		data() {
			return {
				colors:'',
				info: '',
				nowVipTagList: '',
				vipTagList: '',
				title: '',
				imgSrc: '',
				text: '',
				tagList: [],
				inputVisible: false,
				inputValue: '',
				status: '',
				generalStatus: this.$constData.generalStatus,
				id: '',
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
			createChannel() {
				let that = this
				let data = {
					info: this.info,
					img: this.imgSrc
				}
				let cnt = {
					id: this.id,
					module: this.$constData.module,
					title: this.title,
					data: JSON.stringify(data),
					boxBackgroundColor: this.colors,
				}
				this.$api.editChannel(cnt, (res => {
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '修改成功',
							type: 'success'
						});
						that.$router.push('/columnList')
					} else {
						this.$message({
							message: res.data.c,
							type: 'warning'
						});
					}
				}))
			},
			// 标签
			handleClose(tag) {
				this.tagList.splice(this.tagList.indexOf(tag), 1);
			},
			showInput() {
				this.inputVisible = true;
				this.$nextTick(_ => {
					this.$refs.saveTagInput.$refs.input.focus();
				});
			},
			handleInputConfirm() {
				let that = this
				let inputValue = this.inputValue;
				if (inputValue) {
					that.tagList.push(inputValue);
				}
				this.inputVisible = false;
				this.inputValue = '';
			},
		},
		mounted() {
			let info = this.$route.params.info
			this.title = info.title
			this.nowVipTagList = JSON.parse(info.tags).channel
			this.id = info.id
			this.info = JSON.parse(info.data).info
			this.imgSrc = JSON.parse(info.data).img
			this.colors = info.boxBackgroundColor
		}
	}
</script>

<style lang="scss" scoped>
	.row-box1 {
		margin-top: 20px;
		padding: 15px;
		background: #fff;
	}

	.title-box {
		font-size: 1.6rem;
		line-height: 40px;
		letter-spacing: 2px;
		font-weight: 600;
		color: #666;
		text-align: right;
	}

	.text-box {
		margin-left: 50px;
	}

	.el-tag+.el-tag {
		margin-left: 10px;
	}

	.button-new-tag {
		margin-left: 10px;
		height: 32px;
		line-height: 30px;
		padding-top: 0;
		padding-bottom: 0;
	}

	.input-new-tag {
		width: 90px;
		margin-left: 10px;
		vertical-align: bottom;
	}
</style>
