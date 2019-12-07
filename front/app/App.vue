<script>
	export default {
		data() {
			return {
				provider: '',
				show: false,
				versionObj: {}
			}
		},
		onLaunch: function() {
			console.log('App Launch')

			this.getVersion()

			this.setStorage()

			this.getLocation()
		},
		onShow: function() {
			console.log('App Show')
			this.navToContent()
		},
		onHide: function() {
			console.log('App Hide')
			plus.runtime.arguments = ''
			console.log(plus.runtime.arguments)
		},
		methods: {
			navToContent() {
				console.log('1111')
				let pages = getCurrentPages()
				if (pages.length > 0) {
					let page = (pages[pages.length - 1]).route
					if (page != 'pages/ADview/ADview') {
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
					}
				}
			},

			getLocation() {
				plus.geolocation.getCurrentPosition(this.getSuccess, this.getError)
			},

			getSuccess(res) {
				console.log(res)
			},

			getError(err) {
				console.log(err)
			},

			setStorage() {
				uni.getStorage({
					key: 'search_cache',
					fail: () => {
						console.log('111')
						uni.setStorage({
							key: 'search_cache',
							data: []
						})
					}
				})
				uni.getStorage({
					key: 'search_place',
					fail: () => {
						console.log('111')
						uni.setStorage({
							key: 'search_place',
							data: []
						})
					}
				})
			},

			getVersion() {
				this.$api.getVersion({}, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let resData = JSON.parse(res.data.c)
						let platform = uni.getSystemInfoSync().platform
						if (Number(resData.version) > this.$constData.version) {
							this.show = true
							this.versionObj = resData
							this.platform = platform
							uni.showLoading({
								title: '版本更新，已跳转下载...'
							})
							setTimeout(() => {
								this.loadBtn()
							}, 1500)
						}

					} else {
						uni.showToast({
							title: '获取版本更新失败！',
							icon: 'none'
						})
					}
				})
			},

			loadBtn() {
				if (this.platform == 'android') {
					plus.runtime.openURL(this.versionObj.android)
					uni.hideLoading()
				} else if (this.platform == 'ios') {
					plus.runtime.openURL(this.versionObj.ios)
					uni.hideLoading()
				}
			},

		}
	}
</script>

<style>
	/*每个页面公共css */
</style>
