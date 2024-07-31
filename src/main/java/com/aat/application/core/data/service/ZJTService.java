package com.aat.application.core.data.service;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.entity.ZJTItem;

import java.util.List;

public interface ZJTService {
    List<ZJTItem> findByQuery(String query);

    List<Object[]> findEntityByQuery(String query);

    int updateEntityByQuery(String query, Object[] params);

    int deleteEntityByQuery(String query);

    <T> T addNewEntity(Class<?> entityClass);

    ZJTEntity addNewEntity(ZJTEntity entity);
}