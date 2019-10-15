<template>
	<div>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 名称：</span>
				<el-input placeholder="请输入名称" v-model="title" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 价格：</span>
				<el-input placeholder="单位元" v-model="price" style="display: inline-block;width: 400px"></el-input>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="状态">
						<el-select v-model="status" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in statusList" :key="item.value" :label="item.name" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="8">
				<el-form label-width="80px">
					<el-form-item label="所属专栏">
						<el-select v-model="upChannelId" placeholder="请选择" style="margin-right: 10px;">
							<el-option v-for="item in channelList" :key="item.id" :label="item.title" :value="item.id">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
		</el-row>
		<el-col :span="24" style="text-align: center">
			<el-button style="padding: 1em 4em" type="primary" @click="createChannel">提交修改</el-button>
		</el-col>
	</div>
</template>

<script>
	export default {
		name: "addSvip",
		data() {
			return {
				id: '',
				tableData: [],
				upChannelId: '',
				channelList: [{
					title: '',
					id: 0,
				}],
				title: '',
				price: '',
				imgSrc: '',
				text: '',
				tagList: [],
				inputVisible: false,
				inputValue: '',
				status: '',
				generalStatus: this.$constData.generalStatus,
				statusList: this.$constData.statusList,
			}
		},
		methods: {
			typeFliter(row, col, val) {
				let typeList = this.typeList
				for (let i = 0; i < typeList.length; i++) {
					if (typeList[i].value == val) {
						return typeList[i].name
					}
				}
			},
			createChannel() {
				let cnt = {
					moduleId: this.$constData.module,
					id: this.id,
					name: this.title,
					price: this.price,
					channelId: this.upChannelId,
					status: this.status
				};
				this.$api.updateChannelContentTag(cnt, (res => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.$message({
							message: '修改成功',
							type: 'success'
						});
					} else {
						this.$message({
							message: res.data.c,
							type: 'warning'
						});
					}
				}))
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
			getChannelContentTag() {
				let cnt = {
					moduleId: this.$constData.module,
					channelId: this.upChannelId,
					count: 20,
					offset: 0,
				};
				this.$api.getChannelContentTag(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.tableData = this.$util.tryParseJson(res.data.c)
					}
				})
			}
		},
		mounted() {
			let info = this.$route.params.info
			this.title = info.name
			this.price = info.price
			this.status = info.status
			this.upChannelId = info.channelId
			this.id = info.id
			this.getChannels()
			this.getChannelContentTag()
		}
	}
</script>

<style lang="scss" scoped>
	.row-box1 {
		margin-top: 20px;
		padding: 15px;
		background: #fff;
	}

	.title-box {
		font-size: 1.6rem;
		line-height: 40px;
		letter-spacing: 2px;
		font-weight: 600;
		color: #666;
		text-align: right;
	}

	.text-box {
		margin-left: 50px;
	}

	.el-tag+.el-tag {
		margin-left: 10px;
	}

	.button-new-tag {
		margin-left: 10px;
		height: 32px;
		line-height: 30px;
		padding-top: 0;
		padding-bottom: 0;
	}

	.input-new-tag {
		width: 90px;
		margin-left: 10px;
		vertical-align: bottom;
	}
</style>
