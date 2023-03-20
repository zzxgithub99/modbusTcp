package com.zx.modbustcp.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
/**
 * 功能描述: 自定义接收消息格式
 *
 * @Author

 */
public class MyDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte [] b =new byte[byteBuf.readableBytes()];
        //复制内容到b数组
        byteBuf.readBytes(b);
        list.add(bytesToString(b));

    }
    public String bytesToString (byte[] bArray){
        StringBuffer sb =new StringBuffer(bArray.length);
        String sTemp;
        for(int i=0;i<bArray.length;i++){
            //0xFF&bArray[i]表示 255和这个数组的值 进行与运算
            sTemp=Integer.toHexString(0xFF&bArray[i]);
            if(sTemp.length()<2){
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
    public static String toHexString1(byte []b){
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<b.length;i++){
            buffer.append(toHexString1(b[i]));
        }
        return buffer.toString();
    }
    public static String toHexString1(byte b){
        String s=Integer.toHexString(b&0xFF);
        if(s.length()==1){
            return "0"+s;
        }else {
            return s;
        }
    }
}
