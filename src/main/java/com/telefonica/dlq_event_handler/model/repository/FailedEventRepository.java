package com.telefonica.dlq_event_handler.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telefonica.dlq_event_handler.model.entity.FailedEvent;

public interface FailedEventRepository extends JpaRepository<FailedEvent, Long> {
}
