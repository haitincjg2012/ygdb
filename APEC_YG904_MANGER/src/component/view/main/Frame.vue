<template>
    <div>
        <div>
            <hd :user-name="name"></hd>
            <el-row type="flex" justify="end">
                <el-col :span="3" class="sidebar">
                    <el-menu theme="light" :router=true>
                        <el-submenu index="1">
                            <template slot="title">商品</template>
                            <el-menu-item index="1-1"
                                          :route="good.g"
                                          disabled="">
                                          商品
                            </el-menu-item>
                        </el-submenu>
                        <el-submenu index="2">
                            <template slot="title">客户</template>
                            <el-menu-item index="2-1"
                                          :route="customer.member"
                                          disabled="">
                                         客户列表
                            </el-menu-item>
                            <el-menu-item index="2-2"
                                          :route="customer.authentication"
                                          disabled="">
                                        实名认证审核
                            </el-menu-item>
                            <el-menu-item index="2-3"
                                          :route="customer.report"
                                          disabled="">
                                         客户举报
                            </el-menu-item>
                        </el-submenu>
                        <!--<el-menu-item index="2" :route="member">客户列表</el-menu-item>-->
                        <el-submenu index="3">
                            <template slot="title">交易</template>
                            <el-menu-item index="3-1"
                                          :route="manage.supply"
                                          disabled="">
                                          供求管理
                            </el-menu-item>
                            <el-menu-item index="3-2"
                                          :route="manage.buy"
                                          disabled="">
                                          求购管理
                            </el-menu-item>
                            <el-menu-item index="3-3"
                                          :route="manage.delivery"
                                          disabled="">
                                         交收单管理
                            </el-menu-item>
                        </el-submenu>
                        <!--<el-menu-item index="3"-->
                                    <!--:route="Authentication">-->
                                   <!--会员认证-->
                        <!--</el-menu-item>-->
                        <el-submenu index="4">
                            <template slot="title">数据</template>
                            <el-menu-item index="4-1"
                                          :route="dt.analysis"
                                          disabled="">
                                          数据分析
                            </el-menu-item>
                        </el-submenu>
                        <!--<el-menu-item index="4"-->
                                    <!--:route="Delivery">-->
                                    <!--数据分析-->
                        <!--</el-menu-item>-->
                    </el-menu>
                    <!--备用菜单代码-->
                    <!--<el-tree :data="menu" :props="treeData" node-key="id" :default-expanded-keys="defaultExpandedKeys" @node-click="jump" highlight-current>-->
                    <!--</el-tree>-->
                </el-col>
                <el-col :span="21" class="view">
                    <router-view></router-view>
                </el-col>
            </el-row>
        </div>
    </div>
    <!--<el-tabs v-model="activeName" @tab-click="handleClick">-->
        <!--<el-tab-pane label="客户列表" name="first" ></el-tab-pane>-->
        <!--<el-tab-pane label="会员认证" name="second" ></el-tab-pane>-->
        <!--<el-tab-pane label="角色管理" name="third" ></el-tab-pane>-->
        <!--<el-tab-pane label="数据分析" name="fourth" ></el-tab-pane>-->
    <!--</el-tabs>-->
</template>
<script>
    import Hd from './HD.vue'

    export default {
        data() {
            return {
                name: '',
                good:{
                    g:{
                        name:'good'
                    }
                },
//                member: {name: 'member'},
//                Authentication:{name:'Authentication'},
//                Delivery: {name: 'Delivery'},
//                report: {name: 'report'},

                customer:{
                    authentication:{
                        name:'Authentication'
                    },
                    member:{
                        name: 'member'
                    },
                    report:{
                        name:'report'
                    }
                },
                manage: {
                    supply: {
                        name: 'supply'
                    },
                    buy: {
                        name: 'buy'
                    },
                    delivery: {
                        name: 'Delivery'
                    }
                },
                dt:{
                    analysis:{
                           name:'analysis'
                    }
                }
            };
        },
        methods: {
            handleClick(tab, event) {
//                console.log(tab, event);
//                console.log(tab.name);
                var name = tab.index * 1;
                switch (name){
                    case 0:
                        this.$router.push('/M');
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            },
        },
        components:{
            Hd
        }
    };
</script>
<style scoped>
    .sidebar {
        position: fixed;
        left: 0;
        top: 60px;
        height: 100%;
    }
    .con-head {
        height: 50px;
        border-bottom: solid 1px #000;
        font-size: 20px;
        line-height: 50px;
        padding: 0 30px;
    }
    .view {
        position: fixed;
        top: 60px;
        bottom: 0;
        right: 0;
        overflow: auto;
        /*height: 80%;*/
        /*width: 100%;*/
    }
    .el-menu {
        height: 100%;
    }
    /*.table-col {*/
    /*padding: 30px 20px 70px 20px;*/
    /*}*/
    .el-breadcrumb {
        float: right;
        margin-top: 20px;
    }
</style>