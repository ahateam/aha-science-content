<template>
    <div>
        <el-row class="row-box">
            <el-col :span="24">
                {{info.name}}
            </el-col>
        </el-row>
        <div style="text-align: center">
        <iframe :src="src" width="500px" height="500px"></iframe>
        </div>

        <el-col :span="24" style="margin-bottom: 10px">
            <el-col :span="4">
                <div class="title-box">价格:</div>
            </el-col>
            <el-col :span="14">
                <div class="text-box">
                    {{info.money}}
                </div>
            </el-col>
        </el-col>
    </div>
</template>

<script>
    export default {
        name: "templateInfo",
        data() {
            return {
                info:{},
                src:''
            }
        },
        methods: {},
        mounted() {
            this.info = this.$route.params.info
            this.src = JSON.parse(this.info.data).src
            console.log(this.$route.params.info)
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

    .title-box {
        font-size: 3rem;
        line-height: 40px;
        letter-spacing: 2px;
        font-weight: 600;
        color: #666;
        text-align: right;
    }

    .text-box {
        margin-left: 50px;
        font-size: 2rem;
        line-height: 40px;
        color: #333;
    }

</style>
