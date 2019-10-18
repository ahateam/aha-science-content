<template>
	<div>
		<el-row class="title-box">
			标签管理
		</el-row>
		<el-row class="content-box">
			<el-row>
				<el-col :span="8">
					<el-form label-width="80px">
						<el-form-item label="标签状态">
							<el-select v-model="searchData.status" placeholder="请选择状态">
								<el-option v-for="(item,index) in tagstatus" :key="index" :label="item.name" :value="item.value"></el-option>
							</el-select>
						</el-form-item>
					</el-form>
				</el-col>
				<el-button type="primary" @click="searchBtn">查询</el-button>
				<el-button type="primary" @click="getContentsBtn">默认列表</el-button>
			</el-row>
			<el-popover placement="right" width="400" trigger="click" v-model="showTagGroup">
				<el-col style="margin-bottom: 20px;">
					<el-select v-model="tagGroup" placeholder="请选择标签分组">
						<el-option v-for="item in options" :key="item.groupName" :label="item.groupName" :value="item.groupName">
						</el-option>
					</el-select>
				</el-col>
				<el-col :span="20">
					<el-input placeholder="请输入标签名称" v-model="tagTitle" style="display: inline-block;width: 400px"></el-input>
					<el-button style="margin-top: 10px;" @click="subBtn">提交</el-button>
				</el-col>
				<el-button slot="reference">新建标签</el-button>
			</el-popover>
		</el-row>
		<el-row class="table-box">
			<el-table :data="tableData" border style="width: 100%">
				<el-table-column prop="GroupName" label="分组名称" width="400">
				</el-table-column>
				<el-table-column prop="name" label="标签名称" width="400">
				</el-table-column>
				<el-table-column prop="status" label="状态" :formatter="tagStatusFliter">
				</el-table-column>
				<el-table-column label="操作" width="100">
					<template slot-scope="scope">
						<el-button @click="closeBtn(scope.row)" type="text" size="small" v-if="scope.row.status==1">禁用</el-button>
						<el-button @click="closeBtn(scope.row)" type="text" size="small" v-if="scope.row.status==0">启用</el-button>
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
		name: "homeTag",
		data() {
			return {
				options:'',
				tagGroup:'',
				showTagGroup: false,
				tagTitle: '',
				tableData: [],
				count: 10,
				page: 1,
				pageOver: true,
				searchData: {
					type: '',
					status: '',
					power: '',
					tags: '',

				},
				tagstatus: this.$constData.tagstatus,
			}
		},
		methods: {
			/** 过滤器*/
			tagStatusFliter(row, col, val) {
				let tagstatus = this.tagstatus
				for (let i = 0; i < tagstatus.length; i++) {
					if (tagstatus[i].value == val) {
						return tagstatus[i].name
					}
				}
			},
			/*获取首页标签列表*/
			getContents(cnt) {
				this.$api.getContentTag(cnt, (res) => {
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
					moduleId: this.$constData.module,
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
					group: '首页',
					count: this.count,
					status: this.searchData.status,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
			},
			/* 禁用标签*/
			closeBtn(info) {
				this.$confirm('是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					let cnt = {
						moduleId: this.$constData.module,
						group: '首页',
						name: info.name,
						status: 0,
					}
					if (info.status == 1) {
						cnt.status = 0
					} else if (info.status == 0) {
						cnt.status = 1
					}
					this.$api.editteContentTag(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							this.$message({
								type: 'success',
								message: '成功!'
							});
						} else {
							this.$message({
								type: 'error',
								message: '操作失败!'
							});
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消'
					});
				});
			},
			/* 添加标签*/
			subBtn() {
				this.showTagGroup = false
				if(this.tagGroup == ''){
					this.$message({
						type: 'error',
						message: '请选择一个标签分组'
					});
					return;
				}else if(this.tagTitle == ''){
					this.$message({
						type: 'error',
						message: '请填写标签名称'
					});
					return;
				}
				let cnt = {
					moduleId: this.$constData.module, 
					group: this.tagGroup, //  分组
					name: this.tagTitle,
				}
				this.$api.createContentTag(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.$message({
							type: 'success',
							message: '成功!'
						});
					} else {
						this.$message({
							type: 'error',
							message: '操作失败!'
						});
					}
				})
				this.$router.push('/contentList')
			},
			//获取默认列表
			getContentsBtn() {
				this.searchData.type = ''
				this.searchData.status = ''
				this.searchData.power = ''
				this.searchData.tags = ''
				this.page = 1
				let cnt = {
					moduleId: this.$constData.module,
					group: '首页',
					count: this.count,
					offset: (this.page - 1) * this.count,
				}
				this.getContents(cnt)
			},
			getTagGroup(){
				let cnt = {
					moduleId: this.$constData.module,
					count:200,
					offset: 0,
				};
				this.$api.getContentTagGroup(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.options = this.$util.tryParseJson(res.data.c)
					}
				})
			}
		},
		mounted() {
			//获取标签列表
			let cnt = {
				moduleId: this.$constData.module,
				count: this.count,
				offset: (this.page - 1) * this.count,
			}
			this.getContents(cnt)
			this.getTagGroup()
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
