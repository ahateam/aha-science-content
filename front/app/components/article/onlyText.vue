<template>
	<view class="body">
		<view class="title">{{title}}</view>
		<view class="bottom">
			<text class="upName">{{upName}}</text>
			<text class="time">{{time}}</text>
		</view>
	</view>
</template>

<script>
	export default {
		props: ['title', 'imgSrc', 'upName', 'time'],
		data() {
			return {

			};
		}
	}
</script>

<style lang="scss" scoped>
	* {
		margin: 0;
	}

	.body {
		position: relative;
		width: 100%;
	}

	.title {
		font-size: $list-title;
		color: $list-title-color;
		line-height: $list-title-line;
		box-sizing: border-box;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2; //需要显示时文本行数
		overflow: hidden;
	}

	.bottom {
		margin-top: 20upx;
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
</style>
