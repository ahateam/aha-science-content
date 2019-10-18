<template>
	<view style="padding-bottom: 120upx;">
		<navBar type="transparentFixed" transparentFixedFontColor="#FFF" :title="activityTitle"></navBar>
		<view class="imgBox">
			<image :src="imgSrc" mode="aspectFill"></image>
			<view class="titleBox">
				{{placeTitle}}
			</view>
		</view>
		<view class="placeInfo">
			<rich-text :nodes="placeInfo"></rich-text>
		</view>


		<view class="autoTitle">
			活动介绍： <view class="liveBtn" @click="navLive">
				<text class="iconfont kk-zhibobofangshexiangjitianxianxianxing"></text>
			</view>
			<view class="autoInfo" style="text-indent: 2em;">
				{{activityInfo}}
			</view>
		</view>

		<view class="autoTitle">
			活动地点：
			<view class="autoInfo">
				{{address}}
			</view>
		</view>

		<view class="autoTitle">
			活动时间：
			<view class="autoInfo">
				{{time}}
			</view>
		</view>

		<view class="fixBox">
			<view class="leftBox"><button class="signUpBtn shopBtn" type="primary" @click="shopBtn">购票</button></view>
			<view class="rightBox"><button class="signUpBtn" type="primary" @click="signUp">报名</button></view>
		</view>


	</view>
</template>

<script>
	import navBar from '@/components/zhouWei-navBar/index.vue'
	import wButton from '@/components/watch-login/watch-button.vue' //button

	export default {
		components: {
			navBar,
			wButton,

		},
		data() {
			return {
				contentId: '', //活动id
				placeId: '', //基地id

				imgSrc: '', //基地图片
				placeTitle: '', //基地标题
				placeInfo: '', //基地简介
				
				activityTitle:'',//活动标题
				activityInfo: '', //活动简介
				address: '', //活动地址
				time: '', //活动时间

				isRotate: 'false',

				liveSrc: '', //直播地址
				shopSrc: '', //购票地址
			}
		},
		onLoad(res) {
			this.contentId = res.contentId
			let cnt = {
				id: this.contentId, // String 内容编号
			}
			this.getContent(cnt)
		},
		methods: {
			navLive() {
				if (this.liveSrc) {
					let key = this.liveSrc.indexOf('http://')
					let key1 = this.liveSrc.indexOf('https://')
					if (key != -1 || key1 != -1) {
						plus.runtime.openURL(this.liveSrc)
					} else {
						uni.showToast({
							title: '直播地址错误',
							icon: 'none'
						})
					}

				} else {
					uni.showToast({
						title: '暂无直播地址',
						icon: 'none'
					})
				}
			},

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
			},

			datetime_to_unix(datetime) { //能转换的格式'yyyy-mm-dd hh:mm:ss'
				let tmp_datetime = datetime.replace(/:/g, '-')
				tmp_datetime = tmp_datetime.replace(/ /g, '-')
				let arr = tmp_datetime.split("-")
				let now = new Date(Date.UTC(arr[0], arr[1] - 1, arr[2], arr[3] - 8, arr[4], arr[5]))
				return parseInt(now.getTime() / 1000)
			},

			//获取活动详情
			getContent(cnt) {
				this.$api.getContentById(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let info = this.$util.tryParseJson(res.data.c)
						console.log(info)
						let data = this.$util.tryParseJson(info.data)
						this.address = data.address
						this.time = data.time
						this.activityInfo = data.info
						this.activityTitle = info.title

						this.placeId = data.place

						this.liveSrc = data.live
						let cnt1 = {
							moduleId: this.$constData.module, // Long 模块编号
							id: this.placeId, // Long id
						}
						this.getTourBase(cnt1)
					} else {
						console.log('Error')
					}
				})
			},

			//跳转报名
			signUp() {
				uni.navigateTo({
					url: `/pages/index/activity/signUp?id=${this.contentId}&title=${this.activityTitle}`
				})
			},
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
		background-color: rgba($color: $color-main, $alpha: 0.6);
		padding: $box-margin-top $box-margin-left;
		color: #FFFFFF;
		font-size: $list-title-l;
		font-weight: bold;
	}

	.placeInfo {
		box-sizing: border-box;
		padding: $box-margin-top $box-margin-left;
		font-size: $list-title;
		text-indent: 2em;
	}

	.autoTitle {
		position: relative;
		box-sizing: border-box;
		padding: $box-margin-top $box-margin-left;
		font-size: $list-title;
		font-weight: bold;
	}

	.autoInfo {
		box-sizing: border-box;
		padding: $box-margin-top $box-margin-left 0;
		font-size: $list-title;
		font-weight: normal;
	}

	.signUpBtn {
		margin: 0 37.5upx;
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

	.fixBox {
		position: fixed;
		bottom: 0upx;
		width: 100%;
		padding-top: 10upx;
		background-color: rgba($color: #FFFFFF, $alpha: 1);
	}

	.shopBtn {
		background: linear-gradient(to right, rgba(255, 179, 89, 0.7), rgba(255, 179, 89, 0.6));
	}

	.leftBox,
	.rightBox {
		display: inline-block;
	}

	.leftBox {
		.button-hover {
			background: linear-gradient(to right, rgba(255, 179, 89, 0.9), rgba(255, 179, 89, 0.8));
		}
	}

	.rightBox {
		.button-hover {
			background: linear-gradient(to right, rgba(251, 114, 153, 0.9), rgba(251, 114, 153, 0.8));
		}
	}

	.liveBtn {
		position: absolute;
		top: $box-margin-top;
		left: 5.5em;
		color: $color-main;
		// background-color: $color-main;
		border-radius: 100%;
		text-align: center;
		width: 60upx;

		text {
			padding: 0 40upx;
			font-size: 40upx;
		}
	}
</style>
