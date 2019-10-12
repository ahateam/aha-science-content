<template>
	<view class="forget">

		<view class="content">
			<!-- 主体 -->
			<view class="main">
				<view class="tips">若你忘记了密码，可在此重置新密码。</view>
				<wInput v-model="phoneData" type="text" maxlength="11" placeholder="请输入手机号码"></wInput>
				<wInput v-model="passData" type="password" maxlength="11" placeholder="请输入新密码" isShowPass></wInput>

				<wInput v-model="verCode" type="number" maxlength="4" placeholder="验证码" isShowCode codeText="获取重置码" setTime="30"
				 ref="runCode" @setCode="getVerCode()"></wInput>
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
				verCode: "", //验证码
				isRotate: false, //是否加载旋转
			}
		},
		components: {
			wInput,
			wButton
		},
	}
</script>

<style scoped>
	@import url("../../../components/watch-login/css/icon.css");
	@import url("../../../commen/main.css");
</style>
