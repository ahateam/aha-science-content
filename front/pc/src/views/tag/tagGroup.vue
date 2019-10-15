<template>
	<div>
		<el-row class="title-box">
			创建标签分组
		</el-row>
		<el-row style="padding: 5px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 组名称：</span>
				<el-input placeholder="请输入分组名称" v-model="tagGroupName" clearable></el-input>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 备注：</span>
				<el-input placeholder="请输入备注" v-model="remark" clearable></el-input>
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
				tagGroupName: '',
				remark: '',
			}
		},
		methods: {
			subBtn() {
				if(this.tagGroupName == ''){
					this.$message({
						message: '请输入分组名称',
						type: 'error'
					});
					return
				}
				let cnt = {
						moduleId: this.$constData.module,
						group: this.tagGroupName,
						remark: this.remark,
					};
				this.$api.createContentTagGroup(cnt, (res => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.$message({
							message: '添加成功',
							type: 'success'
						});
						this.$router.push('/homeTag')
					} else {
						this.$message({
							message: res.data.c,
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
