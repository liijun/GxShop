<template>
    <div class="goods_index">
        <div class="shop_top"><shop-top @search_goods="to_search" :subnav_show="false" :change_color="true"></shop-top></div>
        <div class="mbx width_center_1200">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item><a href="/">产品</a></el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="goods_where w1200">
            <div class="item">
                <div class="title">商品分类：</div>
                <div class="list">
                    <div class="first">
                        <ul>
                            <li :class="($isEmpty(info.pid) || info.pid==0)?'red':''" @click="classChange(0,0,{},0)">全部</li>
                            <li :class="(!$isEmpty(info.pid) && info.pid==v.id)?'red':''" v-for="(v,k) in goods_class.categoryList" :key="k" @click="classChange(v.pid,v.id,v)">{{v.name}}</li>
                        </ul>
                    </div>
                    <div class="sec">
                        <ul  v-if="(!$isEmpty(info.pid) && !$isEmpty(goods_class.allList))">
                            <li :class="(!$isEmpty(info.sid) && info.sid==v.id)?'red':''" v-for="(v,k) in goods_class.allList[info.pid]" :key="k" @click="classChange(v.pid,v.id,v,1)" >{{v.name}}</li>
                        </ul>
                        <div v-if="(!$isEmpty(info.pid))">
                            <div  v-for="(vo,key) in goods_class.allList[info.pid]" :key="key">
                                <ul style="margin-top:0" v-if="(!$isEmpty(info.sid) && info.sid==vo.id)">
                                    <li :class="(!$isEmpty(info.tid) && info.tid==voo.id)?'red':''" v-for="(voo,keys) in vo.children" :key="keys" @click="classChange(vo.pid,vo.id,voo,2)">{{voo.name}}</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                
                </div>
            </div>
        <!-- <div class="goods_where width_center_1200">
            <div class="goods_cat_list">
                <div class="goods_cat_title">商品分类：</div>
                <ul>
                    <li class="font_color"><router-link to="/">全部</router-link></li>
                    <li><router-link  to="/">荣耀</router-link></li>
                    <li><router-link  to="/">HUAWEI P系列</router-link></li>
                </ul>
                <div class="home_goods_cat_list">
                    <el-cascader v-model="info.class_info" :props="props" :options="class_list" popper-class="home_goods_index_cascader" @change="classChange"></el-cascader>
                </div>
                
            </div> -->
            <div class="item">
                <div class="title">品牌筛选：</div>
                <div class="list">
                    <div class="first">
                        <ul>
                        <li :class="info.brandId==0?'red':''" @click="brand_change(0)">全部</li>
                        <li v-for="(v,k) in brand_list" :key="k" @click="brand_change(v.id)" :class="info.brandId==v.id?'red':''">{{v.name}}</li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="goods_cat_list">
                <div class="goods_cat_title">筛选排序：</div>
                <ul class="goods_sort_ul">
                    <li @click="goods_sort_click(0)" :class="info.goods_type==0?'font_color':''">默认<span class="goods_sort_top"><i :style="(info.goods_type==0 && info.order_type==0)?'color:#ca151e':''" class="icon iconfont">&#xe7fe;</i></span><span class="goods_sort_bottom"><i :style="(info.goods_type==0 && info.order_type==1)?'color:#ca151e':''" class="icon iconfont">&#xe7ff;</i></span></li>
                    <li @click="goods_sort_click(1)" :class="info.goods_type==1?'font_color':''">价格<span class="goods_sort_top"><i :style="(info.goods_type==1 && info.order_type==0)?'color:#ca151e':''" class="icon iconfont">&#xe7fe;</i></span><span class="goods_sort_bottom"><i :style="(info.goods_type==1 && info.order_type==1)?'color:#ca151e':''" class="icon iconfont">&#xe7ff;</i></span></li>
                    <li @click="goods_sort_click(2)" :class="info.goods_type==2?'font_color':''">销量<span class="goods_sort_top"><i :style="(info.goods_type==2 && info.order_type==0)?'color:#ca151e':''" class="icon iconfont">&#xe7fe;</i></span><span class="goods_sort_bottom"><i :style="(info.goods_type==2 && info.order_type==1)?'color:#ca151e':''" class="icon iconfont">&#xe7ff;</i></span></li>
                    <li @click="goods_sort_click(3)" :class="info.goods_type==3?'font_color':''">评论<span class="goods_sort_top"><i :style="(info.goods_type==3 && info.order_type==0)?'color:#ca151e':''" class="icon iconfont">&#xe7fe;</i></span><span class="goods_sort_bottom"><i :style="(info.goods_type==3 && info.order_type==1)?'color:#ca151e':''" class="icon iconfont">&#xe7ff;</i></span></li>
                </ul>
            </div>
        </div>

        <div class="goods_list width_center_1200" v-if="goods_list.length>0">
            <div class="goods_list_item" v-for="(v,k) in goods_list" :key="k">
                <router-link :to="'/goods/info/'+v.id">
                <dl>
                    <dt><el-image :src="v.picUrl" alt="" lazy></el-image></dt>
                    <dd class="title">{{v.name}}</dd>
                    <dd class="price">￥{{v.counterPrice}}</dd>
                    <dd><span>立即购买</span><span class="retailPrice">￥{{v.retailPrice}}</span></dd>
                </dl>
                </router-link>
            </div>
            
        </div>

        <div class="home_empty_list" v-else>
            <i class="font iconfont">&#xe600;</i>暂无获取到相对应数据！
        </div>

        <div class="home_fy_block  width_center_1200">
            <el-pagination @current-change="current_change" background layout="prev, pager, next,jumper,total" :total="total_data" :page-size="page_size" :current-page="current_page"></el-pagination>
        </div>

        <shop-foot></shop-foot>
        <!-- vue 回到顶部 -->
        <el-backtop></el-backtop>
        
    </div>
</template>

<script>
import ShopTop from "@/components/home/public/head.vue"
import ShopFoot from "@/components/home/public/shop_foot.vue"
export default {
    components: {
        ShopTop,
        ShopFoot,
    },
    props: {},
    data() {
      return {
          goods_class:[],
          brand_list:[],
          goods_list:[],
          props:{
              value:'id',
              label:'name',
              checkStrictly:true,
          },
          info:{
              // class_info:[],
            //   goods_brand:0,
            //   goods_type:0,
              keyword:'',
              order:'',
              sort:'',
              categoryId:'',
              brandId:'',
              pid:0,
              sid:0,
              tid:0,
          },
          total_data:0, // 总条数
          page_size:30,
          current_page:1,
          
      };
    },
    watch: {
        info:{
            handler(){
                this.search_goods();
            },
            deep:true,
        }
    },
    computed: {},
    methods: {
        //  获取商品分类列表 选择商品
        get_goods_class_list:function(){
            // this.goods_class = this.$store.state.goods_class;
            this.$get(this.$api.homeGetSubNavInfo).then(res=>{
                this.goods_class = res.data;
            });
        },
        // 选择分类的选择框改变
        classChange(pid,id,vo,deep=0/*sid=0*/){
            // this.info.categoryId = info.id;
            // if(info.pid == 0)
            //     this.info.pid = info.categoryId;
            // else
            //     this.info.pid = info.pid;
            // this.$forceUpdate();
            // this.search_goods();++
            let params = "class_id."+vo.id+"|pid."+pid;
            if(deep==0)
                params = "class_id."+id+"|pid."+id;
            if(deep==1)
                params = params+"|sid."+vo.id;
            else if(deep==2)
                params = params+"|sid."+vo.pid+"|tid."+vo.id;
            this.$router.push("/goods/params/"+params);
        },
        // 获取品牌信息
        get_brand_list:function(){
            this.$get(this.$api.homeGetBrandList).then(res=>{
                this.brand_list = res.data.list;
            });
        },
        brand_change:function(e){
            this.info.brandId = e;
            this.search_goods();
        },
        goods_sort_click:function(e){
            // 是否取反
            if(e == this.info.goods_type){
                if(this.info.order_type == 0){
                    this.info.order_type = 1;
                }else{
                    this.info.order_type = 0;
                }
            }else{ // 新条件先倒叙
                this.info.goods_type = e;
                this.info.order_type = 0;
            }
        },
        // 搜索指定产品
        search_goods:function(){
            this.info.page = this.current_page;
            this.$get(this.$api.homeSearchGoods,this.info).then(res=>{
                this.page_size = res.data.limit;
                this.total_data = res.data.total;
                this.current_page = res.data.page;
                this.goods_list = res.data.list;
            });
        },
        // 子组件执行搜索
        to_search:function(e){
            this.info.keyword = e.keywords;
            this.search_goods();
        },
        // 分页改变
        current_change:function(e){
            this.current_page = e;
            this.search_goods();
        },
        get_search_where:function(route_info=""){
            this.info.pid = 0;
            this.info.sid = 0;
            this.info.tid = 0;
            let params = this.$route.params.info;
            if(route_info !=''){
                params = route_info;
            }

            let paramsArr = params.split('|');
            paramsArr.forEach(res=>{
                let paramsInfo = [];
                paramsInfo = res.split('.');
                if(paramsInfo[0] == 'keywords'){  // 搜索关键词
                    if(paramsInfo.length==2){
                        this.info.keyword = paramsInfo[1];
                    }else{
                        this.info.keyword = '';
                    }
                }
                if(paramsInfo[0] == 'class_id'){
                    if(paramsInfo.length==2){
                        // this.info.class_info = [paramsInfo[1]];
                        this.info.categoryId = paramsInfo[1];
                        // this.info.sid = paramsInfo[1];
                    }else{
                        // this.info.class_info = [];
                        this.info.categoryId = 0;
                    }
                }
                if(paramsInfo[0] == 'pid'){
                    if(paramsInfo.length==2){
                        this.info.pid = paramsInfo[1];
                    }else{
                        this.info.pid = 0;
                    }
                }
                if(paramsInfo[0] == 'sid'){
                    if(paramsInfo.length==2){
                        this.info.sid = paramsInfo[1];
                    }else{
                        this.info.sid = 0;
                    }
                }
                if(paramsInfo[0] == 'tid'){
                    if(paramsInfo.length==2){
                        this.info.tid = paramsInfo[1];
                    }else{
                        this.info.tid = 0;
                    }
                }
                if(paramsInfo[0] == 'brand_id'){
                    if(paramsInfo.length==2){
                        this.info.brandId = paramsInfo[1];
                    }else{
                        this.info.brandId = 0;
                    }
                }
                if(paramsInfo[0] == 'goods_type'){
                    if(paramsInfo.length==2){
                        this.info.goods_type = paramsInfo[1];
                    }else{
                        this.info.goods_type = 0;
                    }
                }
                if(paramsInfo[0] == 'order_type'){
                    if(paramsInfo.length==2){
                        this.info.order_type = paramsInfo[1];
                    }else{
                        this.info.order_type = 0;
                    }
                }
            });

            // if(this.info.pid == 0) this.info.pid = this.info.categoryId;
            this.search_goods();
        },
    },
    created() {
        
        this.get_goods_class_list();
        this.get_brand_list();
        this.get_search_where();
    },
    beforeRouteUpdate:function(to, from, next) {
        // console.log(to,from)
        if(to.path != from.path){
           this.get_search_where(to.params.info);
        }
        // don't forget to call next()
        next();
    },
    mounted() {}
};
</script>
<style lang="scss" scoped>
.goods_where{
        border: 1px solid #efefef;
        line-height: 50px;
        font-size: 14px;
        .item{
            padding:0 20px;
            box-sizing: border-box;
            border-bottom: 1px solid #efefef;
            &:last-child{
                border-bottom: none;
            }
            &:after{
                clear:both;
                display: block;
                content:'';
            }
            .title{
                float: left;
                margin-right: 20px;
            }
            .list{
                float: left;
                ul li{
                    cursor: pointer;
                }
                .first{
                    &:after{
                        clear: both;
                        display: block;
                        content:'';
                    }
                    ul li{
                        background: #efefef;
                        line-height: 30px;
                        padding:0 10px;
                        margin-top: 10px;
                        text-align: center;
                        float: left;
                        margin-right: 20px;
                        border-radius: 3px;
                        &:hover{
                            color:#fff;
                            background-color:#ca151e;
                        }
                        &.red{
                            color:#fff;
                            background-color:#ca151e;
                        }
                    }
                }
                .sec{
                    ul{
                        border-top: 1px dashed #e1e1e1;
                        margin-top: 20px;
                        &:after{
                            clear:both;
                            display: block;
                            content:'';
                        }
                    }
                    ul li{
                        float: left;
                        margin-right: 20px;
                        padding:0 10px;
                        line-height: 40px;
                        color: #666;
                        font-size: 12px;
                        &:hover{
                            color:#ca151e;
                        }
                        &.red{
                            color:#ca151e;
                        }
                    }
                    &:after{
                        clear: both;
                        display: block;
                        content:'';
                    }
                }
                .other{
                    ul li{
                        float: left;
                        margin-right: 20px;
                        padding:0 10px;
                        position: relative;
                        &:hover{
                            color:#ca151e;
                        }
                        &.red{
                            color:#ca151e;
                        }
                        .sorts{
                            position: absolute;
                            top:0;
                            right: 0;
                            color:#666;
                            .caret{
                                font-size: 12px;
                                position: absolute;
                                -webkit-transform-origin-x: 0; //缩小后文字居左
                                -webkit-transform: scale(0.80);   //关键
                                &:first-child{
                                    top:16px;
                                    right:-5px;
                                }
                                &:last-child{
                                    top:22px;
                                    right:-5px;
                                }
                                &.red{
                                    color:#ca151e;
                                }
                            }
                        }

                    }
                }
            }
        }
    }
.goods_sort_top{
    position: relative;
    color:#666;
    i{
        position: absolute;
        right: -16px;
        font-size: 12px;
    }
}
.goods_sort_bottom{
    
    position: relative;
    color:#666;
    i{
        position: absolute;
        right: -16px;
        font-size: 12px;
    }
}
.mbx{
    margin:20px auto;
}
.goods_where{
    border:1px solid #efefef;
    line-height: 50px;
    font-size: 14px;
    .goods_cat_list{
        border-bottom: 1px solid #efefef;
        padding-left:20px; 
        .goods_cat_title{
            float: left;
            margin-right: 20px;
        }
        ul{
            float: left;
        }
        ul.goods_sort_ul{
            li{
                margin-right: 30px;
            }
        }
        ul li{
            float: left;
            color:#666;
            margin-right: 20px;
            a{
                color:#666;
            }
            a:hover{
                color:#ca151e;
            }
        }
        li:hover{
            color:#ca151e;
        }
        ul li.font_color{
            color:#ca151e;
        }
        
    }
    .goods_cat_list:after{
        content:'';
        clear:both;
        display: block;
    }
    .goods_cat_list:last-child{
        border-bottom: none;
    }
}
.goods_list{
    margin:40px auto;
    .goods_list_item{
        float: left;
        width: 224px;
        height: 364px;
        box-sizing: border-box;
        margin: 0 20px 20px 0;
        dl{
            border: 1px solid #efefef;
            box-sizing: border-box;
            width: 224px;
            height: 364px;
            -webkit-transition: all .2s linear;
            transition: all .2s linear;
            .el-image{
                width: 180px;
                height: 180px;
                margin:30px auto 25px auto;
                display: block;
            }
        }
        dl dt img{
            width: 180px;
            height: 180px;
            margin:30px auto 25px auto;
            display: block;
        }
        dl dd{
            font-size: 14px;
        }
        dl dd.title{
            color: #000;
            height: 36px;
            text-align: center;
            padding: 0 15px;
            overflow: hidden;
        }
        dl dd.price {
            color: #e01d20;
            line-height: 50px;
            text-align: center;
            padding: 0 15px;
            font-size: 20px;
            overflow: hidden;
        }
        dl dd span:first-child{
            box-sizing: border-box;
            border-right: 1px solid #efefef;
        }
        dl dd span:first-child:hover{
            color:#fff;
            background: #ca151e;
        }
        dl dd span{
            width: 50%;
            display: block;
            border-top: 1px solid #efefef;
            float: left;
            line-height: 41px;
            text-align: center;
        }
        dl dd span.retailPrice {
            font-size: 14px;
            color:#b0b0b0;
            text-decoration: line-through;
        }
    }
    .goods_list_item:nth-child(5n){
        margin-right: 0;
    }
    // .goods_list_item:hover{
    //     // border:1px solid #ca151e;
    // }
    .goods_list_item:hover dl{
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        margin-top:-3px;
    }
}
</style>