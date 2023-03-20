package com.zx.modbustcp.client;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zx.modbustcp.handle.NettyClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    private static Logger log = LoggerFactory.getLogger(NettyClient.class);

    public static Map<String, ChannelFuture> channels = null;

    static EventLoopGroup eventLoopGroup = new NioEventLoopGroup();


    /**
     * 初始化Bootstrap
     */
    public static final Bootstrap getBootstrap(EventLoopGroup group) {
        if (null == group) {
            group = eventLoopGroup;
        }
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
        return bootstrap;
    }
    //二次重连
    public static final Map<String , ChannelFuture> twoGetChannel(List<Map<String,Object>> portHosts){

        eventLoopGroup.shutdownGracefully();

        eventLoopGroup = new NioEventLoopGroup();

        return getChannel(portHosts);
    }


    //   获取所有连接
    public static final Map<String , ChannelFuture> getChannel(List<Map<String,Object>> portHosts) {
        Map<String , ChannelFuture> result = new HashMap<>();
        Bootstrap bootstrap = getBootstrap(null);

        for (Map<String, Object> map : portHosts) {
            String host = (String) map.get("IP");
            int port = (int) map.get("PORT");
            bootstrap.remoteAddress(host, port);
            //异步连接tcp服务端
            ChannelFuture future = bootstrap.connect().addListener((ChannelFuture futureListener) -> {
                final EventLoop eventLoop = futureListener.channel().eventLoop();
                if (!futureListener.isSuccess()) {

                    log.info("与"+host+":"+port+"连接失败!");                    //与设备的连接失败-推送mqtt主题
               /*     Map<String, Object> pushMessage = new HashMap<String,Object>();
                    pushMessage.put("IP", host);
                    pushMessage.put("PORT", port);
                    pushMessage.put("STATUS", "0");//0-离线 1-正常 2-报警
                    pushMessage.put("CJSJ", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    System.out.println();*/
                  //  MqttPushClient.publish("推送主题", JSONObject.toJSONString(pushMessage));
                }else {
                    System.out.println("连接成功！！！");
                }
            });
            result.put(host+":"+port, future);
        }
        return result;
    }

    //发送消息
    public static void sendMsg(ChannelFuture future, String msg) throws Exception {
        if (future != null && future.channel().isActive()) {
            Channel channel = future.channel();
            InetSocketAddress ipSocket = (InetSocketAddress) channel.remoteAddress();
            String host = ipSocket.getHostString();
            int port = ipSocket.getPort();
            System.out.println("向设备" + host+":"+port+"发送数据");
            //项目封装的util类
            byte[] msgbytes =hexString2Bytes(msg);
            ByteBuf buf = Unpooled.buffer();
            buf.writeBytes(msgbytes);
            // 2.写数据
            future.channel().writeAndFlush(buf).sync();

        }
    }
    public static byte[] hexString2Bytes(String src){
        int l=src.length()/2;
        byte[] ret = new byte[l];
        for (int i=0;i<l;i++){
            ret[i] = (byte) Integer.valueOf(src.substring(i*2,i*2+2),16).byteValue();
        }
        return ret;
    }
}
