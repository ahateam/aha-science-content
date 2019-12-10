<template>
	<div>
		<el-row class="title-box">
			报名信息
		</el-row>
		<el-row class="content-box" style="font-size: 15px;">
			其中报名总人数:
			<span style="font-size: 18px;">{{num}}</span>
			，填写的报名总人数:
			<span style="font-size: 18px;">{{userNum}}</span>
		</el-row>
		<el-row>
			<el-col :span="8">
				<el-input placeholder="搜索用户名" v-model="keyword" @input="changeSearch">
					<el-button slot="append" icon="el-icon-search" @click="search">搜索</el-button>
				</el-input>
			</el-col>
		</el-row>
		<el-row class="table-box">
			<el-table :data="tableData" border style="width: 100%">
				<el-table-column prop="contentName" label="活动标题" width="200">
				</el-table-column>
				<el-table-column prop="name" label="报名者姓名" width="200">
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
				</el-table-column>
				<el-table-column prop="number" label="报名者填写的报名人数">
				</el-table-column>
				<el-table-column prop="createTime" label="报名日期" :formatter="timeFliter">
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
		name: "enrollList",
		data() {
			return {
				keyword:'',
				info:'',
				num: '',
				userNum: '',
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
			getContents(cnt) {
				this.$api.getEnrolls(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let info = this.$util.tryParseJson(res.data.c)
						this.tableData = info[0]
						this.num = info[1].num
						this.userNum = info[2].userNum
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
			search() {
				this.page = 1
				let cnt = {
					moduleId: this.$constData.module,
					contenId: this.info.id,
					name:this.keyword,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				if (this.keyword == '') {
					return
				}
				cnt.keyword = this.keyword
				this.$api.searchEnrollsUserName(cnt, (res) => {
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
			changeSearch() {
				if (this.keyword == '') {
					let cnt = {
						moduleId: this.$constData.module,
						contenId: this.info.id,
						count: this.count,
						offset: (this.page - 1) * this.count
					}
					this.getContents(cnt)
				}
			},
			/* 分页*/
			changePage(page) {
				this.page = page
				//获取内容列表
				let cnt = {
					moduleId: this.$constData.module,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
			},
		},
		mounted() {
			this.info = this.$route.params.info
			let cnt = {
				moduleId: this.$constData.module,
				contenId: this.info.id,
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
