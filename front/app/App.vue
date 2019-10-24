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
		},
		onHide: function() {
			console.log('App Hide')
		},
		methods: {
			getLocation(){
				plus.geolocation.getCurrentPosition(this.getSuccess, this.getError)
			},
			
			getSuccess(res){
				console.log(res)
			},
			
			getError(err){
				console.log(err)
			},
			
			setStorage(){
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
