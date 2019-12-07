<template>
	<view class="body">
		<!-- 自定义导航栏 -->
		<nav-bar bgColor="#fb7299" fontColor="#FFFFFF">
			<view slot="left" class="navBox">
				<view class="cityBox" @click="changeCity">
					<text class="iconfont kk-location"></text>
					<text>{{cityName}}</text>
				</view>
				<view class="search_box" @click="searchBtn">
					<text class="iconfont kk-sousuo"></text>
					<text class="prompt">{{searchTitle}}</text>
				</view>
			</view>
		</nav-bar>

		<!-- 顶部选项卡  scroll scroll-x scroll-with-animation :scroll-left="scrollLeft" -->
		<view id="nav-bar" class="nav-bar">
			<view v-for="(item,index) in tagsList" :key="index" class="nav-item" :class="{current: index === tabCurrentIndex}"
			 :id="'tab'+index" @click="changeTag(index)">{{item.name}}</view>
		</view>
		<view style="padding-top: 90upx;"></view>

		<!-- 广告轮播图 -->
		<uni-swiper-dot :info="info" :current="current" field="remark" :mode="mode" :dotsStyles="dotsStyles" v-if="info.length > 1&&tagName !='科普基地'">
			<swiper class="swiper-box" @change="change" autoplay>
				<swiper-item v-for="(item ,index) in info" :key="index" @click="navInfo(item.linkSrc)">
					<image style="width: 100%;height: 100%;" :src="item.imgSrc" mode="aspectFill"></image>
				</swiper-item>
			</swiper>
		</uni-swiper-dot>

		<!-- 专题列表 -->
		<view class="channleView" v-if="tagName == '科普栏目'||tagName == '专题页面'" :style="tagName == '专题页面'?'display:block':''">
			<view v-for="(item,index) in channelList" :key="index" @click="navChannle(item)" v-if="item.type == 0">
				<channel :title="item.title" :imgSrc="item.img" :text="item.info"></channel>
			</view>
			<view v-for="(item,index) in channelList" :key="index" @click="navChannle(item)" v-if="item.type == 1">
				<special :title="item.title" :imgSrc="item.img" :text="item.info"></special>
			</view>
		</view>

		<!-- 内容列表 -->
		<view class="contentBox" v-for="(item,index) in contents" :key="index" @click="navToInfo(item)" :hidden="tagName == '科普栏目'||tagName == '专题页面'">
			<view v-if="item.type == constData.contentType[1].key||item.type == constData.contentType[2].key||item.type == constData.contentType[3].key">
				<view v-if="item.show == constData.contentShow[0].key">
					<trans-video :item="item"></trans-video>
				</view>

				<view v-else-if="item.show == constData.contentShow[1].key">
					<right-video :item="item" :upName="item.user.name" :time="item.time"></right-video>
				</view>

				<view v-else-if="item.show == constData.contentShow[2].key&&item.type == constData.contentType[2].key">
					<three-img :item="item"></three-img>
				</view>
			</view>

			<view v-else-if="item.type == constData.contentType[0].key">
				<only-text :item="item"></only-text>
			</view>

			<view v-else-if="item.type == -1">
				<right-video :item="item" :upName="item.address" :time="item.margin+'km'"></right-video>
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
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'
	import uniSwiperDot from "@/components/uni-swiper-dot/uni-swiper-dot.vue"
	import navBar from '@/components/zhouWei-navBar/index.vue'
	import channel from '@/components/channel/channel.vue'
	import special from '@/components/special/special.vue'

	let windowWidth = 0


	export default {
		components: {
			transVideo,
			onlyText,
			rightVideo,
			threeImg,
			uniLoadMore,
			uniSwiperDot,
			navBar,
			channel,
			special
		},
		data() {
			return {
				constData: this.$constData, //全局变量引入，防止头条html中报错
				channelList: [], //专栏列表

				/* 广告轮播图 */
				info: [],
				current: 0,
				mode: 'nav',
				dotsStyles: {
					backgroundColor: 'rgba(251,114,153,0.3)',
				},
				/* 轮播图end */

				tagsList: [], //标签列表
				tabCurrentIndex: 0, //当前选项卡索引
				scrollLeft: 0, //顶部选项卡左滑距离,

				userId: '',

				contents: [], //显示列表
				tagName: '', //当前选中标签名字

				//上拉加载 ---分页
				offset: 0,
				count: 10,
				page: 1,

				pageStatus: 'loading', //加载状态 more（loading前）、loading（loading中）、noMore（没有更多了）

				location: '', // 纬 经 度
				cityName: '', //城市名

				searchTitle: '搜索(用户、资讯)',
			}
		},
		onShow() {
			this.$commen.getNewReplay()
		},
		onLoad() {
			// #ifdef APP-PLUS
			this.navToContent()
			// #endif

			let cnt = {
				moduleId: this.constData.module, // String 隶属
				// status: this.constData.tagStatus[1].key, // Byte 标签状态
				group: this.constData.tagGroupType[0].val, // String 标签
				count: 500, // Integer 
				offset: 0, // Integer 
			}
			this.getTagsList(cnt)

			let cnt1 = {
				moduleId: this.$constData.module, // Long 模块编号
				type: this.$constData.adData[0].key, // Byte <选填> 类型
				count: 10, // int 
				offset: 0, // int 
			}
			this.getAdverts(cnt1)

			windowWidth = uni.getSystemInfoSync().windowWidth;
			if (!uni.getStorageSync('userId')) {
				uni.setStorageSync('userId', '1234567890')
			}

			this.userId = uni.getStorageSync('userId')

			this.getLocation()
		},
		methods: {
			navToContent() {
				let args = plus.runtime.arguments;
				if (args) {
					args = args.substr(args.indexOf('://') + 3)
					console.log(args)
					let obj = this.$util.tryParseJson(args)
					if (obj.type == this.$constData.contentType[1].key) {
						uni.navigateTo({
							url: `/pages/index/videoView/videoView?id=${obj.id}`
						})
					} else if (obj.type == this.$constData.contentType[2].key) {
						uni.navigateTo({
							url: `/pages/index/articleView/articleView?id=${obj.id}`
						})
					}
				}
			},

			changeCity() {
				console.log('我不想动')
			},

			//跳转至专题详情
			navChannle(item) {
				if (item.type == 0) {
					uni.navigateTo({
						url: `/pages/follow/channel/channel?id=${item.id}&title=${item.title}`
					})
				} else if (item.type == 1) {
					uni.navigateTo({
						url: `/pages/index/special/special?id=${item.id}&title=${item.title}`
					})
				}
			},

			//获取坐标
			getLocation() {
				uni.getLocation({
					type: 'BD09',
					geocode: true,
					success: (res) => {
						console.log(res)
						this.location = `${res.latitude},${res.longitude}`
						// #ifdef APP-PLUS
						this.cityName = res.address.city
						// #endif
					},
					fail: (err) => {
						this.location = `27.73298,106.933426`
						this.cityName = '遵义市'
						console.log(this.location)
					}
				})
			},

			//跳转基地
			navToPlace(item) {
				console.log('111')
				uni.navigateTo({
					url: `/pages/index/tourBases/tourBases?id=${item.id}`
				})
			},

			//跳转广告
			navInfo(url) {
				if (url) {
					let key = url.indexOf('http://')
					let key1 = url.indexOf('https://')
					if (key != -1 || key1 != -1) {
						plus.runtime.openURL(url)
					} else {
						uni.showToast({
							title: '广告链接错误',
							icon: 'none'
						})
					}
				} else {
					uni.showToast({
						title: '没有广告链接',
						icon: 'none'
					})
				}
			},

			//查询广告
			getAdverts(cnt) {
				this.$api.getAdverts(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.info = this.$util.tryParseJson(res.data.c)
					} else {
						console.log('Error')
					}
				})
			},

			//跳转搜索页
			searchBtn() {
				if (this.tagName == '科普基地') {
					uni.navigateTo({
						url: '/pages/index/searchPlace/searchPlace',
						"animationType": "none",
					})
				} else {
					uni.navigateTo({
						url: '/pages/index/search/search',
						"animationType": "none",
					})
				}

			},

			// 轮播图改变触发
			change(e) {
				this.current = e.detail.current;
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

			//按钮点击跳转
			trigger(e) {
				if (this.userId == '' || this.userId == '1234567890') {
					uni.switchTab({
						url: '/pages/user/user'
					})
					uni.showToast({
						title: '请登录',
						icon: 'none',
						duration: 1000
					})
					return
				}
				console.log(e)
				if (e.item.url == '/pages/index/addContent/addContent?type=1') {
					uni.navigateTo({
						url: '/pages/index/addArticle/addArticle'
					})
				} else if (e.item.url == '/pages/index/addContent/addContent?type=0') {
					uni.navigateTo({
						url: '/pages/index/addVideo/addVideo'
					})
				}
			},

			/* 获取标签列表*/
			getTagsList(cnt) {
				let index = this.tabCurrentIndex
				this.$api.getContentTag(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let tagsList = this.$util.tryParseJson(res.data.c)
						for (let i = 0; i < tagsList.length; i++) {
							tagsList[i].pageOver = false
							tagsList[i].page = 1
						}
						this.tagsList = tagsList
						this.tagName = tagsList[0].name
						let cnt = {
							module: this.$constData.module, // Long 模块编号
							type: 0,
							// status: this.$constData.tagStatus[1].key, // Byte <选填> 状态
							// tags: tags, // String <选填> 标签（json）
							count: this.count, // int 
							offset: this.offset, // int 
						}
						this.getChannels(cnt)
					} else {
						this.tagList = [{
								name: '科普专栏',
								pageOver: false,
								page: 1
							},
							{
								name: '推荐',
								pageOver: false,
								page: 1
							},
							{
								name: '活动',
								pageOver: false,
								page: 1
							},
							{
								name: '科普基地',
								pageOver: false,
								page: 1
							},
							{
								name: '专题页面',
								pageOver: false,
								page: 1
							}
						]
						let cnt = {
							module: this.$constData.module, // Long 模块编号
							// status: this.$constData.tagStatus[1].key, // Byte <选填> 状态
							// tags: tags, // String <选填> 标签（json）
							count: this.count, // int 
							offset: this.offset, // int 
						}
						this.getChannels(cnt)
					}
				})

			},

			getTourBases(cnt, index) {
				this.$api.getTourBases(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						uni.stopPullDownRefresh()

						let list = this.$util.tryParseJson(res.data.c)
						console.log(list)
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
						this.tryDataList(list, index)
					} else {
						console.log(cnt)
						console.log(res.data.c)
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

			getKeywordContent(cnt, index) {
				let list = []
				if (this.tagsList[index].pageOver === true) {
					return
				}
				this.pageStatus = 'loading'
				this.$api.getKeywordContent(cnt, (res) => {
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
						this.tryDataList(list, index)
						uni.stopPullDownRefresh()
					} else {
						this.tagsList[index].pageOver = true //结束拉取
						this.tagsList[index].pageStatus = 'nomore'
						this.pageStatus = this.tagsList[index].pageStatus
						let obj = this.$util.tryParseJson(JSON.stringify(this.tagsList[index]))
						obj.child = []
						this.$nextTick(function() {
							this.tagsList.splice(index, 1, obj)
						})
					}
				})
			},

			/*根据标签获取内容列表*/
			getContentsByTag(cnt, index) {
				let list = []
				if (this.tagsList[index].pageOver === true) {
					return
				}
				this.pageStatus = 'loading'
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
						this.tryDataList(list, index)
						uni.stopPullDownRefresh()
					} else {
						this.tagsList[index].pageOver = true //结束拉取
						this.tagsList[index].pageStatus = 'nomore'
						this.pageStatus = this.tagsList[index].pageStatus
						let obj = this.$util.tryParseJson(JSON.stringify(this.tagsList[index]))
						obj.child = []
						this.$nextTick(function() {
							this.tagsList.splice(index, 1, obj)
						})
					}
				})
			},

			/* 添加新数据进数组并显示 */
			tryDataList(list, index) {
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
				if (this.pageStatus == 'loading') {
					return
				}
				this.tabCurrentIndex = _index
				this.tagName = this.tagsList[_index].name
				this.page = this.tagsList[_index].page

				if (this.tagName == '科普基地') {
					this.searchTitle = '搜索科普基地'
				} else {
					this.searchTitle = '搜索(用户、资讯)'
				}

				// let width = 0;
				// let nowWidth = 0;
				// //获取可滑动总宽度
				// for (let i = 0; i <= _index; i++) {
				// 	let result = await this.getElSize('tab' + _index)
				// 	width += result.width
				// 	if (i === _index) {
				// 		nowWidth = result.width
				// 	}
				// }
				// if (width - nowWidth / 2 > windowWidth / 2) {
				// 	//如果当前项越过中心点，将其放在屏幕中心
				// 	this.scrollLeft = width - nowWidth / 2 - windowWidth / 2;
				// } else {
				// 	this.scrollLeft = 0;
				// }

				if (this.tagsList[_index].child) {
					if (this.tagName == '科普栏目' || this.tagName == '专题页面') {
						this.channelList = this.tagsList[_index].child
						this.pageStatus = this.tagsList[_index].pageStatus
					} else {
						this.pageStatus = this.tagsList[_index].pageStatus
						this.contents = this.tagsList[_index].child
					}

				} else {
					this.getData(_index)
				}
			},

			getData(_index) {
				if (this.tagName == '活动') {
					let cnt = {
						module: this.constData.module, // String 所属模块
						// status: this.constData.contentStatus[4].key, // Byte <选填> 状态
						type: this.constData.contentType[3].key, // Byte <选填> 类型
						count: this.count, // Integer
						offset: this.offset, // Integer
					}
					this.contents = []
					this.getContentsByTag(cnt, _index)
				} else if (this.tagName == '科普基地') {
					let cnt = {
						moduleId: this.constData.module, // String 所属模块
						userCoordinate: this.location, // String <选填> 用户定位经纬度
						count: this.count, // Integer
						offset: this.offset, // Integer
					}
					this.contents = []
					this.getTourBases(cnt, _index)
				} else if (this.tagName == '科普栏目') {
					let cnt = {
						module: this.$constData.module, // Long 模块编号
						type: 0,
						// status: this.$constData.tagStatus[1].key, // Byte <选填> 状态
						// tags: tags, // String <选填> 标签（json）
						count: this.count, // int 
						offset: this.offset, // int 
					}
					this.channelList = []
					this.getChannels(cnt)
				} else if (this.tagName == '专题页面') {
					let cnt = {
						module: this.$constData.module, // Long 模块编号
						type: 1,
						// status: this.$constData.tagStatus[1].key, // Byte <选填> 状态
						// tags: tags, // String <选填> 标签（json）
						count: this.count, // int 
						offset: this.offset, // int 
					}
					this.channelList = []
					this.getChannels(cnt)
				} else if (this.tagName == '推荐') {
					//根据标签获取内容
					let cnt = {
						module: this.constData.module, // String 所属模块
						// status: this.constData.contentStatus[4].key, // Byte <选填> 状态
						// power: this.constData.contentPaid[0].key, // Byte <选填> 是否付费
						// type: type, // Byte <选填> 类型
						count: this.count, // Integer
						offset: this.offset, // Integer
					}
					let userId = uni.getStorageSync('userId')
					if (userId != '' && userId != '1234567890') {
						cnt.userId = userId
					}
					this.contents = []
					this.getKeywordContent(cnt, _index)
				}
			},

			//获取专题
			getChannels(cnt) {
				let index = this.tabCurrentIndex
				this.$api.getChannel(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						uni.stopPullDownRefresh()
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

						this.pageOver = this.tagsList[index].pageOver
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
				} else if (info.type == -1) {
					this.navToPlace(info)
				}
			}
		},
		// 下拉刷新
		onPullDownRefresh() {
			let cnt1 = {
				moduleId: this.$constData.module, // Long 模块编号
				type: this.$constData.adData[0].key, // Byte <选填> 类型
				count: this.count, // int 
				offset: this.offset, // int 
			}
			this.getAdverts(cnt1)
			let index = this.tabCurrentIndex
			this.page = 1
			this.tagsList[this.tabCurrentIndex].page = 1
			this.contents = []
			this.tagsList[this.tabCurrentIndex].pageOver = false
			let cnt = {
				module: this.constData.module, // String 所属模块
				// status: this.constData.contentStatus[4].key, // Byte <选填> 状态
				// power: this.constData.contentPaid[0].key, // Byte <选填> 是否付费
				// type: type, // Byte <选填> 类型
				count: this.count, // Integer
				offset: this.offset, // Integer
			}
			switch (this.tagName) {
				case '活动':
					cnt.type = this.constData.contentType[3].key;
					this.getContentsByTag(cnt, index);
					break;

				case '科普栏目':
					cnt.type = 0
					this.channelList = []
					this.getChannels(cnt)
					break;

				case '科普基地':
					cnt = {
						moduleId: this.constData.module, // String 所属模块
						userCoordinate: this.location, // String <选填> 用户定位经纬度
						count: this.count, // Integer
						offset: this.offset, // Integer
					};
					this.getTourBases(cnt, index);
					break;

				case '专题页面':
					cnt.type = 1;
					this.channelList = [];
					this.getChannels(cnt);
					break;

				case '推荐':
					this.contents = [];
					let userId = uni.getStorageSync('userId');
					console.log(userId);
					if (userId != '' && userId != '1234567890') {
						cnt.userId = userId
					};
					this.getKeywordContent(cnt, index);
			}
			// if (this.tagName != '' && this.tagName != '全部') {
			// 	cnt.tags = `{"homeCotent":"${this.tagName}"}`
			// 	this.contents = []
			// 	this.getContentsByTag(cnt, index)
			// }
		},
		//上滑加载更多
		onReachBottom() {
			let index = this.tabCurrentIndex
			if (this.tagsList[index].pageOver) {
				return
			}
			this.page += 1
			this.tagsList[index].page = this.page
			let cnt = {
				module: this.constData.module, // String 所属模块
				// status: this.constData.contentStatus[4].key, // Byte <选填> 状态
				power: this.constData.contentPaid[0].key, // Byte <选填> 是否付费
				// type: type, // Byte <选填> 类型
				count: this.count, // Integer
				offset: (this.page - 1) * this.count, // Integer
			}

			switch (this.tagName) {
				case '活动':
					cnt.type = this.constData.contentType[3].key
					this.getData(this.tabCurrentIndex)
					break;

				case '科普栏目':
					cnt = {
						module: this.constData.module, // Long 模块编号
						// status: this.$constData.tagStatus[1].key, // Byte <选填> 状态
						// tags: tags, // String <选填> 标签（json）
						count: this.count, // int 
						offset: (this.page - 1) * this.count, // int 
					}
					this.getChannels(cnt)
					break;

				case '科普基地':
					cnt = {
						moduleId: this.constData.module, // String 所属模块
						userCoordinate: this.location, // String <选填> 用户定位经纬度
						count: this.count, // Integer
						offset: (this.page - 1) * this.count, // Integer
					}
					this.getTourBases(cnt, index)
					break;

				case '推荐':
					let userId = uni.getStorageSync('userId')
					if (userId != '' && userId != '1234567890') {
						cnt.userId = userId
					}
					this.getKeywordContent(cnt, index)
					break;

				case '专题页面':
					cnt.type = 1
					this.getChannels(cnt)
			}

			// if (this.tagName != '' && this.tagName != '全部') {
			// 	cnt.tags = `{"homeCotent":"${this.tagName}"}`
			// 	this.getContentsByTag(cnt, index)
			// }
		},

	}
</script>

<style lang="scss" scoped>
	// 头部导航
	.search_box {
		margin-left: 4em;
		width: 450upx;
		height: 64upx;
		background-color: #f5f5f5;
		border-radius: 32upx;
		display: flex;
		align-items: center;
		padding: 0upx 40upx;
		color: #cccccc;

		.prompt {
			margin-left: 10upx;
			font-size: 28upx;
		}

		.icon_search {
			width: 29upx;
			height: 28upx;
			margin-right: 20upx;
		}
	}

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
		text-align: center;
		width: 100vw;

		.nav-item {
			display: inline-block;
			height: 90upx;
			padding: 0 20upx;
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
			font-size: $list-title;
			// transform: translateY(5upx);

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

	.cityBox {
		position: absolute;
		width: 4em;
		white-space: nowrap;
		font-size: $list-title;
		display: inline-block;
		top: 50%;
		margin-top: -0.8em;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.navBox {
		position: relative;

		.iconfont {
			font-size: $list-title;
		}
	}

	.channleView {
		display: flex;
		flex-wrap: wrap;
		padding: $box-margin-top 0;
	}

	.contentBox {
		padding: $box-margin-top $box-margin-left;
	}
</style>
