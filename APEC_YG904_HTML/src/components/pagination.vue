<!--	
	分页组件
	先注册 调用示例 <fe-page @retrieve="retrieve" :data="pageData"></fe-page>
	retrieve指向调用列表函数，pageData是列表返回数据
	pageSizes可选  默认从config/threshold导入
-->
<template>
	<div class="pagination-wrapper">
		<el-pagination  layout="jumper, sizes, prev, pager, next, total" :page-count="data.pageCount" :currentPage="data.currentNo" :page-sizes="pageSizes ? pageSizes:defaultPageSizes" :page-size="data.pageSize" :total="data.totalElements" @size-change="sizeChange" @current-change="toRetrieve"></el-pagination>
	</div>
</template>
<script>
export default {
	props: {
		data: {
			default: ''
		},
		pageSizes: {
			default: ''
		}
	},
	data(){
		return {
			defaultPageSizes: [10, 15, 20, 25, 30],
			currentSize: 0
		}
	},
	mounted() {
		//初始化页组件时请求数据
		this.toRetrieve(1);
	},
	methods: {
        // 每页数据条数变化
        sizeChange(size) {
            this.toRetrieve(this.data.currentNo, size)
        },
        //当前页改变
        toRetrieve(currentNo, size){
        	if(!size && !this.currentSize){
        		let pageSize = this.pageSizes ? this.pageSizes[0]:this.defaultPageSizes[0];
        		this.currentSize = pageSize;
        	}
        	if(size){
        		this.currentSize = size;
        	}
        	this.$emit('retrieve', currentNo, this.currentSize);
        }
	}
}
</script>