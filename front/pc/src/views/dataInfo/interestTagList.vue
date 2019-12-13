<template>
	<div>
		<el-row class="title-box">
			用户兴趣标签

		</el-row>
		<el-row class="content-box">
			<el-col :span="6">
				<el-input placeholder="搜索用户名或者标签" v-model="keyword" @input="changeSearch">
					<el-button slot="append" icon="el-icon-search" @click="search">搜索</el-button>
				</el-input>
			</el-col>
		</el-row>

		<el-row class="table-box">
			<p>查询条数：{{num}}</p>
			<el-table :data="tableData" border style="width: 100%">
				<el-table-column prop="userId" label="用户id">
				</el-table-column>
				<el-table-column label="头像">
					<template scope="scope">
						<img :src="scope.row.head" width="40" height="40" class="head_pic" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="用户名" width="200">
				</el-table-column>
				<el-table-column prop="tags" label="兴趣标签">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button @click="infoBtn(scope.row)" type="text" size="small">查看</el-button>
					</template>
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
		<el-dialog title="详情" :visible.sync="dialogVisible" width="30%">
			<el-table :data="tableData2" border style="width: 100%">
				<el-table-column prop="keyword" label="兴趣标签">
				</el-table-column>
				<el-table-column prop="pageView" label="浏览数" width="200">
				</el-table-column>
			</el-table>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="dialogVisible = false">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		name: "interestTagList",
		data() {
			return {
				num: '',
				dialogVisible: false,
				keyword: '',
				tableData: [],
				tableData2: [],
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
			changeSearch() {
				if (this.keyword == '') {
					let cnt = {
						count: this.count,
						offset: (this.page - 1) * this.count
					}
					this.getContents(cnt)
					this.num = ''
				}
			},
			search() {
				this.page = 1
				let cnt = {
					name: this.keyword,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				if (this.keyword == '') {
					return
				}
				this.$api.searchAllInterestTag(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableData = this.$util.tryParseJson(res.data.c)
						this.num = this.tableData[this.tableData.length - 1]
						this.tableData.splice(this.tableData.length - 1, 1)
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
			getContents(cnt) {
				this.$api.getAllInterestTag(cnt, (res) => {
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
				this.dialogVisible = true
				let cnt = {
					userId: info.userId,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				this.$api.getInterestTags(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableData2 = this.$util.tryParseJson(res.data.c)
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
		},
		mounted() {
			let cnt = {
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
