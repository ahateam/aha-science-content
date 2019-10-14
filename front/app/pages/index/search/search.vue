<template>
	<view>
		<navBar>
			<view class="search">
				<!-- #ifdef APP-PLUS -->
				<image src="/static/zy-search/voice.png" mode="aspectFit" @click="startRecognize()" class="voice-icon"></image>
				<!-- #endif -->
				<input maxlength="20" focus type="text" value="" confirm-type="search" @confirm="searchStart()" placeholder="请输入关键词搜索"
				 v-model.trim="searchText" />
				<image src="/static/zy-search/search.png" mode="aspectFit" @click="searchStart()" class="search-icon"></image>
			</view>
		</navBar>
		<template v-if="isBlock">
			<view class="s-block" v-if="hList.length > 0">
				<view class="header">
					历史记录
					<image src="/static/zy-search/delete.png" mode="aspectFit" @click="delhistory"></image>
				</view>
				<view class="list">
					<view v-for="(item,index) in hList" :key="index" @click="keywordsClick(item)">{{item}}</view>
				</view>
			</view>
			<view class="wanted-block" v-if="showWant">
				<view class="header">猜你想搜的</view>
				<view class="list">
					<view v-for="(item,index) in wantList" :key="index" @click="keywordsClick(item)">{{item}}</view>
				</view>
			</view>
		</template>
		<template v-else>
			<view class="s-circle" v-if="hList.length > 0">
				<view class="header">
					历史记录
					<image src="/static/zy-search/delete.png" mode="aspectFit" @click="delhistory"></image>
				</view>
				<view class="list">
					<view v-for="(item,index) in hList" :key="index" @click="keywordsClick(item)">{{item}}</view>
				</view>
			</view>
			<view class="wanted-circle" v-if="showWant">
				<view class="header">猜你想搜的</view>
				<view class="list">
					<view v-for="(item,index) in wantList" :key="index" @click="keywordsClick(item)">{{item}}</view>
				</view>
			</view>
		</template>
	</view>
</template>

<script>
	import navBar from '@/components/zhouWei-navBar/index.vue'

	export default {
		components: {
			navBar
		},
		data() {
			return {
				showWant: true, //猜你想搜显示
				searchText: '', //搜索内容
				hList: uni.getStorageSync('search_cache'), //搜索历史
				wantList: ['栏目1', '栏目2', '栏目3', '栏目4'], //初始化推荐
			};
		},
		methods: {
			startRecognize() { //语音输入
				let options = {}
				options.engine = 'iFly'
				options.punctuation = false // 是否需要标点符号 
				options.timeout = 10 * 1000
				plus.speech.startRecognize(options, (s) => {
					this.searchText = this.searchText + s
				})
			},
			searchStart() { //触发搜索
				if (this.searchText == '') {
					uni.showToast({
						title: '请输入关键字',
						icon: 'none',
						duration: 1000
					})
					return false
				} else {
					uni.getStorage({
						key: 'search_cache',
						success: (res) => {
							let list = res.data
							console.log(list)
							console.log('-------------------------')
							uni.navigateTo({
								url: `/pages/index/search/searchList/searchList?value=${this.searchText}`,
								 "animationType": "none",
							})
							if (list.length > 5) {
								for (let item of list) {
									if (item == this.searchText) {
										return false
									}
								}
								list.pop()
								list.unshift(this.searchText)
							} else {
								for (let item of list) {
									if (item == this.searchText) {
										return false
									}
								}
								list.unshift(this.searchText)
							}
							this.hList = list
							uni.setStorage({
								key: 'search_cache',
								data: this.hList
							})

						},
						fail: () => {
							console.log('error')
							this.hList = []
							this.hList.push(this.searchText)
							uni.setStorage({
								key: 'search_cache',
								data: this.hList
							})
						}
					})
				}
			},
			keywordsClick(item) { //推荐搜索
				this.searchText = item;
			},
			delhistory() { //清空历史记录
				this.hList = []
				uni.setStorage({
					key: 'search_cache',
					data: []
				})
			},
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

	.s-block {
		margin-top: 30upx;

		.header {
			font-size: 32upx;
			padding: 30upx;
			position: relative;

			image {
				width: 36upx;
				height: 36upx;
				padding: 10upx;
				position: absolute;
				right: 40upx;
				top: 24upx;
			}
		}

		.list {
			display: flex;
			flex-wrap: wrap;

			view {
				width: 50%;
				color: #8A8A8A;
				font-size: 28upx;
				box-sizing: border-box;
				text-align: center;
				padding: 20upx 0;
				border-top: 2upx solid #FFF;
				border-left: 2upx solid #FFF;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
				background-color: #F7F7F7;
			}
		}
	}

	.s-circle {
		margin-top: 30upx;

		.header {
			font-size: 32upx;
			padding: 30upx;
			border-bottom: 2upx solid #F9F9F9;
			position: relative;

			image {
				width: 36upx;
				height: 36upx;
				padding: 10upx;
				position: absolute;
				right: 40upx;
				top: 24upx;
			}
		}

		.list {
			display: flex;
			flex-wrap: wrap;
			padding: 0 30upx 20upx;

			view {
				padding: 8upx 30upx;
				margin: 20upx 30upx 0 0;
				font-size: 28upx;
				color: #8A8A8A;
				background-color: #F7F7F7;
				box-sizing: border-box;
				text-align: center;
				border-radius: 20upx;
			}
		}
	}

	.wanted-block {
		margin-top: 30upx;

		.header {
			font-size: 32upx;
			padding: 30upx;
		}

		.list {
			display: flex;
			flex-wrap: wrap;

			view {
				width: 50%;
				color: #8A8A8A;
				font-size: 28upx;
				box-sizing: border-box;
				text-align: center;
				padding: 20upx 0;
				border-top: 2upx solid #FFF;
				border-left: 2upx solid #FFF;
				background-color: #F7F7F7;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}
		}
	}

	.wanted-circle {
		margin-top: 30upx;

		.header {
			font-size: 32upx;
			padding: 30upx;
		}

		.list {
			display: flex;
			flex-wrap: wrap;
			padding: 0 30upx 20upx;

			view {
				padding: 8upx 30upx;
				margin: 20upx 30upx 0 0;
				font-size: 28upx;
				color: #8A8A8A;
				background-color: #F7F7F7;
				box-sizing: border-box;
				text-align: center;
				border-radius: 20upx;
			}
		}
	}
</style>
