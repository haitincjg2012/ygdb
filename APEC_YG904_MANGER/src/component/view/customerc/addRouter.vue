<template>
    <div>
        <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="基本信息" name="first"></el-tab-pane>
            <el-tab-pane label="关联仓库" name="second"></el-tab-pane>
            <el-tab-pane label="权限" name="third"></el-tab-pane>
        </el-tabs>
        <el-row>
            <el-col :span="12">
                <router-view></router-view>
            </el-col>

        </el-row>
    </div>

</template>
<script>
    export default {
        data() {
            this.$router.push('/BaseInfo');
            return {
                activeName: 'first'
            };
        },
        methods: {
            handleClick(tab, event) {
                var idx= tab.index * 1;
                switch (idx){
                    case 0:
                        this.$router.push('/BaseInfo');
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        }
    };
</script>