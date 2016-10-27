package com.nm.both.memory;

/**
 * Created by huangming on 2016/10/26.
 */

public class ImageBody extends FileBody {

    private final String thumbUrl;

    ImageBody(String url, String thumbUrl) {
        super(url);
        this.thumbUrl = thumbUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }


    @Override
    String getNodeTag() {
        return "image";
    }

    @Override
    protected void writeAttributesTo(StringBuilder xml) {
        xml.append(" ").append("thumbUrl=\"").append(getThumbUrl()).append("\"");
    }
}
