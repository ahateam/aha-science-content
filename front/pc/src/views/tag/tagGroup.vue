<template>
	<div>
		<el-row class="title-box">
			创建关键词
		</el-row>
		<el-row style="padding: 5px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 关键词：</span>
				<el-input placeholder="请输入名称" v-model="keyword" clearable></el-input>
			</el-col>
		</el-row>
		<el-row style="margin-top: 20px">
			<el-button type="primary" @click="subBtn" style="margin: 0 auto;display: block;padding: 15px 50px">提交
			</el-button>
		</el-row>
	</div>
</template>

<script>
	export default {
		name: "tagGroup",
		data() {
			return {
				keyword: '',
			}
		},
		
		methods: {
			subBtn() {
				if(this.keyword == ''){
					this.$message({
						message: '请输入名称',
						type: 'error'
					});
					return
				}
				let cnt = {
						userId:JSON.parse(localStorage.getItem('loginUser')).id,
						keyword:this.keyword
					};
				this.$api.createKeyword(cnt, (res => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.$message({
							message: '添加成功',
							type: 'success'
						});
						this.$router.push('/homeTag')
					} else {
						this.$message({
							message: res.data.rm,
							type: 'warning'
						});
						this.imgList = []
					}
				}))
			}
		}
	}
</script>

<style scoped lang="scss">
	.title-box {
		font-size: 16px;
		color: #666;
		line-height: 50px;
		padding-left: 15px;
	}

	.content-box {
		margin-top: 20px;
		padding: 20px;
	}

	.table-box {
		padding: 20px;

	}
</style>
