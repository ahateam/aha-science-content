<template>
	<div>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 标题：</span>
				<el-input placeholder="请输入标题" v-model="title" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<!-- <el-row style="margin-bottom: 10px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<div id="editor"></div>
			</el-col>
		</el-row> -->
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
					<el-form-item label="专栏">
						<el-select v-model="upChannelId" placeholder="请选择" style="margin-right: 10px;" >
							<el-option v-for="item in channelList" :key="item.id" :label="item.title" :value="item.id" >
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
		</el-row>
		<el-row style="margin-top: 20px">
			<el-button type="primary" @click="editorBtn" style="margin: 0 auto;display: block;padding: 15px 50px">提交修改
			</el-button>
		</el-row>

	</div>
</template>

<script>
	import wangEditor from 'wangeditor'
	export default {
		name: "addContent",
		data() {
			return {
				tagGroupList:'',
				tagGroup:'',
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
				show: 0,
				title: '',
				status: '',
				userId: 401770184378345,
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
					for (let n = 0; n < b.length; n++) {
						if (b[n] instanceof Object && b[n].tag == 'img') {
							let imgSrc = {
								src: b[n].attrs[0].value
							}
							that.imgList.push(imgSrc)
						}
					}
				}
				this.editorBtn()
			},
			editorBtn() {
				let that = this
				//let text = this.editor.txt.html()
				let cid = `{"homeCotent":["${this.homeTagName}"]}`
				let cnt = {
					id: this.id,
					module: this.$constData.module,
					type: this.contentType,
					status: this.status,
					power: this.power,
					upUserId: this.userId,
					upChannelId: this.upChannelId, 
					tags: JSON.parse(cid),
					title: this.title,
				}
				if (that.upChannelId != '') {
					cnt.upChannelId = parseInt(that.upChannelId)
				}
				that.$api.editContent(cnt, (res => {
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '修改成功',
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
			getChannels() {
				let cnt = {
					module: this.$constData.module,
					status: 0,
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
						console.log(this.tagGroupList)
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
			// this.editor = new wangEditor('#editor')
			// this.editor.create()
			let info = this.$route.params.info
			this.id = info.id
			this.title = info.title
			this.status = info.status
			this.power = info.power
			this.show = JSON.parse(info.data).show
			this.contentType = info.type
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
