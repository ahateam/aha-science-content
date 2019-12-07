<template>
	<view class="body">
		<image class="adImg" :src="adList[0].imgSrc" mode="aspectFill" @click="linkSrc"></image>
		<view class="nextBtn" @click="navHome">
			跳过 <text style="margin-left: 10upx;" v-if="time != 0">{{time}}</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				adList: [],
				time: 5,
				imgSrc: 'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/a660ce095e8f553a9ed1515265f4e9fc.jpg?thumb=1&w=234&h=300&f=webp&q=90',
				timeInterval:Function,
			};
		},
		onLoad() {
			let cnt = {
				moduleId: this.$constData.module, // Long 模块编号
				type: this.$constData.adData[2].key, // Byte <选填> 类型
				count: 10, // int 
				offset: 0, // int 
			}
			this.getAdverts(cnt)
			this.setTime()
		},
		methods: {


			linkSrc() {
				let url = this.adList[0].linkSrc
				if (url) {
					let key = url.indexOf('http://')
					let key1 = url.indexOf('https://')
					if (key != -1 || key1 != -1) {
						plus.runtime.openURL(url)
					} else {
						uni.showToast({
							title: '地址错误',
							icon: 'none'
						})
					}

				} else {
					uni.showToast({
						title: '暂无地址',
						icon: 'none'
					})
				}
			},

			getAdverts(cnt) {
				this.$api.getAdverts(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.adList = this.$util.tryParseJson(res.data.c)
						console.log(this.adList)
					} else {
						console.log('Error')
					}
				})
			},
			setTime() {
				this.timeInterval = setInterval(() => {
					this.time = this.time - 1
					if (this.time == 0) {
						clearInterval(this.timeInterval)
						this.navHome()
					}
				}, 1000)
			},
			navHome() {
				clearInterval(this.timeInterval)
				uni.switchTab({
					url: '/pages/index/index'
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.body {
		width: 100vw;
		height: 100vh;
		overflow: hidden;
	}

	.adImg {
		width: 100%;
		height: 100%;
	}

	.nextBtn {
		position: absolute;
		top: 50upx;
		right: 50upx;
		// left: 50upx;
		color: #FFFFFF;
		border-radius: 30upx;
		font-size: $list-title;
		background-color: rgba($color: #000000, $alpha: 0.4);
		padding: 10upx 20upx;
	}
</style>
