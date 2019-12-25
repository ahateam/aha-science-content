<template>
	<div>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 用户名：</span>
				<el-input placeholder="请输入" v-model="title" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 手机号：</span>
				<el-input placeholder="请输入" v-model="phone" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 密码：</span>
				<el-input placeholder="请输入" v-model="pwd" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<el-row style="margin-top: 20px;padding-bottom: 10px">
			<el-col :span="4" style="min-height: 20px"></el-col>
			<el-button type="primary" @click="addBtn" style=";padding: 15px 50px">提交
			</el-button>
		</el-row>

	</div>
</template>

<script>
	export default {
		name: "addUser",
		data() {
			return {
				au:'',
				title: '',
				phone: '',
				pwd: '',
			}
		},
		methods: {
			a(){
				let url = client.signatureUrl("zskpDownload/zskp.apk");
				console.log(url)
			},
			addBtn() {
				let that = this
				let cnt = {
					moduleId: this.$constData.module,
					name: this.title,
					phone: this.phone,
					pwd: this.pwd,
					au:this.au,
				}
				console.log(cnt)
				that.$api.createUser(cnt, (res => {
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '创建成功',
							type: 'success'
						});
						that.$router.push('/contentList')
					} else {
						this.$message({
							message: res.data.rm,
							type: 'warning'
						});
						that.imgList = []
					}
				}))
			}
		},
		mounted(){
			this.au = this.$route.params.info;
		}

	}
</script>

<style>
</style>
