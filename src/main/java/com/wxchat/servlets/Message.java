package com.wxchat.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.wxchat.cases.MessageReply;

public class Message extends HttpServlet {
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		PropertyConfigurator.configure(Message.class.getResourceAsStream("/log4j.properties"));
	}

	private final static Logger LOGGER=Logger.getLogger(Message.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = new PrintWriter(resp.getOutputStream());
		String echostr = req.getParameter("echostr");
		// TODO 检查签名
		if (echostr != null && echostr.length() > 0) {
			writer.write(echostr);
		}
		writer.flush();
		writer.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO检查签名
		ServletInputStream inputStream = req.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		String content = null;
		StringBuffer contentBuff = new StringBuffer();
		while ((content = reader.readLine()) != null) {
			contentBuff.append(content);
		}
		String xml = contentBuff.toString();
		LOGGER.info(xml);
		// 打印出微信传送的消息
		try {
			Document document = new SAXReader().read(new StringReader(xml));
			Element root = document.getRootElement();
			String msgType=root.selectSingleNode("MsgType")
					.getStringValue();
			String fromUserName = root.selectSingleNode("FromUserName")
					.getStringValue();
			String toUserName = root.selectSingleNode("ToUserName")
					.getStringValue();
			PostData data=new PostData(fromUserName, toUserName);
			String msgId = System.nanoTime() + "";
			String now = System.currentTimeMillis() + "";
			if("event".equals(msgType)){
				String event=root.selectSingleNode("Event")
						.getStringValue();
				resp.setCharacterEncoding("UTF-8");
				PrintWriter writer = new PrintWriter(resp.getOutputStream());
				if("LOCATION".equals(event)){
				writer.write("success");
				}else if("CLICK".equals(event)){
					String returnMsg = "<xml>" + "<ToUserName><![CDATA[" + fromUserName
							+ "]]></ToUserName>" + "<FromUserName><![CDATA["
							+ toUserName + "]]></FromUserName>" + "<CreateTime>" + now
							+ "</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
							+ "<Content><![CDATA[点击菜单]]></Content>"
							+ "<MsgId>" + msgId + "</MsgId>" + "</xml>";
					writer.write(returnMsg);
				}
				writer.flush();
				writer.close();
			}else{
				LOGGER.info(root.selectSingleNode("Content")
						.getStringValue());
				String reply = MessageReply.reply(root.selectSingleNode("Content")
						.getStringValue(), data);
				if(reply!=null&&!reply.isEmpty()){
					resp.setCharacterEncoding("UTF-8");
					LOGGER.info(reply);
					PrintWriter writer = new PrintWriter(resp.getOutputStream());
					writer.write(reply);
					writer.flush();
					writer.close();
					return;
				}
				String returnMsg = "<xml>" + "<ToUserName><![CDATA[" + fromUserName
						+ "]]></ToUserName>" + "<FromUserName><![CDATA["
						+ toUserName + "]]></FromUserName>" + "<CreateTime>" + now
						+ "</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
						+ "<Content><![CDATA[你好，你输入的关键字系统没有对应的资源]]></Content>"
						+ "<MsgId>" + msgId + "</MsgId>" + "</xml>";
				resp.setCharacterEncoding("UTF-8");
				LOGGER.info(returnMsg);
				PrintWriter writer = new PrintWriter(resp.getOutputStream());
				writer.write(returnMsg);
				writer.flush();
				writer.close();
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

}
