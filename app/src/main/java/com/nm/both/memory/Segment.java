package com.nm.both.memory;

/**
 * Created by huangming on 2016/10/26.
 */

public class Segment {
    
    private final SegmentBody body;
    
    private final Type type;
    
    Segment(SegmentBody body, Type type) {
        this.body = body;
        this.type = type;
    }
    
    public SegmentBody getBody() {
        return body;
    }
    
    public Type getType() {
        return type;
    }
    
    public int getContentLength() {
        return body.getContentLength();
    }
    
    public enum Type {
        TEXT, IMAGE, NOMAL_FILE, VIDEO, AUDIO
    }
    
    void writeTo(StringBuilder xml) {
        body.writeTo(xml);
    }
    
    public static Segment createTextSegment(String text) {
        return new Segment(new TextBody(text), Type.TEXT);
    }
    
}
