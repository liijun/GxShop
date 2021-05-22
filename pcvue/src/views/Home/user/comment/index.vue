<template>
    <div class="user_content_blcok">
        <div class="user_content_blcok_title" style="position: relative;">
            评论列表
        </div>
        <div class="user_content_blcok_line"></div>

        <div class="admin_table_main">
                <el-table :data="list"  >
                    <el-table-column type="selection"></el-table-column>
                    <!-- <el-table-column prop="id" label="#" fixed="left" width="70px"></el-table-column> -->
                    <!-- <el-table-column prop="id" label="#"  width="70px"></el-table-column> -->
                    <el-table-column label="商品名称">
                        <template slot-scope="scope">
                            <router-link :to="'/goods/info/'+scope.row.goodsInfo.id">
                            <dl class="table_dl">
                                <dt><el-image style="width: 50px; height: 50px" :src="scope.row.goodsInfo.picUrl"><div slot="error" class="image-slot"><i class="el-icon-picture-outline"></i></div></el-image></dt>
                                <dd class="table_dl_dd_all">{{ scope.row.goodsInfo.name }}</dd>
                            </dl>
                            </router-link>
                        </template>
                    </el-table-column>
                    <el-table-column label="评分" prop="star"></el-table-column>
                    <el-table-column label="内容" prop="content"></el-table-column>
                     <!--<el-table-column label="服务态度" prop="service"></el-table-column>-->
                     <!--<el-table-column label="发货速度" prop="speed"></el-table-column>-->
                    
                    <!-- <el-table-column label="操作" fixed="right" width="120px">
                        <template slot-scope="scope">
                            <el-button type="danger" icon="el-icon-delete" @click="del(scope.row.goods.id)">删除</el-button>
                        </template>
                    </el-table-column> -->
                </el-table>
                <div class="home_fy_block" style="margin-top:40px;" >
                    <el-pagination @current-change="current_change" background layout="prev, pager, next,jumper,total" :total="total_data" :page-size="page_size" :current-page="current_page"></el-pagination>
                </div>
            </div>
    </div>
</template>

<script>
export default {
    components: {},
    props: {},
    data() {
      return {
          list:[],
          total_data:0, // 总条数
          page_size:10,
          current_page:1,
          select_id:'',
      };
    },
    watch: {},
    computed: {},
    methods: {
        current_change:function(e){
            this.current_page = e;
            this.get_comment_list();
        },
        get_comment_list:function(){
            this.$get(this.$api.homeGetCommentList,{type:0,page:this.current_page,showType:0,limit:this.page_size}).then(res=>{
                window.console.log(res);
                this.page_size = res.data.limit;
                this.total_data = res.data.total;
                this.current_page = res.data.page;
                this.list = res.data.list;
            })

        },
        
    },
    created() {
        this.get_comment_list();
    },
    mounted() {}
};
</script>
<style lang="scss" scoped>

</style>