package com.aat.application.core.form;

import com.aat.application.util.GlobalData;
import com.vaadin.flow.router.PageTitle;

/**
 * Class definition to create the mapping for the timeline
 */
public class TimeLineViewParameter {

    /**
     * Name of the column to display in the content
     * e.g  e.employeename
     */
    private String[] titleFieldName = null;

    public String getToolTipFieldName() {
        return toolTipFieldName;
    }

    public void setToolTipFieldName(String toolTipFieldName) {
        this.toolTipFieldName = toolTipFieldName;
    }

    /**
     * Name of the column to display in the tooltip
     * e.g,  e.empid || ' ' || e.employeename
     */
    private String toolTipFieldName = null;
    /**
     * Name of the column for the group ID
     * Note : separate definition on how group hieararchies are defined
     * e.g.  e.departmentid
     */
    private String groupIDFieldName = null;

    /**
     * Name of the column for the startdate
     * e.g pd.birthdate
     */
    private String[] startDateFieldNames = null;

    /**
     * Name of the column for the enddate
     * e. pd.birthdate +  interval '1' day  (based on postgres datetime function)
     */
    private String endDateFieldName = null;

    /**
     * Name of the column for the class
     * Classname is the CSS name to control the appearance of the item
     */
    private String classNameFieldName = null;

    /**
     * In simple 1 table definition, it's the name of the table
     * but could be more complex if you use a join statement
     * e.g.
     * employee e
     * inner join personaldetails pd on e.empid = pd.empid
     * inner join department d on e.departmentid = d.departmentid
     */
    private String fromDefinition = null;

    /**
     * Add where condition if applicable
     * e.g. d.orgid = ?
     */
    private String whereDefinition = null;
    private String[] whereDefinitions = null;
    private String selectDefinition = null;
    private String groupSelectDefinition = null;

    private Class<?> groupClass;
    private String groupClassPKField;
    String groupName;
    private String pageName;
    private Object[] parameters;
    private String dateFilterOn;

    /**
     * This is to test if the definition is valid or not
     * this assists the developer if the call is valid
     *
     * @return
     */

    public boolean isValid() {
        boolean valid = titleFieldName != null;
        //TODO - add some logging or notification to developer for missing definition

        if (fromDefinition == null) {
            valid = false;
            //TODO - add some logging or notification to developer for missing definition
        }

        return valid;

    }

    /**
     * This check if it requires filter parameter
     *
     * @return
     */
    public boolean isRequireParameter() {
        return whereDefinition != null;
    }


    public TimeLineViewParameter(String[] titleFieldName, String groupIDFieldName, String[] startDateFieldNames, String endDateFieldName, String classNameFieldName, String fromDefinition) {
        this.titleFieldName = titleFieldName;
        this.groupIDFieldName = groupIDFieldName;
        this.startDateFieldNames = startDateFieldNames;
        this.endDateFieldName = endDateFieldName;
        this.classNameFieldName = classNameFieldName;
        this.fromDefinition = fromDefinition;
    }

    public TimeLineViewParameter(String[] titleFieldName, String groupIDFieldName, String[] startDateFieldNames) {
        this.titleFieldName = titleFieldName;
        this.groupIDFieldName = groupIDFieldName;
        this.startDateFieldNames = startDateFieldNames;
    }

    public String[] getTitleFieldName() {
        return titleFieldName;
    }

    public void setTitleFieldName(String[] titleFieldName) {
        this.titleFieldName = titleFieldName;
    }

    public String getGroupIDFieldName() {
        return groupIDFieldName;
    }

    public void setGroupIDFieldName(String groupIDFieldName) {
        this.groupIDFieldName = groupIDFieldName;
    }

    public String[] getStartDateFieldNames() {
        return startDateFieldNames;
    }

    public void setStartDateFieldNames(String[] startDateFieldNames) {
        this.startDateFieldNames = startDateFieldNames;
    }

    public String getEndDateFieldName() {
        return endDateFieldName;
    }

    public void setEndDateFieldName(String endDateFieldName) {
        this.endDateFieldName = endDateFieldName;
    }

    public String getClassNameFieldName() {
        return classNameFieldName;
    }

    public void setClassNameFieldName(String classNameFieldName) {
        this.classNameFieldName = classNameFieldName;
    }

    public String getFromDefinition() {
        return fromDefinition;
    }

    public void setFromDefinition(String fromDefinition) {
        this.fromDefinition = fromDefinition;
    }

    public String getWhereDefinition() {
        return whereDefinition;
    }

    public void setWhereDefinition(String whereDefinition) {
        this.whereDefinition = whereDefinition;
    }

    public String[] getWhereDefinitions() {
        return whereDefinitions;
    }

    public void setWhereDefinitions(String[] whereDefinitions) {
        this.whereDefinitions = whereDefinitions;
    }


    public String getSelectDefinition() {
        return selectDefinition;
    }

    public void setSelectDefinition(String selectDefinition) {
        this.selectDefinition = selectDefinition;
    }

    public String getGroupSelectDefinition() {
        return groupSelectDefinition;
    }

    public void setGroupSelectDefinition(String groupSelectDefinition) {
        this.groupSelectDefinition = groupSelectDefinition;
    }

    public Class<?> getGroupClass() {
        return groupClass;
    }

    public void setGroupClass(Class<?> groupClass) {
        this.groupClass = groupClass;
        pageName = groupClass.getAnnotation(PageTitle.class).value();
        groupClassPKField = GlobalData.getPrimaryKeyField(groupClass).getName();
    }

    public String getGroupClassPKField() {
        return groupClassPKField;
    }

    public void setGroupClassPKField(String groupClassPKField) {
        this.groupClassPKField = groupClassPKField;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getDateFilterOn() {
        return dateFilterOn;
    }

    public void setDateFilterOn(String dateFilterOn) {
        this.dateFilterOn = dateFilterOn;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}