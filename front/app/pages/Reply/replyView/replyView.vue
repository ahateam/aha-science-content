<template>
	<view>
		<scroll-view class="scroll" scroll-y>
			<view class="eva-item" v-if="comment.length > 0">
				<image style="width: 75upx;height: 75upx;" :src="user.head" mode="aspectFill"></image>
				<view class="eva-right">
					<text>{{user.name}}</text>
					<text>{{replay.time}}</text>
					<view class="zan-box" @click="upZan(id)">
						<text>{{appraiseCount}}</text> <!-- 点赞数 -->
						<text class="yticon iconfont kk-shoucang1"></text>
					</view>
					<text class="content">{{replay.text}}</text>
				</view>
			</view>
			<view class="s-header" v-if="comment.length > 0">
				<text>相关回复</text>
			</view>
			<view v-for="(item, index) in comment" :key="index" class="eva-item">
				<image :src="item.comment.upUserHead" mode="aspectFill"></image>
				<view class="eva-right">
					<text>{{item.comment.upUserName}}</text>
					<text>{{item.time}}</text>
					<view class="zan-box" @click="upvote(item.comment.sequenceId,index,item)" @click.stop v-if="item.appraiseCount||item.appraiseCount === 0">
						<text>{{item.appraiseCount}}</text><!-- 点赞数 -->
						<text class="yticon iconfont kk-shoucang1"></text>
					</view>
					<text class="content">{{item.comment.text}}</text>
				</view>
			</view>
			<uniLoadMore :status="pageStatus"></uniLoadMore>
		</scroll-view>

		<view class="bottom">
			<view class="input-box">
				<text class="yticon icon-huifu"></text>
				<input class="input" type="text" :placeholder="'@'+user.name+'：'" v-model="commentContent" placeholder-style="color:#adb1b9;" />
			</view>
			<text class="confirm-btn" @click="repaly">提交</text>
		</view>

	</view>
</template>

<script>
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'
	import comment from '@/components/comment/comment.vue'

	export default {
		components: {
			uniLoadMore,
			comment
		},
		data() {
			return {
				replay: {},
				user: {},
				appraiseCount: 0,
				zanStatus: false,

				comment: [],

				count: 10,
				offset: 0,
				page: 1,
				pageStatus: 'loading',

				commentContent: '',
			}
		},
		onLoad(res) {
			this.id = res.id
			this.contentId = res.contentId
			let cnt = {
				moduleId: this.$util.module, // String <选填> 模块编号
				ownerId: this.contentId, // Long <选填> 内容编号
				replyId: this.id, // Long <选填> 回复id
				// status: status, // String <选填> 审核状态，不填表示全部，0未审核，1已通过
				orderDesc: true, // Boolean 是否降序（较新的排前面）
				count: this.count, // Integer 
				offset: this.offset, // Integer 
			}
			this.getReplay(cnt)
		},

		methods: {

			//回复
			repaly(id, index, item) {
				let userId = uni.getStorageSync('userId')
				let status = uni.getStorageSync('status')
				if (userId == '' || userId == '1234567890') {
					uni.showToast({
						title: '登录后可评论',
						icon: 'none'
					})
					return
				}

				if (status == this.$constData.userStatus[1].key) {
					uni.showToast({
						title: '已被管理员禁言',
						icon: 'none'
					})
					return
				}
				
				let cnt = {
					replyId: this.id, // Long 回复评论id
					upUserId: uni.getStorageSync('userId'), // Long 提交者编号
					upUserHead: uni.getStorageSync('userHead'), // String 提交者头像
					upUserName: uni.getStorageSync('userName'), // String 提交者昵称
					// toUserId: toUserId, // Long 目标用户编号
					text: this.commentContent, // String 正文
					// toUserId: toUserId, // Long <选填> 目标用户编号
					// toUserName: toUserName, // String <选填> 目标用户昵称
				}
				this.$api.createComment(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.replayBox = false
						uni.showToast({
							title: '回复成功！',
						})

						let time = new Date()
						let y = time.getFullYear()
						let m = time.getMonth() * 1 + 1
						let d = time.getDate()
						let user = {
							comment: {
								upUserName: uni.getStorageSync('userName'),
								upUserHead: uni.getStorageSync('userHead'),
								text: this.commentContent
							},
							time: `${y}-${m}-${d}`
						}
						this.comment.splice(0, 0, user)
						this.commentContent = ''
					} else {
						uni.showToast({
							title: '网络错误',
							icon: 'none'
						})
					}
				})
			},

			// 时间格式化
			getTime(e) {
				let time = new Date(e)
				let y = time.getFullYear()
				let m = 1 + time.getMonth()
				let d = time.getDate()
				let newTime = `${y}-${m}-${d}`
				return newTime
			},

			upZan(id) {
				let userId = uni.getStorageSync('userId')
				if (userId == '' || userId == '1234567890') {
					uni.showToast({
						title: '请登录',
						icon: 'none'
					})
					return
				}

				if (this.zanStatus) {
					uni.showToast({
						title: '你已经点过赞啦',
						icon: 'none'
					})
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
						this.zanStatus = true
					} else {
						uni.showToast({
							title: res.data.c,
							icon: 'none'
						})
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

				if (item.upZan) {
					uni.showToast({
						title: '你已经点过赞啦',
						icon: 'none'
					})
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
						this.comment[index].upZan = true
						this.comment[index].appraiseCount += 1
					} else {
						uni.showToast({
							title: res.data.c,
							icon: 'none'
						})
					}
				})
			},

			getReplay(cnt) {
				this.$api.getReplyListByReplyId(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let data = this.$util.tryParseJson(res.data.c)
						console.log(data)
						data.reply.time = this.getTime(data.reply.createTime)
						this.replay = data.reply
						this.user = data.replyUser
						this.appraiseCount = data.appraiseCount

						let comment = data.comment
						for (let i = 0; i < comment.length; i++) {
							comment[i].time = this.getTime(comment[i].comment.createTime)
						}
						this.comment = comment

						this.pageStatus = 'more'
					} else {
						console.log('error')
					}
				})
			}
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

	.scroll {
		flex: 1;
		position: relative;
		// background-color: #f8f8f8;
		height: calc(100vh - 90upx);
	}

	/* 底部 */
	.bottom {
		flex-shrink: 0;
		display: flex;
		align-items: center;
		height: 90upx;
		padding: 0 30upx;
		box-shadow: 0 -1px 3px rgba(0, 0, 0, .04);
		position: relative;
		z-index: 1;

		.input-box {
			display: flex;
			align-items: center;
			flex: 1;
			height: 60upx;
			background: #f2f3f3;
			border-radius: 100px;
			padding-left: 30upx;

			.icon-huifu {
				font-size: 28upx;
				color: #909399;
			}

			.input {
				padding: 0 20upx;
				font-size: 28upx;
				color: #303133;
			}
		}

		.confirm-btn {
			font-size: 30upx;
			padding-left: 20upx;
			color: #0d9fff;
		}
	}
</style>
