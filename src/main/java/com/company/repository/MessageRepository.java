package com.company.repository;

import com.company.entity.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

    @Query(value = "SELECT * FROM message m WHERE m.date::DATE = now()", nativeQuery = true)
    List<MessageEntity> findTodays();

    Page<MessageEntity> findByUsedIsFalse(Pageable pageable);

    Optional<MessageEntity> findTopByOrderByCreatedDateDesc();

}
