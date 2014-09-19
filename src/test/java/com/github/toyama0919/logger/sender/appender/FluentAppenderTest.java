package com.github.toyama0919.logger.sender.appender;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.github.toyama0919.logger.sender.appender.FluentAppender;

/**
 * @author toyama-h
 *
 */
public class FluentAppenderTest extends TestCase {

    FluentAppender fluentAppender;
    private Logger log = Logger.getRootLogger();
    private Logger classLog = Logger.getLogger(getClass());
    private Logger mapLog = Logger.getLogger("MAP");

    protected void setUp() {
        fluentAppender = new FluentAppender();
    }

    @Test
    public void testStringAppend() {
        log.info("aaa");
        log.info("bbb");
        log.info("ccc");
    }

    @Test
    public void testClassLogger() {
        classLog.info("aaa");
        classLog.info("bbb");
        classLog.info("ccc");
    }

    @Test
    public void testMapLogger() {
        mapLog.info("maplog1");
        mapLog.info("maplog2");
        mapLog.info("maplog3");
    }

    @Test
    public void testMapAppend() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tag", "webapp");
        map.put("id", 1);
        map.put("name", "github tarou");
        log.info(map);
    }
    
    @Test
    public void testDefaultValue() {
        assertThat(fluentAppender.getHost(), is("localhost"));
        assertThat(fluentAppender.getPort(), is(24224));
    }
    
}
