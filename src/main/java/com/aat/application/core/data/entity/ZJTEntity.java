package com.aat.application.core.data.entity;

public interface ZJTEntity {
    int getId();

    /**
     *
     *********  Creating Entity file  ************
     *   A. Define variable
     *        1. primary key field
     *            id = tablename + _id  e.g zjt_vehicle = zjt_vehicle_id
     *        2. Define Primitive Data type
     *            date uses LocalDateTime
     *            number uses int, double, and float
     *        3. Define Object
     *         - foreign key uses class name of the child table.
     *         - Child table must have a field satisfied the following .
     *           a. "mappedBy" uses variable name mapped by parent table.
     *           b. This is not a field in table and is only for joining table.
     *           c. The type of variable must be List<Parent>.
     *
     *            e.g. In parent table
     *            @JoinColumn(name="zjt_user_id")
     *             @DisplayName(value="Logged By")
     *             private ZJTUser user
     *
     *             e.g. In child table
     *             @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     *             @BaseItems
     *             private List<ZJTVehicleServiceType> vehicleServiceTypes;
     *   B. Define annotation
     *         1. use @DisplayName
     *           - The fields with only this annotation are displayed in Grid.
     *           - The value displayed in Grid is the value of annotation DisplayName.
     *           e.g.
     *           @JoinColumn(name="zjt_user_id")
     *           @DisplayName(value="Logged By")
     *           private ZJTUser user
     *          2. use @ContentDisplayedInSelect
     *           - If this Entity will be used as dropdown - it should be added
     *           this annotation in the field
     *           - The value of field with only this annotation will be content of dropdown.
     *            e.g.
     *           @Column
     *           @ContentDisplayedInSelect(value = "name")
     *           @DisplayName(value = "Name")
     *           private String name = "";
     *********  Creating View  ************
     *  Create View for a displaying Entity
     *   A. Define Route
     *          1. simple definition
     *          e.g.
     *              @Route(value = "vehicle", layout = MainLayout.class)
     *          2. definition for context menu
     *          - This needs for parent table or timeline with filtered value
     *          - In here, subcategory means parent, and filter means grid or timeline
     *          e.g.
     *              @Route(value = "vehicle/:subcategory?/:filter?", layout = MainLayout.class)
     *   B. Class
     *          - It should be extended class StandardFormView<ZJTEntity>
     *          - It should be implemented interface HasUrlParameter<String>
     *   C. Constructor
     *          1. It has one constructor with 2 arguments and Pass arguments to parent object
     *          e.g.
     *              public UserView(BaseEntityRepository<ZJTEntity> repository, TableInfoService tableInfoService) {
     *                  super(repository, tableInfoService);
     *              }
     *          2. Set gridview parameter.
     *          e.g.
     *                  gridViewParameter = new GridViewParameter(ZJTServiceKit.class, "");
     *                  gridViewParameter.setSelectDefinition("name");
     *                  super.setGridViewParameter(gridViewParameter);
     *          3. If displaying Entity in here is displayed as dropdown in other View,
     *          please define TimelineView parameter.
     *          e.g.
     *                  TimeLineViewParameter timeLineViewParameter = new TimeLineViewParameter("user.name", "user", "loginDate", null, null, "ZJTUser");
     *                  timeLineViewParameter.setWhereDefinition("user.zjt_user_id");
     *                  timeLineViewParameter.setGroupClass(ZJTVehicle.class);
     *                  timeLineViewParameter.setSelectDefinition("fleetid");
     *                  super.setTimeLineViewParameter(timeLineViewParameter);
     *           - In here, TimeLineViewParameter is structured as the following.
     *                  public TimeLineViewParameter(String titleFieldName,    // The field name selected as the title in timeline
     *                                          String groupIDFieldName,   // The field name selected as the group in timeline
     *                                          String startDateFieldName, // The field name selected as start date in timeline
     *                                          String endDateFieldName,   // The field name selected as end date in timeline
     *                                                                     // It may be null.
     *                                          String classNameFieldName, // The field name selected as the className in timeline
     *                                                                     // It may be null.
     *                                          String fromDefinition) {   // Entity name used query
     *
     *   D. Override Function onAttach
     *          1. Handle event process. This is the same in all Views.
     *          e.g.
     *                  if (this.form != null && this.isbGrid()) {
     *                     CommonForm form =  this.form;
     *                     onAddEvent(ev -> {
     *                         form.onNewItem((GuiItem) ev.getItem());
     *                         this.setMessageStatus("This is new added value " + ((GuiItem) ev.getItem()).getRecordData().get(1));
     *                     });
     *
     *                     onUpdateEvent(ev -> {
     *                         int count;
     *                         try {
     *                             count = form.onUpdateItem(new Object[]{ev.getRow(), ev.getColName(), ev.getColValue()});
     *                         } catch (Exception e) {
     *                             throw new RuntimeException(e);
     *                         }
     *                         if (count > 0)
     *                             this.setMessageStatus(count + " rows is updated.");
     *                     });
     *
     *                     onDeleteEvent(ev -> {
     *                         int count;
     *                         try {
     *                             count = form.onDeleteItemChecked();
     *                         } catch (Exception e) {
     *                             throw new RuntimeException(e);
     *                         }
     *                         if (count > 0)
     *                             this.setMessageStatus(count + " rows is deleted.");
     *                     });
     *
     *   E. Define function addMenu
     *      In here, define custom context menu.
     *            1. can define menu about grid
     *            e.g.
     *                MenuItem gridItem = editItem.addSubItem("Grid");
     *                gridItem.addContextMenuClickListener(e -> UI.getCurrent().navigate("vehicle/serviceschedule/grid/" + e.getRow().get(0).getRowKey()));
     *                In here, ZJTVehicleServiceSchedule is parent Entity and ZJTVehicle is child Entity.
     *              2. can define menu about timeline
     *              e.g.
     *                  timelineItem.addContextMenuClickListener(e -> UI.getCurrent().navigate("vehicle/serviceschedule/timeline/" + e.getRow().get(0).getRowKey()));
     **********  Construct MainLayout andAdding item on MainLayout  ************
     *  A. MainLayout should be extended CoreMainLayout
     *       1. Overrides function getNavigation.
     *       2. Call function setNavigation in constructor and pass the return value of function getNavigation to it.
     *
     *  B. Create new AppNavItem and set parameter to item
     *      e.g.
     *      parent.addItem(new AppNavItem("Vehicle", "vehicle", LineAwesomeIcon.PRODUCT_HUNT.create())
     *                 .withParameter("layout", this.getClass().getName()));
     *      In here, first argument is title on main layout and second one is route name.
     */
}