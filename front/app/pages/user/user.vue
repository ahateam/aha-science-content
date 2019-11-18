<template>
	<view class="body">
		<navBar bgColor="#FB7299" :back="false" fontColor="#FFF">
			<view slot="right" class="iconfont kk-chilun setBtn"></view>
		</navBar>

		<!-- 登录注册 -->
		<view class="userBox" style="text-align: center;" v-if="loginStatus == false">
			<button class="leftButton" type="primary" @click="register">注册</button>
			<button class="rightButton" type="primary" @click="loginBtn">登录</button>
		</view>

		<!-- 用户信息 -->
		<view class="userBox" v-if="loginStatus == true" @click="navToUser">
			<view class="userHead">
				<image :src="userHead" mode="aspectFill"></image>
			</view>
			<view class="userInfo">
				<view class="userName">
					{{userName}}
				</view>

				<view class="contentInfo">
					<text class="tagName">{{authority()}}</text>
				</view>

				<view class="contentInfo" style="color: #fdc7d6;">
					<text>发布内容:{{addContent}}</text>
					<text style="margin-left: 20upx;">我的提问:{{addQuest}}</text>
				</view>
			</view>

			<view class="iconfont kk-jiantou nextIcon"></view>
		</view>

		<view class="bottomBox" v-if="loginStatus">
			<navigator class="autoBox" url="/pages/user/shopping/shopping">
				<text class="iconfont kk-money iconColor"></text>
				<text class="bottomTitle">报名中心</text>
				<!-- <text class="iconfont kk-jiantou rightIcon"></text> -->
			</navigator>
		</view>
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
				userName: '',
				userHead: '',
				userId: '',

				loginStatus: false,

				providerList: [],

				addContent: 0,
				addQuest: 0,
			};
		},
		onLoad() {

		},
		
		onShow() {
			this.$commen.getNewReplay()
			let userId = uni.getStorageSync('userId')
			if (userId != '' && userId != '1234567890') {
				this.userName = uni.getStorageSync('userName')
				this.userHead = uni.getStorageSync('userHead')
				this.userId = userId
				this.loginStatus = true
			} else {
				console.log('未登录')
				this.loginStatus = false
			}
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

			navToUser() {
				uni.navigateTo({
					url: '/pages/user/userData/userData'
				})
			},

			register() {
				uni.navigateTo({
					url: '/pages/user/userLogin/register'
				})
			},
			loginBtn() {
				uni.navigateTo({
					url: '/pages/user/userLogin/userLogin'
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.iconColor {
		color: $color-main;
		font-size: 40upx;
		vertical-align: middle;
	}

	.userBox {
		position: relative;
		background-color: $color-main;
		width: 100vw;
		padding: $box-margin-top 0 35upx;

		button {
			display: inline-flex;
			margin: 0 $box-margin-left;
			padding: 0 60upx;
			font-size: $list-title;

			&:after {
				border: none;
			}
		}

		.leftButton {
			background-color: $color-button;
		}

		.rightButton {
			background-color: $color-button-back;
			color: $color-main;
		}
	}

	.userHead {
		display: inline-block;
		margin-left: 50upx;
		border: 2px solid $color-button-back;
		width: 120upx;
		height: 120upx;
		border-radius: 100%;
		overflow: hidden;

		image {
			width: 100%;
			height: 100%;
		}
	}

	.userInfo {
		display: inline-block;
		margin-left: $box-margin-left;
		vertical-align: top;
	}

	.userName {
		display: block;
		color: $color-button-back;
		font-size: $list-title;
		// padding: 10upx 0;
	}

	.tagName {
		border: 1px solid $color-button-back;
		border-radius: 10upx;
		padding: 0 0.7em;
	}

	.contentInfo {
		color: $color-button-back;
		font-size: $list-info-s;
		margin-top: 10upx;
	}

	.nextIcon {
		position: absolute;
		right: $box-margin-left;
		font-size: 40upx;
		top: 50%;
		margin-top: -0.7em;
		color: $color-button-back;
	}

	.setBtn {
		font-size: 40upx;
		margin-right: $box-margin-left;
	}

	.bottomBox {
		padding: $box-margin-top 0;
		line-height: 60upx;
		font-size: 40upx;
	}

	.rightIcon {
		float: right;
		font-size: $list-title;
	}

	.autoBox {
		padding: 20upx;
		background-color: rgba($color: $uni-text-color-grey, $alpha: 0.1);
	}

	.bottomTitle {
		margin-left: 15upx;
		font-size: $list-title;
		color: $uni-text-color-placeholder;
		vertical-align: middle;
	}
</style>
