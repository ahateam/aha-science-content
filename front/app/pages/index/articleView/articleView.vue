<template>
	<view class="content">
		<view v-if="pageStatus == false">
			<uniLoadMore status="loading"></uniLoadMore>
		</view>

		<view class="scroll" scroll-y v-if="pageStatus">
			<view class="scroll-content">
				<view class="introduce-section">
					<text class="title">{{detailData.title}}</text>
					<view class="introduce">
						<text>{{upInfo.name}}</text>
						<!-- <text>105阅读</text> -->
						<text>{{detailData.time}}</text>
						<button @click="createUserFavorite" class="followBtn" v-if="followStatus == false">关注</button>
						<button @click="delUserFavorite" class="followBtn" v-else-if="followStatus == true">已关注</button>
					</view>

					<view class="otherInfo" v-if="contentRemark||mp3Src||videoSrc">
						<view class="remark" v-if="contentRemark">
							{{contentRemark}}
						</view>
						<audio v-if="mp3Src" :src="mp3Src" :poster="mp3Img" name="mp3" :action="audioAction" controls></audio>
						<view>
							<video v-if="videoSrc" :src="videoSrc" controls></video>
						</view>
					</view>

					<view class="articleInfo">
						<view v-for="(item,index) in flow" :key="index">
							<view v-if="item.type == 'textarea'">
								<rich-text :nodes="item.value"></rich-text>
							</view>
							<view class="contentImg" v-else-if="item.type == 'img'">
								<image :src="item.value" mode="widthFix"></image>
							</view>
						</view>
					</view>

					<view class="writerBox" v-if="contentSource||contentAuthor">
						<view v-if="contentSource">来源:<text>{{contentSource}}</text></view>
						<view v-if="contentAuthor">原作者:<text>{{contentAuthor}}</text></view>
					</view>

					<!-- 点赞分享 -->
					<view class="actions">
						<view class="action-item">
							<button type="primary" @click="upvote(contentId)">
								<i class="yticon iconfont kk-dianzan" :class="{currentIcon:upvoteStatus}"></i>
								<text>{{contentUpvote}}赞</text>
							</button>
						</view>
						<view class="action-item">
							<button type="primary" @click="shareBtn">
								<i class="yticon iconfont kk-share"></i>
								<text>分享</text>
							</button>
						</view>

						<!-- <view class="action-item">
							<button type="primary" @click="createHb">
								<i class="yticon iconfont kk-friendzone centerBox"></i>
								<text>朋友圈</text>
							</button>
						</view> -->
					</view>
					<!-- 点赞分享end -->
				</view>
				<!-- 评论区 -->
				<view style="width: 100%;">
					<comment :comment="comment" @upZan="upZan" @delZan="delZan" @repaly="openReplay"></comment>
				</view>
				<!-- 评论end -->
			</view>
		</view>

		<view class="poster" v-if="posterShow" @click="hiddenPoster">
			<view class="posterImg" @click.stop>
				<image ref="posterImg" :src="posterImg " mode="widthFix" @click="showImg"></image>
			</view>
			<view class="posterBtn">
				<view class="stopBtn" @click.stop>
					<button type="primary" @click="saveImg">保存图片至本地</button>
				</view>
			</view>
		</view>

		<tki-qrcode class="hiddenBox" cid="qrcode1" ref="qrcode" :val="val" :size="size" :unit="unit" :background="background"
		 :foreground="foreground" :pdground="pdground" :icon="icon" :iconSize="iconsize" :lv="lv" :onval="onval" :loadMake="loadMake"
		 :usingComponents="true" @result="qrR" />
		<canvas class="hiddenBox canvasBox" canvas-id="firstCanvas"></canvas>
		<canvas class="hiddenBox upHeadBox" canvas-id="upHeadCanvas"></canvas>

		<view class="bottom" v-if="pageStatus">
			<view class="input-box" v-if="!replayBox">
				<text class="yticon icon-huifu"></text>
				<input class="input" type="text" placeholder="点评一下把.." v-model="commentContent" placeholder-style="color:#adb1b9;" />
			</view>
			<text class="confirm-btn" @click="createComment" v-if="!replayBox">提交</text>
			<view class="input-box" v-if="replayBox">
				<text class="yticon icon-huifu"></text>
				<input focus class="input" type="text" :placeholder="'@'+repalyName+'：'" v-model="commentContent" placeholder-style="color:#adb1b9;" />
			</view>
			<text class="confirm-btn" @click="replayAfter" v-if="replayBox">提交</text>
		</view>
	</view>
</template>

<script>
	import tkiQrcode from '@/components/tki-qrcode/tki-qrcode.vue'
	import comment from '@/components/comment/comment.vue'
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'

	let context = uni.createCanvasContext('firstCanvas')
	let upHead = uni.createCanvasContext('upHeadCanvas')

	export default {
		components: {
			tkiQrcode,
			comment,
			uniLoadMore
		},
		data() {
			return {
				detailData: {}, //内容所有相关数据
				flow: {}, //文章数据
				upInfo: {}, //上传者数据
				contentId: '', //内容id
				userHead: '',

				/* 分享朋友圈 */
				val: '', // 要生成的二维码值
				size: 200, // 二维码大小
				unit: 'px', // 单位
				background: '#FFFFFF', // 背景色
				foreground: '#000000', // 前景色
				pdground: '#32dbc6', // 角标色
				icon: '/static/logo.png', // 二维码图标
				iconsize: 30, // 二维码图标大小
				lv: 3, // 二维码容错级别 ， 一般不用设置，默认就行
				onval: true, // val值变化时自动重新生成二维码
				loadMake: true, // 组件加载完成后自动生成二维码
				src: '', // 二维码生成后的图片地址或base64

				posterImg: '',
				posterShow: false, //控制分享页的显示
				/* 分享朋友圈end */

				/* 点赞 */
				commentId: 0, //点赞对象id
				contentUpvote: 0, //文章点赞数
				upvoteStatus: false, //文章点赞状态
				/* 点赞end */

				/* 评论 */
				comment: [], //评论列表
				totalCount: 0, //文章评论数
				commentContent: '', //评论内容

				//二級回復
				repalyId: '', //评论id
				replayBox: false, //二级回复盒子开关
				repalyIndex: '', //回复列表下标
				repalyName: '', //回复的人的name
				/* 评论end */

				followStatus: false,

				pageStatus: false,

				followId: '',

				// 其他信息
				contentRemark: '',

				// 音频
				mp3Src: '',
				mp3Img: '',
				videoSrc: '',
				audioAction: {
					method: 'pause'
				},

				contentSource: '', //来源
				contentAuthor: '', //原作者

			}
		},
		onLoad(res) {
			let src = ''
			if (res.q) {
				src = res.q
				let params = this.$commen.getSplit(src)
				this.contentId = params.id
			} else {
				this.contentId = res.id
			}

			this.getContentById()
			this.getAppraiseCount()

			this.judgeAppraise()
		},
		methods: {
			judgeAppraise() {
				let cnt = {
					ownerId: this.contentId, // Long 内容编号
					userId: uni.getStorageSync('userId'), // Long 用户编号
					value: this.$constData.appraise[0].key, // Byte 状态0点赞1踩
				}
				this.$api.judgeAppraise(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.upvoteStatus = this.$util.tryParseJson(res.data.c)
					} else {
						console.log('error')
					}
				})
			},

			//顯示二級回復輸入框
			openReplay(id, index, name) {
				if (id) {
					console.log('id:' + id + 'index:' + index + 'name:' + name)
					this.repalyId = id
					this.repalyIndex = index
					this.repalyName = name
					this.replayBox = true
				} else {
					uni.showToast({
						title: '无法回复',
						icon: 'none'
					})
				}
			},

			replayAfter() {
				let userId = uni.getStorageSync('userId')
				if (userId == '' || userId == '1234567890') {
					uni.showToast({
						title: '登录后可评论',
						icon: 'none'
					})
					return
				} else if (this.commentContent == '') {
					uni.showToast({
						title: '请输入回复内容',
						icon: 'none'
					})
					return
				}

				let cnt = {
					replyId: this.repalyId, // Long 回复评论id
					upUserId: uni.getStorageSync('userId'), // Long 提交者编号
					upUserHead: uni.getStorageSync('userHead'), // String 提交者头像
					upUserName: uni.getStorageSync('userName'), // String 提交者昵称
					// toUserId: toUserId, // Long 目标用户编号
					text: this.commentContent, // String 正文
					// toUserId: toUserId, // Long <选填> 目标用户编号
					// toUserName: toUserName, // String <选填> 目标用户昵称
				}
				this.$api.createComment(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.replayBox = false
						uni.showToast({
							title: '回复成功！',
						})
						let user = {
							upUserName: uni.getStorageSync('userName'),
							text: this.commentContent
						}
						this.comment[this.repalyIndex].comment.splice(0, 0, user)
						this.commentContent = ''
					} else {
						uni.showToast({
							title: res.data.rm,
							icon: 'none'
						})
					}
				})
			},
			//查询是否关注
			getBoolFavoriteUser(userId) {
				let cnt = {
					moduleId: this.$constData.module, // String 模块编号
					userId: uni.getStorageSync('userId'), // Long 用户id
					concernId: userId, // Long 被关注用户id,true没有关注
					count: 10, // int 
					offset: 0, // int 
				}
				this.$api.getBoolFavoriteUser(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let data = this.$util.tryParseJson(res.data.c)
						this.followStatus = data.bool
						if (this.followStatus == true) {
							this.followId = data.info.id
						}
					} else {
						console.log('失败')
					}
				})
			},

			//取关
			delUserFavorite() {
				let cnt = {
					moduleId: this.$constData.module, // String 模块编号
					id: this.followId, // Long id
				}
				this.$api.delUserFavorite(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						uni.showToast({
							title: '已取消关注',
							icon: 'none'
						})
						this.followStatus = false
					} else {
						uni.showToast({
							title: '服务器错误！',
							icon: 'none'
						})
					}
				})
			},

			//创建关注
			createUserFavorite() {
				let userId = uni.getStorageSync('userId')
				if (userId == '' || userId == '1234567890') {
					uni.showToast({
						title: '登录后可关注',
						icon: 'none'
					})
					return
				}

				let cnt = {
					moduleId: this.$constData.module, // String 模块编号
					concernId: this.upInfo.id, // Long 被关注用户id
					userId: userId, // Long 用户id
				}
				this.$api.createUserFavorite(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						uni.showToast({
							title: '关注成功'
						})
						this.followStatus = true
						this.followId = this.$util.tryParseJson(res.data.c).id
					} else {
						uni.showToast({
							title: res.data.rm,
							icon: 'none'
						})
						this.followStatus = false
					}
				})
			},

			//更新讚數
			delZan(index) {
				this.comment[index].appraiseCount -= 1
				this.comment[index].isAppraise = false
			},

			//更新赞数
			upZan(index) {
				this.comment[index].appraiseCount += 1
				this.comment[index].isAppraise = true
			},

			/* 评论 */
			createComment() {
				let userId = uni.getStorageSync('userId')
				let status = uni.getStorageSync('status')
				if (userId == '' || userId == '1234567890') {
					uni.showToast({
						title: '登录后可评论',
						icon: 'none'
					})
					return
				} else if (this.commentContent == '') {
					uni.showToast({
						title: '请输入回复内容',
						icon: 'none'
					})
					return
				}

				let cnt = {
					// module: this.$constData.module, // String 隶属
					ownerId: this.contentId, // Long 内容编号
					upUserId: userId, // Long 用户编号
					text: this.commentContent, // String 评论内容
					// data: [], // String 其他数据
					atUserId: 1, // Long <选填> @对象编号
					atUserName: '无', // String <选填> @对象名称
					title: '无', // String <选填> 标题
					ext: '无', // String <选填> 扩展
				};
				this.$api.createReply(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						uni.showToast({
							title: '评论成功',
							duration: 1000
						})
						let time = new Date()
						let y = time.getFullYear()
						let m = 1 + time.getMonth()
						let d = time.getDate()

						let data = {
							text: this.commentContent,
							time: `${y}-${m}-${d}`,
							jsAdd: true,
							name: uni.getStorageSync('userName'),
							head: uni.getStorageSync('userHead')
						}
						this.comment.splice(0, 0, data)
						console.log(this.comment)
						this.commentContent = ''
					} else {
						uni.showToast({
							title: res.data.rm,
							icon: 'none'
						})
					}
				})
			},

			//获取评论列表
			getCommentByContentId() {
				let cnt = {
					// module: this.$constData.module, // String 隶属
					ownerId: this.contentId, // Long 内容编号
					// status: status, // Byte <选填> 审核状态，不填表示全部，STATUS_UNEXAMINED = 0未审核，STATUS_ACCEPT = 1已通过，STATUS_REJECT = 2已回绝
					userId: uni.getStorageSync('userId'), // Long <选填> 当前用户id
					orderDesc: true, // Boolean 是否降序（较新的排前面）
					count: 10, // Integer 
					offset: 0, // Integer 
				}
				this.$api.getCommentByContentId(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						console.log('评论接口返回数据')
						console.log(this.$util.tryParseJson(res.data.c))
						console.log('~~~~~~~~~~~~~~~~~~~~~~~~')
						// this.totalCount = this.$util.tryParseJson(res.data.c).totalCount
						// this.contentUpvote = this.$util.tryParseJson(res.data.c).contentUpvote
						let comment = this.$util.tryParseJson(res.data.c)
						for (let i = 0; i < comment.length; i++) {
							comment[i].jsAdd = false
							let time = new Date(comment[i].createTime)
							let y = time.getFullYear()
							let m = 1 + time.getMonth()
							let d = time.getDate()
							comment[i].time = `${y}-${m}-${d}`
						}
						this.comment = comment
					} else {
						uni.showToast({
							title: '评论获取失败',
							icon: 'none',
							duration: 1000
						})
					}
				})
			},
			/* 评论end */

			/* 点赞相关 */
			//获取赞数
			getAppraiseCount() {
				let cnt = {
					ownerId: this.contentId, // Long 内容编号
					// userId: userId, // Long <选填> 用户编号
					value: this.$constData.appraise[0].key, // String <选填> 状态
				}
				this.$api.getAppraiseCount(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let data = this.$util.tryParseJson(res.data.c)
						this.contentUpvote = data.appraiseCount
						this.upvoteStatus = data.isAppraise
					} else {
						console.log('erorr')
					}
				})
			},

			delAppraise(id) {
				let cnt = {
					ownerId: id,
					userId: uni.getStorageSync('userId')
				}
				this.$api.delAppraise(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						this.upvoteStatus = false
						this.contentUpvote -= 1
					}
				})
			},

			//点赞
			upvote(conid, index) {
				let userId = uni.getStorageSync('userId')
				if (userId == '' || userId == '1234567890') {
					uni.showToast({
						title: '请登录',
						duration: 1000,
						icon: 'none'
					})
					return
				}
				if (this.upvoteStatus == true) {
					this.delAppraise(conid)
					return
				}
				this.upvoteStatus = true
				this.commentId = conid
				this.createUpvote(index)
			},
			createUpvote(index) {
				let userId = uni.getStorageSync('userId')
				let cnt = {
					ownerId: this.commentId, // Long 内容编号/评论编号
					userId: 0 + userId, // Long 用户编号
					value: this.$constData.appraise[0].key //Byte 状态
				}
				this.$api.createUpvote(cnt, (res) => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						if (this.$util.tryParseJson(res.data.c).value == 10) {
							uni.showToast({
								title: '你已经赞过他啦',
								icon: 'none'
							})
							return
						}
						uni.showToast({
							title: '点赞成功'
						})
						this.contentUpvote += 1
					} else {
						uni.showToast({
							title: res.data.c,
							icon: 'none'
						})
					}
				})
			},
			/* 点赞end */


			/* 分享朋友圈 */
			//开始生成海报
			createHb() {
				uni.showLoading({
					title: '生成中'
				})
				this.val = `https://wx.zyxhj.cn?id=${this.contentId}&id1=${this.id1}` //this.val的值改变后自动调取this.qrR()
			},

			qrR(res) { //生成二维码的图片地址
				this.src = res
				this.createCanvas()
			},

			// 生成背景
			createCanvas() {
				//生成背景
				context.setFillStyle('#FFFFFF')
				context.fillRect(0, 0, 450, 800)
				let imgList = this.$util.tryParseJson(this.detailData.data).imgList
				let bgImg = ''
				if (imgList.length > 0) {
					uni.downloadFile({
						url: imgList[0].src,
						success: (res) => {
							context.drawImage(res.tempFilePath, 0, 0, 450, 500)
							this.getUpHead()
						}
					})
				}
			},

			//生成up圆形头像
			getUpHead() {
				let img = this.upInfo.head
				console.log(img)
				console.log('头像地址')
				let imgSrc = ''
				uni.downloadFile({
					url: img,
					success: (res) => {
						imgSrc = res.tempFilePath
						console.log(imgSrc)
						upHead.arc(50, 50, 50, 0, 2 * Math.PI)
						upHead.clip()
						upHead.drawImage(imgSrc, 0, 0, 100, 100)
						upHead.draw()
						setTimeout(() => { //延时生成图片
							uni.canvasToTempFilePath({
								x: 0,
								y: 0,
								width: 100,
								height: 100,
								destWidth: 100,
								destHeight: 100,
								canvasId: 'upHeadCanvas',
								success: (res) => {
									// 在H5平台下，tempFilePath 为 base64
									context.drawImage(res.tempFilePath, 20, 520, 50, 50)
									this.createPoster()
								},
								fail: (error) => {
									console.log(error)
								}
							})
						}, 400)
					}
				})
			},

			//最终生成海报
			createPoster() {
				// 二维码图片
				context.drawImage(this.src, 280, 520, 150, 150)

				// 文字
				context.setFillStyle('#000000')
				context.font = '18px Arial'
				context.fillText(this.upInfo.name, 80, 550)

				context.font = '20px Arial'
				context.fillText(this.detailData.title, 20, 610)

				context.font = '16px Arial'
				context.setFillStyle('#aaa')
				context.fillText('长按扫码查看详情', 20, 650)

				//生成画布
				context.draw()

				// let that = this

				setTimeout(() => { //延时生成图片
					uni.canvasToTempFilePath({
						x: 0,
						y: 0,
						width: 450,
						height: 690,
						destWidth: 450,
						destHeight: 690,
						canvasId: 'firstCanvas',
						success: (res) => {
							uni.hideLoading()
							// 在H5平台下，tempFilePath 为 base64
							this.posterImg = res.tempFilePath
							console.log(this.posterImg)
						}
					})
					this.showHb()
				}, 300)
			},

			// 展示海报
			showHb() {
				this.posterShow = true
			},

			//保存图片至本地
			saveImg() {
				uni.saveImageToPhotosAlbum({
					filePath: this.posterImg,
					success: function() {
						console.log('save success');
					}
				});
			},

			hiddenPoster() {
				this.posterShow = false
			},

			showImg() {
				uni.previewImage({
					urls: [this.posterImg]
				})
			},
			/* 分享朋友圈end */

			/* 获取id对应内容 */
			getContentById() {
				let userId = uni.getStorageSync('userId')
				let cnt = {
					id: this.contentId, // Long 内容编号
				}
				if (userId != '' && userId != '1234567890') {
					cnt.userId = userId
				}
				this.$api.getContentById(cnt, (res => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						let detailData = this.$util.tryParseJson(res.data.c)
						console.log('----------------')
						console.log(detailData)
						let a = new Date(detailData.createTime)
						let y = a.getFullYear()
						let m = 1 + a.getMonth()
						let d = a.getDate()
						let time = y + '年' + m + '月' + d + '日'
						detailData.time = time
						this.detailData = detailData
						this.flow = this.$util.tryParseJson(detailData.data).editor
						this.pageStatus = true
						this.getUserById(detailData.upUserId)
						this.getBoolFavoriteUser(detailData.upUserId)
						this.getCommentByContentId()
						this.contentRemark = detailData.contentRemark
						this.contentSource = detailData.contentSource
						this.contentAuthor = detailData.contentAuthor
						this.mp3Src = detailData.mp3Src
						this.videoSrc = this.$util.tryParseJson(detailData.data).video
						let imgList = this.$util.tryParseJson(detailData.data).imgList
						if (imgList.length > 0) {
							this.mp3Img = imgList[0].src
						}
					}
				}))
			},

			/* 获取id对应用户 */
			getUserById(id) {
				let cnt = {
					moduleId: this.$constData.module,
					id: id, //long 用户编号
				}
				this.$api.getUserById(cnt, (res => {
					if (res.data.rc == this.$util.RC.SUCCESS) {
						console.log(this.$util.tryParseJson(res.data.c))
						this.upInfo = this.$util.tryParseJson(res.data.c)
						console.log('--------------------')
						console.log(this.upInfo)
					} else {
						console.log('error')
					}
				}))
			},

			//分享按钮
			shareBtn() {
				uni.share({
					provider: "weixin",
					scene: "WXSceneSession",
					type: 0,
					href: `http://weapp.datanc.cn/science/sharekp.html?id=${this.contentId}&type=${this.$constData.contentType[2].key}`,
					title: "掌上科普",
					summary: "我正在使用掌上科普app，赶紧跟我一起来体验！",
					imageUrl: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572435385&di=3633a97230e161bda396cb159418e90c&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201210%2F05%2F20121005184845_rSCUj.thumb.700_0.jpeg",
					success: function(res) {
						uni.showToast({
							title: '分享成功！'
						})
					},
					fail: function(err) {
						uni.showToast({
							title: '分享失败',
							icon: 'none'
						})
					}
				})
			},
		},
		onShareAppMessage(res) {
			let pages = getCurrentPages() //获取加载的页面
			let currentPage = pages[pages.length - 1] //获取当前页面的对象
			console.log(currentPage)
			let url = currentPage.route //当前页面url
			if (url == undefined) {
				url = currentPage.__route__
			}
			let options = currentPage.options //如果要获取url中所带的参数可以查看options 
			let id = options.id
			let src = `${url}?id=${id}`
			console.log(src)
			if (res.from == 'button') { // 来自页面内分享按钮
				console.log(res.target)
			}
			return {
				title: 'kkqt',
				path: src
			}
		}
	}
</script>

<style lang="scss" scoped>
	.content {
		display: flex;
		flex-direction: column;
		background: #fff;
		padding-bottom: 90upx;
	}

	.scroll {
		flex: 1;
		position: relative;
		background-color: #f8f8f8;
	}

	.scroll-content {
		display: flex;
		flex-direction: column;
	}

	/* 简介 */
	.introduce-section {
		display: flex;
		flex-direction: column;
		padding: 20upx 30upx;
		background: #fff;
		line-height: 1.5;

		.title {
			font-weight: bold;
			font-size: $list-title-l;
			color: #303133;
			margin-bottom: 16upx;
		}

		.introduce {
			position: relative;
			display: flex;
			font-size: 26upx;
			color: #909399;

			text {
				margin-right: 16upx;
			}
		}
	}

	/* 点赞等操作 */
	.actions {
		display: flex;
		justify-content: space-around;
		align-items: center;
		line-height: 1.3;
		padding: 10upx 40upx 20upx;

		.action-item {
			display: flex;
			text-align: center;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			font-size: 24upx;
			color: #999;

			button {
				font-size: 24upx;
				color: #999;
				background-color: #fff;
				padding: 0;

				&:after {
					border: none;
				}
			}
		}

		.yticon {
			display: flex;
			align-items: center;
			justify-content: center;
			width: 60upx;
			height: 60upx;
			font-size: 52upx;

			&.reverse {
				position: relative;
				top: 6upx;
				transform: scaleY(-1);
			}

			&.active {
				color: #ec706b;
			}
		}

	}

	/* 底部 */
	.bottom {
		position: fixed;
		bottom: 0;
		width: 100%;
		background-color: #FFFFFF;
		flex-shrink: 0;
		display: flex;
		align-items: center;
		height: 90upx;
		padding: 0 30upx;
		box-shadow: 0 -1px 3px rgba(0, 0, 0, .04);
		z-index: 1;
		box-sizing: border-box;

		.input-box {
			display: flex;
			align-items: center;
			flex: 1;
			height: 60upx;
			background: #f2f3f3;
			border-radius: 100px;
			padding-left: 30upx;

			.icon-huifu {
				font-size: 28upx;
				color: #909399;
			}

			.input {
				padding: 0 20upx;
				font-size: 28upx;
				color: #303133;
			}
		}

		.confirm-btn {
			font-size: 30upx;
			padding-left: 20upx;
			color: #0d9fff;
		}
	}


	.contentImg {
		image {
			width: 100%;
		}
	}

	.articleInfo {
		margin-top: 40upx;
	}

	.centerBox {
		margin: 0 auto;
	}

	.hiddenBox {
		position: absolute;
		top: -10000px;
	}

	.canvasBox {
		width: 450px;
		height: 800px;
	}

	.upHeadBox {
		width: 100px;
		height: 100px;
	}

	.poster {
		position: fixed;
		z-index: 3;
		top: 0;
		left: 0;
		width: 100vw;
		height: 100vh;
		background-color: rgba(0, 0, 0, 0.4);
	}

	.posterImg {
		position: absolute;
		top: 20px;
		width: 85vw;
		left: 50%;
		margin-left: -42.5vw;

		image {
			width: 100%;
		}
	}

	.posterBtn {
		position: absolute;
		bottom: 20px;
		font-size: $list-title;
		width: 100vw;

		.stopBtn {
			width: 50vw;
			margin: 0 auto;
		}
	}

	.followBtn {
		position: absolute;
		right: $box-margin-left;
		top: 50%;
		margin-top: -1em;
		display: inline-block;
		// width: 3em;
		text-align: center;
		font-size: $list-info;
		line-height: 2em;
		color: $color-button-back;
		background-color: $color-main;


		&:after {
			border: none;
		}
	}

	.currentIcon {
		color: $color-main;
	}

	.otherInfo {
		margin-top: $box-margin-top;
		font-size: $list-info;
		color: $list-title-color;

		video {
			width: 690upx;
			height: 388upx;
		}
	}

	.remark {
		padding: 10upx;
		background-color: rgba($color: #999999, $alpha: .1);
		margin-bottom: 15upx;
		text-indent: 2em;
	}

	.writerBox {
		// display: flex;
		// align-items: center;
		margin-top: $box-margin-top;
		color: $list-info-color;
		font-size: $list-info;

		view {
			margin: 10upx;
		}

		text {
			margin-left: 10upx;
		}
	}
</style>
