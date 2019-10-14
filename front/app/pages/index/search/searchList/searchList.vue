<template>
	<view>
		<navBar>
			<view class="search" @click="navBack">
				<!-- #ifdef APP-PLUS -->
				<image src="/static/zy-search/voice.png" mode="aspectFit" class="voice-icon"></image>
				<!-- #endif -->
				<input type="text" :value="searchText" readonly="readonly" />
				<image src="/static/zy-search/search.png" mode="aspectFit" class="search-icon"></image>
			</view>
		</navBar>
		<view v-for="(item,index) in contents" :key="index" @click="navToInfo(item)">
			<view v-if="item.type == constData.contentType[1].key||item.type == constData.contentType[2].key">
				<view v-if="item.show == constData.contentShow[0].key">
					<trans-video :title="item.title" :upName="item.user.name" :imgSrc="item.imgList[0].src" time="1小时前" :type="item.type"></trans-video>
				</view>

				<view v-else-if="item.show == constData.contentShow[1].key">
					<right-video :title="item.title" :upName="item.user.name" :imgSrc="item.imgList[0].src" time="1小时前" :type="item.type"></right-video>
				</view>

				<view v-else-if="item.show == constData.contentShow[2].key&&item.type == constData.contentType[2].key">
					<three-img :title="item.title" :upName="item.user.name" :imgList="item.imgList" time="1小时前" :type="item.type"></three-img>
				</view>
			</view>
			<view v-else-if="item.type == constData.contentType[0].key">
				<only-text :title="item.title" :upName="item.user.name" time="1小时前"></only-text>
			</view>
		</view>
		<uni-load-more :status="pageStatus"></uni-load-more>
	</view>
</template>

<script>
	import navBar from '@/components/zhouWei-navBar/index.vue'
	import transVideo from '@/components/video/transVideo.vue'
	import onlyText from '@/components/article/onlyText.vue'
	import rightVideo from '@/components/video/rightVideo.vue'
	import threeImg from '@/components/article/threeImg.vue'
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'

	export default {
		components: {
			uniLoadMore,
			threeImg,
			rightVideo,
			onlyText,
			transVideo,
			navBar
		},
		data() {
			return {
				searchText: '',
				contents: [],
				pageStatus: 'loading'
			};
		},
		onLoad(res) {
			this.searchText = res.value
			let cnt = {
				
			}
		},
		methods:{
			navBack(){
				uni.navigateBack({
					 "animationType": "none",
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.search {
		width: 600upx;
		margin: 20upx auto 0;
		position: relative;
		font-size: $list-title;
		top: 10upx;

		input {
			background-color: #F7F7F7;
			padding: 10upx 54upx;
			font-size: 28upx;
			border-radius: 50upx;
		}

		.voice-icon {
			width: 36upx;
			height: 36upx;
			padding: 16upx 20upx 16upx 0;
			position: absolute;
			left: 16upx;
			top: 4upx;
			z-index: 10;
		}

		.search-icon {
			width: 36upx;
			height: 36upx;
			padding: 16upx 20upx 16upx 0;
			position: absolute;
			right: 0;
			top: 4upx;
			z-index: 10;
		}
	}
</style>
