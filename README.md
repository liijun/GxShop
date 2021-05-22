# 欢迎使用 GxShop商城系统 

------

GxShop是一套前后端分离，功能齐全操作简易的商城框架.目前已实现B2C完整电商功能，支付集成了微信扫码支付和支付宝PC端。
[vue前端基于青梧商城改造](https://gitee.com/qingwuitcn/qwShopVue)
后台基于LiteMall。


> * LiteMall + Vue 前后分离
> * 多商户、秒杀、团购、在线聊天、三级分销、Wechat支付、Alipay支付
> * 支持二次开发

![cmd-markdown-logo](http://123.206.215.59/pc/logo.png)

## 代码地址：

[后端代码地址](https://gitee.com/lijunnew/GxShop/litemall)
[Vue 前端代码地址](https://gitee.com/lijunnew/GxShop/pcvue)


## 演示地址:
[演示地址](http://123.206.215.59) 
演示支付为真实能用，请切勿支付，没时间给退款！！！
> * [后台地址](http://123.206.215.59/shop-admin): admin 123456 
> * [用户登录](http://123.206.215.59/user/login): user123 user123 


> 欢迎各位提交建议、BUG。联系方式 **QQ群:234357526**  `添加请写上说明如：GxShop商城`。

------

## 如何使用GxShop商城？



1. LiteMall 后台
由于LiteMall没有PC端支付，我们增加了这一部分的后台功能。
[LiteMall说明文档](https://linlinjava.gitbook.io/litemall/)

2. Vue

```vue
npm install (安装扩展)

前端记得要修改src\plugins\api.js  应该是第一行 后端api 根域名，应该是127.0.0.1:8080 （这个是你api服务器域名）

npm run build
```




## 常见问题

1. 刷新404 后台无法访问。
https://router.vuejs.org/zh/guide/essentials/history-mode.html   可根据自己环境修改

