<template>
	<div>
		<el-row class="row-box1">
			<h1 style="font-size: 18px;">创建新栏目</h1>
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
						<el-input v-model="text" placeholder="简单介绍"></el-input>
					</div>
				</el-col>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="4">
				<div class="title-box">状态:</div>
			</el-col>
			<el-col :span="18">
				<el-form label-width="80px">
					<el-form-item>
						<el-select v-model="status" placeholder="请选择状态">
							<el-option v-for="(item,index) in generalStatus" :key="index" :label="item.name" :value="item.value"></el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="4">
				<div class="title-box">栏目图片 320x320:</div>
			</el-col>
			<el-col :span="18">
				<img style="height: 160px;" :src="imgSrc" v-if="imgSrc">
				<input @change="getMechData1($event)" type="file" class="upload" />
			</el-col>
		</el-row>
		<el-col :span="24" style="text-align: center">
			<el-button style="padding: 1em 4em" type="primary" @click="createChannel">创建栏目</el-button>
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
				imgList: [],
				address: '',
				vipTagList: '',
				title: '',
				imgSrc: '',
				text: '',
				tagList: [],
				inputVisible: false,
				inputValue: '',
				status: 0,
				generalStatus: this.$constData.generalStatus,
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
			///-----------
			createChannel() {
				if(this.title == ''){
					this.$message({
						message: '请输入标题',
						type: 'warning'
					});
					return;
				}
				if(this.text == ''){
					this.$message({
						message: '请输入简介',
						type: 'warning'
					});
					return;
				}
				if(this.imgSrc == ''){
					this.$message({
						message: '请上传图片',
						type: 'warning'
					});
					return;
				}
				let that = this
				let data = {
					info: this.text,
					img: this.imgSrc
				}
				let vipTag = {
					channel: this.tagList
				}
				let cnt = {
					module: this.$constData.module,
					title: this.title,
					status: this.status,
					tags: JSON.stringify(vipTag),
					data:JSON.stringify(data),
					type:'0',
				}
				this.$api.createChannel(cnt, (res => {
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '添加成功',
							type: 'success'
						});
						that.$router.push('/svipList')
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
