<template>
	<view class="body">
		<view class="imgBox">
			<image :src="imgSrc" mode="aspectFill"></image>
		</view>
		<view class="titleBox">
			{{title}}
		</view>
		<view class="textBox">
			{{text}}
		</view>
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
		width: 100%;
		height: 150upx;
		margin: 20upx 0;
		padding: 0 $box-margin-left;
		box-sizing: border-box;
	}

	.imgBox {
		position: absolute;
		width: 150upx;
		height: 150upx;
		overflow: hidden;

		image {
			width: 150upx;
			height: 150upx;
			border-radius: 10upx;
		}
	}

	.titleBox {
		font-size: $list-title;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
		box-sizing: border-box;
		margin: 20upx;
		margin-left: 170upx;
	}

	.textBox {
		margin-top: 20rpx;
		font-size: $list-info;
		color: $list-info-color;
		padding-left: 170upx;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
		overflow: hidden;
		box-sizing: border-box;
	}
</style>
