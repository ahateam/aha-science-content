<template>
	<view class="body">
		<navBar bgColor="#FB7299" fontColor="#FFF">账号资料</navBar>
		<view class="topBox">
			<view class="autoBox" @click="change('head')">
				<view class="leftBox" style="line-height: 100upx;">
					头像
				</view>
				<view class="userHead">
					<image :src="head" mode="aspectFill"></image>
					<text class="iconfont kk-xiayibu leftMargin"></text>
				</view>
			</view>

			<view class="autoBox" @click="openBox('name')">
				<view class="leftBox">
					昵称
				</view>
				<view class="rightBox">
					<view v-if="userNameBox == false">
						<text class="infoBox">{{userName}}</text>
						<text class="iconfont kk-xiayibu leftMargin"></text>
					</view>

					<view v-if="userNameBox == true" class="leftBox">
						<input type="text" v-model="newName" focus class="changeName" />
						<text class="textBtn" @click="change('name')">保存</text>
					</view>
				</view>
			</view>

			<view class="autoBox" @click="openBox('company')">
				<view class="leftBox">
					单位/学校
				</view>
				<view class="rightBox">
					<view v-if="companyBox == false">
						<text class="infoBox">{{userUnit}}</text>
						<text class="iconfont kk-xiayibu leftMargin"></text>
					</view>

					<view v-if="companyBox == true" class="leftBox">
						<input type="text" v-model="company" focus class="changeName" />
						<text class="textBtn" @click="change('company')">保存</text>
					</view>
				</view>


			</view>

			<view class="autoBox" @click="createApplyAuthority">
				<view class="leftBox">
					权限
				</view>
				<view class="rightBox">
					<text class="infoBox">{{authority()}}</text>
					<text class="iconfont kk-xiayibu leftMargin"></text>
				</view>
			</view>

			<view class="autoBox" @click="bindWx">
				<view class="leftBox">
					微信号
				</view>
				<view class="rightBox">
					<text class="infoBox" v-if="openId">已绑定</text>
					<text class="infoBox" v-if="openId == ''">未绑定</text>
					<text class="iconfont kk-xiayibu leftMargin"></text>
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
	// import uniPopup from '@/components/uni-popup/uni-popup.vue'

	export default {
		components: {
			navBar,
			// uniPopup
		},
		data() {
			return {
				head: uni.getStorageSync('userHead'),
				imgSrc: '',

				userName: uni.getStorageSync('userName'),
				newName: '',

				userUnit: uni.getStorageSync('company'),
				company: '',

				companyBox: false,

				userNameBox: false,

				openId: uni.getStorageSync('openId'),
				bindWxShow: false,

			}
		},
		methods: {
			createApplyAuthority() {
				let cnt = {
					moduleId: this.$constData.module, // Long 模块编号
					userId: uni.getStorageSync('userId'), // Long 用户id
				}
				this.$api.createApplyAuthority(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						uni.showToast({
							title: '已申请，待审核'
						})
					} else {
						uni.showToast({
							title: res.data.rm,
							icon: 'none'
						})
					}
				})
			},

			reMoveBidn() {

			},

			bindWx() {
				if (!this.openId) {
					uni.login({
						success: (res) => {
							uni.showLoading({
								title: '绑定中'
							})
							let openId = res.authResult.openid
							let cnt = {
								moduleId: this.$constData.module, // String 模块编号
								id: uni.getStorageSync('userId'), // Long 用户id
								openId: openId, // String openid
							}
							this.$api.bindingwx(cnt, (res) => {
								if (res.data.rc == this.$util.RC.SUCCESS) {
									uni.setStorageSync('openId', openId)
									this.openId = openId
									uni.showToast({
										title: '绑定成功！'
									})
								} else {
									uni.showToast({
										title: res.data.rm,
										icon: 'none'
									})
								}
							})
						},
						fail: (err) => {
							console.log('login fail:', err);
						}
					})
				} else {
					this.bindWxShow = true
				}
			},

			//打开修改盒子
			openBox(e) {
				if (e == 'name') {
					this.userNameBox = true
				} else if (e == 'company') {
					this.companyBox = true
				}
			},

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

			//修改功能
			change(e) {
				let cnt = {
					moduleId: this.$constData.module, // String 模块编号
					id: uni.getStorageSync('userId'), // Long 用户id
					// name: name, // String <选填> 昵称
					// head: head, // String <选填> 头像
					// company: company, // String <选填> 单位
					// authority: authority, // Byte <选填> 权限
					// status: status, // Byte <选填> 状态
					// ext: ext, // String <选填> 扩展
				}
				if (e == 'head') {
					this.upImg(cnt)
				} else if (e == 'name') {
					cnt.name = this.newName
					this.changeName(cnt)
				} else if (e == 'company') {
					cnt.company = this.company
					this.changeCompany(cnt)
				}
			},

			changeCompany(cnt) {
				this.$api.updateUserInfo(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.userUnit = this.company
						uni.setStorageSync('company', this.userUnit)
						this.companyBox = false
						uni.showToast({
							title: '修改成功！'
						})
					}
				})
			},

			changeName(cnt) {
				this.$api.updateUserInfo(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.userName = this.newName
						uni.setStorageSync('userName', this.newName)
						this.userNameBox = false
						uni.showToast({
							title: '修改成功！'
						})
					}
				})
			},

			//更改用户头像
			upImg(cnt) {
				let tiemr = new Date()
				let address = tiemr.getFullYear() + "" + (tiemr.getMonth() + 1) + "" + tiemr.getDate()
				address = 'zskp/userHead/' + address + '/'
				uni.showLoading({
					title: '上传头像中~'
				})
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
								this.imgSrc = 'https://weapp-xhj.oss-cn-hangzhou.aliyuncs.com/' + nameStr
								this.changeHead(cnt)
							},
							fail: () => {
								uni.hideLoading()
							}
						});
					},
					fail: () => {
						uni.hideLoading()
					}
				});

			},

			changeHead(cnt) {
				cnt.head = this.imgSrc
				this.$api.updateUserInfo(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						uni.hideLoading()
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
		width: 8em;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		display: inline-block;
		text-align: right;
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

	.textBtn {
		color: $color-main;
		font-size: $list-title;
		margin-left: 10upx;
		vertical-align: middle;
	}

	.changeName {
		display: inline-block;
		border-bottom: 2px solid $color-main;
		width: 20vw;
		vertical-align: middle;
	}

	.bindBox {
		width: 500upx;
		height: 500upx;
		color: #333333;
		font-size: $list-title;

		button {
			display: inline-block;
			width: 200upx;
			line-height: 65upx;

			&:after {
				border: none;
			}
		}
	}
</style>
