package com.telefonica.dlq_event_handler.model.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.telefonica.dlq_event_handler.model.entity.FailedEvent;
import com.telefonica.dlq_event_handler.model.repository.FailedEventRepository;

@Service
public class FailedEventService {

    private FailedEventRepository repository;

    public FailedEventService(FailedEventRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void save(String payload, String headers, String error) {
        FailedEvent message = new FailedEvent();

        message.setPayload(payload);
        message.setHeaders(headers);
        
        if(error!=null)
            message.setError(error);
        
        repository.save(message);
    }

    @Transactional
    public void save(String payload, String headers) {
        this.save(payload, headers, null);
    }
    
}

