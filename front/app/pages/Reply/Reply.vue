<template>
	<view class="body">
		<!-- 顶部选项卡 -->
		<scroll-view id="nav-bar" class="nav-bar" scroll-x scroll-with-animation :scroll-left="scrollLeft">
			<view v-for="(item,index) in tagsList" :key="index" class="nav-item" :class="{current: index === tabCurrentIndex}"
			 :id="'tab'+index" @click="changeTime(index)">{{item.val}}</view>
		</scroll-view>
		<view style="padding-top: 90upx;"></view>
		<view class="statusBox">
			<text class="autoStatus" :class="statusCurr == false?'statusCurr':''" @click="changeStatus(false)">评论</text>|
			<text class="autoStatus" :class="statusCurr == true?'statusCurr':''" @click="changeStatus(true)">回复</text>
		</view>
		<view v-if='replyList.length >0'>
			<view class="list">
				<view class="item" :style="index == 0?'margin-top:0':''" v-for="(item,index) in replyList" :key="index" @click="navToInfo(item[0].content)">
					<view class="content">
						{{item[0].content.title}}
					</view>

					<view class="autoBox" v-for="(info,index1) in item" :key="index1">
						<view class="commentBox">
							"{{info.text}}"
						</view>

						<view class="replayBox" 　v-if="info.comment.list.length > 0" @click="navToReplay(info.sequenceId,info.content.id)"
						 @click.stop>
							<view class="topMargin" v-for="(list,index2) in info.comment.list" :key="index2" v-if="index2 < 2">
								<text class="nameBox">{{list.upUserName}}</text>: <text class="textBox">{{list.text}}</text>
								<view class="moreText" v-if="index2 == 1&&info.comment.list.length > 2">查看更多</view>
							</view>
						</view>

						<view class="time-box">
							{{timeFilter(info.createTime)}}
						</view>
					</view>
				</view>
			</view>
		</view>
		<uni-load-more :status="pageStatus"></uni-load-more>
	</view>
</template>

<script>
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'

	export default {
		components: {
			uniLoadMore,
		},
		data() {
			return {
				oneLoading: true,
				pageOver: false,
				pageStatus: 'loading',
				page: 1,
				count: 10,
				offset: 0,

				tagsList: [],
				replyList: [],
				replays: [],

				scrollLeft: 0,
				tabCurrentIndex: 0,

				statusCurr: false,
			}
		},
		methods: {
			changeStatus(e) {
				this.statusCurr = e
				this.tagsList[this.tabCurrentIndex].status = e
				this.replyList = []
				let cnt = {
					upUserId: uni.getStorageSync('userId'), // Long <选填> 提交者编号
					status: 0, // Byte <选填> 审核状态，不填表示全部，0未审核，1已通过
					orderDesc: true, // Boolean 是否降序（较新的排前面）
					count: this.count, // Integer 
					offset: this.offset, // Integer 
					time: this.$constData.timeData[this.tabCurrentIndex].key,
					isComment: this.statusCurr, // Boolean <选填> 是否加载二级评论
				}
				this.getReplyListOnshow(cnt, this.tabCurrentIndex)
			},

			navToReplay(id, contentId) {
				uni.navigateTo({
					url: `/pages/Reply/replyView/replyView?id=${id}&contentId=${contentId}`
				})
			},

			changeTime(index) {
				console.log(this.tagsList)
				if (this.pageStatus == 'loading') {
					return
				}
				this.statusCurr = this.tagsList[index].status

				this.tabCurrentIndex = index
				this.page = this.tagsList[index].page
				this.replyList = []

				let userId = uni.getStorageSync('userId')
				if (userId == '' || userId == '1234567890') {
					return
				}

				if (this.tagsList[index].child) {
					this.replyList = this.tagsList[index].child
					this.pageOver = this.tagsList[index].pageOver
					this.pageStatus = this.tagsList[index].pageStatus
					return
				}

				this.pageStatus = 'loading'

				let cnt = {
					upUserId: uni.getStorageSync('userId'), // Long <选填> 提交者编号
					// status: 0, // Byte <选填> 审核状态，不填表示全部，0未审核，1已通过
					orderDesc: true, // Boolean 是否降序（较新的排前面）
					count: this.count, // Integer 
					offset: this.offset, // Integer 
					isComment: this.statusCurr, // Boolean <选填> 是否加载二级评论
				}
				if (index != 3) {
					cnt.time = this.$constData.timeData[index].key
				}
				this.getReplyListByUser(cnt, index)
			},

			timeFilter(timer) {
				let time = new Date(timer)
				let y = time.getFullYear()
				let m = 1 * time.getMonth() + 1
				let d = time.getDate()
				return `${y}-${m}-${d}`
			},

			getReplyListOnshow(cnt, index) {
				this.$api.getReplyListByUser(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.oneLoading = false
						this.replyList = []
						let list = this.$util.tryParseJson(res.data.c)
						this.tryDataPush(list, index)
					} else {
						console.log('error')
					}
				})
			},

			getReplyListByUser(cnt, index) {
				this.$api.getReplyListByUser(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						uni.stopPullDownRefresh()
						let list = this.$util.tryParseJson(res.data.c)
						this.tryDataPush(list, index)
					} else {
						console.log('error')
					}
				})
			},

			tryDataPush(list, index) {
				if (list.length < this.count) {
					this.tagsList[index].pageStatus = 'nomore'
					this.tagsList[index].pageOver = true
				} else {
					this.tagsList[index].pageStatus = 'more'
					this.tagsList[index].pageOver = false
				}

				this.pageStatus = this.tagsList[index].pageStatus
				this.pageOver = this.tagsList[index].pageOver
				let arr = this.replyList.concat(list)
				this.tagsList[index].child = arr
				this.replyList = arr
				console.log(this.tagsList)
				console.log(this.replyList)
			},

			/* 跳转至详情 */
			navToInfo(info) {
				if (info) {
					if (info.type == this.$constData.contentType[2].key || info.type == this.$constData.contentType[0].key) {
						uni.navigateTo({
							url: `/pages/index/articleView/articleView?id=${info.id}`
						})
					} else if (info.type == this.$constData.contentType[1].key) {
						uni.navigateTo({
							url: `/pages/index/videoView/videoView?id=${info.id}`
						})
					} else if (info.type == this.$constData.contentType[3].key) {
						uni.navigateTo({
							url: `/pages/index/activity/activity?contentId=${info.id}&placeId=${this.$util.tryParseJson(info.data).place}`
						})
					}
				} else {
					uni.showToast({
						title: '内容已被删除',
						icon: 'none'
					})
				}
			},

			getTagList() {
				this.tagsList = [{
						key: this.$constData.timeData[0].key,
						val: '七天',
						status: false
					},
					{
						key: this.$constData.timeData[1].key,
						val: '一个月',
						status: false
					},
					{
						key: this.$constData.timeData[2].key,
						val: '三个月',
						status: false
					},
					{
						val: '全部',
						status: false
					}
				]
				for (let i = 0; i < this.tagsList.length; i++) {
					this.tagsList[i].page = 1
				}
				let index = this.tabCurrentIndex
				let cnt = {
					upUserId: uni.getStorageSync('userId'), // Long <选填> 提交者编号
					status: 0, // Byte <选填> 审核状态，不填表示全部，0未审核，1已通过
					orderDesc: true, // Boolean 是否降序（较新的排前面）
					count: this.count, // Integer 
					offset: this.offset, // Integer 
					time: this.$constData.timeData[this.tabCurrentIndex].key,
					isComment: this.statusCurr, // Boolean <选填> 是否加载二级评论
				}
				this.getReplyListOnshow(cnt, index)
			}
		},

		onLoad() {
			this.getTagList()
		},

		onShow() {
			if (this.oneLoading) {
				return
			}

			if (uni.getStorageSync('userId') == '' || uni.getStorageSync('userId') == '1234567890') {
				this.pageStatus = 'nomore'
				this.replyList = []
				this.tagsList = []
				this.getTagList()
				return
			}

			this.pageStatus = 'loading'
			let index = this.tabCurrentIndex
			let cnt = {
				upUserId: uni.getStorageSync('userId'), // Long <选填> 提交者编号
				status: 0, // Byte <选填> 审核状态，不填表示全部，0未审核，1已通过
				orderDesc: true, // Boolean 是否降序（较新的排前面）
				count: this.count, // Integer 
				offset: this.offset, // Integer 
				time: this.$constData.timeData[this.tabCurrentIndex].key,
				isComment: this.statusCurr, // Boolean <选填> 是否加载二级评论
			}
			this.getReplyListOnshow(cnt, index)
		},
		onPullDownRefresh() {
			if (uni.getStorageSync('userId') == '' || uni.getStorageSync('userId') == '1234567890') {
				setTimeout(() => {
					uni.stopPullDownRefresh()
					this.pageStatus = 'nomore'
				}, 300)
				return
			}

			this.pageStatus = 'loading'
			this.page = 1
			let index = this.tabCurrentIndex
			this.tagsList[index].page = 1
			this.replyList = []
			let cnt = {
				upUserId: uni.getStorageSync('userId'), // Long <选填> 提交者编号
				status: 0, // Byte <选填> 审核状态，不填表示全部，0未审核，1已通过
				orderDesc: true, // Boolean 是否降序（较新的排前面）
				count: this.count, // Integer 
				offset: this.offset, // Integer 
				time: this.$constData.timeData[this.tabCurrentIndex].key,
				isComment: this.statusCurr, // Boolean <选填> 是否加载二级评论
			}
			this.getReplyListByUser(cnt, index)
		},

		//加载更多
		onReachBottom() {
			if (this.pageOver) {
				return
			}
			let index = this.tabCurrentIndex
			this.page += 1
			this.tagsList[index].page += 1
			let cnt = {
				upUserId: uni.getStorageSync('userId'), // Long <选填> 提交者编号
				status: 0, // Byte <选填> 审核状态，不填表示全部，0未审核，1已通过
				orderDesc: true, // Boolean 是否降序（较新的排前面）
				count: this.count, // Integer 
				offset: (this.page - 1) * this.count, // Integer 
				time: this.$constData.timeData[index].key,
				isComment: this.statusCurr, // Boolean <选填> 是否加载二级评论
			}
			this.getReplyListByUser(cnt, index)
		}

	}
</script>

<style scoped lang="scss">
	.topMargin {
		padding-top: 10upx;
	}

	/* 顶部tabbar */
	.nav-bar {
		position: fixed;
		z-index: 10;
		height: 90upx;
		white-space: nowrap;
		box-shadow: 0 2upx 8upx rgba(0, 0, 0, .06);
		background-color: #fff;

		.nav-item {
			display: inline-block;
			height: 90upx;
			padding: 0 20upx;
			text-align: center;
			line-height: 90upx;
			font-size: 30upx;
			color: #303133;
			position: relative;
			transition: .2s;

			&:after {
				content: '';
				width: 0;
				height: 0;
				border-bottom: 4upx solid #fb7299;
				position: absolute;
				left: 50%;
				bottom: 0;
				transform: translateX(-50%);
				transition: .2s;
			}
		}

		.current {
			color: #fb7299;
			font-weight: bold;
			font-size: $list-title;
			// transform: translateY(5upx);

			&:after {
				width: 50%;
				transform: translateX(-50%) translateY(-7upx);
			}
		}
	}

	/* end!!!!! */

	.msg-box {
		padding-top: 20rpx;
		height: 80rpx;
		line-height: 80rpx;
		font-size: 32rpx;
		text-align: center;
		color: #666;
	}

	.list {
		padding: 20rpx 0;
	}

	.item {
		margin-top: 20rpx;
		padding: 20rpx 20rpx 20rpx 20rpx;
		background-color: #FFFFFF;
	}

	.content {
		font-size: $list-title;
		color: #303133;
		line-height: 46rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
	}

	.time-box {
		width: 100%;
		margin-top: 20rpx;
		font-size: 26rpx;
		color: #aaa;
		text-align: right;

	}

	.commentBox {
		margin: 15upx 0;
		padding: 0 10upx;
		font-size: $list-title;
		border-left: 3px $color-main solid;
	}

	.replayBox {
		background-color: $uni-bg-color-grey;
		padding: $box-margin-top $box-margin-left;
		border-radius: 10upx;
		font-size: $list-info;

	}

	.nameBox {
		color: #11BBFF;
		padding-right: 10upx;
	}

	.textBox {
		padding-left: 10upx;
	}

	.moreText {
		color: #11BBFF;
		text-align: right;
		padding-right: 10upx;
	}

	.body {
		background-color: $uni-bg-color-grey;
		min-height: 100vh;
	}

	.autoBox {}

	.autoStatus {
		padding: 0 10upx;
		transition: all .3s;
	}

	.statusBox {
		text-align: right;
		font-size: $list-title;
		color: $list-info-color;
		padding: 10upx $box-margin-left;
	}

	.statusCurr {
		color: $color-main;
	}
</style>
