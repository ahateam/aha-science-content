<template>
	<view style="background-color: #f8f8f8;min-height: 100vh;">
		<!-- 导航栏 -->
		<navBar bgColor="#FB7299" fontColor="#FFFFFF">
			<view class="search" @click="navBack">
				<!-- #ifdef APP-PLUS -->
				<image src="/static/zy-search/voice.png" mode="aspectFit" class="voice-icon"></image>
				<!-- #endif -->
				<input type="text" :value="searchText" readonly="readonly" />
				<image src="/static/zy-search/search.png" mode="aspectFit" class="search-icon"></image>
			</view>
		</navBar>

		<!-- 搜索内容列表 -->
		<view v-for="(item,index) in contents" :key="index" @click="navToInfo(item)" class="listBox">
			<right-video :item="item" :upName="item.address" :time="item.margin+'km'"></right-video>
		</view>

		<uni-load-more :status="pageStatus"></uni-load-more>
	</view>
</template>

<script>
	import navBar from '@/components/zhouWei-navBar/index.vue'
	import transVideo from '@/components/video/transVideo.vue'
	import onlyText from '@/components/article/onlyText.vue'
	import rightVideo from '@/components/video/rightVideo.vue'
	import threeImg from '@/components/article/threeImg.vue'
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'

	export default {
		components: {
			uniLoadMore,
			threeImg,
			rightVideo,
			onlyText,
			transVideo,
			navBar
		},
		data() {
			return {
				page: 1,
				count: 10,
				offset: 0,
				pageOver: false,

				constData: this.$constData,
				contents: [],

				searchText: '',

				pageStatus: 'loading',
				location: '',
			};
		},
		onLoad(res) {
			this.searchText = res.value
			this.getLocation()
		},
		methods: {
			getLocation() {
				uni.getLocation({
					type: 'BD09',
					geocode: true,
					success: (res) => {
						console.log(res)
						this.location = `${res.latitude},${res.longitude}`
						let cnt = {
							moduleId: this.$constData.module, // Long 模块编号
							search: this.searchText, // String 关键字
							userCoordinate: this.location, // String 用户定位经纬度
							count: this.count, // int 
							offset: this.offset, // int 
						}
						this.searchTourBase(cnt)
					},
					fail: (err) => {
						console.log('错误!————:' + err)
					}
				})
			},

			getMargin(code) {
				let arr1 = this.location.split(',')
				let arr2 = code.split(',')

				let lat1 = arr1[0]
				let lat2 = arr2[0]

				let lng1 = arr1[1]
				let lng2 = arr2[1]

				let radLat1 = lat1 * Math.PI / 180.0;
				let radLat2 = lat2 * Math.PI / 180.0;
				let a = radLat1 - radLat2;
				let b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
				let s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
					Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
				s = s * 6378.137; // EARTH_RADIUS;
				s = Math.round(s * 10000) / 10000;
				return s.toFixed(2)
			},

			//跳转对应用户个人中心
			navToUser(item) {
				uni.navigateTo({
					url: `/pages/user/userSpace/userSpace?id=${item.id}`
				})
			},

			//查询
			searchTourBase(cnt) {
				this.pageStatus = 'loading'
				this.$api.searchTourBase(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let searchs = this.$util.tryParseJson(res.data.c)
						this.setContentList(searchs)
					} else {
						console.log('error')
					}
				})
			},

			//内容列表更改
			setContentList(list) {
				for (let i = 0; i < list.length; i++) {
					list[i].title = list[i].name
					list[i].type = -1
					list[i].show = Math.round(Math.random())
					let time = new Date(list[i].createTime)
					let y = time.getFullYear()
					let m = 1 * time.getMonth() + 1
					let d = time.getDate()
					list[i].time = `${y}-${m}-${d}`

					let data = this.$util.tryParseJson(list[i].data)
					list[i].imgList = [{
						src: data.img[0]
					}]

					list[i].margin = this.getMargin(list[i].coordinate)
				}
				// console.log(list)
				this.tryDataList(list)
			},

			//添加内容进数组
			tryDataList(list) {
				console.log(list)
				console.log('--------------')
				if (list.length < this.count) {
					this.pageOver = true
					this.pageStatus = 'nomore'
				} else {
					this.pageOver = false
					this.pageStatus = 'more'
				}
				let newList = this.contents.concat(list)
				this.contents = newList
				console.log(this.contents)
			},

			//返回上一页
			navBack() {
				uni.navigateBack({
					"animationType": "none",
				})
			},

			/* 跳转至详情 */
			navToInfo(info) {
				uni.navigateTo({
					url: `/pages/index/tourBases/tourBases?id=${info.id}`
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.search {
		width: 600upx;
		margin: 20upx auto 0;
		position: relative;
		font-size: $list-title;
		top: 10upx;

		input {
			background-color: #F7F7F7;
			padding: 10upx 54upx;
			font-size: 28upx;
			border-radius: 50upx;
			color: #303133;
			font-weight: normal;
		}

		.voice-icon {
			width: 36upx;
			height: 36upx;
			padding: 16upx 20upx 16upx 0;
			position: absolute;
			left: 16upx;
			top: 4upx;
			z-index: 10;
		}

		.search-icon {
			width: 36upx;
			height: 36upx;
			padding: 16upx 20upx 16upx 0;
			position: absolute;
			right: 0;
			top: 4upx;
			z-index: 10;
		}
	}

	.listBox {
		background-color: #FFFFFF;
		padding: $box-margin-top $box-margin-left;
	}
</style>
