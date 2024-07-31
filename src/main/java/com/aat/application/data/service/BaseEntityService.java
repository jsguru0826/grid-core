package com.aat.application.data.service;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;
import com.aat.application.data.entity.ZJTItem;
import com.aat.application.data.repository.BaseEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseEntityService<T> implements ZJTService {

    private final BaseEntityRepository generalRepository;

    @Autowired
    public BaseEntityService(BaseEntityRepository generalRepository) {
        this.generalRepository = generalRepository;
    }

    @Override
    public List<ZJTItem> findByQuery(String query) {
        return generalRepository.findByQuery(query);
    }

    @Override
    public List<Object[]> findEntityByQuery(String query) {
        return generalRepository.findEntityByQuery(query);
    }

    @Override
    public int updateEntityByQuery(String query, Object[] params) {
        return generalRepository.updateEntityByQuery(query, params);
    }

    @Override
    public int deleteEntityByQuery(String query) {
        return generalRepository.deleteEntityByQuery(query);
    }

    @Override
    public <T1> T1 addNewEntity(Class<?> entityClass) {
        return generalRepository.addNewEntity(entityClass);
    }

    @Override
    public ZJTEntity addNewEntity(ZJTEntity entity) {
        return generalRepository.addNewEntity(entity);
    }
}