package com.app.rsspark.domain.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * Created by konstie on 11.12.16.
 */

public class Link {
    @Attribute(required = false) private String href;
    @Attribute(required = false) private String rel;
    @Attribute(name = "type", required = false)
    private String contentType;
    @Text(required = false) private String link;

    public String getHref() {
        return href;
    }

    public String getRel() {
        return rel;
    }

    public String getContentType() {
        return contentType;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "Link{" +
                "href='" + href + '\'' +
                ", rel='" + rel + '\'' +
                ", contentType='" + contentType + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
