package com.aat.application.form;

import com.aat.application.core.form.GridViewParameter;
import com.aat.application.core.form.StandardForm;
import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;
import com.aat.application.data.service.BaseEntityService;
import com.aat.application.data.service.TableInfoService;

public class GridCommonForm<T extends ZJTEntity> extends StandardForm<T, ZJTService> {
    public GridCommonForm(GridViewParameter gridViewParameter, BaseEntityService<T> service, TableInfoService tableInfoService) {
        super(gridViewParameter, service, tableInfoService);
        addClassName("demo-app-form");
    }
}