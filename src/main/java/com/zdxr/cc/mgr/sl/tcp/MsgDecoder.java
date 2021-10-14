package com.zdxr.cc.mgr.sl.tcp;

import com.zdxr.cc.mgr.sl.tcp.bean.Message;
import com.zdxr.cc.mgr.sl.tcp.bean.type.MsgLengthEnum;
import com.zdxr.cc.mgr.sl.tcp.bean.type.MsgStateEnum;
import com.zdxr.cc.mgr.sl.tcp.util.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 
 * MsgStateEnum：记录解码状态的状态机枚举类
 */
public class MsgDecoder extends ReplayingDecoder<MsgStateEnum> {
	Logger logger = LoggerFactory.getLogger(MsgDecoder.class);
	Message msg = null;

	public MsgDecoder() {
		super(MsgStateEnum.STEP_TYPE);//初始化解析状态
	}
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		MsgStateEnum state = state();
		switch(state) {
			case STEP_TYPE : {
				if(msg == null) {
					msg = new Message();
				}
				byte[] readByte = readByte(in, msg.getTempContent(), MsgLengthEnum.TYPE_LENGTH.getLength());
				logger.info("type:{}", readByte);
				if(readByte.length == MsgLengthEnum.TYPE_LENGTH.getLength()) {//消息类型已经读完
					int type = MessageUtil.byte2int(readByte, MsgLengthEnum.TYPE_LENGTH.getLength());
					msg.setTypeInt(type);
					msg.setTempContent(new byte[0]);
					
				/*if(msg.getType() == MsgType.ERROR) {//未知消息类型
					logger.info("未知消息类型：hashCode：{}，msgType:{}",ctx.channel().hashCode(), type);
					ctx.close();
					return;
				}*/
					checkpoint(MsgStateEnum.STEP_SEQ);//设置下一次读取消息序列号
				}else {
					msg.setTempContent(readByte);
					checkpoint(MsgStateEnum.STEP_TYPE);//设置下一次继续读取消息类型
				}
				break;
			}
			case STEP_SEQ : {
				byte[] readByte =readByte(in, msg.getTempContent(), MsgLengthEnum.SEQ_LENGTH.getLength());
				if(readByte.length == MsgLengthEnum.SEQ_LENGTH.getLength()) {//消息序列号已经读完
					msg.setSeqNo(MessageUtil.byte2int(readByte, MsgLengthEnum.SEQ_LENGTH.getLength()));
					msg.setTempContent(new byte[0]);
					checkpoint(MsgStateEnum.STEP_LENGTH);//设置下一次读取消息序长度
				}else {
					msg.setTempContent(readByte);
					checkpoint(MsgStateEnum.STEP_SEQ);//设置下一次继续读取消息序号
				}
				break;
			}case STEP_LENGTH : {
				byte[] readByte =readByte(in, msg.getTempContent(), MsgLengthEnum.MSG_LENGTH.getLength());
				if(readByte.length == MsgLengthEnum.MSG_LENGTH.getLength()) {//消息长度已经读完
					msg.setByteLength(MessageUtil.byte2int(readByte, MsgLengthEnum.MSG_LENGTH.getLength()));
					msg.setTempContent(new byte[0]);
					
					checkpoint(MsgStateEnum.STEP_CONTENT);
				}else {
					msg.setTempContent(readByte);
					checkpoint(MsgStateEnum.STEP_LENGTH);
				}
				break;
			}case STEP_CONTENT : {
				byte[] readByte =readByte(in, msg.getTempContent(), msg.getByteLength());
				if(readByte.length == msg.getByteLength()) {
					msg.setByteContent(readByte);
					msg.setTempContent(new byte[0]);
					msg.setContent(MessageUtil.decryption(msg.getByteContent()));
					out.add(msg);
					checkpoint(MsgStateEnum.STEP_TYPE);
				}else {
					msg.setTempContent(readByte);
					checkpoint(MsgStateEnum.STEP_CONTENT);
				}
				break;
			}default : {
				break;
			}
		} 
	}
	
	/**
	 * 
	 * @param byteBuf
	 * @param dataByte 已读取的数据
	 * @param readLength 总共需要读取的长度(包括dataByte的数据)
	 * @return
	 */
	private byte[] readByte(ByteBuf byteBuf, byte[] preSubByteBuf, int length) {
		if (length <= 0) {
			return new byte[0];
		}
		//int canReadLength = byteBuf.readableBytes();//不能用此方法获取可读长度，该方法返回的是一个很大的值
		//可读长度
		int sourceLength = byteBuf.writerIndex() - byteBuf.readerIndex();
		int subLength = length;
		if (preSubByteBuf != null) {
			subLength = length - preSubByteBuf.length;
		}
		if (sourceLength < subLength) {
			byte[] dst = new byte[sourceLength];
			byteBuf.readBytes(sourceLength).getBytes(0, dst);
			return MessageUtil.connectByte(preSubByteBuf, dst);
		}
		byte[] dst = new byte[subLength];
		byteBuf.readBytes(subLength).getBytes(0, dst);
		
		System.out.println(byteBuf.writerIndex() + "------ " + byteBuf.readerIndex());
		return MessageUtil.connectByte(preSubByteBuf, dst);
	}

}
