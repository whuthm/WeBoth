package com.nm.both.memory;

/**
 * Created by huangming on 2016/10/26.
 */

public class TextBody extends SegmentBody {
    
    private final String text;
    
    TextBody(String text) {
        this.text = text;
    }
    
    public String getText() {
        return text;
    }
    
    @Override
    public int getContentLength() {
        return text.length();
    }

    @Override
    public void writeTo(StringBuilder xml) {
        xml.append("<text>");
        xml.append(getText());
        xml.append("\n");
        xml.append("</text>");
    }
}
