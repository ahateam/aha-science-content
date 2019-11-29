<template>
	<view style="padding-bottom: 90upx;">
		<comment :comment="comment" @upZan="upZan" @delZan="delZan" @repaly="openReplay"></comment>
		<view class="bottom" v-if="pageStatus">
			<view class="input-box" v-if="!replayBox">
				<text class="yticon icon-huifu"></text>
				<input class="input" type="text" placeholder="点评一下把.." v-model="commentContent" placeholder-style="color:#adb1b9;" />
			</view>
			<text class="confirm-btn" @click="createComment" v-if="!replayBox">提交</text>
			<view class="input-box" v-if="replayBox">
				<text class="yticon icon-huifu"></text>
				<input focus class="input" type="text" :placeholder="'@'+repalyName+'：'" v-model="commentContent" placeholder-style="color:#adb1b9;" />
			</view>
			<text class="confirm-btn" @click="replayAfter" v-if="replayBox">提交</text>
		</view>
	</view>
</template>

<script>
	import comment from '@/components/comment/comment.vue'

	export default {
		components: {
			comment
		},
		data() {
			return {
				count: 10,
				offset: 0,
				pageStatus: 'loading',
				pageOver: false,

				contentId: '',

				/* 点赞 */
				commentId: 0, //点赞对象id
				contentUpvote: 0, //文章点赞数
				upvoteStatus: false, //文章点赞状态
				/* 点赞end */

				/* 评论 */
				comment: [], //评论列表
				totalCount: 0, //文章评论数
				commentContent: '', //评论内容

				//二級回復
				repalyId: '', //评论id
				replayBox: false, //二级回复盒子开关
				repalyIndex: '', //回复列表下标
				repalyName: '', //回复的人的name
				/* 评论end */
			};
		},
		methods: {
			//更新讚數
			delZan(index) {
				this.comment[index].appraiseCount -= 1
				this.comment[index].isAppraise = false
			},

			//更新赞数
			upZan(index) {
				this.comment[index].appraiseCount += 1
				this.comment[index].isAppraise = true
			},

			//顯示二級回復輸入框
			openReplay(id, index, name) {
				if (id) {
					this.repalyId = id
					this.repalyIndex = index
					this.repalyName = name
					this.replayBox = true
				} else {
					uni.showToast({
						title: '无法回复',
						icon: 'none'
					})
				}
			},

			replayAfter() {
				let userId = uni.getStorageSync('userId')
				if (userId == '' || userId == '1234567890') {
					uni.showToast({
						title: '登录后可评论',
						icon: 'none'
					})
					return
				}

				let cnt = {
					replyId: this.repalyId, // Long 回复评论id
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
						let user = {
							upUserName: uni.getStorageSync('userName'),
							text: this.commentContent
						}
						this.comment[this.repalyIndex].comment.splice(0, 0, user)
						this.commentContent = ''
					} else {
						uni.showToast({
							title: res.data.rm,
							icon: 'none'
						})
					}
				})
			},

			/* 评论 */
			createComment() {
				let userId = uni.getStorageSync('userId')
				let status = uni.getStorageSync('status')
				if (userId == '' || userId == '1234567890') {
					uni.showToast({
						title: '登录后可评论',
						icon: 'none'
					})
					return
				}

				let cnt = {
					// module: this.$constData.module, // String 隶属
					ownerId: this.contentId, // Long 内容编号
					upUserId: userId, // Long 用户编号
					text: this.commentContent, // String 评论内容
					// data: [], // String 其他数据
					atUserId: 1, // Long <选填> @对象编号
					atUserName: '无', // String <选填> @对象名称
					title: '无', // String <选填> 标题
					ext: '无', // String <选填> 扩展
				};
				this.$api.createReply(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						uni.showToast({
							title: '评论成功',
							duration: 1000
						})
						let time = new Date()
						let y = time.getFullYear()
						let m = 1 + time.getMonth()
						let d = time.getDate()

						let data = {
							text: this.commentContent,
							time: `${y}-${m}-${d}`,
							jsAdd: true,
							name: uni.getStorageSync('userName'),
							head: uni.getStorageSync('userHead')
						}
						this.comment.splice(0, 0, data)
						console.log(this.comment)
						this.commentContent = ''
					} else {
						uni.showToast({
							title: res.data.rm,
							icon: 'none'
						})
					}
				})
			},

			//获取评论
			getCommentByContentId(cnt) {
				this.$api.getCommentByContentId(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						console.log('评论接口返回数据')
						console.log(this.$util.tryParseJson(res.data.c))
						console.log('~~~~~~~~~~~~~~~~~~~~~~~~')
						// this.totalCount = this.$util.tryParseJson(res.data.c).totalCount
						// this.contentUpvote = this.$util.tryParseJson(res.data.c).contentUpvote
						let comment = this.$util.tryParseJson(res.data.c)
						for (let i = 0; i < comment.length; i++) {
							comment[i].jsAdd = false
							let time = new Date(comment[i].createTime)
							let y = time.getFullYear()
							let m = 1 + time.getMonth()
							let d = time.getDate()
							comment[i].time = `${y}-${m}-${d}`
						}
						this.comment = comment
					} else {
						uni.showToast({
							title: '评论获取失败',
							icon: 'none',
							duration: 1000
						})
					}
				})
			}
		},
		onLoad(res) {
			this.contentId = res.id
			let cnt = {
				ownerId: this.contentId, // Long 内容编号
				userId: uni.getStorageSync('userId'), // Long <选填> 当前用户id
				orderDesc: true, // Boolean 是否降序（较新的排前面）
				count: 10, // Integer 
				offset: 0, // Integer 
			}
			this.getCommentByContentId(cnt)
		}
	}
</script>

<style lang="scss" scoped>
	.bottom {
		position: fixed;
		width: 100%;
		box-sizing: border-box;
		bottom: 0;
		flex-shrink: 0;
		display: flex;
		align-items: center;
		height: 90upx;
		padding: 0 30upx;
		box-shadow: 0 -1px 3px rgba(0, 0, 0, .04);
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
