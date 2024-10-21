package com.telefonica.dlq_event_handler.weblogic;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jms.annotation.JmsListener;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telefonica.dlq_event_handler.model.service.FailedEventService;

@Service
@Transactional
public class JMSListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JMSListener.class.getName());
 
    @Autowired
    private FailedEventService messageService;

    private ObjectMapper mapper = new ObjectMapper();

    @JmsListener(containerFactory = "factory", destination = "${jms.queue.name}")
    public void listenToMessages(Message msg) throws JMSException {
        try {
            final String payload = msg.getBody(String.class);
            final String headers = mapper.writeValueAsString(headers(msg));

            logMessage("MESSAGE RECEIVED", payload, headers);

            messageService.save(payload, headers);

            logMessage("MESSAGE SAVE ON DATABASE", payload, headers);
        } catch (JsonProcessingException ex) {
            logCustomErrorMsg("ERROR PARSING JSON MESSAGE", 
                ex.getMessage());
            JMSException jmsException = 
                new JMSException("Error parsing json message.");
            jmsException.initCause(ex);
            throw jmsException;
        }
    }

    private Map<String, String> headers(Message msg) throws JMSException {
        final Map<String, String> map = new HashMap<String, String>();

        Enumeration<?> propertyNames = msg.getPropertyNames();

        while (propertyNames.hasMoreElements()) {
            String key = (String) propertyNames.nextElement();
            Object value = msg.getObjectProperty(key);
            
            map.put(key, value.toString());
        }

        return map;
    }

    private void logMessage(String header, String body, String headers) {
        final StringBuffer sb = new StringBuffer();

        sb.append("BODY: " + body + "\n");
        sb.append("HEADERS: " + headers + "\n");
    
        logCustomMsg(header, sb.toString());
    }
   
    private String customMsg(String header, String msg) {
        return header +" AT: " + LocalDateTime.now() 
            + ". MSG: " + msg;
    } 
    
    private void logCustomMsg(String header, String msg) {
        LOGGER.info(customMsg(header, msg));
    }

    private void logCustomErrorMsg(String header, String msg) {
        LOGGER.error(customMsg(header, msg));
    }

}
