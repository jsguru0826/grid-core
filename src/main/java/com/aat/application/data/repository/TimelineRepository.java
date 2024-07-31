package com.aat.application.data.repository;

import com.aat.application.data.entity.ZJTItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimelineRepository extends JpaRepository<ZJTItem, Long> {

    List<ZJTItem> findByGroupId(String groupId);
}