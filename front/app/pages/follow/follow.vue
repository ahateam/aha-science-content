<template>
	<view class="body">
		<navBar bgColor="#FB7299" :back="false" fontColor="#FFF">推荐</navBar>
		<!-- 顶部选项卡 -->
		<scroll-view id="nav-bar" class="nav-bar" scroll-x scroll-with-animation :scroll-left="scrollLeft">
			<view v-for="(item,index) in tagsList" :key="index" class="nav-item" :class="{current: index === tabCurrentIndex}"
			 :id="'tab'+index" @click="changeTag(index)">{{item.name}}</view>
		</scroll-view>
		<view style="padding-top: 90upx;"></view>

		<scroll-view scroll-x class="userList" v-if="tabCurrentIndex == 0">
			<view v-if="userList.length == 0" class="noUser">
				还没有关注其他用户哦
			</view>
			<view class="item-user" v-for="(item,index) in userList" :key="index">
				<image :src="item.user.head" mode="aspectFill"></image>
				<view class="userName">
					{{item.user.name}}
				</view>
			</view>
		</scroll-view>

		<view v-for="(item,index) in contents" :key="index" @click="navToInfo(item)" :hidden="tabCurrentIndex == 1" style="background-color: #FFFFFF;">
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

		<view :hidden="tabCurrentIndex == 0">
			<view v-for="(item,index) in channelList" :key="index" @click="navChannle(item)">
				<channel :title="item.title" :imgSrc="item.img" :text="item.info"></channel>
			</view>
		</view>

		<uni-load-more :status="pageStatus"></uni-load-more>
	</view>
</template>

<script>
	import transVideo from '@/components/video/transVideo.vue'
	import onlyText from '@/components/article/onlyText.vue'
	import rightVideo from '@/components/video/rightVideo.vue'
	import threeImg from '@/components/article/threeImg.vue'
	import channel from '@/components/channel/channel.vue'

	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'
	import navBar from '@/components/zhouWei-navBar/index.vue'

	let windowWidth = 0

	export default {
		components: {
			transVideo,
			onlyText,
			rightVideo,
			threeImg,
			uniLoadMore,
			navBar,
			channel
		},
		data() {
			return {

				constData: this.$constData, //全局变量引入，防止头条html中报错

				tagsList: [{
						name: '关注列表',
						pageOver: false,
						pageStatus: 'loading'
					},
					{
						name: '推荐专题',
						pageOver: false,
						pageStatus: 'loading'
					}
				], //标签列表
				tabCurrentIndex: 0, //当前选项卡索引
				scrollLeft: 0, //顶部选项卡左滑距离,

				userId: '',

				contents: [], //显示列表
				channelList: [],


				//上拉加载 ---分页
				offset: 0,
				count: 10,
				page: 1,

				pageStatus: 'loading', //加载状态 more（loading前）、loading（loading中）、noMore（没有更多了）

				userList: [],
			}
		},
		onLoad() {
			windowWidth = uni.getSystemInfoSync().windowWidth

			let cnt = {
				moduleId: this.$constData.module, // String 模块编号
				userId: uni.getStorageSync('userId'), // Long 用户id
				count: this.count, // int 
				offset: this.offset, // int 
			}
			this.getAUserFavorite(cnt)
			this.getFavoriteUser(cnt)
		},
		methods: {
			//获取专题
			getChannels(cnt) {
				let index = this.tabCurrentIndex
				this.$api.getChannel(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let list = this.$util.tryParseJson(res.data.c)
						for (let i = 0; i < list.length; i++) {
							let data = this.$util.tryParseJson(list[i].data)
							list[i].info = data.info
							list[i].img = data.img
						}
						console.log(list)

						if (list.length < this.count) { //判断长度是否为等于设定this.count，是则可能还有剩余数据，否则无
							this.tagsList[index].pageOver = true //结束拉取
							this.tagsList[index].pageStatus = 'nomore'
						} else {
							this.tagsList[index].pageOver = false
							this.tagsList[index].pageStatus = 'more'
						}

						this.pageStatus = this.tagsList[index].pageStatus //改变'uni-load-more'组件的状态

						let arr = this.channelList.concat(list)
						this.tagsList[index].child = arr
						this.channelList = arr

					} else {
						console.log('error')
						uni.stopPullDownRefresh()
					}
				})
			},

			//获取关注的用户列表
			getFavoriteUser(cnt) {
				this.$api.getFavoriteUser(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.userList = this.$util.tryParseJson(res.data.c)
						console.log(this.userList)
					} else {
						this.userList = []
					}
				})
			},

			//获取关注内容列表
			getAUserFavorite(cnt) {
				let index = this.tabCurrentIndex
				if (this.tagsList[index].pageOver === true) {
					return
				}
				this.pageStatus = 'loading'

				this.$api.getAUserFavorite(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let list = this.$util.tryParseJson(res.data.c)
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
						uni.stopPullDownRefresh()
					} else {
						console.log('error')
					}
				})
			},

			/* 添加新数据进数组并显示 */
			tryDataList(list) {
				let index = this.tabCurrentIndex
				if (list.length < this.count) { //判断长度是否为等于设定this.count，是则可能还有剩余数据，否则无
					this.tagsList[index].pageOver = true //结束拉取
					this.tagsList[index].pageStatus = 'nomore'
				} else {
					this.tagsList[index].pageOver = false
					this.tagsList[index].pageStatus = 'more'
				}

				this.pageStatus = this.tagsList[index].pageStatus //改变'uni-load-more'组件的状态

				let arr = this.contents.concat(list)
				this.contents = arr
				console.log(this.contents)
				let obj = this.$util.tryParseJson(JSON.stringify(this.tagsList[index]))
				obj.child = arr
				this.$nextTick(function() {
					this.tagsList.splice(index, 1, obj)
				})
			},



			/* 触发改变选中标签*/
			async changeTag(_index) {
				this.tabCurrentIndex = _index
				this.page = this.tagsList[_index].page

				let width = 0;
				let nowWidth = 0;
				//获取可滑动总宽度
				for (let i = 0; i <= _index; i++) {
					let result = await this.getElSize('tab' + _index)
					width += result.width
					if (i === _index) {
						nowWidth = result.width
					}
				}
				if (width - nowWidth / 2 > windowWidth / 2) {
					//如果当前项越过中心点，将其放在屏幕中心
					this.scrollLeft = width - nowWidth / 2 - windowWidth / 2;
				} else {
					this.scrollLeft = 0;
				}
				//滑动end

				//如加载过则读取当前数组
				if (undefined != this.tagsList[_index].child) {
					this.pageStatus = this.tagsList[_index].pageStatus
					this.contents = this.tagsList[_index].child
					console.log(this.contents)
					return
				}
				//读取end

				//点击推荐专题
				if (_index == 1) {
					let cnt = {
						module: this.$constData.module, // Long 模块编号
						// status: this.$constData.tagStatus[1].key, // Byte <选填> 状态
						// tags: tags, // String <选填> 标签（json）
						count: this.count, // int 
						offset: this.offset, // int 
					}
					this.getChannels(cnt)
					return
				}
				//推荐专题end
			},
			
			//跳转至专题详情
			navChannle(item){
				uni.navigateTo({
					url:`/pages/follow/channel/channel?id=${item.id}&title=${item.title}`
				})
			},

			/* 跳转至详情 */
			navToInfo(info) {
				if (info.type == this.constData.contentType[2].key || info.type == this.constData.contentType[0].key) {
					if (info.upChannelId) {
						uni.navigateTo({
							url: `/pages/vip/column/details/details?id=${info.id}`
						})
					} else {
						uni.navigateTo({
							url: `/pages/index/articleView/articleView?id=${info.id}`
						})
					}

				} else if (info.type == this.constData.contentType[1].key) {
					// uni.navigateTo({
					// 	url: `/pages/index/videoView/videoView?id=${info.id}&id1=${info._id}`
					// })
					if (info.upChannelId) {
						uni.navigateTo({
							url: `/pages/vip/column/detailsVideo/detailsVideo?id=${info.id}`
						})
					} else {
						uni.navigateTo({
							url: `/pages/index/videoView/videoView?id=${info.id}`
						})
					}
				}
			},

			//获得元素的size
			getElSize(id) {
				return new Promise((res, rej) => {
					let el = uni.createSelectorQuery().select('#' + id);
					el.fields({
						size: true,
						scrollOffset: true,
						rect: true
					}, (data) => {
						res(data);
					}).exec();
				});
			},
		},
		// 下拉刷新
		onPullDownRefresh() {
			this.page = 1
			this.tagsList[this.tabCurrentIndex].page = 1
			this.tagsList[this.tabCurrentIndex].pageOver = false
			let cnt = {
				count: this.count, // int 
				offset: this.offset, // int 
			}
			if (this.tabCurrentIndex == 0) {
				this.contents = []
				cnt.userId = uni.getStorageSync('userId') // Long 用户id
				cnt.moduleId = this.$constData.module // String 模块编号
				this.getAUserFavorite(cnt)
				this.getFavoriteUser(cnt)
			} else if (this.tabCurrentIndex == 1) {
				this.channelList = []
				cnt.module = this.$constData.module
				this.getChannels(cnt)
			}
		},
		//上滑加载更多
		onReachBottom() {
			this.page += 1
			this.tagsList[this.tabCurrentIndex].page = this.page
			let cnt = {
				count: this.count, // Integer
				offset: (this.page - 1) * this.count, // Integer
			}
			if (this.tabCurrentIndex == 0) {
				cnt.moduleId = this.$constData.module // String 模块编号
				cnt.userId = uni.getStorageSync('userId') // Long 用户id
				this.getAUserFavorite(cnt)
			} else if (this.tabCurrentIndex == 1) {
				cnt.module = this.$constData.module
				this.getChannels(cnt)
			}
		},

	}
</script>

<style lang="scss" scoped>
	.body {
		background-color: #f8f8f8;
		min-height: 100vh;
		overflow: hidden;
	}

	/* 顶部tabbar */
	.nav-bar {
		position: fixed;
		z-index: 10;
		height: 90upx;
		white-space: nowrap;
		box-shadow: 0 2upx 8upx rgba(0, 0, 0, .06);
		background-color: #fff;

		.nav-item {
			display: inline-block;
			padding: 0 $box-margin-left;
			height: 90upx;
			text-align: center;
			line-height: 90upx;
			font-size: 30upx;
			color: #303133;
			position: relative;
			transition: .2s;

			&:after {
				content: '';
				width: 0;
				height: 0;
				border-bottom: 4upx solid #fb7299;
				position: absolute;
				left: 50%;
				bottom: 0;
				transform: translateX(-50%);
				transition: .2s;
			}
		}

		.current {
			color: #fb7299;
			font-weight: bold;
			font-size: $list-title-m;
			transform: translateY(5upx);

			&:after {
				width: 50%;
				transform: translateX(-50%) translateY(-7upx);
			}
		}
	}

	.item-box {
		width: 750rpx;
		height: 200rpx;
		background: #fff;
		margin-top: 10rpx;
	}

	.userList {
		white-space: nowrap;
		background-color: #fff;
		margin-bottom: $box-margin-top;

		.item-user {
			display: inline-block;
			padding: $box-margin-top $box-margin-left;
			width: 100upx;
			text-align: center;
			font-size: 30upx;
			color: #303133;

			image {
				width: 100upx;
				height: 100upx;
				border-radius: 100%;
				overflow: hidden;
			}
		}
	}

	.userName {
		font-size: $list-info;
		padding-top: 5upx;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.noUser {
		font-size: $list-info;
		padding: $box-margin-top $box-margin-left;
	}
</style>
