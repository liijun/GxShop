package org.linlinjava.litemall.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 类名：AlipayConfig 功能：基础配置类 详细：设置帐户有关信息及返回路径
 */
@Configuration
@ConfigurationProperties(prefix = "litemall.alipay")
public class AlipayConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id;

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key;

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key;

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url;


    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url;

    // 签名方式
    public static String sign_type;

    // 字符编码格式
    public static String charset;

    // 支付宝网关
    public static String gatewayUrl;

    // 支付宝网关
    public static String log_path;


    public String getApp_id() {
        return app_id;
    }

//    @Value("${alipay.app_id}")
    public void setApp_id(String app_id) {
        AlipayConfig.app_id = app_id;
    }

    public String getMerchant_private_key() {
        return merchant_private_key;
    }

//    @Value("${alipay.merchant_private_key}")
    public void setMerchant_private_key(String merchant_private_key) {
        AlipayConfig.merchant_private_key = merchant_private_key;
    }

    public String getAlipay_public_key() {
        return alipay_public_key;
    }

//    @Value("${alipay.alipay_public_key}")
    public void setAlipay_public_key(String alipay_public_key) {
        AlipayConfig.alipay_public_key = alipay_public_key;
    }

    public String getNotify_url() {
        return notify_url;
    }

//    @Value("${alipay.notify_url}")
    public void setNotify_url(String notify_url) {
        AlipayConfig.notify_url = notify_url;
    }

    public String getReturn_url() {
        return return_url;
    }

//    @Value("${alipay.return_url}")
    public void setReturn_url(String return_url) {
        AlipayConfig.return_url = return_url;
    }

    public String getSign_type() {
        return sign_type;
    }

//    @Value("${alipay.sign_type}")
    public void setSign_type(String sign_type) {
        AlipayConfig.sign_type = sign_type;
    }

    public String getCharset() {
        return charset;
    }

//    @Value("${alipay.charset}")
    public void setCharset(String charset) {
        AlipayConfig.charset = charset;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

//    @Value("${alipay.gatewayUrl}")
    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public String getLog_path() {
        return log_path;
    }

//    @Value("${alipay.log_path}")
    public void setLog_path(String log_path) {
        AlipayConfig.log_path = log_path;
    }

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
