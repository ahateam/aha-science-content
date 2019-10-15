<template>
    <div>
        <el-row class="row-box">
            <el-col :span="24">
                {{taskInfo.title}}
            </el-col>
        </el-row>

        <el-col :span="24" class="row-box1" style="font-size: 18px" v-if="taskInfo.type == 1">
            <div style="font-weight: bold;font-size:22px">任务要求：</div>
            <div v-html="text.text"></div>
        </el-col>

        <el-col :span="24" class="row-box1" style="font-size: 18px" v-if="taskInfo.type == 0">

            <div style="padding: 5px">
                <div style="font-weight: bold;font-size:22px">任务要求：</div>
                <div v-for="(item,index) in text.purpose" :key="index">
                    {{item.name}}
                </div>
            </div>

            <div style="padding: 5px">
                <div style="font-weight: bold;font-size:22px">对象要求：</div>
                <div v-for="(item,index) in text.obj" :key="index">
                    {{item.name}}
                </div>
            </div>

        </el-col>

        <el-col :span="24" class="row-box1" style="font-size: 18px" v-if="taskInfo.type == 0">
            <div style="font-weight: bold;font-size:22px">联系电话：</div>
            <div v-html="text.tel"></div>
        </el-col>

        <el-col :span="24" style="text-align: center">
            <el-button style="padding: 1em 4em" type="primary" @click="changeBtn(true)">审核通过</el-button>
            <el-button style="padding: 1em 4em" type="info" @click="changeBtn(false)">审核不通过</el-button>
        </el-col>
    </div>
</template>

<script>
    export default {
        name: "taskInfo",
        data() {
            return {
                taskInfo: {},
                text: {},

            }
        },
        methods: {
            goBack() {
                this.$router.go(-1)
            },
            changeBtn(e) {
                let taskInfo = this.taskInfo
                let cnt = {
                    id: taskInfo.id, // Long 任务id
                    bool: e, // Boolean 是否通过
                }
                this.$api.auditTask(cnt, (res => {
                    if (res.data.rc == this.$util.RC.SUCCESS) {
                        this.$message({
                            message: '审核完成',
                            type: 'success'
                        })
                        this.goBack()
                    } else {
                        this.$message({
                            message:res.data.c,
                            type: 'warning'
                        })
                    }
                }))
            },
        },
        mounted() {
            this.taskInfo = this.$route.params.info
            this.text = JSON.parse(this.taskInfo.detail)
            console.log(this.taskInfo)
        }
    }
</script>

<style lang="scss" scoped>
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
</style>
