<template>
	<view class="body">
		<view class="title">{{title}}</view>
		<view class="bottom">
			<text>{{upName}}</text>
			<text class="time">{{time}}</text>
			<text class="seeNumber">阅读量：{{pageView}}</text>
		</view>
	</view>
</template>

<script>
	export default {
		props: ['title', 'imgSrc', 'upName', 'time', 'pageView'],
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
		font-size: 26upx;
		color: #aaa;
	}

	.time {
		margin-left: 20rpx;
	}

	.seeNumber {
		margin-left: 20rpx;
	}
</style>
