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
		padding: $box-margin-top $box-margin-left;
		min-height: calc(100upx + 48upx);
		box-sizing: border-box;
	}

	.imgBox {
		position: absolute;
		top: $box-margin-top;
		left: $box-margin-left;
		width: 100rpx;
		height: 100rpx;

		image {
			width: 100%;
			height: 100%;
			border-radius: 10upx;
			overflow: hidden;
		}
	}

	.titleBox {
		font-size: $list-title;
		padding-left: 110upx;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
		box-sizing: border-box;
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
