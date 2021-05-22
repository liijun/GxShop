<template>
    <div class="user_content_blcok">
        <div class="user_content_blcok_title" style="position: relative;">
            个人订单
            <div class="order_search">
                <el-input placeholder="请输入订单号" v-model="order_no" >
                    <el-button @click="get_user_order()" slot="append" icon="el-icon-search"></el-button>
                </el-input>
            </div>
            <div class="order_type">
                <ul>
                    <li @click="order_type='0';order_no='';get_user_order()" :class="order_type==''?'red':''">全部 ({{ this.total_data}})</li>
                    <li @click="order_type='1';order_no='';get_user_order()" :class="order_type=='NOPAY'?'red':''">待支付 ({{order_num.unpaid}})</li>
                    <li @click="order_type='2';order_no='';get_user_order()" :class="order_type=='WAITSEND'?'red':''">待发货 ({{order_num.unship}})</li>
                    <li @click="order_type='3';order_no='';get_user_order()" :class="order_type=='WAITREC'?'red':''">待收货 ({{order_num.unrecv}})</li>
                    <li @click="order_type='4';order_no='';get_user_order()" :class="order_type=='WAITCOMMENT'?'red':''">待评论 ({{order_num.uncomment}})</li>
                    <li @click="order_type='5';order_no='';get_user_order()" :class="order_type=='REFUND'?'red':''">售后处理 ({{order_num.refund}})</li>
                </ul>
            </div>
            <div class="clear"></div>
        </div>
        <div class="user_content_blcok_line"></div>

        <div class="user_index_order_list" v-if="order_list.length>0">
            <div class="user_index_store_list" v-for="(v,k) in order_list" :key="k">
                <div class="user_index_store_title">订单号：{{v.orderSn}}<div class="order_time" :style="'color:'+v.cn_status_color">{{v.orderStatusText}}</div><div class="order_time">￥{{v.actualPrice}}</div></div>
                <ul>
                    <li v-for="(vo,key) in v.goodsList" :key="key">
                        <router-link :to="'/goods/info/'+vo.goods_id">
                        <div class="user_order_goods_thumb"><img width="40px" height="40px" :src="vo.picUrl" alt=""></div>
                        <div class="user_order_goods_title">{{vo.goodsName}}</div>
                        <div class="user_order_goods_price">￥{{vo.price}}</div>
                        <div class="user_order_goods_num">{{vo.specifications.join(',')||' - '}}</div>
                        <div class="user_order_goods_num">x{{vo.number}}</div>
                        </router-link>
                        <div class="clear"></div>
                        
                    </li>
                </ul>
                <!-- 处理订单 -->
                <div class="handle_btn">
                    <el-button type="danger" v-if="v.handleOption.pay" @click="pay(v.id)">前往支付</el-button>
                    <el-button v-if="v.handleOption.cancel" @click="orderCancel(v.id)">取消订单</el-button>
                    <el-button type="danger" v-if="v.handleOption.confirm" @click="orderConfirm(v.id)">确认收货</el-button>
                    <el-button v-if="v.handleOption.confirm" @click="delivery_show(v.id)">查看物流</el-button>
                    <el-button type="danger" v-if="v.handleOption.comment" @click="$router.push('/user/comment/add/'+v.id)">前往评论</el-button>
                    <el-button type="danger" v-if="v.handleOption.refund" @click="orderRefund(v.id)">申请退款</el-button>
                    <el-button v-if="v.handleOption.delete" @click="orderDelete(v.id)">删除订单</el-button>
<!--                    <el-button type="warning" v-if="v.handleOption.refund" @click="open_after_sale(v)">查看售后</el-button>-->
                </div>
            </div>
        </div>
        <!-- 没有订单则 -->
        <div class="empty_order" v-else>
            <dl>
                <dt><img src="/pc/not-common-icon.png" alt=""></dt>
                <dd>主人，您近期还没有购买任何商品哟~</dd>
                <dd class="btn"><router-link to="/">前往商城</router-link></dd>
            </dl>
        </div>

        <div class="home_fy_block" v-show="order_list.length>0">
            <el-pagination @current-change="current_change" background layout="prev, pager, next,jumper,total" :total="total_data" :page-size="page_size" :current-page="current_page"></el-pagination>
        </div>

        <!-- 物流显示 -->
        <el-dialog title="物流列表" :visible.sync="deliveryShow">
            <!-- 快递信息 -->
            <div class="freight_info">
                <span>【{{expressInfo.shipperName}} | {{expressInfo.logisticCode}}】</span>
                <el-timeline v-if="expressInfo  && expressInfo.Traces && expressInfo.Traces.length>0">
                    <el-timeline-item v-for="(vo,kess) in expressInfo.Traces" :key="kess" v-show="kess< expressInfo.Traces.length-1" :timestamp="vo.acceptTime">{{vo.acceptStation}}</el-timeline-item>
                    <el-timeline-item size='large' color="#0bbd87" :timestamp="expressInfo.Traces[expressInfo.Traces.length-1].acceptTime">{{expressInfo.Traces[expressInfo.Traces.length-1].acceptStation}}</el-timeline-item>
                </el-timeline>
                <div class="no_freight" v-else>
                    没有任何快递信息
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="deliveryShow = false">关 闭</el-button>
            </span>
        </el-dialog>

        <!-- 售后处理 -->
        <el-dialog title="售后处理" :visible.sync="afterSale">
            
            <div class="after_sale">
                <el-form ref="after_sale" label-width="120px">
                    <el-form-item label="订单号">
                        {{afterSaleInfo.order_no}}
                    </el-form-item>
                    <el-form-item label="退款/换货">
                        <div class="red_radio" v-if="afterSaleInfo.refund_step == 0">
                            <el-radio-group v-model="afterParams.refund_status">
                                <el-radio  :label="1">退款</el-radio>
                                <el-radio  :label="2">换货</el-radio>
                            </el-radio-group>
                        </div>
                        <div v-else>{{afterSaleInfo.refund_status==1?'退款':'退货'}}</div>
                    </el-form-item>
                    <el-form-item label="退款/退货原因" >
                        <el-input v-if="afterSaleInfo.refund_step == 0" type="text" placeholder="请输入原因" v-model="afterParams.refund_info" maxlength="100" show-word-limit ></el-input>
                        <div v-else>{{afterSaleInfo.refund_info}}</div>
                    </el-form-item>
                    <el-form-item label="输入快递单号" v-if="afterSaleInfo.refund_step == 1">
                        <el-input type="text" placeholder="请输入快递单号" v-model="afterParams.refund_delivery_no" maxlength="100" show-word-limit ></el-input>
                    </el-form-item>
                    <el-form-item label="商家发货单号" v-if="afterSaleInfo.refund_step == 3">
                        {{afterSaleInfo.refund_no+'（商家已经发货，收到商品后请确定收货）'}}
                    </el-form-item>
                    <el-form-item label="商家发货单号" v-if="afterSaleInfo.refund_step == 4">
                        售后已经处理结束！
                    </el-form-item>
                </el-form>
                
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="afterSale = false">关 闭</el-button>
                <el-button type="danger"  v-if="afterSaleInfo.refund_step==0" @click="refund()">申 请</el-button>
                <el-button type="danger"  v-if="afterSaleInfo.refund_step==1" @click="refund_no()">提交</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    components: {},
    props: {},
    data() {
      return {
          order_list:[],
          order_num:{},
          total_data:0, // 总条数
          page_size:20,
          current_page:1,
          order_no:'',
          order_type:0,
          deliveryShow:false,
          // delivery_list:undefined,
          expressInfo:{},
          afterSale:false,
          afterSaleInfo:{
              id:0,
          },
          afterParams:{
              refund_status:1,
          },
      };
    },
    watch: {},
    computed: {},
    methods: {
        get_user_order:function(){
            // console.log(this.order_type);
            this.$get(this.$api.homeGetUserIndex,{}).then(res=>{
                if(res.errno== 0)
                    this.order_num = res.data.order;
            });
            this.$get(this.$api.homeGetUserOrder,{page:this.current_page,order_no:this.order_no,showType:this.order_type}).then(res=>{
                window.console.log(res);
                this.order_list = res.data.list;
                // this.order_num = res.data.order_num;
                this.page_size = res.data.limit;
                this.total_data = res.data.total;
                this.current_page = res.data.page;
            });
        },
        // 分页改变
        current_change:function(e){
            this.current_page = e;
            this.get_user_order();
        },
        pay:function(orderId){
            // let order_info = this.order_list[index];
            // console.log(this.order_list[index]);
            // let strArr = [];
            // let str = '';
            // order_info.order_goods.forEach(res => {
            //     str = res.goods_id+'|'+order_info.store_id+'|'+res.goods_num;
            //     if(!this.$isEmpty(res['spec_id']) && res['spec_id']>0){
            //         str += '|'+res['spec_id'];
            //     }
            //     strArr.push(str);
            // });
            // let str_info = strArr.join(',');
            return this.$router.push('/order/chose_pay/'+orderId)//+'/0/'+str_info)
        },
        // 取消订单
        orderCancel:function(orderId){
            this.$post(this.$api.OrderCancel,{orderId:orderId}).then(res=>{
                if(res.errno == 0){
                    this.get_user_order();
                    return this.$message.success('取消订单成功');
                }else{
                    return this.$message.error(res.errmsg);
                }
            });
        },
        // 删除订单
        orderDelete:function(orderId){
            this.$post(this.$api.OrderDelete,{orderId:orderId}).then(res=>{
                if(res.errno == 0){
                    this.get_user_order();
                    return this.$message.success('删除订单成功');
                }else{
                    return this.$message.error(res.errmsg);
                }
            });
        },
        // 退款申请
        orderRefund:function(orderId){
            this.$post(this.$api.OrderRefund,{orderId:orderId}).then(res=>{
                if(res.errno == 0){
                    this.get_user_order();
                    return this.$message.success('退款申请成功');
                }else{
                    return this.$message.error(res.errmsg);
                }
            });
        },
        // 确认收货
        orderConfirm:function(orderId){
            this.$post(this.$api.OrderConfirm,{orderId:orderId}).then(res=>{
                if(res.errno == 0){
                    this.get_user_order();
                    return this.$message.success('确认收货成功');
                }else{
                    return this.$message.error(res.errmsg);
                }
            });
        },
        delivery_show:function(orderId){
            // this.$get(this.$api.homeGetDeliveryList,{delivery_no:no}).then(res=>{
            //     if(res.code == 500){
            //         return this.$message.error(res.msg);
            //     }else{
            //         this.delivery_list = res.data;
            //         this.deliveryShow = true;
            //     }
            // })
            this.$get(this.$api.homeGetOrderDetail,{orderId:orderId}).then(res=>{
                // window.console.log(res);
                if(res.errno==0){
                    this.expressInfo = res.data.expressInfo;
                    this.deliveryShow = true;
                }else
                    return this.$message.error(res.msg);
            });
        },
        // 修改订单状态
        change_order_status:function(id){
            this.$post(this.$api.homeChangeOrderStatus,{order_id:id}).then(()=>{
                this.get_user_order();
                this.$message.success('确认收货');
            });
        },
        // 打开售后处理
        open_after_sale:function(v){
            this.afterSale = true;
            this.afterSaleInfo = v;
        },
        refund:function(){
            this.afterParams.id = this.afterSaleInfo.id;
            this.$post(this.$api.homeOrderRefund,this.afterParams).then(()=>{
                this.$message.success('申请成功');
                this.afterSale = false;
                this.get_user_order();
            });
        },
        refund_no:function(){
            this.afterParams.id = this.afterSaleInfo.id;
            this.$post(this.$api.homeOrderRefundDelivery,this.afterParams).then(()=>{
                this.$message.success('提交成功');
                this.afterSale = false;
                this.get_user_order();
            });
        }
    },
    created() {
        if(!this.$isEmpty(this.$route.params.order_type)){
            this.order_type = this.$route.params.order_type;
        }
        this.get_user_order();
    },
    mounted() {}
};
</script>
<style lang="scss" scoped>
.no_freight{
    line-height: 60px;
    text-align: center;
    font-size: 14px;
    color:#999;
}
.handle_btn{
    text-align: right;
    margin: 20px 0;
}
.user_index_order_list{
    margin-bottom: 50px;
}
.order_time{
    margin-left: 20px;
    color:#666;
    float: right;
    font-size: 12px;
}
.user_index_store_list{
    ul{
        border:1px solid #efefef;
        
    }
    ul li{
        border-bottom: 1px solid #efefef;
        padding:20px 15px;
    }
    ul li:last-child{
        border-bottom: none;
    }
}
.user_order_goods_thumb{
    float: left;
    margin-right: 15px;
    display: block;
    width: 40px;
    
}

.user_order_goods_title{
    font-size: 12px;
    color:#666;
    width: 280px;
    float: left;
}
.user_order_goods_num{
    font-size: 12px;
    color:#666;
    float: left;
    display: block;
    line-height: 40px;
    width: 200px;
    text-align: center;
}
.user_order_goods_price{
    font-size: 12px;
    line-height: 40px;
    color:#ca151e;
    width: 90px;
    float: left;
}
.order_search{
    float: right;
    margin-left: 20px;
    position: absolute;
    right: 10px;
    top: -5px;
    width: 180px;
}
.user_index_store_title{
    background: #f6f6f6;
    line-height: 40px;
    color:#666;
    padding:0 15px;
    margin:20px 0;
    a:hover{
        color:#ca151e;
    }
}
.order_type{
    float: right;
    font-size: 14px;
    margin-top: 2px;
    ul{
        margin-right: 280px;
        
    }
    ul li {
        color:#999;
        float: left;
        margin-right: 20px;
    }
    ul li.red{
        color:#ca151e;
    }
    ul li:hover{
        color: #ca151e;
    }
}
.freight_info { span{margin-bottom: 20px; font-size:14px;}}

</style>