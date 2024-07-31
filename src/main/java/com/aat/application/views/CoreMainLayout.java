package com.aat.application.views;


import com.aat.application.components.appnav.AppNav;
import com.aat.application.core.event.EventBus;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
//@Route(value = "")
@CssImport("./styles/styles.css")
public class CoreMainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {
    private H2 viewTitle;
    protected AppNav nav;
    private Div content;
    Span filterText = new Span("");
    Button btnGoOriginView = new Button("");

    public CoreMainLayout() {
        setPrimarySection(Section.DRAWER);
        addHeaderContent();
    }

    protected AppNav getNavigation() {
        return nav;
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        btnGoOriginView.addClickListener(e -> {
            if (!btnGoOriginView.getText().isEmpty()) {
                String previousView = (String) VaadinSession.getCurrent().getAttribute("previousView");
                if (previousView != null) {
                    UI.getCurrent().navigate(previousView);
                }
            }
        });
        btnGoOriginView.getElement().setAttribute("theme", "tertiary-inline");
        btnGoOriginView.addClassName("link-button");

        HorizontalLayout layout = new HorizontalLayout(toggle, btnGoOriginView, filterText);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.addClickListener(event -> {
            EventBus.getInstance().post("DrawerToggleClicked");
        });

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        addToNavbar(false, layout, viewTitle);
    }

    private void addDrawerContent(String strAppName) {
        H1 appName = new H1(strAppName);
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);
        addToDrawer(header, createNavigation(), createFooter());
        content = new Div();
        content.setId("content");
        content.setWidth("100%");
        setContent(content);
    }

    protected AppNav createNavigation() {
        return nav;
    }

    protected void setNavigation(AppNav nav, String strAppName) {
        this.nav = nav;
        addDrawerContent(strAppName);
    }

    protected Footer createFooter() {
        return new Footer();
    }

    protected void setHamburgerTitle(String title) {
        filterText.setText(title);
    }

    protected void setGoOriginText(String originText) {
        btnGoOriginView.setText(originText);
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        if (getContent() != null) {
            PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
            return title == null ? "" : title.value();
        } else
            return "";
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI.getCurrent().getPage().executeJs(
                "var contentElement = document.querySelector('[content]');" +
                        "contentElement.style.overflow = 'hidden';");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

    }
}