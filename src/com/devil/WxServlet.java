package com.devil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.devil.global.MessageType;
import com.devil.global.ReplyContent;
import com.devil.model.InputMessage;
import com.devil.model.OutputMessage;
import com.devil.utils.SerializeXmlUtil;
import com.thoughtworks.xstream.XStream;

@RequestMapping(value = "/wx")
@Controller
public class WxServlet {

	@RequestMapping(value = "/test", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String Test() {
		return "springMvc is ok (SpringMVC环境已经搭建好)";
	}

	private String wx_token = "devil_token";
	private String xml_root = "xml";

	/**
	 * 与微信服务器建立连接
	 * 详细步骤，请参见微信公众号开发文档（接入指南)-https://mp.weixin.qq.com/wiki?t=resource
	 * /res_main&id=mp1421135319
	 * 
	 * @param request
	 * @param response
	 * @param out
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public void wxConnect(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");

		List<String> dataList = new ArrayList<String>();
		dataList.add(wx_token);
		dataList.add(timestamp);
		dataList.add(nonce);
		// 字典排序
		Collections.sort(dataList);

		StringBuffer dataBuffer = new StringBuffer();
		for (String str : dataList) {
			dataBuffer.append(str);
		}
		// 调用commons-codec.jar库中的sha1加密方法
		String sha1Data = DigestUtils.sha1Hex(dataBuffer.toString());
		if (signature.equals(sha1Data)) {
			System.out.println("wx connect is success");
			out.print(echostr);
			out.flush();
		} else {
			System.out.println("wx connect is fail");
		}
	}

	/**
	 * 接收微信服务器POST过来的XML消息
	 * 
	 * @param request
	 * @param response
	 * @param out
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void echoMsg(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		InputMessage inputMsg = requestXmlToInputMsg(request);
		// 消息处理后返回
		OutputMessage outputMsg = messageManager(inputMsg);
		String xmlReply = outputMsgToXML(outputMsg);
		out.print(xmlReply);
		out.flush();
	}

	/**
	 * 消息管理中心
	 * 
	 * @param inputMsg
	 * @return
	 */
	private OutputMessage messageManager(InputMessage inputMsg) {
		OutputMessage outputMsg = new OutputMessage();

		outputMsg.setToUserName(inputMsg.getFromUserName());
		outputMsg.setFromUserName(inputMsg.getToUserName());
		outputMsg.setCreateTime(Calendar.getInstance().getTimeInMillis() / 1000);

		String msgType = inputMsg.getMsgType();
		if (MessageType.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {
			outputMsg.setMsgType(MessageType.REQ_MESSAGE_TYPE_TEXT);
			if ("0".equals(inputMsg.getContent()) || "你好".equals(inputMsg.getContent())) {
				outputMsg.setContent(ReplyContent.getInstance().getMainContent());
			}else if ("zhuzhu".equals(inputMsg.getContent()) || "yh".equals(inputMsg.getContent())) {
				outputMsg.setContent(ReplyContent.getInstance().getTypeZhuContent());
			}else {
				outputMsg.setContent(ReplyContent.getInstance().getElseContent());
			}
		}

		return outputMsg;
	}

	private String outputMsgToXML(OutputMessage outputMsg) {
		XStream stream = new XStream();
		stream.alias(xml_root, outputMsg.getClass());
		String xmlType = stream.toXML(outputMsg);
		System.out.println("回复的内容 : " + xmlType);
		return xmlType;
	}

	/**
	 * @param request
	 * @return
	 */
	private InputMessage requestXmlToInputMsg(HttpServletRequest request) {
		String xmlMsg = inputStreamToString(request);
		// xstream.jar第三方库的使用，将xml->对象。具体用法，查官方文档
		// 引入3个jar包（xstream.jar/xmlpull.jar/xpp3_min.jar），缺一不可
		XStream xs = SerializeXmlUtil.createXstream();
		xs.processAnnotations(InputMessage.class);
		xs.alias(xml_root, InputMessage.class);
		InputMessage inputMsg = (InputMessage) xs.fromXML(xmlMsg);
		return inputMsg;
	}

	/**
	 * @param request
	 * @return String字符串
	 */
	private String inputStreamToString(HttpServletRequest request) {
		StringBuilder xmlMsg = new StringBuilder();
		byte[] b = new byte[4096];
		try {
			for (int n; (n = request.getInputStream().read(b)) != -1;) {
				xmlMsg.append(new String(b, 0, n, "UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xmlMsg.toString();
	}

}
