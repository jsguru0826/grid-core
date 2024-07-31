package com.aat.application.form;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;
import com.aat.application.core.form.TimeLineForm;
import com.aat.application.core.form.TimeLineViewParameter;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.flow.component.button.Button;

public class TimeLineCommonForm extends TimeLineForm<ZJTService> {
    public TimeLineCommonForm(TimeLineViewParameter timeLineViewParameter,
                              ZJTService service) {
        super(timeLineViewParameter, service);
        addClassName("demo-app-form");
    }

    @Override
    public void onNewItem(GuiItem item) {
    }

    @Override
    public ZJTEntity onNewItem(ZJTEntity entity, int id) {

        return entity;
    }

    @Override
    public int onUpdateItem(Object[] objects) {
        return 0;
    }

    @Override
    public int onDeleteItemChecked() {
        return 0;
    }

    @Override
    public void setContextMenu(AATContextMenu contextMenu) {

    }

    @Override
    public void setMessageStatus(String msg) {

    }

    @Override
    public void addCustomButton(Button button) {

    }
}