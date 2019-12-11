<template>
	<view class="body">
		<navBar type="transparent" fontColor="#000000"></navBar>
		<view style="padding-top: 64px;"></view>

		<view class="autoInput">
			<input type="text" v-model="name" placeholder="请输入你的真实姓名" />
		</view>

		<view class="autoInput">
			<input type="text" v-model="phone" placeholder="请填写电话号码" />
		</view>

		<view class="autoInput">
			<input type="text" v-model="people" placeholder="参与人数" />
		</view>

		<view class="fixBox">
			<view class="info">*报名后可在报名中心查看哦*</view>
			<wButton text="报 名" :rotate="isRotate" @click.native="signUp" style="margin-top: 96upx;"></wButton>
		</view>
	</view>
</template>

<script>
	import navBar from '@/components/zhouWei-navBar/index.vue'
	import wButton from '@/components/watch-login/watch-button.vue' //button

	export default {
		components: {
			navBar,
			wButton
		},

		data() {
			return {
				contentId: '',
				name: '',
				phone: uni.getStorageSync('phone'),
				people: '',
				title: '',

				isRotate: false,
			}
		},

		onLoad(res) {
			this.contentId = res.id
			this.title = res.title
		},

		methods: {
			signUp() {
				if (this.name == '' || this.people == '' || this.title == '') {
					uni.showToast({
						title: '请将资料填写完整',
						icon: 'none'
					})
				} else {
					let cnt = {
						moduleId: this.$constData.module, // Long 模块编号
						contenId: this.contentId, // Long 内容id
						userId: uni.getStorageSync('userId'), // Long 用户id
						name: this.name, // String 用户名
						contentName: this.title, // String 内容标题
						phone: this.phone, // String 手机号
						number: this.people, //String 报名人数
					}
					this.$api.createEnroll(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							uni.reLaunch({
								url: '/pages/user/shopping/shopping'
							})
							uni.showToast({
								title: '报名成功'
							})
						} else {
							uni.showToast({
								title: '服务器错误',
								icon: 'none'
							})
						}
					})
				}
			},
		}
	}
</script>

<style lang="scss" scoped>
	.body {
		min-height: 100vh;
		// background-color: rgba($color: #000000, $alpha: 1.0);
	}

	.autoInput {
		margin: 0 auto;
		width: 70vw;
		border-radius: 2.5rem;
		box-shadow: 0 0 60rpx 0 rgba(43, 86, 112, .1);
		display: flex;
		align-items: center;
		height: 36rpx;
		color: #333333;
		padding: 32rpx;
		margin-top: 24rpx;
		margin-bottom: 60rpx;

		input {
			font-size: 28rpx;
			margin-left: 20rpx;
		}
	}

	.signUpBtn {
		display: block;
		margin: 0 auto;
		padding-left: 14px;
		padding-right: 14px;
		box-sizing: border-box;
		line-height: 100rpx;
		text-align: center;
		background: linear-gradient(to right, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.6));
		color: #FFFFFF;
		font-size: 30rpx;
		width: 601rpx;
		height: 100rpx;
		border-radius: 2.5rem;
	}

	.fixBox {
		position: fixed;
		bottom: 20upx;
		width: 100%;
	}

	.info {
		text-align: center;
		font-size: $list-info;
		color: #AAAAAA;
		margin-bottom: 10upx;
	}
</style>
