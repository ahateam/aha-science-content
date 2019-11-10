<template>
	<view class="body">
		<view class="title"><text class="liveTag" v-if="item.type == constData.contentType[3].key&&item.liveStartTime <nowTime&&item.liveEndTime>nowTime">直播中</text>{{item.title}}</view>
		<view class="img">
			<image :src="item.imgList[0].src" mode="scaleToFill"></image>
			<view class="videoBtn" v-if="item.type == constData.contentType[1].key">
				<image :src="iconSrc" mode="widthFix"></image>
			</view>
		</view>
		<view class="bottom">
			<text class="upName" v-if="upName">{{upName}}</text>
			<text class="time" v-if="item.type != -1">{{time}}</text>
			<view v-else class="address"><text class="iconfont kk-location"></text>{{time}}</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: ['upName', 'time', 'item'],
		data() {
			return {
				iconSrc: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAEC0lEQVRoQ+2ajVEVMRDHdzuwA6ACpQKxArECtQKxAqECoQKhAqECoQKxAqEDrWCdn7Nx8vJy+bp3T4YhM2+O8S7J/rO7//2IKo9k6CPBIU9Acpo0s10ReSkiPA8mtH0tIncicqOqPDcyZmvEhX8rIu8cQI9gtyJyKSIXc0ENA3EAnxxAj/BT356LyEdV/TWyWDcQM3smIgA4mtjwXkQ4aX4Mngj3QkSYy5PfTmb+laoeLg7EzBDga8aEEB4TOVfVAKAoj2sUc+QXQC0PxMzY8Esi3W8ROVbV05FTDHPMDC1AEBzEcqY1AeLMQQxtXANuZvjMa/cb/i6Oqo9kQKCFI1WtLl7bfOq9mUHd3/w9ND1F5f+WKAJxn/gebQiIg1Y/mAEEUsDX8J0zVZ0iljoQZydAYLuMrYCIwXOQrYc2qREzw4E/RAu/X9KcRrUX5mWBODX+jBY/UdXjuZuNznd5PnscepNjtikgODJpRzCp3VFaHBU+MTEOkSDMIJ0hFKyMNSAZbZA2NMUJn7ujqjebABDWyDDnXpqb5YDEvnGvqsHZi7I5CMgBxiHDxRx5bmSYGZlyyADWmCwHBN8IwjdRH5Im3B+En5UIJuYFBeMnjFtV3Y/frwDJmNV+K/1NAGEvIv+pqp7MUU1GthXzSoHE+VSzWRU0EsuOaUDhw+aWmNdKOEiBxOzQlYkWNJIqAiAI0V0dmhkZNvkXYyUkpEDYhFJ17cOaWXQACUtxaPhgc9JpZvFBr+Rg/xNI8B+0w0lXR0LDzUCIoE0bNPpISdC1uJD7uJQVlzTyEIFQgFGhMpo10pVfDfgIwlAiU9s0af4h+gglARkE8WURZ98G/V65Fhal3zgg3qnqXpVK/IMG0/rhAOYExDh9KgZEcqy4DtlEirKpTgutqLjsnk5RnEaLWeaUhiY0srFOS1KxrqVPtTS+2by8xsdsnkONNN5G0pDCQcVmtcaoLYVVV63e0zDo8L+0OVgvrNy84lIXemRiM022CtjynWsabVCwMdpKXQeSOlZXcGwRsPWbJAgyLZvOPOh2UKZWn6xYS0Dibl/IVF+1VoytJ15wbqyCtmkwKdIZGnZZE+9tmbLI4mC8VRuDAG8xpo00sQFDi2iRJrabU2jGBYVVmbMKxJ0/dzfSXeGVzM3ZiRZt2tGsgmDdJiAFMGiHNPxijk+YGV1NsuHgD82aCB82A4lomdohvf8jrQm3s61XbzgzAMJtVXwWOPZhD7F0AXEwnBrqjzv1sRCACnfp/HvIdsNlTbiDn+pgDuVn3UCCxN4wA1Bods+xrr8R26/yuuuULh8p8D0nSzsTE8ldOZcAhttgKsUhAEM+Ujty1xIm1PJfOK7nCh/LM2xaNVDbfv8EZNsnXtvvDyrmF1FIBKIwAAAAAElFTkSuQmCC',
				constData: this.$constData,
				nowTime: this.getNowTime()
			}
		},
		methods: {
			getNowTime() {
				let time = Math.round(new Date() * 1)
				return time
			}
		}
	}
</script>

<style lang="scss" scoped>
	* {
		margin: 0;
	}

	.body {
		position: relative;
		width: 100%;
		min-height: 140upx;
	}

	.title {
		font-size: $list-title;
		color: $list-title-color;
		line-height: $list-title-line;
		padding-right: 220upx;
		box-sizing: border-box;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1; //需要显示时文本行数
		overflow: hidden;
	}

	.img {
		position: absolute;
		top: 0;
		right: 0;
		width: 220upx;
		height: 140upx;

		image {
			width: 100%;
			height: 100%;
		}
	}

	.videoBtn {
		position: absolute;
		background-color: rgba(0, 0, 0, 0.3);
		width: 100%;
		height: 100%;
		top: 0;

		image {
			position: absolute;
			left: 50%;
			top: 50%;
			margin-left: -30upx;
			margin-top: -30upx;
			width: 60upx;
			height: 60upx;
		}
	}

	.bottom {
		margin-top: 20upx;
		font-size: $list-info;
		color: $list-info-color;
		padding-right: 230upx;
		box-sizing: border-box;
	}

	.time {
		display: inline-block;
		margin-left: 20upx;
		vertical-align: middle;
	}

	.upName {
		display: inline-block;
		vertical-align: middle;
		max-width: 100%;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.address {
		margin-top: 10upx;

		.iconfont {
			font-size: $list-info;
		}
	}

	.liveTag {
		border: 1px solid $color-main;
		color: $color-main;
		border-radius: 10upx;
		font-size: $list-info;
		padding: 0 10upx;
		margin-right: 10upx;
	}
</style>
