package com.aat.application.core.form;

import com.aat.application.core.data.entity.ZJTEntity;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Locale;

public abstract class CommonForm extends VerticalLayout {

    public TuiGrid grid;
    protected String dateFilterOn;
    protected final DatePicker startDatePicker = new DatePicker("");
    protected final DatePicker endDatePicker = new DatePicker("");
    private final ComboBox<EnumDateFilter> dateFilterComboBox = new ComboBox<>("");
    protected HorizontalLayout dateFilter;

    abstract public void onNewItem(GuiItem item);

    abstract public ZJTEntity onNewItem(ZJTEntity entity, int id);

    abstract public int onUpdateItem(Object[] objects) throws Exception;

    abstract public int onDeleteItemChecked() throws Exception;

    abstract public void setContextMenu(AATContextMenu contextMenu);

    abstract public void setMessageStatus(String msg);

    abstract public void addCustomButton(Button button);

    abstract public String getHamburgerText();

    abstract public String getOriginViewText();

    abstract public void onUpdateForm() throws Exception;

    public CommonForm() {
        startDatePicker.addValueChangeListener(e -> {
            try {
                if (endDatePicker.getValue() != null)
                    this.filterByDate(e.getValue().atStartOfDay(), endDatePicker.getValue().atStartOfDay());
                else
                    this.filterByDate(e.getValue().atStartOfDay(), null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        endDatePicker.addValueChangeListener(e -> {
            try {
                if (startDatePicker.getValue() != null)
                    this.filterByDate(startDatePicker.getValue().atStartOfDay(), e.getValue().atStartOfDay());
                else
                    this.filterByDate(null, e.getValue().atStartOfDay());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        dateFilterComboBox.setItems(EnumDateFilter.values());
        dateFilterComboBox.addValueChangeListener(e -> updateDateFilter());
        dateFilterComboBox.setValue(EnumDateFilter.TM);
        dateFilter = new HorizontalLayout(dateFilterComboBox, startDatePicker, new Span("To"), endDatePicker);
        dateFilter.setAlignItems(FlexComponent.Alignment.CENTER);
    }

    private void updateDateFilter() {
        LocalDate dateFrom = null;
        LocalDate dateTo = null;

        switch (dateFilterComboBox.getValue()) {
            case TD:
                dateFrom = LocalDate.from(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
                dateTo = LocalDate.from(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
                break;

            case TW:
                dateFrom = LocalDate.now();
                dateFrom = dateFrom.with(WeekFields.of(Locale.UK).getFirstDayOfWeek());
                dateTo = dateFrom.plusWeeks(1).minusDays(1);
                break;
            case NW:
                dateFrom = LocalDate.now().plusWeeks(1);
                dateFrom = dateFrom.with(WeekFields.of(Locale.UK).getFirstDayOfWeek());
                dateTo = dateFrom.plusWeeks(1).minusDays(1);
                break;
            case TM:
                dateFrom = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
                dateTo = YearMonth.of(LocalDate.now().getYear(), LocalDate.now().getMonth()).atEndOfMonth();
                break;
            case NM:
                dateFrom = LocalDate.now().plusMonths(1);
                dateFrom = LocalDate.of(dateFrom.getYear(), dateFrom.getMonth(), 1);
                dateTo = YearMonth.of(dateFrom.getYear(), dateFrom.getMonth()).atEndOfMonth();
                break;
        }

        if (dateFrom != null)
            startDatePicker.setValue(dateFrom);
        if (dateTo != null)
            endDatePicker.setValue(dateTo);
    }

    public void filterByDate(LocalDateTime start, LocalDateTime end) throws Exception {
        if (start != null && end != null) {
            if (!start.isEqual(end) && !start.isBefore(end)) {
                Notification.show("End date should be after start date", 5000, Notification.Position.MIDDLE);
                return;
            }
        }
        onUpdateForm();
    }

    protected void addConditionWhenFilteringDate(StringBuilder query) {
        if (startDatePicker.getValue() != null
                && endDatePicker.getValue() != null) {
            if (!startDatePicker.getValue().equals(endDatePicker.getValue())) {
                query.append(" AND DATE(p.").append(dateFilterOn)
                        .append(") >= '").append(startDatePicker.getValue().atStartOfDay()).append("'");
                query.append(" AND DATE(p.").append(dateFilterOn)
                        .append(") < '").append(endDatePicker.getValue().plusDays(1).atStartOfDay()).append("'");
            } else {
                query.append(" AND DATE(p.").append(dateFilterOn)
                        .append(") >= '").append(startDatePicker.getValue().atStartOfDay()).append("'");
                query.append(" AND DATE(p.").append(dateFilterOn)
                        .append(") < '").append(endDatePicker.getValue().plusDays(1).atStartOfDay()).append("'");
            }

        } else {
            if (startDatePicker.getValue() != null)
                query.append(" AND DATE(p.").append(dateFilterOn)
                        .append(") >= '").append(startDatePicker.getValue().atStartOfDay()).append("'");
            if (endDatePicker.getValue() != null)
                query.append(" AND DATE(p.").append(dateFilterOn)
                        .append(") < '").append(endDatePicker.getValue().plusDays(1).atStartOfDay()).append("'");
        }
    }
}