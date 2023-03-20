package com.zx.modbustcp.timber;

import java.util.*;


import com.zx.modbustcp.client.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import io.netty.channel.ChannelFuture;


@Component
public class NettyTaskTimer {
    private static Logger log = LoggerFactory.getLogger(NettyTaskTimer.class);

    /**
     * 定时更新设备netty连接
     */
    @Scheduled(cron = "0/50 * * * * ?")
    public void getDeviceWarterInfo() {
        try {
            System.out.println("每50秒执行NettyTaskTimer的getDeviceWarterInfo 方法 ");
            Map<String, Object> servle =new HashMap<>();
            servle.put("IP","127.0.0.1");
            servle.put("PORT",502);

            Map<String, Object> servle1=new HashMap<>();
            servle1.put("IP","127.0.0.1");
            servle1.put("PORT",503);
            List<Map<String, Object>> list =new LinkedList<>();
            list.add(servle);
            list.add(servle1);
            NettyClient.channels = NettyClient.twoGetChannel(list);
            log.info("更新设备netty连接成功!");
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 通过netty客户端定时向所有长连接的设备请求数据
     * 每整点分钟获取一次,间隔1分钟
     */
    @Scheduled(cron = "0/2 * * * * ?")
    private void instantaneousFlow() {
        try {
            Map<String, ChannelFuture> channels = NettyClient.channels;
            Set<String> keySet = channels.keySet();
            for (String key : keySet) {
                ChannelFuture channel = channels.get(key);
                String msg="000000000006010300020004";
                NettyClient.sendMsg(channel,msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
