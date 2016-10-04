package com.ehealth.mc.payment.service.impl;

import org.springframework.beans.factory.annotation.Value;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.ehealth.mc.payment.service.AlipayService;

public class AlipayServiceImpl implements AlipayService {
	
	@Value("${mc.base.url}")
	public String BASE_URL;

	@Value("${mc.alipay.api_url}")
	public String API_URL;

	@Value("${mc.alipay.app_id}")
	public String APP_ID;

	@Value("${mc.alipay.app_private_key}")
	public String APP_PRIVATE_KEY;

	@Value("${mc.alipay.alipay_public_key}")
	public String ALIPAY_PUBLIC_KEY;

	@Value("${mc.alipay.request_format}")
	public String REQUEST_FORMAT;

	@Value("${mc.alipay.request_charset}")
	public String REQUEST_CHARSET;

	@Value("${mc.alipay.return_url}")
	public String RETURN_URL;

	@Value("${mc.alipay.notify_url}")
	public String NOTIFIY_URL;

	@Override
	public String getPayForm(String reqContent) {
		String form = null;
		AlipayClient alipayClient = new DefaultAlipayClient(API_URL, APP_ID, APP_PRIVATE_KEY, REQUEST_FORMAT,
				REQUEST_CHARSET, ALIPAY_PUBLIC_KEY);
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

		alipayRequest.setBizContent(reqContent);
		alipayRequest.setReturnUrl(BASE_URL + RETURN_URL);
		alipayRequest.setNotifyUrl(BASE_URL + NOTIFIY_URL);

		try {
			AlipayTradeWapPayResponse alipayResponse = alipayClient.pageExecute(alipayRequest);
			if (alipayResponse.isSuccess()) {
				form = alipayResponse.getBody();
			}
		} catch (Exception e) {

		}
		return form;
	}

}
