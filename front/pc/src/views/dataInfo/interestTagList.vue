<template>
	<div>
		<el-row class="title-box">
			用户兴趣标签
		</el-row>
		<el-row class="content-box">
		</el-row>
		<el-row class="table-box">
			<el-table :data="tableData" border style="width: 100%">
				<el-table-column label="头像">
					<template scope="scope">
						<img :src="scope.row.user.head" width="40" height="40" class="head_pic" />
					</template>
				</el-table-column>
				<el-table-column prop="user.name" label="用户名" width="200">
				</el-table-column>
				<el-table-column prop="tagsArray" label="兴趣标签">
				</el-table-column>
			</el-table>
		</el-row>
		<el-row style="height: 80px;">
			<el-col :span="24">
				当前页数：{{page}}
				<el-button type="primary" size="small" :disabled="page==1" @click="changePage(page-1)">上一页</el-button>
				<el-button type="primary" size="small" :disabled="pageOver" @click="changePage(page+1)">下一页</el-button>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	export default {
		name: "interestTagList",
		data() {
			return {
				tableData: [],
				count: 10,
				page: 1,
				pageOver: true,
			}
		},
		methods: {
			timeFliter(row, col, val) {
				let timer = new Date(val)
				let dataTime = timer.toLocaleDateString() + ' ' + timer.toLocaleTimeString('chinese', {
					hour12: false
				})
				return dataTime
			},
			/*获取评论列表*/
			getContents(cnt) {
				this.$api.getInterestTags(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableData = this.$util.tryParseJson(res.data.c)
					} else {
						this.tableData = []
					}
					if (this.tableData.length < this.count) {
						this.pageOver = true
					} else {
						this.pageOver = false
					}
				})
			},
			/* 分页*/
			changePage(page) {
				this.page = page
				//获取内容列表
				let cnt = {
					module: this.$constData.module,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
			},
			//查看 详情
			infoBtn(info) {
				this.$router.push({
					path: '/userInfo',
					name: 'userInfo',
					params: {
						info: info
					}
				})
			},
		},
		mounted() {
			let cnt = {
				moduleId: this.$constData.module,
				count: this.count,
				offset: (this.page - 1) * this.count
			}
			this.getContents(cnt)
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
