<template>
	<view>
		<navBar type="transparent" fontColor="#FFF"></navBar>
		<view class="imgBox">
			<image :src="imgSrc" mode="aspectFill"></image>
			<view class="titleBox">
				{{placeTitle}}
			</view>
		</view>
		<view class="placeInfo">
			{{placeInfo}}
		</view>

		<view class="autoTitle">
			活动地点：
			<view class="autoInfo">
				{{address}}
			</view>
		</view>

		<view class="autoTitle">
			活动时间：
			<view class="autoInfo">
				{{time}}
			</view>
		</view>
		
		<view class="fixBox">
			<button class="signUpBtn" type="primary">报名</button>
		</view>
	</view>
</template>

<script>
	import navBar from '@/components/zhouWei-navBar/index.vue'
	import wButton from '@/components/watch-login/watch-button.vue' //button

	export default {
		components: {
			navBar,
			wButton,

		},
		data() {
			return {
				imgSrc: 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1048618502,452709363&fm=26&gp=0.jpg',
				placeTitle: '遵义科技基地',
				placeInfo: '《遵义科技》主办单位遵义市科学技术局 遵义市生产力促进中心。本刊坚持为社会主义服务的方向，坚持以马克思列宁主义、毛泽东思想和邓小平理论为指导，贯彻“百花齐放、百家争鸣”和“古为今用、洋为中用”的方针，坚持实事求是、理论与实际相结合的严谨学风，传播先进的科学文化知识，弘扬民族优秀科学文化，促进国际科学文化交流，探索防灾科技教育、教学及管理诸方面的规律，活跃教学与科研的学术风气，为教学与科研服务。',
				address: '遵义市新蒲新区湿地公园科技路',
				time: '2019年10月15日',

				isRotate: 'false'
			}
		},
		methods: {
			signUp() {

			},
		}
	}
</script>

<style lang="scss" scoped>
	@import url("../../../components/watch-login/css/icon.css");
	@import url("../../../commen/main.css");

	.imgBox {
		position: relative;
		width: 100vw;
		height: 56.25vw;

		image {
			width: 100%;
			height: 100%;
		}
	}

	.titleBox {
		position: absolute;
		bottom: 0;
		box-sizing: border-box;
		width: 100%;
		background-color: rgba($color: #000000, $alpha: 0.5);
		padding: $box-margin-top $box-margin-left;
		color: #FFFFFF;
		font-size: $list-title-l;
		font-weight: bold;
	}

	.placeInfo {
		box-sizing: border-box;
		padding: $box-margin-top $box-margin-left;
		font-size: $list-title;
		text-indent: 2em;
	}

	.autoTitle {
		box-sizing: border-box;
		padding: $box-margin-top $box-margin-left;
		font-size: $list-title;
		font-weight: bold;
	}

	.autoInfo {
		box-sizing: border-box;
		padding: $box-margin-top $box-margin-left 0;
		font-size: $list-title;
		font-weight: normal;
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
	
	.fixBox{
		position: fixed;
		bottom: 20upx;
		width: 100%;
	}
</style>
