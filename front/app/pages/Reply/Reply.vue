<template>
	<view>
		<view v-if='replyList.length >0'>
			<view class="list">
				<view class="item" v-for="(item,index) in replyList" :key="index" @click="navToInfo(item.content)">
					<view class="content">
						{{item.text}}
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
				replyList: [],
			};
		},
		methods: {
			timeFilter(timer){
				let time = new Date(timer)
				let y = time.getFullYear()
				let m = 1 * time.getMonth() + 1
				let d = time.getDate()
				return `${y}-${m}-${d}`
			},
			
			getReplyList(cnt) {
				this.$api.getReplyList(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let list = this.$util.tryParseJson(res.data.c)
						console.log(list)
						for (let i = 0; i.length < list.length; i++) {
							
						}
						this.replyList = list
					} else {
						console.log('error')
					}
				})

			},
			
			/* 跳转至详情 */
			navToInfo(info) {
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
			}
		},
		onShow() {
			if(uni.getStorageSync('userId') == ''||uni.getStorageSync('userId') == '1234567890'){
				return
			}
			
			let cnt = {
				upUserId: uni.getStorageSync('userId'), // Long <选填> 提交者编号
				status: 0, // Byte <选填> 审核状态，不填表示全部，0未审核，1已通过
				orderDesc: true, // Boolean 是否降序（较新的排前面）
				count: this.count, // Integer 
				offset: (this.page - 1) * this.count, // Integer 
			}
			this.getReplyList(cnt)

		}
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
