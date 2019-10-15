<template>
    <div>
        <el-row class="row-box">
            <el-col :span="24">
                {{contentInfo.title}}
            </el-col>
        </el-row>

        <div v-if="contentInfo.type != '3'">
            <div v-for="(item,index) in textList" :key="index">
                <div v-html="item.value" v-if="item.type == 'textarea'" style="padding: 20px;" class="textInfo"></div>
                <img :src="item.value" v-else>
            </div>
        </div>

        <div v-else style="width: 100%">
            <video width="576" height="324" style="margin: 0 auto;display: block" controls>
                <source :src="JSON.parse(contentInfo.data).url" type="video/mp4">
                您的浏览器不支持html5的video标签
            </video>
            <div style="font-size: 20px;font-weight: bold;margin-left:50px">
                封面图：
                <img :src="JSON.parse(contentInfo.data).imgSrc">
            </div>
        </div>

        <el-col :span="24" class="row-box1">
            <el-button type="primary" @click="goEdit">修改内容信息</el-button>
            <el-button @click="goBack()">返回上一层</el-button>
        </el-col>
    </div>
</template>

<script>
    export default {
        name: "contetnInfo",
        data() {
            return {
                contentInfo: {},
                textList: [],
            }
        },
        methods: {
            goBack() {
                this.$router.go(-1)
            },
            goEdit() {
                let that = this
                if (that.contentInfo.type != '3') {
                    this.$router.push({
                        name: `editContent`,
                        params: {
                            info: that.contentInfo
                        }
                    })
                } else {
                    this.$router.push({
                        name: `editVideo`,
                        params: {
                            info: that.contentInfo
                        }
                    })
                }
            },
        },
        mounted() {
            this.contentInfo = this.$route.params.info
            let a = JSON.parse(this.contentInfo.data).editor
            if (a != '' && a != undefined) {
                this.textList = a
            }
        }
    }
</script>

<style scoped>
    .row-box {
        background: #fff;
        padding: 15px;
        font-size: 3rem;
        color: #666;
        border-left: 4px solid #67C23A;
    }

    .row-box1 {
        margin-top: 40px;
        text-align: center;
        padding-bottom: 20px;
    }

    .textInfo>>> table {
        border-top: 1px solid #ccc;
        border-left: 1px solid #ccc;
    }

    .textInfo>>> table td, table th {
        border-bottom: 1px solid #ccc;
        border-right: 1px solid #ccc;
        padding: 3px 5px;
    }

    .textInfo>>> table th {
        border-bottom: 2px solid #ccc;
        text-align: center;
    }

    /* blockquote 样式 */
    .textInfo>>> blockquote{
        display: block;
        border-left: 8px solid #d0e5f2;
        padding: 5px 10px;
        margin: 10px 0;
        line-height: 1.4;
        font-size: 100%;
        background-color: #f1f1f1;
    }

    /* code 样式 */
    .textInfo>>> code {
        display: inline-block;
        *display: inline;
        *zoom: 1;
        background-color: #f1f1f1;
        border-radius: 3px;
        padding: 3px 5px;
        margin: 0 3px;
    }

    .textInfo>>> pre code {
        display: block;
    }

    /* ul ol 样式 */
    .textInfo>>> ul, ol {
        margin: 10px 0 10px 20px;
    }

</style>
