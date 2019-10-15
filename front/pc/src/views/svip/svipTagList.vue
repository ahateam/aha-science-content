<template>
	<div>
		<el-row class="title-box">
			信息信
		</el-row>
		<el-row class="content-box">
			<el-row>
				<el-col :span="8">
					<el-form label-width="80px">
						<el-form-item label="状态">
							<el-select v-model="searchData.status" placeholder="请选择状态">
								<el-option v-for="(item,index) in generalStatus" :key="index" :label="item.name" :value="item.value"></el-option>
							</el-select>
						</el-form-item>
					</el-form>
				</el-col>
				<el-col :span="8">
					<el-form label-width="80px">
						<el-form-item label="所属专栏">
							<el-select v-model="searchData.upChannelId" placeholder="请选择" style="margin-right: 10px;">
								<el-option v-for="item in channelList" :key="item.id" :label="item.title" :value="item.id">
								</el-option>
							</el-select>
						</el-form-item>
					</el-form>
				</el-col>
			</el-row>
			<el-row>
				<el-button type="primary" @click="searchBtn">查询</el-button>
				<el-button type="primary" @click="getContentsBtn">默认列表</el-button>
			</el-row>
		</el-row>
		<el-row class="table-box">
			<el-table :data="tableData" border style="width: 100%">
				<el-table-column prop="name" label="名称" width="400">
				</el-table-column>
				<el-table-column prop="status" label="状态" :formatter="statusFliter">
				</el-table-column>
				<el-table-column prop="price" label="价格">
				</el-table-column>
				<el-table-column label="操作" width="100">
					<template slot-scope="scope">
						<el-button @click="updateChannelContentTag(scope.row)" type="text" size="small">编辑</el-button>
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
	</div>
</template>

<script>
	export default {
		name: "contentList",
		data() {
			return {
				dialogTableVisible: false,
				dialogFormVisible: false,
				name: '',
				price: '',
				upChannelId: '',
				channelList: '',
				tableData: [],
				svipTagList: [],
				count: 10,
				page: 1,
				pageOver: true,
				searchData: {
					type: '',
					status: '',
					power: '',
					tags: '',
				},
				typeList: this.$constData.typeList,
				statusList: this.$constData.statusList,
				powerList: this.$constData.powerList,
				generalStatus: this.$constData.generalStatus,
			}
		},
		methods: {
			/** 过滤器*/
			statusFliter(row, col, val) {
				let statusList = this.statusList
				for (let i = 0; i < statusList.length; i++) {
					if (statusList[i].value == val) {
						return statusList[i].name
					}
				}
			},
			/*获取内容列表*/
			getContents(cnt) {
				this.$api.getChannelContentTag(cnt, (res) => {
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
			/* 查询数据*/
			searchBtn() {
				this.page = 1
				let cnt = {
					moduleId: this.$constData.module,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				if (this.searchData.status) {
					cnt.status = this.searchData.status
				}
				if (this.searchData.upChannelId) {
					cnt.channelId = this.searchData.upChannelId
				}
				this.getContents(cnt)
			},
			//获取默认列表
			getContentsBtn() {
				this.searchData.type = ''
				this.searchData.status = ''
				this.searchData.power = ''
				this.searchData.tags = ''
				this.page = 1
				let cnt = {
					module: this.$constData.module,
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
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
			updateChannelContentTag(info) {
				this.$router.push({
					path: '/editSvipTagList',
					name: 'editSvipTagList',
					params: {
						info: info
					}
				})
			}
		},
		mounted() {
			this.upChannelId = '401852218607524'
			//获取内容列表
			let cnt = {
				moduleId: this.$constData.module,
				channelId: this.upChannelId,
				count: this.count,
				offset: (this.page - 1) * this.count
			}
			this.getContents(cnt)
			this.getChannels()
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
