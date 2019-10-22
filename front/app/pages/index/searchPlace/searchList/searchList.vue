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
		<view v-for="(item,index) in contents" :key="index" @click="navToInfo(item)" style="background-color: #FFFFFF;">
			<view v-if="item.type == constData.contentType[1].key||item.type == constData.contentType[2].key">
				<view v-if="item.show == constData.contentShow[0].key">
					<trans-video :title="item.title" :upName="item.user.name" :imgSrc="item.imgList[0].src" time="1小时前" :type="item.type"></trans-video>
				</view>

				<view v-else-if="item.show == constData.contentShow[1].key">
					<right-video :title="item.title" :upName="item.user.name" :imgSrc="item.imgList[0].src" time="1小时前" :type="item.type"></right-video>
				</view>

				<view v-else-if="item.show == constData.contentShow[2].key&&item.type == constData.contentType[2].key">
					<three-img :title="item.title" :upName="item.user.name" :imgList="item.imgList" time="1小时前" :type="item.type"></three-img>
				</view>
			</view>
			<view v-else-if="item.type == constData.contentType[0].key">
				<only-text :title="item.title" :upName="item.user.name" time="1小时前"></only-text>
			</view>
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
				
				pageStatus: 'loading'
			};
		},
		onLoad(res) {
			this.searchText = res.value
			let cnt = {
				moduleId: this.$constData.module, // Long 模块编号
				search: this.searchText, // String 关键字
				count: this.count, // int 
				offset: this.offset, // int 
			}
			this.searchContentAndUser(cnt)
		},
		methods: {
			//跳转对应用户个人中心
			navToUser(item) {
				uni.navigateTo({
					url: `/pages/user/userSpace/userSpace?id=${item.id}`
				})
			},

			//查询
			searchContentAndUser(cnt) {
				this.pageStatus = 'loading'
				this.$api.searchContentAndUser(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let searchs = this.$util.tryParseJson(res.data.c)
						console.log(searchs)
						this.userList = searchs.userSearch

						this.setContentList(searchs.contentSearch)
					} else {
						console.log('error')
					}
				})
			},

			//内容列表更改
			setContentList(list) {
				for (let i = 0; i < list.length; i++) {
					let show = this.$util.tryParseJson(list[i].data).show
					list[i].show = show
					if (list[i].type == this.$constData.contentType[2].key || list[i].type == this.$constData.contentType[3].key) {
						let imgList = this.$util.tryParseJson(list[i].data).imgList
						list[i].imgList = imgList
					}
					if (list[i].type == this.$constData.contentType[1].key) {
						let imgList = [{
							src: this.$util.tryParseJson(list[i].data).imgSrc
						}]
						list[i].imgList = imgList
						// if(this.versionStatus == this.$constData.showStatus[0].key){
						// 	list[i].type = 999
						// }
					}
					let time = new Date(list[i].createTime)
					let y = time.getFullYear()
					let m = 1 * time.getMonth() + 1
					let d = time.getDate()
					list[i].time = `${y}-${m}-${d}`
				}
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
				if (info.type == this.constData.contentType[2].key || info.type == this.constData.contentType[0].key) {
					uni.navigateTo({
						url: `/pages/index/articleView/articleView?id=${info.id}`
					})
				} else if (info.type == this.constData.contentType[1].key) {
					uni.navigateTo({
						url: `/pages/index/videoView/videoView?id=${info.id}`
					})
				} else if (info.type == this.constData.contentType[3].key) {
					uni.navigateTo({
						url: `/pages/index/activity/activity?contentId=${info.id}&placeId=${this.$util.tryParseJson(info.data).place}`
					})
				}
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
</style>
