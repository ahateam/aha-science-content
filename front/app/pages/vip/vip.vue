<template>
	<view class="body">
		<scroll-view id="nav-bar" class="nav-bar" scroll-x scroll-with-animation :scroll-left="scrollLeft">
			<view v-for="(item,index) in tagsList" :key="index" class="nav-item" :class="{current: index === tabCurrentIndex}"
			 :id="'tab'+index" @click="changeTag(index)">{{item.name}}</view>
		</scroll-view>
		<view style="padding-top: 90upx;"></view>
		<view v-for="(item,index) in columnList" :key="index">
			<column :obj="item"></column>
		</view>
		<uni-load-more :status="pageStatus"></uni-load-more>
	</view>
</template>

<script>
	import column from '@/components/vip/column.vue'
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'

	export default {
		components: {
			column,
			uniLoadMore,
		},
		data() {
			return {
				constData: this.$constData, //全局变量

				count: 10,
				offset: 0,
				page: 1,

				pageStatus: 'loading',

				columnList: [
					{
						title:'知乎最精辟的50条段子，已笑喷！',
						createTime:new Date().getTime()
					}
				], //课程列表

				tagsList: [
					{
						name:'全部'
					}
				], //标签列表
				tabCurrentIndex: 0, //选中标签下标
				tagName: '', //当前选中标签名
			}
		},
		onLoad() {
			// this.getTags()
		},
		methods: {

			//根据标签切换内容
			changeTag(e) {
				this.tabCurrentIndex = e
				this.columnList = []
				if (e == 0) {
					this.tagName = ''
				} else {
					this.tagName = this.tagsList[e].name
				}
				this.page = this.tagsList[e].page
				if (this.tagsList[e].child != undefined) {
					this.columnList = this.tagsList[e].child
					this.pageStatus = this.tagsList[e].pageStatus
					return
				}
				this.pageStatus = 'loading'

				let cnt = {
					module: this.$constData.module, // String 隶属
					status: 0, // Byte <选填> 状态
					// tags: tags, // String <选填> 标签
					count: this.count, // Integer 
					offset: this.offset, // Integer 
				}

				if (e != 0) {
					cnt.tags = `{"channel":"${this.tagName}"}`
					this.getChannel(cnt)
				} else {
					this.getChannel(cnt)
				}
			},

			//获取专栏标签
			getTags() {
				let cnt = {
					moduleId: this.$constData.module, // String 隶属
					status: this.$constData.tagStatus[1].key, // Byte 标签状态
					group: 'VIP', // String 标签
					count: 500, // Integer 
					offset: 0, // Integer 
				}
				this.$api.getContentTag(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let tagsList = this.$util.tryParseJson(res.data.c)
						for (let i = 0; i < tagsList.length; i++) {
							tagsList[i].pageOver = false
							tagsList[i].page = 1
							tagsList[i].pageStatus = 'loading'
						}
						this.tagsList = tagsList
						console.log(this.tagsList)
						let cnt1 = {
							module: this.$constData.module, // String 隶属
							status: 0, // Byte <选填> 状态 默认显示0
							// tags: tags, // String <选填> 标签
							count: this.count, // Integer 
							offset: this.offset, // Integer 
						}
						this.getChannel(cnt1)
					} else {
						console.log('错误')
					}
				})
			},

			//获取专栏
			getChannel(cnt) {
				let index = this.tabCurrentIndex
				let obj = this.$util.tryParseJson(JSON.stringify(this.tagsList[index]))
				this.$api.getChannel(cnt, (res => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let arr = this.$util.tryParseJson(res.data.c)
						if (arr.length < this.count) {
							obj.pageOver = true
							obj.pageStatus = 'nomore'
						} else {
							obj.pageOver = false
							obj.pageStatus = 'more'
						}
						this.columnList = this.columnList.concat(arr)
						arr = this.columnList
						obj.child = arr
						this.$nextTick(function() {
							this.pageStatus = obj.pageStatus
							this.tagsList.splice(index, 1, obj)
							this.columnList = this.tagsList[index].child
						})
						uni.stopPullDownRefresh()
					} else {
						obj.child = []
						obj.pageStatus = 'nomore'
						this.pageStatus = 'nomore'
						this.$nextTick(function() {
							this.tagsList.splice(index, 1, obj)
							this.columnList = this.tagsList[index].child
							this.tagsList[index].pageOver == true
						})
					}
				}))
			},
		},
		onPullDownRefresh() {
			let index = this.tabCurrentIndex
			this.page = 1
			this.tagsList[index].child.page = 1
			this.columnList = []

			let cnt = {
				module: this.$constData.module, // String 隶属
				status: 0, // Byte <选填> 状态
				// tags: tags, // String <选填> 标签
				count: this.count, // Integer 
				offset: this.offset, // Integer 
			}
			if (this.tabCurrentIndex == 0) {
				this.getChannel(cnt)
			} else {
				cnt.tags = `{"channel":"${this.tagName}"}`
				this.getChannel(cnt)
			}
		},
		onReachBottom: function() {
			let index = this.tabCurrentIndex
			if (this.tagsList[index].pageOver == true) {
				return
			}
			this.pageStatus = 'loading'
			this.page += 1
			this.tagsList[index].page += 1
			let offset = (this.page - 1) * this.count
			let cnt = {
				module: this.$constData.module, // Long 模块编号
				status: 0, // Byte <选填> 状态
				// tags: tags, // String <选填> 标签（json）
				count: this.count, // int 
				offset: offset, // int 
			}
			if (index != 0) {
				cnt.tags = `{"channel":"${this.tagName}"}`
			}
			this.getChannel(cnt)
		}
	}
</script>

<style lang="scss" scoped>
	.body {
		background-color: #f8f8f8;
		min-height: 100vh;
		overflow: hidden;
	}
	
	/* 顶部tabbar */
	.nav-bar {
		position: fixed;
		top: 0;
		z-index: 10;
		height: 90upx;
		white-space: nowrap;
		box-shadow: 0 2upx 8upx rgba(0, 0, 0, .06);
		background-color: #fff;

		.nav-item {
			display: inline-block;
			width: 150upx;
			height: 90upx;
			text-align: center;
			line-height: 90upx;
			font-size: 30upx;
			color: #303133;
			position: relative;

			&:after {
				content: '';
				width: 0;
				height: 0;
				border-bottom: 4upx solid #007aff;
				position: absolute;
				left: 50%;
				bottom: 0;
				transform: translateX(-50%);
				transition: .3s;
			}
		}

		.current {
			color: #007aff;

			&:after {
				width: 50%;
			}
		}
	}

	.testText {
		padding: $box-margin-top $box-margin-left;
		font-size: $list-title;
	}

	.image {
		width: 100vw;

		image {
			display: block;
			padding: $box-margin-top;
			margin: 0 auto;
			width: 80vw;
		}
	}
</style>
