package org.zxy.sell.config;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WechatPayConfig {

    @Autowired
    private WechatPayAccountConfig wechatPayAccountConfig;

    @Bean
    public BestPayServiceImpl bestPayService(WxPayConfig wxPayConfig) {


        BestPayServiceImpl bestPayService = new BestPayServiceImpl();

        bestPayService.setWxPayConfig(wxPayConfig);
        return bestPayService;
    }

    @Bean
    public WxPayConfig wxPayConfig() {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(wechatPayAccountConfig.getMpAppId());
        wxPayConfig.setAppSecret(wechatPayAccountConfig.getMpAppSecret());
        wxPayConfig.setMchId(wechatPayAccountConfig.getMchId());
        wxPayConfig.setMchKey(wechatPayAccountConfig.getMchKey());
        wxPayConfig.setKeyPath(wechatPayAccountConfig.getKeyPath());
        wxPayConfig.setNotifyUrl(wechatPayAccountConfig.getNotifyUrl());

        return wxPayConfig;
    }
}
