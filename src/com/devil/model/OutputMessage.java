package com.devil.model;
import com.devil.utils.XStreamCDATA;
import com.thoughtworks.xstream.annotations.XStreamAlias;  

/**
 * @author devil
 * 
 */
@XStreamAlias("xml")  
public class OutputMessage {  
  
    @XStreamAlias("ToUserName")  
    @XStreamCDATA  
    private String ToUserName;  
  
    @XStreamAlias("FromUserName")  
    @XStreamCDATA  
    private String FromUserName;  
  
    @XStreamAlias("CreateTime")  
    private Long CreateTime;  
  
    @XStreamAlias("MsgType")  
    @XStreamCDATA  
    private String MsgType = "text";  
    
    //文本
    @XStreamAlias("Content")
    @XStreamCDATA 
    private String Content;

	//图片
    private ImageMessage Image;  
  
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
  
    public Long getCreateTime() {  
        return CreateTime;  
    }  
  
    public void setCreateTime(Long createTime) {  
        CreateTime = createTime;  
    }  
  
    public String getMsgType() {  
        return MsgType;  
    }  
  
    public void setMsgType(String msgType) {  
        MsgType = msgType;  
    }  
  
    public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
    
    public ImageMessage getImage() {  
        return Image;  
    }  
  
    public void setImage(ImageMessage image) {  
        Image = image;  
    }
 }
