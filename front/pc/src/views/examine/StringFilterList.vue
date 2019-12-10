<template>
	<div>
		<el-row class="title-box">
			敏感词库管理
		</el-row>
		<el-row class="content-box">
			<el-col :span="12">
				<span style="font-size: 18px;color: #0086B3;">直接过滤*敏感词：</span>
				<el-input placeholder="请输入要搜索的敏感词" v-model="searchSensitive" style="width: 90%;margin: 8px" @input= "getDefault(0)">
					<el-button plain style="width:80px;" slot="append" icon="el-icon-search" @click= "search"></el-button>
				</el-input>
				<div style="width:90%; height:500px; overflow:auto; border:1px solid #000000;margin-bottom: 15px;">
					<el-checkbox-group v-model="checkList">
						<el-checkbox :label="item.wordId" v-for="(item ,index) in tableData" :key="index">{{item.badword}}</el-checkbox>
					</el-checkbox-group>
				</div>
				<el-button type="primary" plain icon="el-icon-arrow-left" :disabled="page==1" @click="load(0)">上一页</el-button>
				<el-button type="primary" plain @click="load(1)" :disabled="pageOver">下一页<i class="el-icon-arrow-right el-icon--right"></i></el-button>
				<div style="margin-top: 15px;">
					<el-button type="primary" plain @click="dialogVisible = true">添加敏感词</el-button>
					<el-button type="danger" plain @click="delSensitive">选中删除</el-button>
					<el-button plain @click="moveRight">选中移动到右边<i class="el-icon-arrow-right el-icon--right"></i></el-button>
				</div>
			</el-col>
			<!-- ====================================== -->
			<el-col :span="12">
				<span style="font-size: 18px;color: #0086B3;">争议敏感词：</span>
				<el-input placeholder="请输入要搜索的敏感词" v-model="searchSensitiveByExamine" style="width: 90%;margin: 8px" @input= "getDefault(1)">
					<el-button plain style="width:80px;" slot="append" icon="el-icon-search" @click= "searchByExamine"></el-button>
				</el-input>
				<div style="width:90%; height:500px; overflow:auto; border:1px solid #000000;margin-bottom: 15px;">
					<el-checkbox-group v-model="checkListByExamine">
						<el-checkbox :label="item.wordId" v-for="(item ,index) in tableDataByExamine" :key="index">{{item.badword}}</el-checkbox>
					</el-checkbox-group>
				</div>
				<el-button type="primary" plain icon="el-icon-arrow-left" :disabled="pageByExamine==1" @click="loadByexamine(0)">上一页</el-button>
				<el-button type="primary" plain @click="loadByexamine(1)" :disabled="pageOverByExmine">下一页<i class="el-icon-arrow-right el-icon--right"></i></el-button>
				<div style="margin-top: 15px;">
					<el-button plain icon="el-icon-arrow-left" @click="moveLeft">选中移动到左边</el-button>
					<el-button type="danger" plain @click="delSensitiveByExamine">选中删除</el-button>
					<el-button type="primary" plain plain @click="dialogVisibleByExamine = true">添加敏感词</el-button>
				</div>
			</el-col>
		</el-row>
		<el-dialog title="新建敏感词-过滤*" :visible.sync="dialogVisible" width="30%">
			<el-input v-model="input" placeholder="请输入内容"></el-input>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="add(0)">确 定</el-button>
			</span>
		</el-dialog>

		<el-dialog title="新建敏感词-审核" :visible.sync="dialogVisibleByExamine" width="30%">
			<el-input v-model="input" placeholder="请输入内容"></el-input>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisibleByExamine = false">取 消</el-button>
				<el-button type="primary" @click="add(1)">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		name: "StringFilterList",
		data() {
			return {
				searchSensitiveByExamine: '',
				searchSensitive: '',
				pageOver: true,
				pageOverByExmine: true,
				checkList: [],
				checkListByExamine: [],
				input: '',
				inputByExamine: '',
				dialogVisible: false,
				dialogVisibleByExamine: false,
				tableData: [],
				tableDataByExamine: [],
				count: 200,
				countByExamine: 200,
				page: 1,
				pageByExamine: 1,
			}
		},
		methods: {
			getDefault(e){
				if(this.searchSensitiveByExamine == ''){
					let cnt = {
						type: 1,
						count: this.countByExamine,
						offset: (this.pageByExamine - 1) * this.countByExamine
					}
					this.getSensitiveWordByexamine(cnt)
				}
				if(this.searchSensitive == ''){
					let cnt = {
						type: '0',
						count: this.count,
						offset: (this.page - 1) * this.count
					}
					this.getContents(cnt)
				}
			},
			searchByExamine(){
				if(this.searchSensitiveByExamine == ''){
					return
				}
				let cnt = {
					type:1,
					badword:this.searchSensitiveByExamine
				}
				this.$api.searchSensitiveWord(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableDataByExamine = this.$util.tryParseJson(res.data.c)
					}
					if (this.tableDataByExamine.length < this.count) {
						this.pageOverByExmine = true
					} else {
						this.pageOverByExmine = false
					}
				})
			},
			search(){
				if(this.searchSensitive == ''){
					return
				}
				let cnt = {
					type:'0',
					badword:this.searchSensitive
				}
				this.$api.searchSensitiveWord(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableData = this.$util.tryParseJson(res.data.c)
					}
					if (this.tableData.length < this.count) {
						this.pageOver = true
					} else {
						this.pageOver = false
					}
				})
			},
			load(e) {
				if (e) {
					this.page += 1
				} else {
					this.page -= 1
				}
				let cnt = {
					type: '0',
					count: this.count,
					offset: (this.page - 1) * this.count
				}
				this.getContents(cnt)
			},
			loadByexamine(e) {
				if (e) {
					this.pageByExamine += 1
				} else {
					this.pageByExamine -= 1
				}
				let cnt = {
					type: 1,
					count: this.countByExamine,
					offset: (this.pageByExamine - 1) * this.countByExamine
				}
				this.getSensitiveWordByexamine(cnt)
			},
			getContents(cnt) {
				this.$api.getFilterText(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableData = this.$util.tryParseJson(res.data.c)
					}
					if (this.tableData.length < this.count) {
						this.pageOver = true
					} else {
						this.pageOver = false
					}
				})
			},
			getSensitiveWordByexamine(cnt) {
				this.$api.getFilterText(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableDataByExamine = this.$util.tryParseJson(res.data.c)
					}
					if (this.tableDataByExamine.length < this.count) {
						this.pageOverByExmine = true
					} else {
						this.pageOverByExmine = false
					}
				})
			},
			add(e) {
				let cnt = {
					type: e,
					badword: this.input
				}
				this.$api.createSensitiveWord(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableData = this.$util.tryParseJson(res.data.c)
						this.$message({
							type: 'success',
							message: '操作成功!'
						});
						this.input = ''
						this.inputByExamine = ''
						let cnt_a = {
							type: '0',
							count: this.count,
							offset: (this.page - 1) * this.count
						}
						let cnt_b = {
							type: 1,
							count: this.countByExamine,
							offset: (this.pageByExamine - 1) * this.countByExamine
						}
						this.getContents(cnt_a)
						this.getSensitiveWordByexamine(cnt_b)
					}
					this.getContents(1)
				})
			},
			delSensitive() {
				if (this.checkList == '') {
					this.$message({
						type: 'warning',
						message: '请至少选择一个敏感词!'
					});
					return
				}
				this.$confirm('此操作将永久删除, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					let cnt = {
						ids: this.checkList
					}
					this.$api.delSensitiveWord(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							this.$message({
								type: 'success',
								message: '删除成功!'
							});
							let cnt_a = {
								type: '0',
								count: this.count,
								offset: (this.page - 1) * this.count
							}
							let cnt_b = {
								type: 1,
								count: this.countByExamine,
								offset: (this.pageByExamine - 1) * this.countByExamine
							}
							this.getContents(cnt_a)
							this.getSensitiveWordByexamine(cnt_b)
						} else {
							this.$message({
								type: 'error',
								message: '删除失败!'
							});
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});

			},
			delSensitiveByExamine() {
				if (this.checkListByExamine == '') {
					this.$message({
						type: 'warning',
						message: '请至少选择一个敏感词!'
					});
					return
				}
				this.$confirm('此操作将永久删除, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					let cnt = {
						ids: this.checkListByExamine
					}
					this.$api.delSensitiveWord(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							this.$message({
								type: 'success',
								message: '删除成功!'
							});
							let cnt_a = {
								type: '0',
								count: this.count,
								offset: (this.page - 1) * this.count
							}
							let cnt_b = {
								type: 1,
								count: this.countByExamine,
								offset: (this.pageByExamine - 1) * this.countByExamine
							}
							this.getContents(cnt_a)
							this.getSensitiveWordByexamine(cnt_b)
						} else {
							this.$message({
								type: 'error',
								message: '删除失败!'
							});
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			moveLeft() {
				if (this.checkListByExamine == '') {
					this.$message({
						type: 'warning',
						message: '请至少选择一个敏感词!'
					});
					return
				}
				this.$confirm('是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					let cnt = {
						ids: this.checkListByExamine,
						type: 0
					}
					this.$api.updateSensitiveWord(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							this.$message({
								type: 'success',
								message: '操作成功!'
							});
							let cnt_a = {
								type: '0',
								count: this.count,
								offset: (this.page - 1) * this.count
							}
							let cnt_b = {
								type: 1,
								count: this.countByExamine,
								offset: (this.pageByExamine - 1) * this.countByExamine
							}
							this.getContents(cnt_a)
							this.getSensitiveWordByexamine(cnt_b)
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
			moveRight() {
				if (this.checkList == '') {
					this.$message({
						type: 'warning',
						message: '请至少选择一个敏感词!'
					});
					return
				}
				this.$confirm('是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					let cnt = {
						ids: this.checkList,
						type: 1
					}
					this.$api.updateSensitiveWord(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							this.$message({
								type: 'success',
								message: '操作成功!'
							});
							let cnt_a = {
								type: '0',
								count: this.count,
								offset: (this.page - 1) * this.count
							}
							let cnt_b = {
								type: 1,
								count: this.countByExamine,
								offset: (this.pageByExamine - 1) * this.countByExamine
							}
							this.getContents(cnt_a)
							this.getSensitiveWordByexamine(cnt_b)
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
			}
		},
		mounted() {
			let cnt_a = {
				type: '0',
				count: this.count,
				offset: (this.page - 1) * this.count
			}
			let cnt_b = {
				type: 1,
				count: this.countByExamine,
				offset: (this.pageByExamine - 1) * this.countByExamine
			}
			this.getContents(cnt_a)
			this.getSensitiveWordByexamine(cnt_b)
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

	.divdate {
		width: 120px;
		height: 20px;
		margin-left: 50px;
	}
</style>
