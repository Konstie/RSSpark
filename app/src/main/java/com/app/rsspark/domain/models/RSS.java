package com.app.rsspark.domain.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by konstie on 11.12.16.
 */

@Root(name = "rss", strict = false)
public class RSS implements Serializable {
    @Element private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "RSS{" +
                "channel=" + channel +
                '}';
    }
}
