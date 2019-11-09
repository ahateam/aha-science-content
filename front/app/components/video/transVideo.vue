<template>
	<view class="body">
		<text class="title"><text class="liveTag" v-if="type == constData.contentType[3].key">直播中</text>{{title}}</text>
		<view class="img">
			<image :src="imgSrc" mode="scaleToFill"></image>
			<view class="videoBtn" v-if="type == constData.contentType[1].key">
				<image :src="iconSrc" mode="widthFix"></image>
			</view>
		</view>
		<view>
			<text class="upName" v-if="upName">{{upName}}</text>
			<text class="time">{{time}}</text>
		</view>
	</view>
</template>

<script>
	export default {
		props: ['title', 'imgSrc', 'upName', 'time', 'type'],
		data() {
			return {
				iconSrc: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAEC0lEQVRoQ+2ajVEVMRDHdzuwA6ACpQKxArECtQKxAqECoQKhAqECoQKxAqEDrWCdn7Nx8vJy+bp3T4YhM2+O8S7J/rO7//2IKo9k6CPBIU9Acpo0s10ReSkiPA8mtH0tIncicqOqPDcyZmvEhX8rIu8cQI9gtyJyKSIXc0ENA3EAnxxAj/BT356LyEdV/TWyWDcQM3smIgA4mtjwXkQ4aX4Mngj3QkSYy5PfTmb+laoeLg7EzBDga8aEEB4TOVfVAKAoj2sUc+QXQC0PxMzY8Esi3W8ROVbV05FTDHPMDC1AEBzEcqY1AeLMQQxtXANuZvjMa/cb/i6Oqo9kQKCFI1WtLl7bfOq9mUHd3/w9ND1F5f+WKAJxn/gebQiIg1Y/mAEEUsDX8J0zVZ0iljoQZydAYLuMrYCIwXOQrYc2qREzw4E/RAu/X9KcRrUX5mWBODX+jBY/UdXjuZuNznd5PnscepNjtikgODJpRzCp3VFaHBU+MTEOkSDMIJ0hFKyMNSAZbZA2NMUJn7ujqjebABDWyDDnXpqb5YDEvnGvqsHZi7I5CMgBxiHDxRx5bmSYGZlyyADWmCwHBN8IwjdRH5Im3B+En5UIJuYFBeMnjFtV3Y/frwDJmNV+K/1NAGEvIv+pqp7MUU1GthXzSoHE+VSzWRU0EsuOaUDhw+aWmNdKOEiBxOzQlYkWNJIqAiAI0V0dmhkZNvkXYyUkpEDYhFJ17cOaWXQACUtxaPhgc9JpZvFBr+Rg/xNI8B+0w0lXR0LDzUCIoE0bNPpISdC1uJD7uJQVlzTyEIFQgFGhMpo10pVfDfgIwlAiU9s0af4h+gglARkE8WURZ98G/V65Fhal3zgg3qnqXpVK/IMG0/rhAOYExDh9KgZEcqy4DtlEirKpTgutqLjsnk5RnEaLWeaUhiY0srFOS1KxrqVPtTS+2by8xsdsnkONNN5G0pDCQcVmtcaoLYVVV63e0zDo8L+0OVgvrNy84lIXemRiM022CtjynWsabVCwMdpKXQeSOlZXcGwRsPWbJAgyLZvOPOh2UKZWn6xYS0Dibl/IVF+1VoytJ15wbqyCtmkwKdIZGnZZE+9tmbLI4mC8VRuDAG8xpo00sQFDi2iRJrabU2jGBYVVmbMKxJ0/dzfSXeGVzM3ZiRZt2tGsgmDdJiAFMGiHNPxijk+YGV1NsuHgD82aCB82A4lomdohvf8jrQm3s61XbzgzAMJtVXwWOPZhD7F0AXEwnBrqjzv1sRCACnfp/HvIdsNlTbiDn+pgDuVn3UCCxN4wA1Bods+xrr8R26/yuuuULh8p8D0nSzsTE8ldOZcAhttgKsUhAEM+Ujty1xIm1PJfOK7nCh/LM2xaNVDbfv8EZNsnXtvvDyrmF1FIBKIwAAAAAElFTkSuQmCC',
				constData:this.$constData
			}
		}
	}
</script>

<style lang="scss" scoped>
	* {
		margin: 0;
	}

	.body {
		width: 100%;
	}

	.title {
		font-size: $list-title;
		color: $list-title-color;
		line-height: $list-title-line;
	}

	.img {
		position: relative;
		width: 690upx;
		height: 388.125upx;
		margin: $box-margin-s 0upx;

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

	.upName {
		font-size: 26upx;
		color: #aaa;
	}

	.time {
		font-size: 26rpx;
		color: #aaa;
		margin-left: 20rpx;
	}
	
	.liveTag{
		border: 1px solid $color-main;
		color: $color-main;
		border-radius: 10upx;
		margin-right: 20upx;
		font-size: $list-info;
		padding: 0 10upx;
	}
</style>
