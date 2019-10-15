<template>
	<div>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 标题：</span>
				<el-input placeholder="请输入标题" v-model="name" style="display: inline-block;width: 400px">
				</el-input>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 简介：</span>
				<el-input placeholder="请输入简介" v-model="text" style="display: inline-block;width: 400px">
				</el-input>
			</el-col>
		</el-row>

		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 任务类型：</span>
				<el-select v-model="tasType" placeholder="请选择" style="margin-right: 10px;" @change="getContentTag">
					<el-option v-for="item in TaskType" :key="item.value" :label="item.name" :value="item.value">
					</el-option>
				</el-select>
			</el-col>
		</el-row>
		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 模板类型：</span>
				<el-select v-model="temType" placeholder="请选择" style="margin-right: 10px;">
					<el-option v-for="item in templateType" :key="item.value" :label="item.name" :value="item.value">
					</el-option>
				</el-select>
			</el-col>
		</el-row>

		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 价格：</span>
				<el-input placeholder="请输入价格" v-model="money" style="display: inline-block;width: 400px">
				</el-input>
			</el-col>
		</el-row>

		<el-row>
			<el-col :span="4">
				<div class="title-box">你可输入的标签:</div>
			</el-col>
			<el-col :span="18">
				<el-tag type="info" v-for="tag in TemTag" :key="tag.name">{{tag.name}}</el-tag>
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

		<el-row style="padding: 20px">
			<el-col :span="2" style="min-height: 20px"></el-col>
			<el-col :span="20">
				<span class="title-box"> 模板地址：</span>
				<el-input placeholder="请输入模板网络地址" v-model="src" style="display: inline-block;width: 400px">
				</el-input>
			</el-col>
		</el-row>

		<div style="text-align: center;">
			<el-button type="primary" @click="addTemplate">添加模板</el-button>
		</div>

		<div style="margin-top: 20px;text-align: center">
			<iframe :src="src" width="50%" height="300px">
			</iframe>
		</div>

	</div>
</template>

<script>
	export default {
		name: "addTemplate",
		data() {
			return {
				TemTag: '',
				name: '',
				temType: '',
				tasType: '',
				text: '',
				//标签
				tagList: [],
				inputVisible: false,
				inputValue: '',
				templateType: this.$constData.templateType,
				TaskType: this.$constData.TaskType,
				money: '',
				src: '',
			}
		},
		methods: {
			addTemplate() {
				let that = this
				let data = {
					src: this.src
				}
				let cnt = {
					module: this.$constData.module,
					type: this.tasType, // Byte 任务类型
					status: 0, // Byte 状态
					tags: JSON.stringify(that.tagList), // String 标签
					name: this.name, // String 名称
					data: JSON.stringify(data),
					money: parseFloat(this.money), // Double 金额
					text: this.text, // String 简介
					tempType: this.temType,
				};
				this.$api.addTemplate(cnt, (res => {
					if (res.data.rc == that.$util.RC.SUCCESS) {
						that.$message({
							message: '添加成功',
							type: 'success'
						});
						that.$router.push('/templateList')
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
				console.log(this.tagList)
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
				console.log(this.tagList)
			},
			getContentTag() {
				let cnt = {
					moduleId: this.$constData.chnnelTagmodule,
					group: this.tasType, // String <选填> 分组
					status: 1, // Byte <选填> 状态
					count: 20, // Integer 
					offset: 0, // Integer 
				};
				this.$api.getContentTag(cnt, (res) => {
					this.TemTag = this.$util.tryParseJson(res.data.c)
				})
			}

			//标签end
		},
	}
</script>

<style lang="scss" scoped>
	.title-box {
		font-size: 1.6rem;
		line-height: 40px;
		letter-spacing: 2px;
		font-weight: 600;
		color: #666;
		text-align: right;
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
