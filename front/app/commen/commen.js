import api from './api.js'

const commen = {}


/**
 *@tryParseJson json转换容错
 *
 * @param data      需要json.parse的数据
 * @param dataType  异常后期望返回的类型（{} []）
 * @param isCompel  是否强制转换
 */
commen.tryParseJson = function(data, dataType = [], isCompel = true) {
	let resData = null
	try {
		resData = JSON.parse(data)
	} catch (e) {
		console.log('数据异常' + e)
		resData = isCompel ? dataType : data
	}
	return resData
}

commen.getSplit = function(res) {
	res = decodeURIComponent(res)
	if (res.indexOf('?') != -1) {
		let arr = res.split('?');
		let sub
		let sub1
		let id = ""
		let id1 = ""
		if (arr && arr.length > 0) {
			sub = arr[1].split("&")
			for (let i in sub) {
				let ca = sub[i].split('=');
				if (ca.length > 0 && ca[0] == "id") {
					id = ca[1]
				}
				if (ca.length > 0 && ca[0] == "id1") {
					id1 = ca[1]
				}
			}
		}
		return {
			id: id,
			id1: id1
		}
	} else {
		uni.showToast({
			title: '参数错误!',
			duration: 2000,
			icon: 'none'
		});
	}
}

commen.getNewReplay = () => {
	let id = uni.getStorageSync('userId')
	if (id) {
		let cnt = {
			userId: id
		}
		api.getIsRead(cnt, (res) => {
			if (res.data.rc == 'succ') {
				let index = JSON.parse(res.data.c)
				if (index == 0) {
					uni.hideTabBarRedDot({
						index: 2
					})
				} else {
					uni.showTabBarRedDot({
						index: 2
					})
				}
			} else {
				console.log('获取未读消息失败')
			}
		})
	}
}


export default commen
