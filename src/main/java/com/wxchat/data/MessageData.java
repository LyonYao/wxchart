package com.wxchat.data;

public class MessageData {

	protected String ToUserName;
	protected String FromUserName;
	protected long CreateTime;
	protected long MsgId;
	protected final String MsgType;

	public MessageData(final String msgType) {
		super();
		MsgType = msgType;
		setCreateTime(System.nanoTime());
		setMsgId(System.currentTimeMillis());
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public long getMsgId() {
		return MsgId;
	}

	public void setMsgId(long msgId) {
		MsgId = msgId;
	}

	public String getMsgType() {
		return MsgType;
	}

}