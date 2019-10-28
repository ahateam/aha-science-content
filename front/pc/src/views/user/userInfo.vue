<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="用户基本信息" name="first">
				<div style="margin: 5px;">
					<p><img :src="this.head" width="40" height="40" class="head_pic" /></p>
					<p>用户id：{{this.id}}</p>
					<p>用户昵称：{{this.name}}</p>
					<p>用户密码：{{this.pwd}}</p>
					<p>学校/单位：{{this.company}}</p>
					<p>手机号：{{this.phone}}</p>
					<p>权限：{{this.authority==1?'普通':this.authority==2?'内部人员':'管理员'}}</p>
					<p>状态：{{this.status==0?'正常':'评论封禁'}}</p>
					<p>更改时间：{{this.updateTime}}</p>
					<p>注册时间：{{this.createTime}}</p>
					<p>其他</p>
					<el-button type="primary" @click="isShowl=true" style="margin: 0 auto;display: block;padding: 15px 50px">设置权限
					</el-button>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="发布内容" name="second">无数据</el-tab-pane>
			<el-tab-pane label="发布评论" name="third">无数据</el-tab-pane> -->
		</el-tabs>

		<el-row v-if="isShowl">
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="设置权限:">
						<el-select v-model="auth" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in authList" :key="item.id" :label="item.name" :value="item.id">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
				<el-button type="primary" @click="upAuth" style="margin: 0 auto;display: block;padding: 15px 50px">提交
				</el-button>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	export default {
		name: "userInfo",
		data() {
			return {
				auth:'',
				contentId: '',
				activeName: 'first',
				info: '',
				id: '',
				head: '',
				name: '',
				pwd: '',
				company: '',
				phone: '',
				authority: '',
				status: '',
				updateTime: '',
				createTime: '',
				isShowl: false,
				authList:this.$constData.authList
			}
		},
		methods: {
			timeFliter(val) {
				let timer = new Date(val)
				let dataTime = timer.toLocaleDateString() + ' ' + timer.toLocaleTimeString('chinese', {
					hour12: false
				})
				return dataTime
			},
			handleClick(tab, event) {

			},
			upAuth(){
				let cnt = {
					moduleId:this.$constData.module,
					id:this.id,
					authority:this.auth,
				}
				this.$api.updateUserInfo(cnt, (res => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.$message({
							message: '设置成功',
							type: 'success'
						});
						this.$router.push('/contentList')
					} else {
						this.$message({
							message: res.data.c,
							type: 'warning'
						});
						this.imgList = []
					}
				}))
			}
		},
		mounted() {
			let info = this.$route.params.info;
			this.id = info.id
			this.head = info.head
			this.name = info.name
			this.pwd = info.pwd
			this.company = info.company
			this.phone = info.phone
			this.authority = info.authority
			this.status = info.status
			this.updateTime = this.timeFliter(info.updateTime)
			this.createTime = this.timeFliter(info.createTime)
		}
	}
</script>

<style scoped>
	p {
		font-size: 18px;
	}
</style>
