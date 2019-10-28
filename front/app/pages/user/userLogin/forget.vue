<template>
	<view class="forget">

		<view class="content">
			<!-- 主体 -->
			<view class="main">
				<view class="tips">若你忘记了密码，可在此重置新密码。</view>
				<wInput v-model="phoneData" type="text" maxlength="11" placeholder="请输入手机号码"></wInput>
				<wInput v-model="verCode" type="number" maxlength="6" placeholder="验证码" isShowCode codeText="获取重置码" setTime="30"
				 ref="runCode" @setCode="getVerCode()"></wInput>
				<wInput v-model="passData" type="password" maxlength="11" placeholder="请输入新密码" isShowPass></wInput>
				<wInput v-model="passData1" type="password" maxlength="11" placeholder="请再次输入密码" isShowPass></wInput>
			</view>

			<wButton text="重置密码" :rotate="isRotate" @click.native="startRePass()"></wButton>

		</view>
	</view>
</template>

<script>
	import wInput from '@/components/watch-login/watch-input.vue' //input
	import wButton from '@/components/watch-login/watch-button.vue' //button

	export default {
		data() {
			return {
				phoneData: "", //电话
				passData: "", //密码
				passData1: "", //确认密码
				verCode: "", //验证码
				isRotate: false, //是否加载旋转
			}
		},
		components: {
			wInput,
			wButton
		},
		methods: {
			getVerCode() {
				//获取验证码
				if (this.phoneData.length != 11) {
					uni.showToast({
						icon: 'none',
						position: 'bottom',
						title: '手机号不正确',
					})
					return false
				}
				
				let cnt = {
					moduleId: this.$constData.module, // Long 模块编号
					phone: this.phoneData, // Long 手机号
					type: 'forgetPwd',
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
			},

			//重置密码
			startRePass() {
				if (this.phoneData.length != 11) {
					uni.showToast({
						icon: 'none',
						title: '手机号不正确'
					})
					return false
				} else if (this.passData == '') {
					uni.showToast({
						icon: 'none',
						title: '密码不正确'
					})
					return false
				} else if (this.passData != this.passData1) {
					uni.showToast({
						icon: 'none',
						title: '密码不相同'
					})
					return false
				} else if (this.verCode.length != 6) {
					uni.showToast({
						icon: 'none',
						title: '验证码不正确'
					})
					return false
				}
				this.rePass()
			},
			rePass() {
				let cnt = {
					moduleId: this.$constData.module, // String 模块编号
					phone: this.phoneData, // String 手机号
					pwd: this.passData, // String 密码
					code: this.verCode, // String 验证码
				}
				this.$api.forgetPwd(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						uni.redirectTo({
							url: '/pages/user/userLogin/userLogin'
						})
						uni.showToast({
							title: '重置密码成功'
						})
					} else {
						uni.showToast({
							title: res.data.rm,
							icon: 'none'
						})
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
