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
			<view v-for="(item, index) in comment" :key="index" class="eva-item">
				<image :src="item.user.head" mode="aspectFill"></image>
				<view class="eva-right">
					<text>{{item.user.name}}</text>
					<text>{{item.time}}</text>
					<view class="zan-box" @click="upvote(item.sequenceId,index,item)" v-if="item.jsAdd == false">
						<text>{{item.appraiseCount}}</text><!-- 点赞数 -->
						<text class="yticon iconfont kk-shoucang1"></text>
					</view>
					<text class="content">{{item.text}}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: ['comment'],
		data() {
			return {
				upvote(id,index,item) {
					if(item.upZan){
						uni.showToast({
							title:'你已经点过赞啦',
							icon:'none'
						})
						return
					}
					
					let cnt = {
						ownerId: id, // Long 内容编号
						userId: uni.getStorageSync('userId'), // Long 用户编号
						value: this.$constData.appraise[0].key, // Byte 状态
					}
					this.$api.createUpvote(cnt,(res)=>{
						if(res.data.rc == this.$util.RC.SUCCESS){
							if(this.$util.tryParseJson(res.data.c).value == 10){
								uni.showToast({
									title:'你已经点过赞啦',
									icon:'none'
								})
								return
							}
							uni.showToast({
								title:'点赞成功'
							})
							this.$emit('upZan',index)
						}else{
							uni.showToast({
								title:res.data.c,
								icon:'none'
							})
						}
					})
				}
			}
		},
		methods: {

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
</style>
