<template>
	<view>
		<view class="titleBox" :style="'background:'+color">
			<view class="titleText">{{titleText}}</view>
		</view>

		<view class="contentBox">
			<!-- 课程列表 -->
			<view v-for="(item,index) in list" :key="index">
				<view class="lists" @click="navigator(item)">
					<view class="imgBox">
						<image v-if="item.type == constData.contentType[2].key" :src="item.data.imgList[0].src" mode="aspectFill"></image>
						<image v-else-if="item.type == constData.contentType[1].key" :src="item.data.imgSrc" mode="aspectFill"></image>
					</view>
					<view class="rightBox">
						<view class="title">{{ item.title }}</view>
						<text class="msg">{{ item.time }}</text>
					</view>
					<view class="clearBoth"></view>
				</view>
			</view>
		</view>

		<view class="bottomBtn" v-if="payOrNo">
			<button type="primary" @click="navToPay">立即支付{{price}}￥</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				constData: this.$constData,
				titleText: '', //课程标题
				color: '',

				list: [], //课程内列表

				id: '', //课程id
				channelId: '', //专栏id

				payOrNo: false, //是否购买

				price: '', //课程价格
			}
		},
		onLoad(res) {
			console.log(res)
			this.titleText = res.title
			this.color = res.color
			this.id = res.id
			this.channelId = res.channelId
			this.getChannlContentTagByChannelId()
			this.getPayStatus()
			let a = `{"t${this.channelId}":"${this.titleText}"}`
			let cnt = {
				count: 10,
				module: this.$constData.module,
				offset: 0,
				status: this.$constData.contentStatus[4].key,
				tags: this.$util.tryParseJson(a),
				upChannelId: this.channelId
			}
			this.getContent(cnt)
		},
		methods: {
			//获取专栏下标签
			getChannlContentTagByChannelId() {
				let cnt = {
					module: this.$constData.module, // String 模块编号
					channelId: parseInt(this.channelId), // Long 专栏编号
					status: this.$constData.contentStatus[4].key, // Byte <选填> 内容状态
					count: 500, // Integer 
					offset: 0, // Integer 
				}
				this.$api.getChannlContentTagByChannelId(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let arr = this.$util.tryParseJson(res.data.c)
						console.log('标签列表~~~~~~~~~~~~~~~')
						console.log(arr)
						for (let i = 0; i < arr.length; i++) {
							console.log(i)
							if (arr[i].name == this.titleText) {
								this.price = arr[i].price
								console.log(this.price)
								return
							}
						}
					}
				})
			},

			//跳转支付页面
			navToPay() {
				let userId = uni.getStorageSync('userId')
				if(userId == ''|| userId == '1234567890'){
					uni.switchTab({
						url:'/pages/user/user'
					})
					uni.showToast({
						title: '请登录',
						icon:'none'
					})
					return
				}
				uni.navigateTo({
					url: `/pages/vip/column/payView/payView?id=${this.channelId}&columnId=${this.id}&title=${this.titleText}&price=${this.price}`
				})
			},

			//获取当前课程是否购买状态
			getPayStatus() {
				let cnt = {
					modeuleId: this.$constData.module, // Long 模块编号
					channelId: this.channelId, // Long 专栏id
					channelContentTagId: this.id, // Long 课程名id
					userId: uni.getStorageSync('userId'), // Long 用户id
				}
				this.$api.getChannelContentTagPower(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let paidStatus = this.$util.tryParseJson(res.data.c).resultStatus
						
						if (paidStatus) {
							this.payOrNo = false
						} else {
							this.payOrNo = true
						}
					} else {
						console.log(res.data.c)
					}
				})
			},

			//路由跳转
			navigator(item) {
				let url = ''
				console.log(item)
				if (item.power == this.$constData.contentPaid[1].key) {
					this.getChannelContentTagPower(item)
				} else if (item.power == this.$constData.contentPaid[0].key) {
					if (item.type == this.$constData.contentType[0].key || item.type == this.$constData.contentType[2].key) {
						url = 'details'
					} else if (item.type == this.$constData.contentType[1].key) {
						url = 'detailsVideo'
					}
					uni.redirectTo({
						url: `/pages/vip/column/${url}/${url}?id=${item.id}`
					})
				}
			},

			//获取可观看状态
			getChannelContentTagPower(list) {
				let cnt = {
					modeuleId: this.$constData.module, // Long 模块编号
					channelId: this.channelId, // Long 专栏id
					channelContentTagId: this.id, // Long 课程名id
					userId: uni.getStorageSync('userId'), // Long 用户id
				}
				this.$api.getChannelContentTagPower(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let paidStatus = this.$util.tryParseJson(res.data.c).resultStatus
						if (paidStatus) {
							let url = ''
							if (list.type == this.$constData.contentType[0].key || list.type == this.$constData.contentType[2].key) {
								url = 'details'
							} else if (list.type == this.$constData.contentType[1].key) {
								url = 'detailsVideo'
							}
							uni.redirectTo({
								url: `/pages/vip/column/${url}/${url}?id=${list.id}`
							})
						} else {
							uni.showToast({
								title: '购买课程可观看',
								icon: 'none'
							});
						}
					} else {
						console.log(res.data.c)
					}
				})
			},

			//获取课程内列表
			getContent(cnt) {
				this.$api.getContents(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						console.log('-------------课程内列表---------------')
						let list = this.$util.tryParseJson(res.data.c)
						console.log(list)

						for (let i = 0; i < list.length; i++) {
							list[i].data = this.$util.tryParseJson(list[i].data)
						}
						this.list = list
					}
				})
			}
		},
	}
</script>

<style lang="scss" scoped>
	.titleBox {
		position: relative;
		width: 100vw;
		height: 15vh;
		overflow: hidden;
		color: #fff;

		.titleText {
			padding-left: $box-margin-left;
			height: 100%;
			color: rgb(234, 242, 248);
			font-size: $list-title-l;
			font-weight: bold;
			line-height: 15vh;
		}
	}

	.contentBox {
		width: 100vw;
		margin: 0 auto;
		border-radius: 20px;
		overflow: hidden;
	}

	.lists {
		position: relative;
		padding: 2vw;
		height: 24vw;
		font-size: 18px;
		line-height: 18px;

		.title {
			margin-bottom: 50upx;
			color: $list-title-color;
			font-size: $list-title;
			line-height: $list-title-line;
			box-sizing: border-box;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 2; //需要显示时文本行数
			overflow: hidden;
		}


		.msg {
			font-size: 14px;
			color: #dcdcdc;
		}
	}

	.imgBox {
		position: absolute;
		top: 50%;
		margin-top: -10vw;
		width: 20vw;
		height: 20vw;
		border-radius: 5px;
		overflow: hidden;

		image {
			width: 100%;
			height: 100%;
		}
	}

	.rightBox {
		position: absolute;
		margin-left: 24vw;
		margin-right: 20upx;
		top: 50%;
		margin-top: -50upx;
	}

	.bottomBtn {
		position: fixed;
		width: 100vw;
		bottom: 0;

		button {
			width: 100%;
			border-radius: 0;
			font-size: $list-title;
			background-color: #ec706b;
		}

		.button-hover {
			background-color: rgba(236, 112, 107, 0.5);
			color: rgba(255, 255, 255, 0.5)
		}

	}
</style>
