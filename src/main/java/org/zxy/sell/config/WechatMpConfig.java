package org.zxy.sell.config;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WechatMpConfig {

    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Bean
    public WxMpService wxMpService(WxMpDefaultConfigImpl wxMpDefaultConfig) {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpDefaultConfig);

        return wxMpService;
    }

    @Bean
    public WxMpDefaultConfigImpl wxMpDefaultConfig() {
         WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
         wxMpDefaultConfig.setAppId(wechatAccountConfig.getMpAppId());
         wxMpDefaultConfig.setSecret(wechatAccountConfig.getMpAppSecret());

         return wxMpDefaultConfig;
    }
}
