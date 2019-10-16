<template>
	<div>
		<el-row class="row-box1">
			<el-col :span="24" style="margin-bottom: 10px">
				<el-col :span="4">
					<div class="title-box">标题:</div>
				</el-col>
				<el-col :span="14">
					<div class="text-box">
						<el-input v-model="title" placeholder="标题"></el-input>
					</div>
				</el-col>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="4">
				<div class="title-box">状态:</div>
			</el-col>
			<el-col :span="18">
				<el-form label-width="80px">
					<el-form-item>
						<el-select v-model="status" placeholder="请选择状态">
							<el-option v-for="(item,index) in generalStatus" :key="index" :label="item.name" :value="item.value"></el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="4">
				<div class="title-box">你可输入的标签:</div>
			</el-col>
			<el-col :span="18">
				<el-tag type="info" v-for="tag in vipTagList" :key="tag.name">{{tag.name}}</el-tag>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="4">
				<div class="title-box">标签:</div>
			</el-col>
			<el-col :span="18" class="text-box">
				<el-tag :key="tag" v-for="tag in tagList" closable :disable-transitions="false" @close="handleClose(tag)">
					{{tag}}
				</el-tag>
				<el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput" size="small"
				 @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
				</el-input>
				<el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
			</el-col>
		</el-row>
		<el-col :span="24" style="text-align: center">
			<el-button style="padding: 1em 4em" type="primary" @click="createChannel">创建专栏</el-button>
		</el-col>
	</div>
</template>

<script>
	export default {
		name: "addSvip",
		data() {
			return {
				vipTagList: '',
				title: '',
				imgSrc: '',
				text: '',
				tagList: [],
				inputVisible: false,
				inputValue: '',
				status: '',
				generalStatus: this.$constData.generalStatus,
			}
		},
		methods: {
			createChannel() {
				let that = this
				let data = {
					text: this.text,
					src: this.imgSrc
				}
				let vipTag = {
					channel: this.tagList
				}
				let cnt = {
					module: this.$constData.module,
					title: this.title,
					status: this.status,
					tags: JSON.stringify(vipTag),
				}
				this.$api.createChannel(cnt, (res => {
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '添加成功',
							type: 'success'
						});
						that.$router.push('/svipList')
					} else {
						this.$message({
							message: res.data.c,
							type: 'warning'
						});
					}
				}))
			},
			// 标签
			handleClose(tag) {
				this.tagList.splice(this.tagList.indexOf(tag), 1);
			},
			showInput() {
				this.inputVisible = true;
				this.$nextTick(_ => {
					this.$refs.saveTagInput.$refs.input.focus();
				});
			},
			handleInputConfirm() {
				let that = this
				let inputValue = this.inputValue;
				if (inputValue) {
					that.tagList.push(inputValue);
				}
				this.inputVisible = false;
				this.inputValue = '';
			},
			getContentTag() {
				let cnt = {
					moduleId: this.$constData.module,
					group: '专题', 
					status: 1, 
					count: 20, 
					offset: 0, 
				};
				this.$api.getContentTag(cnt, (res) => {
					this.vipTagList = this.$util.tryParseJson(res.data.c)
				})
			}
		},
		mounted() {
			this.getContentTag()
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
