<template>
	<view style="padding-bottom: 120upx;">
		<navBar type="transparentFixed" transparentFixedFontColor="#FFF" :title="title"></navBar>
		<view class="imgBox">
			<image :src="imgSrc" mode="aspectFill"></image>
			<view class="titleBox">
				{{placeTitle}}
			</view>
		</view>
		<view class="placeInfo">
			<rich-text :nodes="placeInfo"></rich-text>
		</view>

		<view class="fixBox">
			<button class="signUpBtn" @click="shopBtn">购	票</button>
		</view>
	</view>
</template>

<script>
	import navBar from '@/components/zhouWei-navBar/index.vue'

	export default {
		components: {
			navBar
		},
		data() {
			return {
				placeTitle: '', //基地标题
				placeInfo: '', //基地简介

				id: '',
				imgSrc: '',

				shopSrc: '', //购票地址
			}
		},
		onLoad(res) {
			this.id = res.id
			let cnt = {
				moduleId: this.$constData.module, // Long 模块编号
				id: this.id, // Long id
			}
			this.getTourBase(cnt)
		},
		methods: {
			//购票
			shopBtn() {
				if (this.shopSrc) {
					let key = this.shopSrc.indexOf('http://')
					let key1 = this.shopSrc.indexOf('https://')
					if (key != -1 || key1 != -1) {
						plus.runtime.openURL(this.shopSrc)
					} else {
						uni.showToast({
							title: '直播地址错误',
							icon: 'none'
						})
					}
				} else {
					uni.showToast({
						title: '暂无购票地址',
						icon: 'none'
					})
				}
			},
			
			//获取基地详情
			getTourBase(cnt) {
				this.$api.getTourBase(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						console.log('基地详情------------------------↓')
						let info = this.$util.tryParseJson(res.data.c)
						console.log(info)
						let data = this.$util.tryParseJson(info.data)
						this.placeInfo = data.info
						this.placeTitle = info.name
						this.imgSrc = data.img[0]
						this.shopSrc = info.buyTicketsLink
					} else {
						console.log('error')
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.imgBox {
		position: relative;
		width: 100vw;
		height: 56.25vw;

		image {
			width: 100%;
			height: 100%;
		}
	}

	.titleBox {
		position: absolute;
		bottom: 0;
		box-sizing: border-box;
		width: 100%;
		background-color: rgba($color: $color-main, $alpha: 0.5);
		padding: $box-margin-top $box-margin-left;
		color: #FFFFFF;
		font-size: $list-title-l;
		font-weight: bold;
	}

	.placeInfo {
		box-sizing: border-box;
		text-align: center;
		padding: $box-margin-top $box-margin-left;
		font-size: $list-title;
		text-indent: 2em;
	}
	
	.fixBox {
		position: fixed;
		bottom: 0upx;
		width: 100%;
		padding: 10upx 0;
		background-color: rgba($color: #FFFFFF, $alpha: 1);
	}
	
	.signUpBtn {
		padding-left: 14px;
		padding-right: 14px;
		box-sizing: border-box;
		line-height: 100rpx;
		background: linear-gradient(to right, rgba(251, 114, 153, 0.7), rgba(251, 114, 153, 0.6));
		color: #FFFFFF;
		font-size: 30rpx;
		width: 300rpx;
		height: 100rpx;
		border-radius: 2.5rem;
		text-align: center;
	}
</style>
