package com.ehealth.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ehealth.mc.bo.OrderBilling;
import com.ehealth.mc.payment.service.AlipayService;
import com.ehealth.mc.service.PaymentService;
import com.google.gson.Gson;

public class PaymentServiceImpl implements PaymentService {

	private static Gson gson = new Gson();

	@Autowired
	AlipayService alipayService;

	@Override
	public String getPayForm(OrderBilling orderBilling) {

		AlipayPayRequest payRequest = new AlipayPayRequest();
		payRequest.out_trade_no = orderBilling.getBillingCode();
		payRequest.total_amount = orderBilling.getPrice();
		payRequest.subject = orderBilling.getTitle();
		payRequest.body = orderBilling.getDescription();
		payRequest.product_code = orderBilling.getProductCode();

		String alipayForm = alipayService.getPayForm(gson.toJson(payRequest));
		return alipayForm;
	}
}

class AlipayPayRequest {

	/*
	 * 参数 类型 是否必填 最大长度 描述 示例值 body String 否 128 Iphone6 16G
	 * 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。 subject String 是 256
	 * 商品的标题/交易标题/订单标题/订单关键字等。 大乐透 out_trade_no String 是 64 商户网站唯一订单号
	 * 70501111111S001111119 timeout_express String 否 6
	 * 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，
	 * 都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。 90m total_amount Price 是 9
	 * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000] 9.00 seller_id String 否 28
	 * 收款支付宝用户ID。 如果该值为空，则默认为商户签约账号对应的支付宝用户ID 2088102147948060 auth_token String
	 * 否 40 针对用户授权接口，获取用户相关数据时，用于标识用户授权关系
	 * appopenBb64d181d0146481ab6a762c00714cC27 product_code String 是 64
	 * 销售产品码，商家和支付宝签约的产品码 QUICK_WAP_PAY
	 */

	String body;
	String subject;
	String out_trade_no;
	String timeout_express;
	Double total_amount;
	String seller_id;
	String auth_token;
	String product_code;
}
