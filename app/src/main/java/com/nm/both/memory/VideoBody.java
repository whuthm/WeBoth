package com.nm.both.memory;

/**
 * Created by huangming on 2016/10/26.
 */

public class VideoBody extends FileBody {
    
    private final String thumbUrl;
    private final long duration;
    
    VideoBody(String url, String thumbUrl, long duration) {
        super(url);
        this.thumbUrl = thumbUrl;
        this.duration = duration;
    }
    
    public String getThumbUrl() {
        return thumbUrl;
    }
    
    public long getDuration() {
        return duration;
    }
    
    @Override
    String getNodeTag() {
        return "video";
    }
    
    @Override
    protected void writeAttributesTo(StringBuilder xml) {
        xml.append(" ").append("thumbUrl=\"").append(getThumbUrl()).append("\"");
        xml.append(" ").append("duration=\"").append(getDuration()).append("\"");
    }
}
