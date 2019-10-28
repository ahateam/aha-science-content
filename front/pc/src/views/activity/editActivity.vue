<template>
	<div>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 标题：</span>
				<el-input placeholder="请输入标题" v-model="title" style="display: inline-block;width: 400px"></el-input>
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
			<!-- <el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="显示">
						<el-select v-model="show" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in showList" :key="item.value" :label="item.name" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col> -->
		</el-row>
		<el-row style="margin-top: 20px">
			<el-button type="primary" @click="subBtn" style="margin: 0 auto;display: block;padding: 15px 50px">提交修改
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
				place:'',
				live:'',
				newTime:'',
				address:'',
				cotentHtml:'',
				id:'',
				homeTagName:'',
				channelTag:'',
				homeTag:'',
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
				power:0,
				show: 0,
				title: '',
				status: '',
				userId: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
				statusList: this.$constData.statusList,
				showList: this.$constData.showList,
			}
		},
		methods: {
			subBtn() {
				let that = this
				let a = this.editor.txt.getJSON()
				for (let i = 0; i < a.length; i++) {
					let b = a[i].children
					if(b){
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
			editorBtn() {
				let that = this
				let text = this.editor.txt.html()
				let data = {
					show: this.show,
					imgList: this.imgList,
					place:this.place,
					address:this.address,
					time:this.newTime,
					info:text,
					live:this.live,
				}
				let cnt = {
					id: this.id,
					module: this.$constData.module,
					status: this.status,
					title: this.title,
					data: JSON.stringify(data), 
				}
				console.log(data)
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
			let info = this.$route.params.info
			this.id = info.id
			this.title = info.title
			this.status = info.status
			this.power = info.power
			this.show = JSON.parse(info.data).show
			this.contentType = info.type
			this.editor = new wangEditor('#editor')
			this.editor.create()
			this.cotentHtml = JSON.parse(info.data).info
			this.editor.txt.html(this.cotentHtml)
			this.imgList = JSON.parse(info.data).imgList
			this.address = JSON.parse(info.data).address
			this.time = JSON.parse(info.data).time
			this.live = JSON.parse(info.data).live
			this.place = JSON.parse(info.data).place
			this.newTime = JSON.parse(info.data).newTime
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
