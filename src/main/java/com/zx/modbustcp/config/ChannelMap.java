package com.zx.modbustcp.config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ChannelMap {
    //管理一个全局map,保存连接进服务端的通道数量
    private static final ConcurrentHashMap <ChannelId, Channel> CHANNEL_MAP = new ConcurrentHashMap<>(128);

    public static ConcurrentHashMap<ChannelId,Channel> getChannelMap(){
        return CHANNEL_MAP;
    }
    /**
     *  获取指定的name 和channel
     * */
    public static Channel getChannelByName(ChannelId channelId){
        if(CollectionUtils.isEmpty(CHANNEL_MAP)){
            log.info("获取的id为 空");
            return null;
        }
        return CHANNEL_MAP.get(channelId);
    }
    /**
     *  将通道中的信息推送到每个客户端
     * */
    public static boolean pushNewsToAllClient(String obj){
        if(CollectionUtils.isEmpty(CHANNEL_MAP)){
            return false;
        }
        for(ChannelId channelId:CHANNEL_MAP.keySet()){
            Channel channel=CHANNEL_MAP.get(channelId);
            channel.writeAndFlush(new TextWebSocketFrame(obj));
        }
        return true;
    }
    /**
     *  将channel和对应的name添加到ContcurrentHashMap
    * */
    public static void addChannel(ChannelId channelId,Channel channel){
        CHANNEL_MAP.put(channelId,channel);
    }

    public static boolean removeChannelByName(ChannelId channelId){
        if(CHANNEL_MAP.containsKey(channelId)){
            CHANNEL_MAP.remove(channelId);
            return true;
        }
        return false;
    }

}
