<template>
	<view style="padding-bottom: 120upx;">
		<navBar type="transparentFixed" transparentFixedFontColor="#FFF" :title="activityTitle"></navBar>
		<view class="imgBox" @click="navToPlace">
			<image :src="imgSrc" mode="aspectFill"></image>
			<view class="titleBox">
				{{placeTitle}}
			</view>
		</view>
		<view class="placeInfo">
			*点击图片查看相关基地详情哦*
		</view>
		<!-- <view class="placeInfo">
			<rich-text :nodes="placeInfo"></rich-text>
		</view> -->


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
			<button class="signUpBtn" type="primary" @click="signUp" v-if="shopStatus == false">报 名</button>
			<button class="signUpBtn cntShop" type="primary" @click="signUp" v-if="shopStatus == true">报 名</button>
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

				activityTitle: '', //活动标题
				activityInfo: '', //活动简介
				address: '', //活动地址
				time: '', //活动时间

				isRotate: 'false',

				liveSrc: '', //直播地址
				
				shopStatus:true,
			}
		},
		onLoad(res) {
			this.contentId = res.contentId
			let cnt = {
				id: this.contentId, // String 内容编号
			}
			this.getContent(cnt)

			this.getEnrolls()
		},
		methods: {
			//获取报名
			getEnrolls() {
				let cnt = {
					moduleId: this.$constData.module, // Long 模块编号
					userId: uni.getStorageSync('userId'), // Long <选填> 用户id
					contenId: this.contentId, // Long <选填> 内容id
					count: 10, // int 
					offset: 0, // int
				}
				this.$api.getEnrolls(cnt,(res)=>{
					if(res.data.rc == this.$util.RC.SUCCESS){
						let list = this.$util.tryParseJson(res.data.c)
						if(list.length == 0){
							this.shopStatus = false
						}else{
							this.shopStatus = true
						}
					}else{
						console.log('error')
					}
				})
			},

			//跳转基地
			navToPlace() {
				uni.navigateTo({
					url: `/pages/index/tourBases/tourBases?id=${this.placeId}`
				})
			},

			//直播
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
							title: '购票地址错误',
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
				if(this.shopStatus == true){
					uni.showToast({
						title: '您已报名',
						icon: 'none'
					})
					return
				}
				
				let user = uni.getStorageSync('userId')
				if (user == '' || user == '1234567890') {
					uni.showToast({
						title: '登录后可报名',
						icon: 'none'
					})
				} else {
					uni.navigateTo({
						url: `/pages/index/activity/signUp?id=${this.contentId}&title=${this.activityTitle}`
					})
				}
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

	// .placeInfo {
	// 	box-sizing: border-box;
	// 	padding: $box-margin-top $box-margin-left;
	// 	font-size: $list-title;
	// 	text-indent: 2em;
	// }

	.placeInfo {
		padding: 0 $box-margin-left;
		font-size: $list-info;
		color: #AAAAAA;
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
		padding-left: 14px;
		padding-right: 14px;
		box-sizing: border-box;
		line-height: 100rpx;
		background: linear-gradient(to right, rgba(251, 114, 153, 0.7), rgba(251, 114, 153, 0.6));
		color: #FFFFFF;
		font-size: 30rpx;
		width: 600rpx;
		height: 100rpx;
		border-radius: 2.5rem;
		text-align: center;
	}

	.fixBox {
		position: fixed;
		padding-bottom: 10upx;
		bottom: 0upx;
		width: 100%;
		padding-top: 10upx;
		background-color: rgba($color: #FFFFFF, $alpha: 1);
		text-align: center;
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
	
	.cntShop{
		background: linear-gradient(to right, rgba($color: #AAAAAA, $alpha: 0.4), rgba($color: #AAAAAA, $alpha: 0.3));
	}
</style>
