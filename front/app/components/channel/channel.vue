<template>
	<view class="body">
		<view class="imgBox">
			<image :src="imgSrc" mode="aspectFill"></image>
		</view>
		<view class="titleBox">
			{{title}}
		</view>
		<!-- <view class="textBox">
			{{text}}
		</view> -->
	</view>
</template>

<script>
	export default {
		props: ['title', 'imgSrc', 'text'],
		data() {
			return {

			}
		}
	}
</script>

<style lang="scss" scoped>
	.body {
		position: relative;
		box-sizing: border-box;
		padding: 20upx 0;
		box-sizing: border-box;
		margin-left: 26.25upx;
		text-align: center;
	}

	.imgBox {
		width: 180upx;
		height: 180upx;
		margin: 0 auto;
		
		image {
			width: 100%;
			height: 100%;
			border-radius: 10upx;
			overflow: hidden;
		}
	}

	.titleBox {
		font-size: $list-title;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
		box-sizing: border-box;
		width: 215upx;
		text-align: center;
		margin-top: 20upx;
	}

	.textBox {
		margin-top: 20rpx;
		font-size: $list-info;
		color: $list-info-color;
		padding-left: 110upx;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
		box-sizing: border-box;
	}
</style>
