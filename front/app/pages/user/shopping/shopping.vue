<template>
	<view>
		<view v-if='list.length >0'>
			<view class="list">
				<view class="item" v-for="(item,index) in list" :key="index" @click="navToInfo(item)">
					<view class="content">
						{{item.contentName}}
					</view>
					<view class="time-box">
						{{timeFilter(item.createTime)}}
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
				list: [],
			};
		},
		onLoad() {
			let cnt = {
				moduleId: this.$constData.module, // Long 模块编号
				userId: uni.getStorageSync('userId'), // Long <选填> 用户id
				// contenId: contenId, // Long <选填> 内容id
				count: this.count, // int 
				offset: this.offset, // int 
			}
			this.getEnrolls(cnt)
		},

		methods: {
			timeFilter(timer) {
				let time = new Date(timer)
				let y = time.getFullYear()
				let m = 1 * time.getMonth() + 1
				let d = time.getDate()
				return `${y}-${m}-${d}`
			},

			getEnrolls(cnt) {
				this.$api.getEnrolls(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.list = this.$util.tryParseJson(res.data.c)
						console.log(this.list)
					} else {
						console.log('error')
					}
				})
			},

			/* 跳转至详情 */
			navToInfo(info) {
				uni.navigateTo({
					url: `/pages/index/activity/activity?contentId=${info.contenId}`
				})
			}

		},
	}
</script>

<style scoped lang="scss">
	.msg-box {
		margin-top: 20rpx;
		width: 80%;
		height: 80rpx;
		line-height: 80rpx;
		font-size: 32rpx;
		text-align: center;
		color: #666;
	}

	.list {
		padding: 20rpx;
	}

	.item {
		margin-top: 10rpx;
		padding: 20rpx 20rpx 20rpx 20rpx;
	}

	.content {

		font-size: 32rpx;
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
</style>
