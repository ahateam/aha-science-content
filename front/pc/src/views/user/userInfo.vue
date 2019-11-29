<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="用户基本信息" name="first">
				<div style="margin: 5px;">
					<p><img :src="this.head" width="40" height="40" class="head_pic" /></p>
					<p>用户id：{{this.id}}</p>
					<p>用户昵称：{{this.name}}</p>
					<p>用户密码： <el-button type="danger" icon="el-icon-question" v-if="!showPwd" circle @click="showPwdFun"></el-button>
						<span v-if="showPwd">{{this.pwd}}</span>
						<el-button type="danger" plain @click="dialogVisible = true">重置密码</el-button>
					</p>
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
		<el-dialog title="提示" :visible.sync="dialogVisible" width="30%">
			<p>请输入密码：</p>
			<el-input v-model="repwd1" placeholder="请输入密码"></el-input>
			<p>请再一次输入密码：</p>
			<el-input v-model="repwd2" placeholder="请输入密码"></el-input>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="resPwd()">确 定</el-button>
			</span>
		</el-dialog>
	</div>

</template>

<script>
	export default {
		name: "userInfo",
		data() {
			return {
				showPwd: false,
				repwd1: '',
				repwd2: '',
				dialogVisible: false,
				auth: '',
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
				authList: this.$constData.authList
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
			handleClose() {

			},
			upAuth() {
				let cnt = {
					moduleId: this.$constData.module,
					adminId: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
					authority: this.auth,
					userId: this.id,
				}
				this.$api.setUserauthority(cnt, (res => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.$message({
							message: '设置成功',
							type: 'success'
						});
						this.$router.push('/userInfo')
						this.authority = this.auth;
					} else {
						this.$message({
							message: res.data.rm,
							type: 'warning'
						});
						this.imgList = []
					}
				}))
			},
			showPwdFun() {
				let cnt = {
					moduleId: this.$constData.module,
					id: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
				}
				this.$api.getUser(cnt, (res => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						if (JSON.parse(res.data.c).authority == 3 || JSON.parse(res.data.c).authority == 4) {
							this.showPwd = true
						} else {
							this.$message({
								message: '你不是超级管理员，没有权限查看密码',
								type: 'warning'
							});
							return
						}
					} else {
						this.$message({
							message: res.data.rc,
							type: 'warning'
						});
						this.imgList = []
					}
				}))
			},
			resPwd(id) {
				if (this.repwd1 != this.repwd2) {
					this.$message({
						message: "密码不一致",
						type: 'warning'
					});
					return
				}
				this.dialogVisible = false
				let cnt = {
					moduleId: this.$constData.module,
					adminId: this.$util.tryParseJson(localStorage.getItem('loginUser')).id,
					pwd: this.repwd2,
					userId: this.id,
				}
				this.$api.resUserPwd(cnt, (res => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.$message({
							message: '设置成功',
							type: 'success'
						});
						this.$router.push('/userInfo')
						this.pwd = this.repwd1;
					} else {
						this.$message({
							message: res.data.rm,
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
