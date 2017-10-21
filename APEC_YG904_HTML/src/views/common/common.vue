/**
* Created by gsy on 2017/08/21.
* 主内容父路由
*/
<template>
    <div id="boxContainer">
        <my-topnav></my-topnav>
        <my-leftnav></my-leftnav>
        <transition>
            <keep-alive>
                <router-view class="mainContainer"></router-view>
            </keep-alive>
        </transition>

    </div>
</template>

<script>
    import topNav from '~/components/nav/topNav.vue'
    import leftNav from '~/components/nav/leftNav.vue'
    export default{
        data(){
            return {}
        },
        methods: {},
        components: {
            'my-topnav': topNav,
            'my-leftnav': leftNav

        }
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
