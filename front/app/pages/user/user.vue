<template>
	<view class="body">
		<!-- 用户信息 -->
		<view class="userBox">
			<view class="topBox" @click="loginBtn">
				<image class="userhead" :src="userHead" mode="widthFix"></image>
				<view class="leftBox">
					<view class="userName">{{userName}}</view>
					<view class="userView" v-if="boxShow">个人主页
						></view>
					<button type="primary" class="loginBtn" v-else>登录</button>
				</view>
				<view class="rightBox" v-if="boxShow">
					<view class="userMoney">余额 0.00</view>
					<view class="cashMoney">提现 ></view>
				</view>
			</view>
		</view>

		<!-- 任务 -->
		<view class="taskBox" v-if="boxShow">
			<navigator url="/pages/user/userTask/userAddsTask" class="leftTask">
				已发任务
			</navigator>
			<navigator url="/pages/user/userTask/userTask" class="rightTask">
				已领任务
			</navigator>
		</view>

		<view class="bottomBox" v-if="boxShow" v-for="(item,index) in contentList" :key="index">
			<navigator class="autoBoxHeight" :url="item.url">
				<text :class="item.iconClass"></text>
				<text class="bottomTitle">{{item.name}}</text>
				<text class="iconfont kk-xiayibu rightIcon"></text>
			</navigator>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				userName: '您还没有登录',
				userHead: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567487052843&di=cbce4c55f56dc06f7a8941ad4c9a307e&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F69ad7a731f43d4b8729f1a2fbe65c43801ca0f033250-EV1vMf_fw658',
				userId: null,

				boxShow: false,

				providerList: [],
			};
		},
		onLoad() {
			
		},
		methods: {
			loginBtn() {
				if (this.boxShow == true) {
					return
				}
				uni.navigateTo({
					url: '/pages/user/userLogin/userLogin'
				})
			},
		},
		onShow() {
			let userId = uni.getStorageSync('userId')
			if (userId != '' && userId != '1234567890') {
				this.userName = uni.getStorageSync('userName')
				this.userHead = uni.getStorageSync('userHead')
				this.userId = userId
				this.boxShow = true
			} else {
				console.log('未登录')
			}
		},
	};
</script>

<style lang="scss" scoped>
	.autoBoxHeight {
		height: 60upx;
	}

	.body {
		min-height: 100vh;
		overflow: hidden;
		background-color: rgba(176, 196, 222, 0.1);
		font-size: 36upx;
	}

	.clearBoth {
		clear: both;
	}

	.userBox {
		background: linear-gradient(left, #5ee7df, #b490ca);
		font-size: $list-title;
	}

	.userhead {
		position: absolute;
		top: 50%;
		left: 20upx;
		margin-top: -80upx;
		height: 160upx;
		width: 160upx;
		border-radius: 80px;
		overflow: hidden;
	}

	.loginBtn {
		line-height: 45upx;
		height: 45upx;
		font-size: $list-info;
		background-color: #fff;
		color: #5ee7df;
	}

	.topBox {
		position: relative;
		height: 250upx;
		border-bottom: 1px solid rgba(176, 196, 222, 0.3);
	}

	.leftBox {
		position: absolute;
		left: 210upx;
		top: 50%;
		font-size: 28upx;
		line-height: 32upx;
		margin-top: -49upx;
		text-align: center;
	}

	.rightBox {
		position: absolute;
		right: 40upx;
		top: 50%;
		font-size: 28upx;
		line-height: 32upx;
		margin-top: -49upx;
		padding-left: 70upx;
		border-left: 1px dashed #fff;
	}

	.taskBox {
		background-color: #fff;
		box-sizing: border-box;
		width: 100vw;
		margin-top: 30upx;
		text-align: center;
		line-height: 80upx;
		padding: $box-margin-top $box-margin-left;
		font-size: $list-title;
	}

	.bottomBox {
		margin: 20upx 0;
		padding: $box-margin-top $box-margin-left;
		background-color: #fff;
		line-height: 60upx;
		font-size: $list-title;
	}

	.userName {
		color: #fff;
		margin-bottom: 30upx;
		font-size: $list-title;
	}

	.userView {
		margin: 0 auto;
		color: #5ee7df;
		background-color: #fff;
		border-radius: 20px;
		text-align: center;
		padding: 10upx 20upx;
		width: 7em;
	}

	.userMoney {
		color: #fff;
		margin-bottom: 30upx;
	}

	.cashMoney {
		color: #fff;
		border-radius: 20px;
		text-align: center;
		padding: 10upx 20upx;
		border: 1px solid #fff;
	}

	.leftTask {
		display: inline-block;
		width: 49%;
		border-right: 1px solid #f0f0f0;
	}

	.rightTask {
		display: inline-block;
		width: 49%;
	}

	.leftIcon {
		margin-right: 20upx;
		font-size: $list-title-l;
		vertical-align: middle;
	}

	.rightIcon {
		float: right;
		font-size: $list-title-l;
	}

	.bottomTitle {
		line-height: 70upx;
		vertical-align: middle;
	}
</style>
