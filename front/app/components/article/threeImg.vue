<template>
	<view class="body">
		<text class="title">{{item.title}}</text>
		<view class="imgList">
			<view class="imgBox" v-for="(list,index) in item.imgList" :key="index" v-if="index < 3">
				<image :src="list.src" mode="scaleToFill"></image>
			</view>
		</view>
		<view class="bottom">
			<text class="upName">{{item.user.name}}</text>
			<text class="time">{{item.time}}</text>
			<text class="seeNumber" v-if="item.isPageView">阅读量：{{item.pageView}}</text>
		</view>
	</view>
</template>

<script>
	export default {
		props: ['item'],
		data() {
			return {};
		}
	}
</script>

<style lang="scss" scoped>
	* {
		margin: 0;
	}

	.body {
		width: 100%;
	}

	.title {
		font-size: $list-title;
		color: $list-title-color;
		line-height: $list-title-line;
	}

	.imgList {
		position: relative;
		width: 700upx;
		height: 140upx;
		margin: $box-margin-s 0upx;
		flex-shrink: 0;
		flex-direction: row;
		display: flex;


		image {
			width: 100%;
			height: 100%;
		}
	}

	.videoBtn {
		position: absolute;
		background-color: rgba(0, 0, 0, 0.3);
		width: 100%;
		height: 100%;
		top: 0;

		image {
			position: absolute;
			left: 50%;
			top: 50%;
			margin-left: -30upx;
			margin-top: -30upx;
			width: 60upx;
			height: 60upx;
		}
	}

	.bottom {
		margin-top: 20upx;
		font-size: 26upx;
		color: #aaa;
	}

	.time {
		margin-left: 20rpx;
	}

	.seeNumber {
		margin-left: 20rpx;
	}

	.imgBox {
		margin-right: 4rpx;
		flex: 1;

	}
</style>
