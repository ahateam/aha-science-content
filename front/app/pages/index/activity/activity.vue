<template>
	<view>
		<navBar type="transparent" fontColor="#FFF"></navBar>
		<view class="imgBox">
			<image :src="imgSrc" mode="aspectFill"></image>
			<view class="titleBox">
				{{placeTitle}}
			</view>
		</view>
		<view class="placeInfo">
			{{placeInfo}}
		</view>
		
		<view class="autoTitle">
			活动介绍：
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
			<button class="signUpBtn" type="primary" @click="signUp">报名</button>
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
				
				imgSrc: '',//基地图片
				placeTitle: '',//基地标题
				placeInfo: '',//基地简介
				
				activityInfo:'',//活动简介
				address: '',//活动地址
				time: '',//活动时间

				isRotate: 'false'
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
						this.imgSrc = data.img
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
					url:`/pages/index/activity/signUp?id=${this.contentId}&title=${this.activityTitle}`
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
		background-color: rgba($color: #000000, $alpha: 0.5);
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
		display: block;
		margin: 0 auto;
		padding-left: 14px;
		padding-right: 14px;
		box-sizing: border-box;
		line-height: 100rpx;
		text-align: center;
		background: linear-gradient(to right, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.6));
		color: #FFFFFF;
		font-size: 30rpx;
		width: 601rpx;
		height: 100rpx;
		border-radius: 2.5rem;
	}

	.fixBox {
		position: fixed;
		bottom: 20upx;
		width: 100%;
	}
</style>
