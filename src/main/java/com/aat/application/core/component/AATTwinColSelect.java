package com.aat.application.core.component;

import com.aat.application.util.GlobalData;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dnd.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.tatu.TwinColSelect;

import java.lang.reflect.Field;
import java.util.*;

public class AATTwinColSelect extends Div {
    public enum Direction {
        Up, Down;

        public int getCustomOrdinal() {
            if (this == Up) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private HorizontalLayout parentLayout;
    private TwinColSelect<String> twinColSelect;
    private List<Component> childrenDisplayed;
    private List<String> selectedIDArray;
    private List<String> totalItems;
    private Set<String> orderedItems;
    private int selectedItemCount = 0;
    private boolean bDrop = false;
    Button upBtn;
    Button downBtn;

    public AATTwinColSelect() {
        initial();
    }

    public Set<?> getValue() {
        return Collections.singleton(twinColSelect.getValue());
    }

    public Set<String> getSelectedItems() {
        return twinColSelect.getSelectedItems();
    }

    public void setItems(List<String> items) {
        totalItems = items;
        twinColSelect.setItems(items);
    }

    private void initial() {
        HorizontalLayout mainLayout = new HorizontalLayout();
        twinColSelect = new TwinColSelect<>();
        twinColSelect.addValueChangeListener(event -> addEventInItems(twinColSelect));

        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.add(twinColSelect, moveToolbar());

        parentLayout = mainLayout;
        DragSource<Component> dragSourceWrapper = DragSource.create(parentLayout);
        dragSourceWrapper.addDragStartListener(this::onDragStart);
        dragSourceWrapper.addDragEndListener(this::onDragEnd);

        add(parentLayout);
        addClickListener(this::handleClickEvent);
        addDoubleClickListener(this::handleDbClickEvent);
    }

    public void select(Iterable<String> items) {
        if (items != null) {
            if (twinColSelect == null)
                initial();
            twinColSelect.select(items);
            twinColSelect.clearTicks(TwinColSelect.ColType.RIGHT);
            selectedItemCount = twinColSelect.getSelectedItems().size();
            addEventInItems(twinColSelect);
        }
    }

    public void setLabel(String label) {
        twinColSelect.setLabel(label);
    }

    public void deselectAll() {
        twinColSelect.deselectAll();
    }

    private VerticalLayout moveToolbar() {
        VerticalLayout uptownLayout = new VerticalLayout();
        uptownLayout.setSizeFull();
        upBtn = new Button(VaadinIcon.ARROW_UP.create());
        upBtn.addClickListener(e -> moveItems(Direction.Up));
        downBtn = new Button(VaadinIcon.ARROW_DOWN.create());
        downBtn.addClickListener(e -> moveItems(Direction.Down));
        uptownLayout.add(upBtn, downBtn);
        return uptownLayout;
    }

    private void moveItems(Direction direction) {
        List<Component> liveParent = GlobalData.findComponentsWithAttribute(twinColSelect, "aria-live");
        List<Component> selectedComponents = GlobalData.findComponentsWithAttribute(liveParent.get(0), "aria-selected", "true");

        Component endPoint;
        if (direction == Direction.Up)
            endPoint = childrenDisplayed.get(0);
        else {
            endPoint = childrenDisplayed.get(childrenDisplayed.size() - 1);
            Collections.reverse(selectedComponents);
        }
        if (!selectedComponents.contains(endPoint)) {
            upBtn.setEnabled(false);
            downBtn.setEnabled(false);
            selectedIDArray = new ArrayList<>();

            for (Component droppedComponent :
                    selectedComponents) {
                int index = childrenDisplayed.indexOf(droppedComponent) + direction.getCustomOrdinal();
                selectedIDArray.add(droppedComponent.getElement().getText());
                Component sourceComponent = childrenDisplayed.get(index);
                setOrderedItemsFromComponent(droppedComponent, sourceComponent);
            }
            updateOrderedItems();
            updateTwinColSelectOrder();
        }
    }

    private void addEventInItems(TwinColSelect<String> twinColSelect) {
        List<Component> liveParent = GlobalData.findComponentsWithAttribute(twinColSelect, "aria-live");
        childrenDisplayed = GlobalData.findComponentsWithAttribute(liveParent.get(0), "aria-selected");

        for (Component child : childrenDisplayed) {
            DropTarget<Component> dropTargetWrapper = DropTarget.create(child);
            dropTargetWrapper.addDropListener(this::onDrop);
        }
    }

    private void handleClickEvent(ClickEvent<Div> event) {
    }

    private void handleDbClickEvent(ClickEvent<Div> event) {
        if (selectedItemCount < twinColSelect.getSelectedItems().size()) {
            addEventInItems(twinColSelect);
        }
        selectedItemCount = twinColSelect.getSelectedItems().size();
        twinColSelect.clearTicks(TwinColSelect.ColType.RIGHT);
    }

    private void onDragStart(DragStartEvent<Component> event) {
    }

    private void onDragEnd(DragEndEvent<Component> event) {
        if (bDrop) {
            bDrop = false;
            updateOrderedItems();
            this.updateTwinColSelectOrder();
        }
    }

    private void onDrop(DropEvent<Component> event) {
        Component droppedComponent = event.getDragSourceComponent().orElse(null);
        selectedIDArray = new ArrayList<>();
        if (droppedComponent != null && droppedComponent.getElement() != null) {
            selectedIDArray.add(droppedComponent.getElement().getText());
        }
        setOrderedItemsFromComponent(droppedComponent, event.getSource());
        bDrop = true;
    }

    private void updateTwinColSelectOrder() {
        List<String> newOrder = new ArrayList<>(orderedItems);
        TwinColSelect<String> newTwinColSelect = new TwinColSelect<>();

        newTwinColSelect.setItems(getOrderedTotalItems(newOrder));
        newTwinColSelect.select(newOrder);
        addEventInItems(newTwinColSelect);

        parentLayout.removeAll();
        parentLayout.add(newTwinColSelect, moveToolbar());
        twinColSelect = newTwinColSelect;
        twinColSelect.clearTicks(TwinColSelect.ColType.RIGHT);
        UI.getCurrent().access(() -> checkAllGenerations(twinColSelect));
    }

    private void checkAllGenerations(Component component) {
        component.getChildren().forEach(child -> {
            if (child.getId().isPresent() && selectedIDArray.contains(child.getElement().getText())) {
                child.getElement().setAttribute("checked", "");
                child.getElement().setAttribute("aria-selected", "true");
            }
            checkAllGenerations(child);
        });
        UI.getCurrent().access(() -> {
            upBtn.setEnabled(true);
            downBtn.setEnabled(true);
        });
    }

    private void setOrderedItemsFromComponent(Component droppedComponent, Component sourceComponent) {
        if (droppedComponent != null) {
            int childIndex = childrenDisplayed.indexOf(sourceComponent);
            if (childIndex >= 0) {
                childrenDisplayed.remove(droppedComponent);
                childrenDisplayed.add(childIndex, droppedComponent);
            }
        }
    }

    private void updateOrderedItems() {
        orderedItems = new LinkedHashSet<>();
        for (Component child : childrenDisplayed) {
            try {
                Field itemField = getItemField(child);
                itemField.setAccessible(true);
                orderedItems.add((String) itemField.get(child));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                // Log the exception
            }
        }
    }

    private Field getItemField(Component component) throws NoSuchFieldException {
        return component.getClass().getDeclaredField("item");
    }

    private List<String> getOrderedTotalItems(List<String> newOrder) {
        List<String> tempTotalItems = new ArrayList<>();
        for (String item :
                totalItems) {
            if (!newOrder.contains(item))
                tempTotalItems.add(item);
        }
        tempTotalItems.addAll(newOrder);
        return tempTotalItems;
    }
}