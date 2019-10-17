<template>
	<view>
		<view v-if='replyList.length >0'>
			<view class="list">
				<view class="item">
					<view class="content">
						asdhasadasd对号函数第八号
					</view>
					<view class="tiem">
						
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
				status: 1, // Byte <选填> 审核状态，不填表示全部，0未审核，1已通过
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
		padding: 20rpx;
		
	}
	.content{
		padding:15rpx;
	}
	.time{
		
	}
</style>
