package com.aat.application.views;

import com.aat.application.core.form.CommonForm;
import com.aat.application.data.repository.BaseEntityRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.lang.reflect.InvocationTargetException;

public abstract class CommonView extends VerticalLayout implements RouterLayout, BeforeEnterObserver, HasDynamicTitle {

    protected final BaseEntityRepository repository;
    protected CoreMainLayout layout;
    protected Class<?> LayoutClass;
    protected String filterObjectId = null;


    public CommonView(BaseEntityRepository repository) {
        this.repository = repository;
    }

    protected void setForm(CommonForm form) {
        try {
            layout = (CoreMainLayout) LayoutClass.getDeclaredConstructor().newInstance();
            layout.setContent(form);
            layout.setHamburgerTitle(form.getHamburgerText());
            layout.setGoOriginText(form.getOriginViewText());
            Div outlet = new Div();
            outlet.setId("outlet");
            outlet.add(layout);
            Div selection = new Div();
            selection.setId("selection");
            UI.getCurrent().getChildren().forEach(Component::removeFromParent);
            UI.getCurrent().removeAll();
            UI.getCurrent().add(outlet);
            UI.getCurrent().add(selection);
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException |
                 IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String layoutClassName = (String) VaadinSession.getCurrent().getAttribute("layout");
        VaadinSession.getCurrent().setAttribute("previousView", UI.getCurrent().getInternals().getActiveViewLocation().getPath());

        if (layoutClassName != null) {
            try {
                LayoutClass = Class.forName(layoutClassName);
            } catch (ClassNotFoundException e) {
                event.rerouteToError(NotFoundException.class);
            }
        }

        if (event.getRouteParameters().getParameterNames().size() > 1) {
            filterObjectId = event.getRouteParameters().get("___url_parameter").orElse("");
        } else
            filterObjectId = null;
    }

    @Override
    public String getPageTitle() {
        return "";
    }
}