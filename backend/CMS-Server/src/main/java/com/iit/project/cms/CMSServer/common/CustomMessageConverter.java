package com.iit.project.cms.CMSServer.common;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class CustomMessageConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        String message = event.getFormattedMessage();
        // Modify the log message here as per your requirement
        if (message.contains("Executing prepared SQL query")) {
            return message.replace("Executing prepared SQL query", "");
        }
        if (message.contains("Executing prepared SQL statement")) {
            return message.replace("Executing prepared SQL statement", "");
        }
        return message;
    }
}
