package com.devil.model;

import com.devil.utils.XStreamCDATA;
import com.thoughtworks.xstream.annotations.XStreamAlias;  

/**
 * @author devil
 * 多媒体ID
 */
public class MediaIdMessage {  
    @XStreamAlias("MediaId")  
    @XStreamCDATA  
    private String MediaId;  
  
    public String getMediaId() {  
        return MediaId;  
    }  
  
    public void setMediaId(String mediaId) {  
        MediaId = mediaId;  
    }  
  
}  
