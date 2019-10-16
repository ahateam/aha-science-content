<template>
	<view class="body">
		<navBar bgColor="#FB7299" :back="false" fontColor="#FFF"></navBar>
		<!-- 用户信息 -->
		<view class="userBox" v-if="loginStatus == false">
			<button class="leftButton" type="primary" @click="register">注册</button>
			<button class="rightButton" type="primary" @click="loginBtn">登录</button>
		</view>
		<view class="userBox" v-if="loginStatus == true">
			<view class="userHead">
				<image  :src="userHead" mode="aspectFill"></image>
			</view>
			<view class="userInfo">
				<view class="userName">
					{{userName}}
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import navBar from '@/components/zhouWei-navBar/index.vue'
	
	export default {
		components:{
			navBar,
		},
		data() {
			return {
				userName: '',
				userHead: '',
				userId: '',

				loginStatus: false,

				providerList: [],
			};
		},
		onLoad() {
			
		},
		onShow() {
			let userId = uni.getStorageSync('userId')
			if (userId != '' && userId != '1234567890') {
				this.userName = uni.getStorageSync('userName')
				this.userHead = uni.getStorageSync('userHead')
				this.userId = userId
				this.loginStatus = true
			} else {
				console.log('未登录')
			}
		},
		methods:{
			register(){
				uni.navigateTo({
					url:'/pages/user/userLogin/register'
				})
			},
			loginBtn() {
				uni.navigateTo({
					url: '/pages/user/userLogin/userLogin'
				})
			},
		}
	};
</script>

<style lang="scss" scoped>
	.userBox{
		background-color: $color-main;
		width: 100vw;
		padding: $box-margin-top 0 40upx;
		text-align: center;
		
		button{
			display: inline-flex;
			margin: 0 $box-margin-left;
			padding: 0 60upx;
			font-size: $list-title;
			&:after{
				border: none;
			}
		}
		
		.leftButton{
			background-color: $color-button;
		}
		
		.rightButton{
			background-color: #FFFFFF;
			color: $color-main;
		}
	}
	
	.userHead{
		display: inline-flex;
		margin-left: $box-margin-left;
		border: 2px solid #FFFFFF;
		width: 120upx;
		height: 120upx;
		border-radius: 100%;
		overflow: hidden;
		image{
			width: 100%;
			height: 100%;
		}
	}
	
	.userInfo{
		display: inline-flex;
		margin-left: $box-margin-left;
	}
	
	.userName{
		color: #FFFFFF;
	}
</style>
