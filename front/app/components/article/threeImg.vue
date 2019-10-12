<template>
	<view class="body">
		<text class="title">{{title}}</text>
		<view class="imgList">
			<view class="imgBox" v-for="(item,index) in imgList" :key="index" v-if="index < 3">
				<image :src="item.src" mode="scaleToFill"></image>
			</view>
		</view>
		<view>
			<text class="upName">{{upName}}</text>
			<text class="time">{{time}}</text>
		</view>
	</view>
</template>

<script>
	export default {
		props: ['title', 'imgList', 'upName', 'time', 'type'],
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
		padding: $box-margin-top $box-margin-left;
		width: 750upx;
		box-sizing: border-box;
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

	.upName {
		font-size: 26upx;
		color: #aaa;
	}

	.time {
		font-size: 26rpx;
		color: #aaa;
		margin-left: 20rpx;
	}

	.imgBox {
		margin-right: 4rpx;
		flex: 1;
		
	}
</style>
