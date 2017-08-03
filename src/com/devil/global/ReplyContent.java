package com.devil.global;

/**
 * TODO 消息回复的内容
 *
 * @author devil
 * 2017年8月3日
 */
public class ReplyContent {

	private static ReplyContent rc = null;
	
	public ReplyContent() {
	}
	
	public static ReplyContent getInstance(){
		if (rc == null) {
			rc = new ReplyContent();
		}
		return rc;
	}
	
	/**
	 * 指令：0
	 * @return
	 */
	public String getMainContent() {
		return "做一个『会写代码，有点商业嗅觉，又有点审美』的程序员";
	}
	
	/**
	 * 指令：其他
	 * @return
	 */
	public String getElseContent() {
		return "无法识别您的指令，请重新输入！";
	}
	
	/**
	 * 指令：zhuzhu
	 * @return
	 */
	public String getTypeZhuContent() {
		return "你是我心中的太阳，是我最在乎的人。I love you !";
	}
	
}
