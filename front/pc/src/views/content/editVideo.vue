<template>
    <div>
        <el-row class="row-box">
            <el-col :span="24">
                修改内容
            </el-col>
        </el-row>

        <el-row class="row-box1">

            <el-col :span="24" style="margin-bottom: 10px">
                <el-col :span="4">
                    <div class="title-box">标题:</div>
                </el-col>
                <el-col :span="14">
                    <div class="text-box">
                        <el-input v-model="title" placeholder="标题"></el-input>
                    </div>
                </el-col>
            </el-col>

            <el-col :span="24" style="margin-bottom: 10px">
                <el-col :span="4">
                    <div class="title-box">简介:</div>
                </el-col>
                <el-col :span="14">
                    <div class="text-box">
                        <el-input v-model="contentList.text" placeholder="简介"></el-input>
                    </div>
                </el-col>
            </el-col>

            <el-col :span="24">
                <el-col :span="4">
                    <div class="title-box">标签:</div>
                </el-col>
                <el-col :span="18" class="text-box">
                    <el-tag
                            :key="tag"
                            v-for="tag in tagList"
                            closable
                            :disable-transitions="false"
                            @close="handleClose(tag)">
                        {{tag}}
                    </el-tag>
                    <el-input
                            class="input-new-tag"
                            v-if="inputVisible"
                            v-model="inputValue"
                            ref="saveTagInput"
                            size="small"
                            @keyup.enter.native="handleInputConfirm"
                            @blur="handleInputConfirm"
                    >
                    </el-input>
                    <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
                </el-col>
            </el-col>

<!--            <el-col :span="24" style="margin-bottom: 10px">-->
<!--                <el-col :span="4">-->
<!--                    <div class="title-box">状态:</div>-->
<!--                </el-col>-->
<!--                <el-col :span="18">-->
<!--                    <div class="text-box">-->
<!--                        <el-select v-model="status" placeholder="请选择" style="margin-right: 10px;">-->
<!--                            <el-option-->
<!--                                    v-for="item in statusList"-->
<!--                                    :key="item.value"-->
<!--                                    :label="item.name"-->
<!--                                    :value="item.value"-->
<!--                                    :disabled="item.disabled"-->
<!--                            >-->
<!--                            </el-option>-->
<!--                        </el-select>-->
<!--                    </div>-->
<!--                </el-col>-->
<!--            </el-col>-->

            <el-col :span="24" style="margin-bottom: 10px">
                <el-col :span="4">
                    <div class="title-box">展示样式:</div>
                </el-col>
                <el-col :span="18">
                    <div class="text-box">
                        <el-select v-model="contentList.show" placeholder="请选择" style="margin-right: 10px;">
                            <el-option
                                    v-for="item in showList"
                                    :key="item.value"
                                    :label="item.name"
                                    :value="item.value"
                            >
                            </el-option>
                        </el-select>
                    </div>
                </el-col>
            </el-col>

            <el-col :span="24" style="margin-bottom: 10px">
                <el-col :span="4">
                    <div class="title-box">视频:</div>
                </el-col>
                <el-col :span="14">
<!--                    <video width="576" height="324" style="margin: 0 auto;display: block" controls>-->
<!--                        <source :src="contentList.url" type="video/mp4">-->
<!--                        您的浏览器不支持html5的video标签-->
<!--                    </video>-->
                    <iframe :src="contentList.url" width="576" height="324"></iframe>
                    <div class="text-box">
                        <div style="font-size: 20px;margin: 10px;">输入视频地址:</div>
                        <el-input v-model="contentList.url" placeholder="标题"></el-input>
                    </div>
                </el-col>
            </el-col>
            <el-col :span="24" style="margin-bottom: 10px">
                <el-col :span="4">
                    <div class="title-box">封面:</div>
                </el-col>
                <el-col :span="14">
                    <img :src="contentList.imgSrc">
                    <div class="text-box">
                        <div style="font-size: 20px;margin: 10px">输入封面地址:</div>
                        <el-input v-model="contentList.imgSrc" placeholder="标题"></el-input>
                    </div>
                </el-col>
            </el-col>
            <el-button style="padding: 1em 4em" type="primary" @click="changeBtn(4)">审核通过</el-button>
            <el-button style="padding: 1em 4em" type="info" @click="changeBtn(5)">审核不通过</el-button>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "editVideo",
        data() {
            return {
                contentInfo: '',
                title: '',
                //标签
                tagList: [],
                inputVisible: false,
                inputValue: '',
                //状态列表
                statusList: [
                    {
                        name: '草稿',
                        value: 0,
                        disabled: true
                    },
                    {
                        name: '正常',
                        value: 1,
                        disabled: true
                    },
                    {
                        name: '已关闭',
                        value: 2,
                        disabled: true
                    },
                    {
                        name: '已删除',
                        value: 3,
                        disabled: true
                    },
                    {
                        name: '已发布',
                        value: 4,
                    },
                    {
                        name: '发布失败',
                        value: 5,
                    }
                ],
                status: '',
                //展示样式
                showList: [
                    {
                        name: '横屏',
                        value: 0,
                    },
                    {
                        name: '右侧',
                        value: 1,
                    },
                    {
                        name: '竖屏',
                        value: 2,
                    }
                ],

                contentList: [],
            }
        },
        methods: {
            changeBtn(status) {
                let that = this
                let contentInfo = this.contentInfo
                let data = this.contentList
                let cnt = {
                    userId: contentInfo.upUserId, // Long 用户编号
                    _id: contentInfo._id, // String 内容分片编号
                    id: contentInfo.id, // Long 内容编号
                    status: status, // Byte 状态Content.STATUS
                    // upChannelId: upChannelId, // Long <选填> 上传专栏编号
                    title: that.title, // String 标题
                    tags: JSON.stringify(that.tagList), // String <选填> 标签
                    data: JSON.stringify(data), // String 数据（JSON）
                }
                this.$api.editContent(cnt, (res => {
                    if (res.data.rc == that.$util.RC.SUCCESS) {
                        that.$router.push('examineList')
                        that.$message({
                            message: '审核完毕',
                            type: 'success'
                        });
                    } else {
                        that.$message({
                            message: res.data.c,
                            type: 'warning'
                        });
                    }
                }))
            },

            // 标签
            handleClose(tag) {
                this.tagList.splice(this.tagList.indexOf(tag), 1);
            },

            showInput() {
                this.inputVisible = true;
                this.$nextTick(_ => {
                    this.$refs.saveTagInput.$refs.input.focus();
                });
            },

            handleInputConfirm() {
                let that = this
                let inputValue = this.inputValue;
                if (inputValue) {
                    that.tagList.push(inputValue);
                }
                this.inputVisible = false;
                this.inputValue = '';
            }

            //end
        },
        mounted() {
            this.contentInfo = this.$route.params.info
            console.log(this.contentInfo)
            this.title = this.contentInfo.title
            this.tagList = JSON.parse(this.contentInfo.tags)
            this.status = this.contentInfo.status
            this.contentList = JSON.parse(this.contentInfo.data)
            this.text = this.contentList.text
            this.show = this.contentList.show
            console.log(this.contentList)
        }
    }
</script>

<style scoped lang="scss">
    .row-box {
        background: #fff;
        padding: 15px;
        font-size: 1.6rem;
        color: #666;
        border-left: 4px solid #67C23A;

    }

    .row-box1 {
        margin-top: 20px;
        padding: 15px;
        background: #fff;
    }

    .title-box {
        font-size: 1.6rem;
        line-height: 40px;
        letter-spacing: 2px;
        font-weight: 600;
        color: #666;
        text-align: right;
    }

    .text-box {
        margin-left: 50px;
    }

    .row-box2 {
        margin-top: 20px;
    }

    .row-box3 {
        margin-top: 20px;
        text-align: center;
        padding-bottom: 20px;
    }

    .image-box {
        margin-left: 50px;
        width: 400px;
        height: 300px;
        overflow: hidden;
        float: left;
    }

    .image-box img {
        width: 400px;
        height: 300px;
        overflow: hidden;
        cursor: pointer;
    }

    .image-load {
        float: left;
        margin-left: 50px;
        line-height: 150px;
    }

    .el-tag + .el-tag {
        margin-left: 10px;
    }

    .button-new-tag {
        margin-left: 10px;
        height: 32px;
        line-height: 30px;
        padding-top: 0;
        padding-bottom: 0;
    }

    .input-new-tag {
        width: 90px;
        margin-left: 10px;
        vertical-align: bottom;
    }
</style>
