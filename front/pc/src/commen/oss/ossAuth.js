// import 'babel-polyfill'
// import './promise-6.1.0'

// import './aliyun-oss-sdk.min'

const OSS = require('ali-oss');


// let client = new OSS({
//     region: 'oss-cn-chengdu',
// 	// region: 'oss-cn-zhangjiakou',
	
//     //云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，部署在服务端使用RAM子账号或STS，部署在客户端使用STS。
//     accessKeyId: 'LTAI4FcuT5ZQCiukxiMZimeY',
//     accessKeySecret: 'yeisKKgf7MbgRpe7Cq8DWSG2C6c144',
//     bucket: 'dzkx-kepu',
// 	timeout: '1200000',
// 	// bucket: 'zskp-file'
// });
let client = new OSS({
    region: 'oss-cn-hangzhou.aliyuncs.com',
	// region: 'oss-cn-zhangjiakou',
	
    //云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，部署在服务端使用RAM子账号或STS，部署在客户端使用STS。
    accessKeyId: 'LTAIJ9mYIjuW54Cj',
    accessKeySecret: '89EMlXLsP13H8mWKIvdr4iM1OvdVxs',
    bucket: 'weapp-xhj',
	timeout: '1200000',
	// bucket: 'zskp-file'
});

export default {
    client,
}
