package com.github.toyama0919.logger.sender.appender;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.fluentd.logger.FluentLogger;

/**
 * @author toyama-h
 *
 *  setting exsample
 *  <appender name="fluentd" class="ipros.common.logger.sender.appender.FluentAppender" >
 *    <param name="Tag" value="shiva" />
 *    <param name="Label" value="application" />
 *    <param name="Host" value="localhost" />
 *    <param name="Port" value="24224" />
 *  </appender>
 */
public class FluentAppender extends AppenderSkeleton {
    
    private final String TAG_KEY = "tag";

    private FluentLogger fluentLogger;

    private String tag = "log4j-appender";

    private String host = "localhost";

    private int port = 24224;

    private String label = "label";

    private String hostname = "localhost";

    private String stage = null;

    private boolean addHostname = false;

    private boolean addTag = false;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isAddHostname() {
        return addHostname;
    }

    public void setAddHostname(boolean addHostname) {
        this.addHostname = addHostname;
    }

    public boolean isAddTag() {
        return addTag;
    }

    public void setAddTag(boolean addTag) {
        this.addTag = addTag;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public FluentLogger getFluentLogger() {
        return fluentLogger;
    }

    public void setFluentLogger(FluentLogger fluentLogger) {
        this.fluentLogger = fluentLogger;
    }

    @Override
    public void activateOptions() {
        try {
            fluentLogger = FluentLogger.getLogger(tag, host, port);
            setHostname(InetAddress.getLocalHost().getHostName());
        } catch (RuntimeException e) {
        } catch (UnknownHostException e) {
        }
        super.activateOptions();
    }

    @Override
    public void close() {
        try {
            fluentLogger.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void append(LoggingEvent event) {
        Map<String, Object> messages = new HashMap<String, Object>();
        String label = getLabel();
        Object message = event.getMessage();
        if (message instanceof Map) {
            Map<String, Object> data = (Map<String, Object>)message;
            label = data.containsKey(TAG_KEY) ? data.get(TAG_KEY).toString() : getLabel();
            messages = createMessages(data);
        } else if (message instanceof Exception) {
            Exception exception = (Exception)message;
            messages = createMessages(event, exception);
        } else {
            messages = createMessages(event);
        }
        // add hostname
        if (isAddHostname()) {
            messages.put("hostname", getHostname()); 
        }
        // add tag
        if (isAddTag()) {
            messages.put("tag", getTag() + "." + label); 
        }
        // add stage
        if (getStage() != null) {
            messages.put("stage", getStage()); 
        }
        
        fluentLogger.log(label, messages);
    }

    /**
     * @param event
     * @param exception
     * @return
     */
    private Map<String, Object> createMessages(LoggingEvent event, Exception exception) {
        Map<String, Object> messages = new HashMap<String, Object>();
        messages.put("message", exception.getMessage());
        messages.put("level", event.getLevel().toString());
        messages.put("loggerName", event.getLoggerName());
        messages.put("location", event.getLocationInformation().fullInfo);
        messages.put("thread", event.getThreadName());
        messages.put("throwable", exception.getStackTrace());
        return messages;
    }

    /**
     * @param event
     * @return
     */
    private Map<String, Object> createMessages(LoggingEvent event) {
        Map<String, Object> messages = new HashMap<String, Object>();
        messages.put("message", event.getMessage());
        messages.put("level", event.getLevel().toString());
        messages.put("loggerName", event.getLoggerName());
        messages.put("location", event.getLocationInformation().fullInfo);
        messages.put("thread", event.getThreadName());
        messages.put("throwable", event.getThrowableStrRep());
        return messages;
    }
    
    /**
     * @param data
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> createMessages(Map<String, Object> data) {
        Map<String, Object> messages = new HashMap<String, Object>();
        for(Map.Entry<String, Object> e : data.entrySet()) {
            if (!TAG_KEY.equals(e.getKey())) messages.put(e.getKey(), e.getValue());
        }
        return messages;
    }

}
