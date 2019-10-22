<template>
	<view>
		<wInput v-model="phoneData" type="text" maxlength="11" placeholder="手机号"></wInput>
		<wInput v-model="verCode" type="number" maxlength="4" placeholder="验证码" isShowCode ref="runCode" @setCode="getVerCode()"></wInput>
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

			},
			bindingPhone() {
				if (this.isRotate == true) {
					return
				}
				this.isRotate = true
				let cnt = {
					moduleId: this.$constData.module, // String 模块编号
					id: this.userId, // Long 用户id
					phone: this.phoneData, // String 手机号
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
						console.log(res.data.c)
					}
				})
			},

		}
	}
</script>

<style scoped>
	@import url("../../../components/watch-login/css/icon.css");
	@import url("../../../commen/main.css");
</style>
