package com.nm.both.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangming on 2016/10/27.
 */

public class MemoryContent {
    
    private int length = 0;
    
    private List<Segment> segments = new ArrayList<>();
    
    private void addSegment(Segment segment, int index) {
        length += segment.getContentLength();
        segments.add(index, segment);
    }
    
    private void removeSegment(Segment segment) {
        length -= segment.getContentLength();
        segments.remove(segment);
    }
    
    public void insertCharacters(int start, String value) {
        Segment segment = findPreSegmentBy(start);
        int contentLengthOfPreSegments = getContentLengthOfPreSegments(start);
        Segment preSegment = findPreSegmentBy(start);
        int segmentIndex = getSegmentIndex(start);
        if (preSegment != null && preSegment.getType() == Segment.Type.TEXT
                && contentLengthOfPreSegments == start) {
            String preText = ((TextBody) preSegment.getBody()).getText();
            removeSegment(preSegment);
            addSegment(Segment.createTextSegment(preText + value), segmentIndex - 1);
        }
        else if (segment != null && segment.getType() == Segment.Type.TEXT) {
            String currentText = ((TextBody) segment.getBody()).getText();
            StringBuilder sb = new StringBuilder();
            sb.append(currentText.substring(0, start - contentLengthOfPreSegments));
            sb.append(value);
            sb.append(currentText.substring(start - contentLengthOfPreSegments,
                    currentText.length()));
            removeSegment(segment);
            addSegment(Segment.createTextSegment(sb.toString()), segmentIndex);
        }
        else {
            addSegment(Segment.createTextSegment(value), segmentIndex);
        }
    }
    
    public void deleteCharacters(int start, String value) {
        Segment segment = findPreSegmentBy(start);
        int contentLengthOfPreSegments = getContentLengthOfPreSegments(start);
        int segmentIndex = getSegmentIndex(start);
        if (segment != null) {
            String currentText = ((TextBody) segment.getBody()).getText();
            StringBuilder sb = new StringBuilder();
            sb.append(currentText.substring(0, start - contentLengthOfPreSegments));
            sb.append(
                    currentText.substring(start + value.length(), currentText.length()));
            removeSegment(segment);
            if (sb.length() > 0) {
                addSegment(Segment.createTextSegment(sb.toString()), segmentIndex);
            }
        }
    }
    
    public void insertFile(int start, Segment segment) {
        addSegment(segment, start);
    }
    
    public void deleteFile(int start) {
        Segment segment = findPreSegmentBy(start);
        if (segment != null) {
            removeSegment(segment);
            mergeSerialTextSegment();
        }
    }
    
    private void mergeSerialTextSegment() {
        int index = 0;
        int size = segments.size();
        while (index < size) {
            
        }
    }
    
    private int getContentLengthOfPreSegments(int start) {
        int segmentSize = segments.size();
        if (start < 0) {
            return -1;
        }
        
        if (start >= segmentSize) {
            return getLength();
        }
        
        int contentLength = 0;
        int ContentLengthOfPreSegments;
        for (int i = 0; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            ContentLengthOfPreSegments = contentLength;
            contentLength += segment.getContentLength();
            if (start < contentLength) {
                return ContentLengthOfPreSegments;
            }
        }
        return -1;
    }
    
    private int getSegmentIndex(int start) {
        int segmentSize = segments.size();
        if (start < 0 || start >= segmentSize) {
            return -1;
        }
        int contentLength = 0;
        for (int i = 0; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            contentLength += segment.getContentLength();
            if (start < contentLength) {
                return i;
            }
        }
        return -1;
    }
    
    private Segment findSegmentBy(int start) {
        int segmentSize = segments.size();
        int segmentIndex = getSegmentIndex(start);
        if (segmentIndex >= 0 && segmentIndex < segmentSize) {
            return segments.get(segmentIndex);
        }
        return null;
    }
    
    private Segment findPreSegmentBy(int start) {
        int segmentSize = segments.size();
        int segmentIndex = getSegmentIndex(start);
        if (segmentIndex > 0 && segmentIndex < segmentSize) {
            return segments.get(segmentIndex - 1);
        }
        return null;
    }
    
    public int getLength() {
        return length;
    }
    
    public String toXml() {
        StringBuilder xml = new StringBuilder();
        xml.append("<memory>");
        for (Segment segment : segments) {
            xml.append("\n");
            segment.writeTo(xml);
        }
        xml.append("\n");
        xml.append("</memory>");
        return xml.toString();
    }
}
