<template>
	<view class="body">
		<!-- 顶部选项卡 -->
		<scroll-view id="nav-bar" class="nav-bar" scroll-x scroll-with-animation :scroll-left="scrollLeft">
			<view v-for="(item,index) in tagsList" :key="index" class="nav-item" :class="{current: index === tabCurrentIndex}"
			 :id="'tab'+index" @click="changeTime(index)">{{item.name}}</view>
		</scroll-view>
		<view style="padding-top: 70upx;"></view>

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
		<view v-else>
			<view class="msg-box">
				暂无任何回复消息...
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				count: 10,
				tagsList: [{
						name: '一周'
					}, {
						name: '一个月'
					},
					{
						name: '三个月'
					},
					{
						name: '全部'
					}
				],
				replyList: [],
				replays: [],

				scrollLeft: 0,
				tabCurrentIndex: 0,

			}
		},
		methods: {
			navToReplay(id, contentId) {
				uni.navigateTo({
					url: `/pages/Reply/replyView/replyView?id=${id}&contentId=${contentId}`
				})
			},

			changeTime(index) {
				this.tabCurrentIndex = index

			},

			timeFilter(timer) {
				let time = new Date(timer)
				let y = time.getFullYear()
				let m = 1 * time.getMonth() + 1
				let d = time.getDate()
				return `${y}-${m}-${d}`
			},

			getReplyListByUser(cnt) {
				this.$api.getReplyListByUser(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let list = this.$util.tryParseJson(res.data.c)
						console.log(list)
						// for (let i = 0; i.length < list.length; i++) {

						// }
						this.replyList = list
					} else {
						console.log('error')
					}
				})

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

			}
		},
		onShow() {
			if (uni.getStorageSync('userId') == '' || uni.getStorageSync('userId') == '1234567890') {
				return
			}

			let cnt = {
				upUserId: uni.getStorageSync('userId'), // Long <选填> 提交者编号
				status: 0, // Byte <选填> 审核状态，不填表示全部，0未审核，1已通过
				orderDesc: true, // Boolean 是否降序（较新的排前面）
				count: this.count, // Integer 
				offset: (this.page - 1) * this.count, // Integer 
			}
			this.getReplyListByUser(cnt)

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
</style>
