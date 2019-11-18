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

		<view class="autoBox">
			<view style="font-weight: bold;">
				开放时间：
			</view>
			<view style="padding: 10upx 0;text-indent:2em">
				{{time}}
			</view>
		</view>

		<view class="autoBox">
			<view style="font-weight: bold;">
				基地地址：
			</view>
			<view style="padding: 10upx 0;text-indent:2em">
				{{address}}
			</view>
		</view>

		<view class="autoBox">
			<view style="font-weight: bold;">
				相关活动：
			</view>
		</view>
		<view class="active-list" v-for="(item,index) in actList" :key="index" @click="navToAct(item)">
			<right-video :item="item" :upName="item.user.name" :time="getNowTime(item.createTime)"></right-video>
		</view>
		<uni-load-more :status="pageStatus"></uni-load-more>

		<view class="fixBox">
			<button class="signUpBtn" @click="shopBtn">购 票</button>
		</view>
	</view>
</template>

<script>
	import navBar from '@/components/zhouWei-navBar/index.vue'
	import rightVideo from '@/components/video/rightVideo.vue'
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'

	export default {
		components: {
			navBar,
			rightVideo,
			uniLoadMore
		},
		data() {
			return {
				placeTitle: '', //基地标题
				placeInfo: '', //基地简介
				address: '', //基地地址

				time: '',

				id: '',
				imgSrc: '',

				shopSrc: '', //购票地址

				actList: [], //活动列表

				count: 10,
				offset: 0,
				pageOver: false,
				pageStatus: 'loading'
			}
		},
		onLoad(res) {
			this.id = res.id
			let cnt = {
				count: 10,
				offset: 0,
				moduleId: this.$constData.module, // Long 模块编号
				id: this.id, // Long id
			}
			this.getTourBase(cnt)
		},
		methods: {
			getNowTime(time) {
				let newTime = new Date(time)
				let y = newTime.getFullYear()
				let m = newTime.getMonth() * 1 + 1
				let d = newTime.getDate()
				return `${y}-${m}-${d}`
			},

			navToAct(item) {
				uni.redirectTo({
					url: `/pages/index/activity/activity?contentId=${item.id}`
				})
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
						let info = this.$util.tryParseJson(res.data.c).TourBases
						console.log(info)
						let data = this.$util.tryParseJson(info.data)
						this.placeInfo = data.info
						this.placeTitle = info.name
						this.imgSrc = data.img[0]
						this.shopSrc = info.buyTicketsLink
						this.time = data.workTime
						this.address = info.address

						let actList = this.$util.tryParseJson(res.data.c).content
						console.log(actList)
						this.tryParseData(actList)
					} else {
						console.log('error')
					}
				})
			},

			tryParseData(list) {
				if (list.length < this.count) {
					this.pageOver = true
					this.pageStatus = 'nomore'
				} else {
					this.pageOver = false
					this.pageStatus = 'more'
				}
				for (let i = 0; i < list.length; i++) {
					list[i].imgList = this.$util.tryParseJson(list[i].data).imgList
				}
				this.actList = this.actList.concat(list)

			},

			newTime(time) {
				let newDate = new Date(time)
				let y = newDate.getFullYear()
				let m = newDate.getMonth() * 1 + 1
				let d = newDate.getDate()
				return `${y}-${m}-${d}`
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
		background-color: rgba($color: $color-main, $alpha: 0.5);
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

	.fixBox {
		position: fixed;
		bottom: 0upx;
		width: 100%;
		padding: 10upx 0;
		background-color: rgba($color: #FFFFFF, $alpha: 1);
	}

	.signUpBtn {
		box-sizing: border-box;
		line-height: 80rpx;
		background: linear-gradient(to right, rgba(251, 114, 153, 0.7), rgba(251, 114, 153, 0.6));
		color: #FFFFFF;
		font-size: 30rpx;
		border-radius: 2.5rem;
		text-align: center;
		width: 6em;
	}

	.autoBox {
		font-size: $list-title;
		padding: 24upx 30upx;
	}

	.active-list {
		padding: $box-margin-top $box-margin-left;
	}
</style>
