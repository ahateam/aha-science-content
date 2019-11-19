<template>
	<view>
		<!-- 评论 -->
		<view class="s-header">
			<text>网友评论</text>
		</view>
		<view class="evalution">
			<view class="noEva" v-if="comment.length == 0">
				还没有人评论哦,快来抢个首发吧~
			</view>
			<view v-for="(item, index) in comment" :key="index" class="eva-item" @click="repaly(item.sequenceId,index,item)">
				<image :src="item.head" mode="aspectFill"></image>
				<view class="eva-right">
					<text>{{item.name}}</text>
					<text>{{item.time}}</text>
					<view class="zan-box" @click="upvote(item.sequenceId,index,item)" @click.stop v-if="item.jsAdd == false">
						<text>{{item.appraiseCount}}</text><!-- 点赞数 -->
						<text class="yticon iconfont kk-shoucang1" :class="{iconCurrent:item.isAppraise}"></text>
					</view>
					<text class="content">{{item.text}}</text>
					<view class="replayBox" v-if="item.comment.length > 0" @click.stop="navToreplay(item.sequenceId,item.ownerId)">
						<view class="replayList" v-for="(list,index1) in item.comment" :key="index1" v-if="index1 < 3">
							<text class="replayUser">{{list.upUserName}}</text>：<text class="replayText">{{list.text}}</text>
							<view class="moreReplay" v-if="index1 == 2 && item.comment.list.length>3">
								查看更多
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: ['comment'],
		data() {
			return {}
		},
		methods: {
			//跳转至二级评论
			navToreplay(id, contentId) {
				console.log(contentId)
				uni.navigateTo({
					url: `/pages/Reply/replyView/replyView?id=${id}&contentId=${contentId}`
				})
			},

			//回复
			repaly(id, index, item) {
				let userId = uni.getStorageSync('userId')
				if (userId == '' || userId == '1234567890') {
					uni.showToast({
						title: '请登录',
						icon: 'none'
					})
					return
				}

				this.$emit('repaly', id, index, item.name)
			},

			delAppraise(id, index) {
				let cnt = {
					ownerId: id,
					userId: uni.getStorageSync('userId')
				}
				this.$api.delAppraise(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.$emit('delZan', index)
					}
				})
			},

			upvote(id, index, item) {
				let userId = uni.getStorageSync('userId')
				if (userId == '' || userId == '1234567890') {
					uni.showToast({
						title: '请登录',
						icon: 'none'
					})
					return
				}

				if (item.isAppraise) {
					this.delAppraise(id, index)
					return
				}

				let cnt = {
					ownerId: id, // Long 内容编号
					userId: uni.getStorageSync('userId'), // Long 用户编号
					value: this.$constData.appraise[0].key, // Byte 状态
				}
				this.$api.createUpvote(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						if (this.$util.tryParseJson(res.data.c).value == 10) {
							uni.showToast({
								title: '你已经点过赞啦',
								icon: 'none'
							})
							return
						}
						uni.showToast({
							title: '点赞成功'
						})
						this.$emit('upZan', index)
					} else {
						uni.showToast({
							title: res.data.c,
							icon: 'none'
						})
					}
				})
			},

		}
	}
</script>

<style lang="scss" scoped>
	.s-header {
		padding: 20upx 30upx;
		font-size: 30upx;
		color: #303133;
		background: #fff;
		margin-top: 16upx;

		&:before {
			content: '';
			width: 0;
			height: 40upx;
			margin-right: 24upx;
			border-left: 6upx solid #ec706b;
		}
	}

	.noEva {
		font-size: 30upx;
		color: #303133;
		padding: 20rpx 30rpx;
	}

	.evalution {
		display: flex;
		flex-direction: column;
		background: #fff;
		padding: 20upx 0;
	}

	.eva-item {
		display: flex;
		padding: 20upx 30upx;
		position: relative;

		image {
			width: 60upx;
			height: 60upx;
			border-radius: 50px;
			flex-shrink: 0;
			margin-right: 24upx;
		}

		&:after {
			content: '';
			position: absolute;
			left: 30upx;
			bottom: 0;
			right: 0;
			height: 0;
			border-bottom: 1px solid #eee;
			transform: translateY(50%);
		}

		&:last-child:after {
			border: 0;
		}
	}

	.eva-right {
		display: flex;
		flex-direction: column;
		flex: 1;
		font-size: 26upx;
		color: #909399;
		position: relative;

		.zan-box {
			display: flex;
			align-items: base-line;
			position: absolute;
			top: 10upx;
			right: 10upx;

			.yticon {
				margin-left: 8upx;
			}
		}

		.content {
			font-size: 28upx;
			color: #333;
			padding-top: 20upx;
		}
	}

	.replayBox {
		border-radius: 10upx;
		background-color: $uni-bg-color-grey;
		padding: 10upx;
	}

	.replayList {
		padding: 10upx 0;
		font-size: $list-info;
	}

	.replayUser {
		color: #11BBFF;
	}

	.replayText {}

	.moreReplay {
		text-align: right;
		padding-right: 10upx;
	}

	.iconCurrent {
		color: $color-main;
	}
</style>
