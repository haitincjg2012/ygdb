<template>
    <div>
        <el-row>
            <el-col>
                <el-form>
                     <el-form-item label="商品标题">
                         <el-input v-model="titile" placeholder=""></el-input>
                     </el-form-item>
                      <el-form-item label="发布人">
                          <el-input v-model="publisher" placeholder=""></el-input>
                      </el-form-item>
                       <el-form-item>
                            <el-button type="primary">搜索</el-button>
                       </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <div class="division"></div>
        <el-row>
            <el-col>
                <el-button type="primary">发布求购</el-button>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="1">
                <div>已上市</div>
            </el-col>
            <el-col :span="1">
                <div>已下架</div>
            </el-col>
        </el-row>
        <el-table
                :data="tableData"
                border
                style="width: 100%" class="z-top">
            <el-table-column
                    prop="pos"
                    label="置顶"
                    width="100">
            </el-table-column>
             <el-table-column
                     porp="num"
                     label="单号"
                     width="180">
             </el-table-column>
             <el-table-column
                     prop="title"
                     label="商品标题"
                     width="300"
             ></el-table-column>
            <el-table-column
                    prop="weight"
                    label="求购量"
                    width="100"
            >
            </el-table-column>
            <el-table-column
                    prop="price"
                    labe="期望价格"
                    width="100"
            >
            </el-table-column>
            <el-table-column
                    prop="area"
                    label="货源区域"
                    width="200"
            ></el-table-column>
            <el-table-column
                    prop="day"
                    label="上架时间"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="path"
                    label="发布路径"
                    width="150"
            >
            </el-table-column>
            <el-table-column
                    prop="people"
                    label='发布人'
                    width="100"
            ></el-table-column>
            <el-table-column
                    label="操作"
            >
                <template scope="scope">
                    <el-button type="text" size="small">编辑</el-button>
                    <el-button
                            @click.native.prevent="deleteRow(scope.$index, tableData)"
                            type="text"
                            size="small">
                        移除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>
<script>
    var list =["是","否"]
    var data = [{
        pos:list[1],
        num:"12345",
        title:'红苹果',
        weight:"10000",
        price:"10",
    }]

</script>