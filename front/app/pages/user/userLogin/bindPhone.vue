<template>
	<view>
		<wInput v-model="phoneData" type="text" maxlength="11" placeholder="手机号"></wInput>
		<wInput v-model="verCode" type="number" maxlength="6" placeholder="验证码" isShowCode ref="runCode" @setCode="getVerCode"></wInput>
		<wButton text="绑定手机" :rotate="isRotate" @click.native="bindingPhone" style="margin-top: 96upx;"></wButton>
	</view>
</template>

<script>
	import wInput from '@/components/watch-login/watch-input.vue' //input
	import wButton from '@/components/watch-login/watch-button.vue' //button
	export default {
		components: {
			wInput,
			wButton
		},
		data() {
			return {
				phoneData: '', //手机
				verCode: '', //验证码
				userId: '', //用户id
				isRotate: false,
			}
		},
		onLoad(res) {
			this.userId = res.id
		},
		methods: {
			getVerCode() {
				if (this.phoneData.length == 11) {
					let cnt = {
						moduleId: this.$constData.module, // Long 模块编号
						phone: this.phoneData, // Long 手机号
					}
					this.$api.sendSms(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							uni.showToast({
								title: '验证码已发送'
							})
							this.$refs.runCode.$emit('runCode'); //触发倒计时（一般用于请求成功验证码后调用）
							console.log(this.$util.tryParseJson(res.data.c))
						} else {
							uni.showToast({
								title: '验证码获取失败！',
								icon: 'none'
							})
						}
					})
				} else {
					uni.showToast({
						title: '请输入正确的手机号',
						icon: 'none'
					})
				}

			},
			bindingPhone() {
				if (this.isRotate == true) {
					return
				}
				if (this.verCode.length < 6) {
					uni.showToast({
						title: '请输入正确验证码',
						icon: 'none'
					})
				} else if (this.phoneData.length < 11) {
					uni.showToast({
						title: '请输入正确手机号',
						icon: 'none'
					})
				} else {
					this.isRotate = true
					let cnt = {
						moduleId: this.$constData.module, // String 模块编号
						id: this.userId, // Long 用户id
						phone: this.phoneData, // String 手机号
						code: this.verCode, //String 
					}
					console.log(cnt)
					this.$api.bindingPhone(cnt, (res) => {
						if (res.data.rc == this.$util.RC.SUCCESS) {
							let userData = this.$util.tryParseJson(res.data.c)
							let userId = userData.id
							let userName = userData.name
							let userHead = this.$util.tryParseJson(userData.ext).userHead
							/* 将用户信息存至本地 */
							uni.setStorageSync('userId', userId)
							uni.setStorageSync('userName', userName)
							uni.setStorageSync('userHead', userHead)
							uni.switchTab({
								url: '/pages/user/user'
							})
							uni.showToast({
								title: '已登录！'
							})
						} else {
							uni.showToast({
								title: res.data.rm,
								icon: 'none'
							})
						}
					})
				}


			},

		}
	}
</script>

<style scoped>
	@import url("../../../components/watch-login/css/icon.css");
	@import url("../../../commen/main.css");
</style>
