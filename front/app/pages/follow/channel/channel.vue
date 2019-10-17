<template>
	<view>
		<navBar type="transparentFixed" fontColor="#FFF">{{title}}</navBar>
		<view class="topBox">
			<image :src="channelInfo.data.img" mode="aspectFill"></image>
			<view class="block"></view>
			<view class="infoBox">
				{{channelInfo.data.info}}
			</view>
		</view>
		<view class="contentBox">
			<view v-for="(item,index) in contents" :key="index" @click="navToInfo(item)">

				<view v-if="item.type == constData.contentType[1].key||item.type == constData.contentType[2].key||item.type == constData.contentType[3].key">
					<view v-if="item.show == constData.contentShow[0].key">
						<trans-video :title="item.title" :upName="item.user.name" :imgSrc="item.imgList[0].src" :time="item.time" :type="item.type"></trans-video>
					</view>

					<view v-else-if="item.show == constData.contentShow[1].key">
						<right-video :title="item.title" :upName="item.user.name" :imgSrc="item.imgList[0].src" :time="item.time" :type="item.type"></right-video>
					</view>

					<view v-else-if="item.show == constData.contentShow[2].key&&item.type == constData.contentType[2].key">
						<three-img :title="item.title" :upName="item.user.name" :imgList="item.imgList" :time="item.time" :type="item.type"></three-img>
					</view>
				</view>

				<view v-else-if="item.type == constData.contentType[0].key">
					<only-text :title="item.title" :upName="item.user.name" :time="item.time"></only-text>
				</view>
			</view>
			
			<uni-load-more :status="pageStatus"></uni-load-more>
		</view>
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
			navBar,
			threeImg,
			rightVideo,
			onlyText,
			transVideo,
			uniLoadMore
		},
		data() {
			return {
				title: '',
				id: 0,
				constData:this.$constData,

				channelInfo: {},
				contents: [],

				count: 10,
				offset: 0,
				page: 0,
				
				pageStatus:'loading',
				pageOver:false,

				imgSrc: '',
			}
		},
		onLoad(res) {
			this.title = res.title
			this.id = res.id

			let cnt = {
				module: this.$constData.module, // String 所属模块
				status: parseInt(this.$constData.contentStatus[4].key),
				upChannelId: this.id,
				count: this.count,
				offset: this.offset,
				power: this.$constData.contentPaid[0].key
			}
			this.getContents(cnt)

			let cnt1 = {
				id: this.id
			}
			this.getChannlById(cnt1)
		},
		methods: {
			
			// 获取专题
			getChannlById(cnt) {
				this.$api.getChannlById(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let obj = this.$util.tryParseJson(res.data.c)
						console.log(obj)
						obj.data = this.$util.tryParseJson(obj.data)
						this.channelInfo = obj
					} else {
						console.log('error')
					}
				})
			},

			//获取内容列表
			getContents(cnt) {
				let list = []
				this.$api.getContents(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						list = this.$util.tryParseJson(res.data.c)
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

					} else {
						console.log('error')
					}
				})
			},
			
			/* 添加新数据进数组并显示 */
			tryDataList(list) {
				let index = this.tabCurrentIndex
				if (list.length < this.count) { //判断长度是否为等于设定this.count，是则可能还有剩余数据，否则无
					this.pageStatus = true //结束拉取
					this.pageStatus = 'nomore'
				} else {
					this.pageStatus = false
					this.pageStatus = 'more'
				}
			
				let arr = this.contents.concat(list)
				this.contents = arr
				console.log(this.contents)
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
						url: `/pages/index/activity/activity?contentId=${info.id}`
					})
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.topBox {
		width: 100vw;
		height: 45vw;

		image {
			width: 100%;
			height: 100%;
		}
	}

	.block {
		position: absolute;
		top: 0;
		background-color: rgba($color: $color-main, $alpha: 0.4);
		width: 100vw;
		height: 45vw;
	}

	.infoBox {
		position: absolute;
		width: 100%;
		text-align: center;
		top: 64px;
		color: $color-button-back;
		font-size: $list-title;
	}
</style>
