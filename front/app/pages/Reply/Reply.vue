<template>
	<view>
		<view v-if='replyList.length >0'>
			<view class="list">
				<view class="item">
					<view class="content">
						asdhasadasd对号函数第八号按时大会上八点哈搜不到had阿达萨达所大所阿萨德啊实打实达到阿斯达萨达啊飒飒大嫂
					</view>
					<view class="time-box">
						2019-05-5
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
				replyList: [{val:'223123123123123',time:'18663355'}],
			};
		},
		methods: {
			getReplyList(cnt) {
				this.$api.getReplyList(cnt, (res) => {
					console.log(JSON.parse(res.data.c))
					
				})

			}
		},
		onShow() {
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
	.msg-box{
		margin-top: 20rpx;
		width: 80%;
		height: 80rpx;
		line-height: 80rpx;
		font-size: 32rpx;
		text-align: center;
		color: #666;
	}
	.list{
		padding: 20rpx;
	}
	.item{
		margin-top: 10rpx;
		padding: 20rpx 20rpx 20rpx 20rpx;
	}
	.content{
		
		font-size: 32rpx;
		color: #303133;
		line-height: 46rpx;
		overflow:hidden;
		text-overflow:ellipsis;
		display:-webkit-box;
		-webkit-box-orient:vertical;
		-webkit-line-clamp:2;
	}
	.time-box{
			width: 100%;
		margin-top: 20rpx;
		font-size: 26rpx;
		color: #aaa;
		text-align: right;
	
	}
</style>
