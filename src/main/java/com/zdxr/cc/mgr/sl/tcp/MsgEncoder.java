package com.zdxr.cc.mgr.sl.tcp;

import com.zdxr.cc.mgr.sl.tcp.bean.Message;
import com.zdxr.cc.mgr.sl.tcp.bean.type.MsgLengthEnum;
import com.zdxr.cc.mgr.sl.tcp.util.MessageUtil;
import com.zdxr.cc.mgr.sl.tcp.util.NullUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MsgEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        if (msg == null) {
            return;
        }

        byte[] wirteBytes = new byte[0];
        if (NullUtils.isNotEmpty(msg.getContent())) {
            wirteBytes = MessageUtil.encryption(msg.getContent());
        }
        out.writeBytes(MessageUtil.long2Bytes(msg.getTypeInt(), MsgLengthEnum.TYPE_LENGTH.getLength()));
        out.writeBytes(MessageUtil.long2Bytes(msg.getSeqNo(), MsgLengthEnum.SEQ_LENGTH.getLength()));
        out.writeBytes(MessageUtil.long2Bytes(wirteBytes.length, MsgLengthEnum.MSG_LENGTH.getLength()));
        out.writeBytes(wirteBytes);
    }
}
