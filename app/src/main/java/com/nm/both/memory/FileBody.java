package com.nm.both.memory;

/**
 * Created by huangming on 2016/10/26.
 */

public abstract class FileBody extends SegmentBody {
    
    private String url;
    private String localUrl;
    private String filename;
    private long length;
    
    FileBody(String url) {
        this.url = url;
    }
    
    public long getLength() {
        return length;
    }
    
    public void setLength(long length) {
        this.length = length;
    }
    
    public String getFilename() {
        return filename;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getLocalUrl() {
        return localUrl;
    }
    
    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }
    
    @Override
    public int getContentLength() {
        return 1;
    }
    
    abstract String getNodeTag();
    
    @Override
    public void writeTo(StringBuilder xml) {
        xml.append("<").append(getNodeTag());
        xml.append(" ").append("url=\"").append(getUrl()).append("\"");
        xml.append(" ").append("filename=\"").append(getFilename()).append("\"");
        xml.append(" ").append("length=\"").append(getLength()).append("\"");
        writeAttributesTo(xml);
        xml.append("/>");
    }
    
    protected void writeAttributesTo(StringBuilder xml) {
        
    }
    
}
