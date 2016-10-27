package com.nm.both.memory;

/**
 * Created by huangming on 2016/10/26.
 */

public class AudioBody extends FileBody {
    
    private final long duration;
    
    AudioBody(String url, long duration) {
        super(url);
        this.duration = duration;
    }
    
    public long getDuration() {
        return duration;
    }
    
    @Override
    String getNodeTag() {
        return "audio";
    }
    
    @Override
    protected void writeAttributesTo(StringBuilder xml) {
        xml.append(" ").append("duration=\"").append(getDuration()).append("\"");
    }
}
