<template>
	<view class="body">
		<navBar bgColor="#FB7299" fontColor="#FFF">账号资料</navBar>
		<view class="topBox">
			<view class="autoBox" @click="change(0)">
				<view class="leftBox" style="line-height: 100upx;">
					头像
				</view>
				<view class="userHead">
					<image :src="head" mode="aspectFill"></image>
					<text class="iconfont kk-jiantou leftMargin"></text>
				</view>
			</view>

			<view class="autoBox">
				<view class="leftBox">
					昵称
				</view>
				<view class="rightBox">
					<text class="infoBox">{{userName}}</text>
					<text class="iconfont kk-jiantou leftMargin"></text>
				</view>
			</view>

			<view class="autoBox">
				<view class="leftBox">
					单位/学校
				</view>
				<view class="rightBox">
					<text class="infoBox">{{userUnit}}</text>
					<text class="iconfont kk-jiantou leftMargin"></text>
				</view>
			</view>

			<view class="autoBox">
				<view class="leftBox">
					权限
				</view>
				<view class="rightBox">
					<text class="infoBox">正式会员</text>
					<text class="iconfont kk-jiantou leftMargin"></text>
				</view>
			</view>
		</view>

		<view class="tipBox">
			你可以在上面修改资料嗷
		</view>

		<button type="primary" @click="logout" class="logoutBtn">退出登录</button>
	</view>
</template>

<script>
	import navBar from '@/components/zhouWei-navBar/index.vue'

	export default {
		components: {
			navBar,
		},
		data() {
			return {
				head: uni.getStorageSync('userHead'),
				userName: uni.getStorageSync('userName'),
				userUnit: '遵义小红椒',

			};
		},
		methods: {
			change(e) {
				let cnt = {
					moduleId: this.$constData.module, // String 模块编号
					id: uni.getStorageInfoSync('userId'), // Long 用户id
					// name: name, // String <选填> 昵称
					// head: head, // String <选填> 头像
					// company: company, // String <选填> 单位
					// authority: authority, // Byte <选填> 权限
					// status: status, // Byte <选填> 状态
					// ext: ext, // String <选填> 扩展
				}
				if (e == 0) {
					this.upImg(cnt)
				}
			},

			//更改用户头像
			upImg(cnt) {
				let tiemr = new Date()
				let address = tiemr.getFullYear() + "" + (tiemr.getMonth() + 1) + "" + tiemr.getDate()
				address = 'zskp/userHead/' + address + '/'
				if (type == "image")
					uni.chooseImage({
						count: 1, //默认9
						sizeType: ['compressed'],
						sourceType: ['album'],
						success: res => {
							uni.showLoading({
								mask: true,
							})
							var imageSrc = res.tempFilePaths[0]
							let str = res.tempFilePaths[0].substr(res.tempFilePaths[0].lastIndexOf('.'))
							let nameStr = address + tiemr.getTime() + str
							uni.uploadFile({
								url: 'https://weapp-xhj.oss-cn-hangzhou.aliyuncs.com',
								filePath: imageSrc,
								fileType: 'image',
								name: 'file',
								formData: {
									name: nameStr,
									'key': nameStr,
									'policy': 'eyJleHBpcmF0aW9uIjoiMjAyMi0wMS0wMVQxMjowMDowMC4wMDBaIiwiY29uZGl0aW9ucyI6W1siY29udGVudC1sZW5ndGgtcmFuZ2UiLDAsMTA0ODU3NjAwMF1dfQ==',
									'OSSAccessKeyId': 'LTAIJ9mYIjuW54Cj',
									'success_action_status': '200',
									//让服务端返回200,不然，默认会返回204
									'signature': 'kgQ5n4s0oKpFHp35EI12CuTFvVM=',
								},
								success: (res) => {
									uni.showToast({
										title: '上传成功',
										icon: 'success',
										duration: 1000
									})
									this.imgSrc = 'https://weapp-xhj.oss-cn-hangzhou.aliyuncs.com/' + nameStr
									this.changeHead(cnt)
								},
								complete: () => {
									uni.hideLoading()
									this.hideInputPopup()
								}
							});
						}
					});

			},

			changeHead(cnt) {
				cnt.head = this.imgSrc
				this.$api.updateUserInfo(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.head = this.imgSrc
						uni.setStorageSync('userHead', this.imgSrc)
						uni.showToast({
							title: '修改成功！'
						})
					}
				})
			},

			//注销
			logout() {
				uni.clearStorageSync()
				uni.switchTab({
					url: '/pages/user/user'
				})
				uni.showToast({
					title: '已退出登录！',
					icon: 'none'
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.body {
		min-height: 100vh;
		background-color: #f8f8f8;
	}

	.topBox {
		padding: 0 $box-margin-left;
		background: $color-button-back;
		;
	}

	.autoBox {
		position: relative;
		border-bottom: 1px solid $uni-bg-color-grey;
		padding: $box-margin-top 0;
	}

	.leftBox {
		font-size: $list-title;
	}

	.rightBox {
		position: absolute;
		top: $box-margin-top;
		right: $box-margin-left;
		font-size: $list-title;
	}

	.userHead {
		position: absolute;
		right: $box-margin-left;
		top: $box-margin-top;
		line-height: 100upx;
		vertical-align: middle;

		image {
			width: 100upx;
			height: 100upx;
			border-radius: 100%;
			overflow: hidden;
			vertical-align: middle;
		}

		text {
			line-height: 100upx;
			vertical-align: middle;
		}
	}

	.infoBox {
		color: $list-info-color;
		font-size: $list-title;
	}

	.leftMargin {
		margin-left: 15upx;
		color: $list-info-color;
		font-weight: bold;
	}

	.tipBox {
		font-size: $list-info-s;
		text-align: center;
		padding: 10upx 0;
		color: $list-info-color;
	}

	.logoutBtn {
		background-color: #FFFFFF;
		color: #000000;
		font-size: $list-title;

		&:after {
			border: none;
		}
	}
</style>
