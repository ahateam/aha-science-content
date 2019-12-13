<template>
	<div>
		<el-row class="title-box">
			广告管理
		</el-row>
		<el-row class="content-box">
			<el-col :span="12">
				<span style="font-size: 16px;">广告类别：</span>
				<el-button :type="check == -1?'primary':'text'" size="mini" round @click="getDefault(0)">全部</el-button>
				<el-button :type="check == index?'primary':'text'" size="mini" round @click="getAdvertsByType(item.id,index)" v-for="(item,index) in advertTypeList"
				 :key="item.id">{{item.name}}</el-button>
			</el-col>
			<el-col :span="12">
				<span style="font-size: 16px;">广告状态：</span>
				
				<el-button :type="checkStatus == index?'primary':'text'" size="mini" round @click="getAdvertsByStatus(item.value,index)" v-for="(item,index) in generalStatus"
				 :key="item.id">{{item.name}}</el-button>
			</el-col>
		</el-row>
		<el-row class="table-box">
			<el-table :data="tableData" border style="width: 100%">
				<el-table-column label="展示图">
					<template scope="scope">
						<img :src="scope.row.imgSrc" width="40" height="40" class="head_pic" />
					</template>
				</el-table-column>
				<el-table-column prop="linkSrc" label="链接" width="200">
				</el-table-column>
				<el-table-column prop="remark" label="备注">
				</el-table-column>
				<el-table-column prop="type" label="类别" :formatter="advertTypeListFliter">
				</el-table-column>
				<el-table-column prop="sortSize" label="排序大小">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button @click="updateStatusBtn(scope.row,'0')" type="text" size="small" v-if="scope.row.status == '1'">启用</el-button>
						<el-button @click="updateStatusBtn(scope.row,'1')" type="text" size="small" v-if="scope.row.status == '0'">禁用</el-button>
						<el-button @click="updateBtn(scope.row)" type="text" size="small">设置排序</el-button>
						<el-button @click="delBtn(scope.row)" type="text" size="small" style="color: red;">删除</el-button>
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
		name: "advertList",
		data() {
			return {
				type:'',
				status:'',
				checkStatus:0,
				check: -1,
				tableData: [],
				count: 10,
				page: 1,
				pageOver: true,
				advertTypeList: this.$constData.advertTypeList,
				generalStatus:this.$constData.generalStatus,
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
			advertTypeListFliter(row, col, val) {
				let typeList = this.advertTypeList
				for (let i = 0; i < typeList.length; i++) {
					if (typeList[i].id == val) {
						return typeList[i].name
					}
				}
			},
			/*获取广告列表*/
			getContents(cnt) {
				this.$api.getAdverts(cnt, (res) => {
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
			delBtn(info) {
				let msg = ''

				this.$confirm('此操作将永久删除此数据, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					let cnt = {
						moduleId: this.$constData.module,
						id: info.id,
					};
					this.$api.delAdvert(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							this.$message({
								type: 'success',
								message: '操作成功!'
							});
							let cnt = {
								moduleId: this.$constData.module,
								authority: 3,
								count: this.count,
								offset: (this.page - 1) * this.count
							}
							this.getContents(cnt)
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
			updateBtn(info) {
				this.$prompt('请输入排序大小,数值越大越靠前', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
				}).then(({
					value
				}) => {
					let cnt = {
						moduleId: this.$constData.module,
						id: info.id,
						sortSize: value,
					}
					this.$api.updateAdvert(cnt, (res) => {
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
						let cnt = {
							moduleId: this.$constData.module,
							authority: 3,
							count: this.count,
							offset: (this.page - 1) * this.count
						}
						this.getContents(cnt)
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '取消输入'
					});
				});

			},
			updateStatusBtn(info, val) {
				let cnt = {
					moduleId: this.$constData.module,
					id: info.id,
					status: val,
				}
				this.$api.updateAdvert(cnt, (res) => {
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
					let cnt = {
						moduleId: this.$constData.module,
						authority: 3,
						count: this.count,
						offset: (this.page - 1) * this.count
					}
					this.getContents(cnt)
				})
			},
			getDefault(e){
				if(e == 0){
					this.check = -1
					this.type = ''
				}else{
					this.checkStatus = -1
					this.status = ''
				}
				let cnt = {
					moduleId: this.$constData.module,
					count: this.count,
					offset: (this.page - 1) * this.count,
				}
				if(this.status != ''){
					cnt.status = this.status
				}
				if(this.type != ''){
					cnt.type = this.type
				}
				this.getContents(cnt)
			},
			getAdvertsByType(info, index) {
				this.check = index
				this.type = info
				let cnt = {
					moduleId: this.$constData.module,
					count: this.count,
					offset: (this.page - 1) * this.count,
				}
				if(this.status != ''){
					cnt.status = this.status
				}
				if(this.type != ''){
					cnt.type = this.type
				}
				this.getContents(cnt)
			},
			getAdvertsByStatus(info,index) {
				this.checkStatus = index
				this.status = index
				let cnt = {
					moduleId: this.$constData.module,
					count: this.count,
					offset: (this.page - 1) * this.count,
				}
				if(this.status != ''){
					cnt.status = this.status
				}
				if(this.type != ''){
					cnt.type = this.type
				}
				this.getContents(cnt)
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
