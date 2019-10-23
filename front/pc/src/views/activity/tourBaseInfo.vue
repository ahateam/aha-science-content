<template>
	<div>
		<div style="margin: 5px;">
			<p><img :src="this.img"  style="width: 60%;" /></p>
			<p>基地id：{{this.id}}</p>
			<p>基地名称：{{this.name}}</p>
			<hr />
			<p>地点：{{this.address}}</p>
			<p>营业时间：{{this.workTime}}</p>
			<p>购票链接：<a :href=this.buyTicketsLink>{{this.buyTicketsLink}}</a></p>
			<p>创建时间：{{this.createTime}}</p>
			<hr />
			<p>简介：</p>
				<p v-html="info"></p>
			</el-button>
		</div>
	</div>
</template>

<script>
	export default {
		name: "tourBaseInfo",
		data() {
			return {
				id: '',
				name: '',
				address: '',
				buyTicketsLink: '',
				info: '',
				img: '',
				createTime: '',
				workTime: '',
				authList: this.$constData.authList
			}
		},
		methods: {
			timeFliter(val) {
				let timer = new Date(val)
				let dataTime = timer.toLocaleDateString() + ' ' + timer.toLocaleTimeString('chinese', {
					hour12: false
				})
				return dataTime
			},
		},
		mounted() {
			let info = this.$route.params.info;
			this.id = info.id
			this.name = info.name
			this.address = info.address
			this.buyTicketsLink = info.buyTicketsLink
			this.createTime = this.timeFliter(info.createTime)
			this.info = this.$util.tryParseJson(info.data).info
			this.img = this.$util.tryParseJson(info.data).img
			this.workTime = this.$util.tryParseJson(info.data).workTime
		}
	}
</script>

<style scoped>
	p {
		font-size: 18px;
	}
</style>
