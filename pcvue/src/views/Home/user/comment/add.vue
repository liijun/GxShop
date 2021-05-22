<template>
    <div class="user_content_blcok">
        <div class="user_content_blcok_title">
            订单评论
        </div>
        <div class="user_content_blcok_line"></div>

        <div class="user_comment_order_info">
            <div class="order_no" v-if="order_info.orderInfo">订单号：{{order_info.orderInfo.orderSn}}</div>
            <dl v-for="(vo,key) in order_info.orderGoods" :key="key">
                <dt style="width:100px;height:100px;"><el-image width="100px" height="100px" :src="vo.picUrl"></el-image></dt>
                <dd>{{vo.goodsName}}</dd>
                <div class="clear"></div>
            </dl>
        </div>

        <div class="user_content_block_show">
            <div class="admin_form_main">
                <el-form  label-width="100px" ref="info" :model="info">
                    <el-form-item label="产品相符" prop="agree"><div class="user_comment_star"><el-rate show-text :texts="texts" v-model ="info.agree" ></el-rate></div></el-form-item>
                    <!--<el-form-item label="服务态度" prop="service"><div class="user_comment_star"><el-rate show-text :texts="texts" v-model ="info.service" ></el-rate></div></el-form-item>-->
                    <!--<el-form-item label="发货速度" prop="speed"><div class="user_comment_star"><el-rate show-text :texts="texts" v-model ="info.speed" ></el-rate></div></el-form-item>-->
                    
                    <el-form-item label="评论内容" prop="content" :rules="[{required:true,message:'评论内容不能为空',trigger: 'blur' }]"><div class="user_comment_input"><el-input  type="textarea" placeholder="请输入内容" v-model="info.content"></el-input></div></el-form-item>
                    <el-form-item label="评论图片" prop="comment_images" class="width_auto">
                        <el-upload 
                        :action="$api.homeUploadImage"
                        :headers="upload_headers"
                        list-type="picture-card"
                        :on-preview="handlePictureCardPreview"
                        :limit="4"
                        :on-success="onSuccess"
                        :on-remove="handleRemove">
                        <i class="el-icon-plus"></i>
                        </el-upload>
                        <el-dialog :visible.sync="dialogVisible">
                        <img width="100%" :src="dialogImageUrl" alt="">
                        </el-dialog>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="danger" @click="submitForm('info')">提交</el-button>
                    </el-form-item>
                </el-form>
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
          info:{
              agree:5,
              service:5,
              speed:5,
              content:'非常不错！',
              hasPicture:false,
          },
          texts:['1分','2分','3分','4分','5分'],
          order_info:{},
          dialogImageUrl:'',
          dialogVisible:false,
          upload_headers:{},
          fileList:[],
      };
    },
    watch: {},
    computed: {},
    methods: {
        get_order_info:function(){
            this.$get(this.$api.homeGetOrderDetail,{orderId:this.$route.params.order_id}).then(res=>{
                // window.console.log(res);
                if(res.errno==0){
                    this.order_info = res.data;
                    // this.deliveryShow = true;
                }else
                    return this.$message.error(res.msg);
            });
        },
        submitForm:function(e){
            let _this = this;

            // 验证表单
            this.$refs[e].validate(function(res){
                if(res){
                    _this.submit_start();
                }
            });
        },
        submit_start:function(){
            // this.info.goods_list = this.order_info['goods_list'];
            // window.console.log(this.info);
            let errors='';
            for(var i=0;i<this.order_info.orderGoods.length;i++) {
                this.info.orderGoodsId = this.order_info.orderGoods[i].id;
                this.info.star = this.info.agree,
                this.info.hasPicture = this.info.hasPicture,
                this.info.picUrls =  this.fileList;
                // this.info.comment_list = this.fileList;
                this.$post(this.$api.homeAddComment, this.info).then(res => {
                    if (res.errno != 0) {
                        errors = errors + res.errmsg;
                    }
                });
                if(errors=='') {
                    this.$message.success('添加成功');
                    return this.$router.push('/user/order/');
                }else
                    return this.$message.error(errors);
            }
        },
        handleRemove(file, fileList) {
            // eslint-disable-next-line no-console
            window.console.log(fileList);
            let index = this.fileList.indexOf(file.url);
            this.fileList.splice(index,1);
        },
        handlePictureCardPreview(file) {
            this.dialogImageUrl = file.url;
            this.dialogVisible = true;
        },
        onSuccess:function(e){
            // window.console.log(e);
            // this.fileList.push({name:'',url:e.data})
            this.fileList.push(e.data.url);

            this.info.hasPicture = true;
        }
    },
    created() {
        this.get_order_info();
        this.upload_headers.Authorization = 'Bearer '+localStorage.getItem('token'); // 要保证取到
    },
    mounted() {}
};
</script>
<style lang="scss" scoped>
.user_comment_star{
    .el-rate{
        line-height: 2;
    }
}
.order_no{
    background: #efefef;
    line-height: 30px;
    padding-left: 15px;
    margin-bottom: 20px;
}
.user_content_block_show{
    clear:both;
}
.user_comment_order_info{
    dl{
        border-bottom: 1px solid #efefef;
        margin-bottom: 20px;
        padding-bottom: 20px;
    }
    dl dt{
        float: left;
        margin-right: 20px;
        
    }
    dl dd{
        line-height: 35px;
        color:#666;
    }
    
    
}
</style>