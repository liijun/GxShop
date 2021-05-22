package org.linlinjava.litemall.core.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import org.linlinjava.litemall.core.config.AlipayConfig;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 支付宝接口工具类
 *
 * @author mowen
 *
 */
public class AlipayUtil {
    /**
     * 发起支付
     * @param outTradeNo      商户订单号
     * @param amount          金额
     * @param subject         主题
     * @param body          内容
     * @param passback_params 自定义回调参数
     * @return
     * @throws AlipayApiException
     * @throws Exception
     */
    public static String pay(String outTradeNo,String amount, String subject, String body, String passback_params) throws AlipayApiException {
        // 获得初始化的AlipayClient
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        // 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = outTradeNo;
        // 付款金额，必填
        String total_amount = amount;

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\"," + "\"body\":\"" + body
                + "\"," + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\","
                + "\"passback_params\":\"" + passback_params + "\"}");

        // 请求
        AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest);

        String result = "";
        if (response.isSuccess()) {

            result = response.getBody();
        }

        return result;

    }

    /**
     * 支付宝退款接口
     *
     * @param outTradeNo     商户订单号
     * @param tradeNo        支付宝交易号
     * @param refundAmount   退款的金额
     * @param refundReason   退款的原因说明
     * @param out_request_no 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
     * @return
     */
    public static String aliRefund(String outTradeNo, String tradeNo, String refundAmount, String refundReason, String out_request_no) {
        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        // 设置请求参数
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        try {
            alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\"," + "\"trade_no\":\"" + tradeNo + "\"," + "\"refund_amount\":\"" + refundAmount + "\"," + "\"refund_reason\":\"" + refundReason + "\"," + "\"out_request_no\":\"" + out_request_no + "\"}");
            // 请求
            String result;
            result = alipayClient.execute(alipayRequest).getBody();
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 支付宝的验签方法
     *
     * @param req
     * @return
     */
    public static boolean checkSign(HttpServletRequest req) {
        Map<String, String[]> requestMap = req.getParameterMap();
        Map<String, String> paramsMap = new HashMap<>();
        requestMap.forEach((key, values) -> {
            String strs = "";
            for (String value : values) {
                strs = strs + value;
            }
            System.out.println(("key值为" + key + "value为：" + strs));
            paramsMap.put(key, strs);
        });

        // 调用SDK验证签名
        try {
            return AlipaySignature.rsaCheckV1(paramsMap, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("*********************验签失败********************");
            return false;
        }
    }

}