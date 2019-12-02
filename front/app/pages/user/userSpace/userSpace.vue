<template>
	<view class="body">
		<nav-bar type="transparentFixed" transparentFixedFontColor="#FFF" title=" "></nav-bar>
		<view class="imgBg">
			<image :src="imgSrc" mode="aspectFill"></image>
		</view>
		<view class="userInfo">
			<image class="userHead" :src="userHead" mode="aspectFill"></image>
			<button class="followBtn" v-if="followStatus == false" @click="followBtn">关注</button>
			<button class="followBtn" v-else-if="followStatus == true" @click="delUserFavorite">已关注</button>
			<view class="userName">
				{{userName}}
				<text class="tagBox">{{authority()}}</text>
			</view>
		</view>

		<view class="titleBox">动态列表</view>

		<view class="contentBox" v-for="(item,index) in contents" :key="index" @click="navToInfo(item)" :hidden="tabCurrentIndex == 1"
		 style="background-color: #FFFFFF;">
			<view v-if="item.type == constData.contentType[1].key||item.type == constData.contentType[2].key">
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
		</view>
		<uniLoadMore :status="pageStatus"></uniLoadMore>
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
			transVideo,
			onlyText,
			rightVideo,
			threeImg,
			uniLoadMore
		},
		data() {
			return {
				constData: this.$constData, //全局变量

				id: '',
				imgSrc: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571668534893&di=d796606e8a00a0281a7386a5e2095d8f&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F9b0fb4644fba26cfea631c3a305a3d03a5b431a45f9787-0ir9D6_fw658',
				userHead: '',
				userName: '',

				followStatus: false,
				followId: '',

				contents: [], //内容列表

				count: 10,
				offset: 0,
				pageOver: false, //是否结束拉取数据
				pageStatus: 'loading',
				page: 1,

			}
		},
		onLoad(res) {
			this.id = res.id
			let cnt = {
				moduleId: this.$constData.module, // String 模块编号
				id: this.id, // Long 用户id
			}
			this.getUserById(cnt)
			this.getBoolFavoriteUser()

			let cnt1 = {
				module: this.$constData.module, // String 模块
				status: this.$constData.contentStatus[4].key, // Byte <选填> 状态编号
				upUserId: this.id, // Long <选填> 上传用户编号
				count: this.count, // int 
				offset: this.offset, // int 
			}
			this.getContent(cnt1)
		},
		methods: {
			authority() {
				let authority = uni.getStorageSync('authority')
				if (authority == this.$constData.authority[0].key) {
					return this.$constData.authority[0].val
				} else if (authority == this.$constData.authority[1].key) {
					return this.$constData.authority[1].val
				} else if (authority == this.$constData.authority[2].key) {
					return this.$constData.authority[2].val
				}
			},

			//取关
			delUserFavorite() {
				let cnt = {
					moduleId: this.$constData.module, // String 模块编号
					id: this.followId, // Long id
				}
				this.$api.delUserFavorite(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						uni.showToast({
							title: '已取消关注',
							icon: 'none'
						})
						this.followStatus = false
					} else {
						uni.showToast({
							title: '服务器错误！',
							icon: 'none'
						})
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
			},

			//内容列表
			getContent(cnt) {
				this.$api.getContents(cnt, (res) => {
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
					}
				})
			},

			// 添加进数组
			tryDataList(list) {
				if (list.length < this.count) { //判断长度是否为等于设定this.count，是则可能还有剩余数据，否则无
					this.pageOver = true //结束拉取
					this.pageStatus = 'nomore'
				} else {
					this.pageOver = false
					this.pageStatus = 'more'
				}
				let arr = this.contents.concat(list)
				this.contents = arr
				console.log(this.contents)
			},

			//查询是否关注
			getBoolFavoriteUser() {
				let cnt = {
					moduleId: this.$constData.module, // String 模块编号
					userId: uni.getStorageSync('userId'), // Long 用户id
					concernId: this.id, // Long 被关注用户id,true没有关注
					count: 10, // int
					offset: 0, // int
				}
				this.$api.getBoolFavoriteUser(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let data = this.$util.tryParseJson(res.data.c)
						this.followStatus = data.bool
						if (this.followStatus == true) {
							this.followId = data.info.id
						}
					} else {
						console.log('失败')
					}
				})
			},

			//关注用户
			followBtn() {
				let userId = uni.getStorageSync('userId')
				if (userId == '' || userId == '1234567890') {
					uni.showToast({
						title: '登录后可关注',
						icon: 'none'
					})
					return
				}

				let cnt = {
					moduleId: this.$constData.module, // String 模块编号
					concernId: this.id, // Long 被关注用户id
					userId: userId, // Long 用户id
				}
				this.$api.createUserFavorite(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						uni.showToast({
							title: '关注成功'
						})
						this.followId = this.$util.tryParseJson(res.data.c).id
						this.followStatus = true
					} else {
						uni.showToast({
							title: res.data.rm,
							icon: 'none'
						})
						this.followStatus = false
					}
				})
			},

			//获取对应用户
			getUserById(cnt) {
				this.$api.getUserById(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let userInfo = this.$util.tryParseJson(res.data.c)
						console.log(userInfo)
						this.userHead = userInfo.head
						this.userName = userInfo.name
					} else {
						console.log(res.data.c)
					}
				})
			},
		},
		//下拉刷新
		onPullDownRefresh() {
			this.page = 1
			this.contents = []
			this.pageStatus = 'loading'
			let cnt1 = {
				module: this.$constData.module, // String 模块
				status: this.$constData.contentStatus[4].key, // Byte <选填> 状态编号
				upUserId: this.id, // Long <选填> 上传用户编号
				count: this.count, // int 
				offset: this.offset, // int 
			}
			this.getContent(cnt1)
		},
		//上滑加载
		onReachBottom() {
			if (this.pageOver == false) {
				this.page += 1
				this.pageStatus = 'loading'
				let cnt1 = {
					module: this.$constData.module, // String 模块
					status: this.$constData.contentStatus[4].key, // Byte <选填> 状态编号
					upUserId: this.id, // Long <选填> 上传用户编号
					count: this.count, // int 
					offset: (this.page - 1) * this.count, // int 
				}
				this.getContent(cnt1)
			}
		}
	}
</script>

<style lang="scss" scoped>
	.body {
		background-color: $uni-bg-color-grey;
	}

	.imgBg {
		width: 100vw;
		height: 45vw;
		overflow: hidden;

		image {
			width: 100%;
			height: 100%;
		}
	}

	.userInfo {
		position: relative;
		padding: $box-margin-top $box-margin-left;
		padding-top: 70upx;
		background-color: #FFFFFF;
	}

	.userHead {
		position: absolute;
		width: 120upx;
		height: 120upx;
		border-radius: 100%;
		overflow: hidden;
		left: $box-margin-left;
		top: -60upx;
	}

	.followBtn {
		position: absolute;
		right: $box-margin-left;
		top: 10upx;
		display: inline-block;
		// width: 3em;
		text-align: center;
		font-size: $list-info;
		color: $color-button-back;
		background-color: $color-main;

		&:after {
			border: none;
		}
	}

	.userName {
		font-size: $list-title;
		font-weight: bold;
	}

	.tagBox {
		color: $color-main;
		border: 1px solid $color-main;
		margin-left: 10upx;
		padding: 0 5upx;
		font-size: $list-info;
		font-weight: normal;
		border-radius: 5upx;
	}

	.titleBox {
		font-size: $list-title-l;
		margin-top: 15upx;
		padding: 0 0 0 $box-margin-left;
		background-color: #FFFFFF;
	}

	.contentBox {
		padding: $box-margin-top $box-margin-left;
	}
</style>
