package com.aat.application.data.repository;

import com.aat.application.data.entity.ZJTTableInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TableInfoRepository extends JpaRepository<ZJTTableInfo, Integer> {
    @Query("SELECT t FROM ZJTTableInfo t WHERE t.table_name = :tableName")
    ZJTTableInfo findByTableName(@Param("tableName") String tableName);
    ZJTTableInfo save(ZJTTableInfo tableInfo);

}
