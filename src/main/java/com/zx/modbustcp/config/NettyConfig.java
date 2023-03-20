package com.zx.modbustcp.config;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.zx.modbustcp.client.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;


@Component
public class NettyConfig {

    private static Logger log = LoggerFactory.getLogger(NettyConfig.class);



    /**
     * 初始化连接
     * 查询数据库表中所有host和port不为空的设备
     * netty客户端尝试连接并保存至channels内存中
     */
    @PostConstruct
    public void getDeviceWarterInfo() {
        try {
            Map<String,Object> mapParam =new HashMap<String, Object>();
          //  List<Map<String, Object>> list = serviceDeviceWarterInfo.selectDeviceWarterInfoByIPPortNotNull(mapParam);
            Map<String, Object> servle =new HashMap<>();
            servle.put("IP","127.0.0.1");
            servle.put("PORT",502);

            Map<String, Object> servle1=new HashMap<>();
            servle1.put("IP","127.0.0.1");
            servle1.put("PORT",503);
            List<Map<String, Object>> list =new LinkedList<>();
            list.add(servle);
            list.add(servle1);
            NettyClient.channels = NettyClient.getChannel(list);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
