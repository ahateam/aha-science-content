<template>
    <div>
        <el-row style="padding: 20px">
            <el-col :span="2" style="min-height: 20px"></el-col>
            <el-col :span="20">
                <span class="title-box"> 标题：</span>
                <el-input placeholder="请输入标题" v-model="title" style="display: inline-block;width: 400px"></el-input>
            </el-col>
        </el-row>

        <el-row style="padding: 20px">
            <el-col :span="2" style="min-height: 20px"></el-col>
            <el-col :span="20">
                <span class="title-box"> 地点：</span>
                <el-input placeholder="请输入活动地点" v-model="address" style="display: inline-block;width: 400px"></el-input>
            </el-col>
        </el-row>

        <el-row style="padding: 20px">
            <el-col :span="2" style="min-height: 20px"></el-col>
            <el-col :span="20">
                <span class="title-box"> 活动时间：</span>
                <el-date-picker
                        v-model="time"
                        type="daterange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                </el-date-picker>
            </el-col>
        </el-row>

        <el-row style="padding: 20px">
            <el-col :span="2" style="min-height: 20px"></el-col>
            <el-col :span="20">
                <span class="title-box"> 活动封面图：</span>
                <img width="500" :src="imgSrc" v-if="imgSrc">
                <input @change="getMechData1($event)" type="file" class="upload" v-if="imgSrc == ''"/>
            </el-col>
        </el-row>

        <el-row style="padding: 20px">
            <el-col :span="2" style="min-height: 20px"></el-col>
            <el-col :span="20">
                    <span class="title-box"> 科普基地：</span>
                    <el-select filterable v-model="place" placeholder="请选择" style="margin-right: 10px;">
                        <el-option v-for="(item,index) in placeList" :key="index" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
            </el-col>
        </el-row>

        <el-row style="margin-top: 20px;padding-bottom: 10px">
            <el-col :span="4" style="min-height: 20px"></el-col>
            <el-button type="primary" @click="createBtn" style=";padding: 15px 50px">提交
            </el-button>
        </el-row>

    </div>
</template>

<script>
    import ossAuth from '@/commen/oss/ossAuth.js'

    let client = ossAuth.client

    export default {
        name: "addContent",
        data() {
            return {
                homeTagName: '',
                homeTag: '',
                imgSrc: '',
                imgList: [],
                tag: '',
                time: '',
                workTime:'',

                address:'',

                place: '',
                placeList: [],

                show: Math.round(Math.random()),
                title: '',
                power: 0,
                status: this.$constData.statusList[3].value,
                userId: 401770184378345,

                contentType: this.$constData.typeList[3].value,
            }
        },
        methods: {
            getMechData1() {
                this.mechGrantImg = event.target.files[0]
                this.doUpload(this.mechGrantImg)
            },

            doUpload(file) {
                let date = new Date()
                this.size = file.size
                let tmpName = 'zskp/image/' + date.getFullYear() + '' + (1 * date.getMonth() + 1) + '' + date.getDate() + '/' + encodeURIComponent(file.name)

                console.log(tmpName)

                this.multipartUpload(tmpName, file)
            },

            multipartUpload(upName, upFile) {
                //Vue中封装的分片上传方法（详见官方文档）
                let _this = this
                try {
                    let result = client.multipartUpload(upName, upFile, {
                        meta: {
                            year: 2017,
                            people: 'test'
                        }
                    }).then(res => {
                        //取出存好的url
                        let address = res.res.requestUrls[0]
                        console.log(address)
                        let _index = address.indexOf('?')
                        console.log(_index)
                        if (_index == -1) {
                            _this.imgSrc = address
                        } else {
                            _this.imgSrc = address.substring(0, _index)
                        }

                    }).catch(err => {
                        console.log(err)
                    })

                } catch (e) {
                    // 捕获超时异常
                    if (e.code === 'ConnectionTimeoutError') {
                        console.log("Woops,超时啦!");
                    }
                    console.log(e)
                }
            },

            getTime(time) {
                let y = time.getFullYear()
                let m = time.getMonth() * 1 + 1
                let d = time.getDate()
                return `${y}-${m}-${d}`
            },

            createBtn() {
                if (this.title == '') {
                    this.$message({
                        message: '请填写标题',
                        type: 'warning'
                    })
                    return
                }
                if (this.imgSrc == '') {
                    this.$message({
                        message: '请上传封面图',
                        type: 'warning'
                    })
                    return
                }
                if (this.place == '') {
                    this.$message({
                        message: '请填写活动地点',
                        type: 'warning'
                    })
                    return
                }
                if (this.address == '') {
                    this.$message({
                        message: '请选择科普基地(景点)',
                        type: 'warning'
                    })
                    return
                }
                if (this.time == '') {
                    this.$message({
                        message: '请选择活动时间',
                        type: 'warning'
                    })
                    return
                }
                this.editorBtn()
            },

            editorBtn() {
                let that = this
                let imgSrc = {
                    src: this.imgSrc
                }
                this.imgList.push(imgSrc)
                let time1 = new Date(this.time[0])
                let time2 = new Date(this.time[1])
                let newTime = this.getTime(time1) + '至' + this.getTime(time2)
                let data = {
                    show: this.show,
                    imgList: this.imgList,
                    place:this.place,
                    address:this.address,
                    time:newTime,
                }
                let cnt = {
                    module: this.$constData.module,
                    type: this.contentType,
                    status: this.status,
                    power: this.power,
                    upUserId: this.userId,
                    title: this.title,
                    data: JSON.stringify(data),
                }
                that.$api.addContent(cnt, (res => {
                    if (res.data.rc == that.$util.RC.SUCCESS) {
                        that.$message({
                            message: '创建成功',
                            type: 'success'
                        });
                        that.$router.push('/contentList')
                    } else {
                        this.$message({
                            message: res.data.c,
                            type: 'warning'
                        });
                        that.imgList = []
                    }
                }))
            },
            getHomeTag() {
                let cnt = {
                    moduleId: this.$constData.module,
                    status: 1,
                    group: '首页',
                    count: 20,
                    offset: 0,
                };
                this.$api.getContentTag(cnt, (res) => {
                    if (res.data.rc == this.$util.RC.SUCCESS) {
                        this.homeTag = this.$util.tryParseJson(res.data.c)
                    }
                })
            },

            getPlace() {
                let cnt = {
                    moduleId: this.$constData.module, // Long 模块编号
                    count: 50, // int
                    offset: 0, // int
                }
                this.$api.getTourBases(cnt, (res) => {
                    if (res.data.rc == this.$util.RC.SUCCESS) {
                        this.placeList = this.$util.tryParseJson(res.data.c)
                        console.log(this.placeList)
                    } else {
                        this.addressList = []
                    }
                })
            },
        },
        mounted() {
            this.getHomeTag()
            this.getPlace()
        }
    }
</script>

<style lang="scss" scoped>
    .title-box {
        font-size: 1.6rem;
        line-height: 40px;
        letter-spacing: 2px;
        font-weight: 600;
        color: #666;
        text-align: right;
    }
</style>
