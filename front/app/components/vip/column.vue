<template>
	<!-- :style="{backgroundImage: 'url(' + JSON.parse(obj.data).src + ')' }" -->
	<view class="contentBox" :style="'background:'+randomColor">
		<navigator :url="'/pages/vip/courseView/courseView?id='+obj.id+'&title='+obj.title+'&time='+obj.createTime+'&color='+randomColor">
			<view class="columnTitle">{{ obj.title }}</view>
			<view class="columnTime">{{ getTime(obj.createTime) }}</view>
		</navigator>
	</view>
</template>

<script>
	export default {
		props: ['obj'],
		data() {
			return {
				randomColor: '',
				contentType: this.$constData.contentType,
			}
		},
		methods: {
			getTime(date) {
				this.getrandomColor()
				var t = parseInt(date)
				var a = new Date(t)
				var y = a.getFullYear()
				var m = 1 + a.getMonth()
				var d = a.getDate()
				var time = y + '年' + m + '月' + d + '日'
				return time
			},
			getrandomColor() { //随机赋值一种渐变色
				this.randomColor = this.$constData.colorData[Math.floor(Math.random() * this.$constData.colorData.length)]
			},
		}
	}
</script>

<style lang="scss" scoped>
	.clearBoth {
		clear: both;
	}

	.columnTitle {
		padding-top: 60upx;
		margin-left: 20upx;
		color: rgb(234, 242, 248);
		font-size: $list-title-l;
		font-weight: bold;
	}

	.columnTime {
		padding: 10upx 0 60upx 20upx;
		color: rgb(212, 228, 245);
		font-size: $list-info;
	}

	.contentBox {
		// background:linear-gradient(#69b5e1,#bedbed);
		border-radius: 5px;
		overflow: hidden;
		letter-spacing: 1px;
		position: relative;
		margin: $box-margin-top $box-margin-left;
		width: 690upx;
		box-sizing: border-box;

		.contentList {
			background-color: #fff;
			width: 94%;
			border-radius: 5px;
			margin: 10upx auto;
			margin-bottom: 20upx;

			.lists {
				position: relative;
				padding: 2vw;
				height: 24vw;
				font-size: 18px;
				line-height: 18px;

				.title {
					margin-bottom: 50upx;
					color: $list-title-color;
					font-size: $list-title;
					line-height: $list-title-line;
					box-sizing: border-box;
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2; //需要显示时文本行数
					overflow: hidden;
				}


				.msg {
					font-size: 14px;
					color: #dcdcdc;
				}
			}

			.imgBox {
				position: absolute;
				top: 50%;
				margin-top: -10vw;
				width: 20vw;
				height: 20vw;
				border-radius: 5px;
				overflow: hidden;

				image {
					width: 100%;
					height: 100%;
				}
			}


		}
	}

	.rightBox {
		position: absolute;
		margin-left: 24vw;
		margin-right: 20upx;
		top: 50%;
		margin-top: -50upx;
	}
</style>
