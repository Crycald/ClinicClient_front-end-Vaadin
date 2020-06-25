package com.client.frontend.home.clinic;

import com.client.frontend.api.Client;
import com.client.frontend.api.domain.Clinic;
import com.client.frontend.api.domain.Operation;
import com.client.frontend.api.domain.OperationConnector;
import com.client.frontend.api.domain.enums.TypeOfAnimal;
import com.client.frontend.api.domain.enums.TypeOfOperation;
import com.client.frontend.home.LoginPage;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;


@Route("clinicPage")
@StyleSheet("/css/style.css")
@PageTitle("Clinic Page")
public class ClinicPage extends Div {
    private Button logOutButton;
    private Button addOperationButton;
    private Button settingsButton;
    private Button deleteAccButton;
    private Grid operationListGrid;
    private Grid typeOfOperationsGrid;
    private Dialog popUpAddDialog;
    private Dialog editOperationDialog;
    private ComboBox<TypeOfOperation> typeOfOperationComboBox;
    private ComboBox<TypeOfOperation> innerTypeOfOperationComboBox;
    private NumberField costField;
    private Button innerAddButton;
    private Button innerUpdateOperationButton;
    private Button innerDeleteOperationButton;
    private Button updateAccButton;
    private TextField companyNameTextField;
    private ComboBox typeOfAnimalComboBox;
    private TextField addressTextField;
    private TextField nipNumberField;
    private TextField phoneNumberField;
    private NumberField innerCostOfOperationNumberField;
    private EmailField emailField;
    private PasswordField passwordField;
    private Dialog updateAccDialog;
    private Label loginLabel;
    private Long clinicId = LoginPage.getClinicId();

    private Set<Long> operationId;
    private Long actId;
    private Set<Long> operationClinicId;
    private Long actClinicId;
    private Set<TypeOfOperation> operationName;
    private String typeOfOperationName;
    private Set<BigDecimal> costOfOperation;
    private BigDecimal operationCost;

    private Client client;
    private Clinic clinic;
    private Operation operationUpdate;
    private Operation operation = new Operation();


    @Autowired
    public ClinicPage(Client client) {
        this.client = client;
        validateClinicId();
    }

    public void init() {
        setFormForClinicPage();
    }

    private void setFormForLoginLabel() {
        Label label = new Label("Logged in as: ");
        label.setClassName("label");
        loginLabel = new Label(getClinicLogin());
        loginLabel.setClassName("login-label");

        VerticalLayout verticalLayout = new VerticalLayout(label, loginLabel);
        add(verticalLayout);
    }

    private void setFormForClinicPage() {
        setFormForLoginLabel();
        setFormForLogOutButton();
        setFormForSettingsButton();
        setFormForTypeOfOperationGrid();
        setFormForOperationListGrid();
        setFormForAddOperationButton();

        HorizontalLayout horizontalLayout = new HorizontalLayout(typeOfOperationsGrid, operationListGrid, logOutButton, settingsButton);
        VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout, addOperationButton);

        add(verticalLayout);
    }

    private void validateClinicId() {
        if (clinicId == null) {
            redirectToHomePage();
            UI.getCurrent().getPage().reload();
        } else {
            init();
        }
    }

    private void logOutButtonEvent(ClickEvent<Button> event) {
        UI.getCurrent().navigate("");
    }

    private void innerAddButtonEvent (ClickEvent<Button> event) {
        operation.setClinicId(clinicId);
        operation.setOperations(typeOfOperationComboBox.getValue());
        operation.setCost(new BigDecimal(costField.getValue()));

        client.createOperation(operation);

        popUpAddDialogEventClose();
        Notification.show("Successfully added. Refresh the page");
    }

    private void popUpAddDialogEventOpen() {
        popUpAddDialog.open();
    }

    private void popUpAddDialogEventClose() {
        popUpAddDialog.close();
    }

    private void redirectToHomePage() {
        UI.getCurrent().navigate("");
    }

    private void setFormForLogOutButton() {
        logOutButton = new Button();
        logOutButton.setClassName("log-out-button");
        logOutButton.setIcon(new Icon(VaadinIcon.POWER_OFF));
        logOutButton.addClickListener(this::logOutButtonEvent);
    }

    private void setFormForSettingsButton() {
        settingsButton = new Button();
        settingsButton.setIcon(new Icon(VaadinIcon.COGS));
        settingsButton.setClassName("update-acc-button");
        settingsButton.addClickListener(this::settingsButtonEvent);
        add(settingsButton);
    }

    private TypeOfAnimal typeOfAnimalComboBoxListener(HasValue.ValueChangeEvent event) {
        return TypeOfAnimal.valueOf(event.getValue().toString());
    }

    private void setFormForUpdateAccDialog() {
        updateAccDialog = new Dialog();
        updateAccDialog.open();
        updateAccDialog.setCloseOnOutsideClick(true);

        setFormForCompanyNameTextField();
        setFormForTypeOfAnimalComboBox();
        setFormForAddressTextField();
        setFormForNipNumberField();
        setFormForPhoneNumberField();
        setFormForEmailField();
        setFormForPasswordField();

        setFormForInnerUpdateButton();
        setFormForDeleteAccButton();

        VerticalLayout verticalLayout = new VerticalLayout(companyNameTextField, typeOfAnimalComboBox, addressTextField, nipNumberField,
                phoneNumberField, emailField, passwordField, updateAccButton);

        updateAccButton.addClickListener(event -> {
            clinic = new Clinic();
            clinic.setId(clinicId);
            clinic.setLogin(getClinicLogin());

            if (companyNameTextField.isEmpty()) {
                clinic.setName(getClinicCompanyName());
            } else {
                clinic.setName(companyNameTextField.getValue());
            }
            if (typeOfAnimalComboBox.isEmpty()) {
                clinic.setTypeOfAnimal(getClinicSpecialization());
            } else {
                clinic.setTypeOfAnimal(TypeOfAnimal.valueOf(typeOfAnimalComboBox.getValue().toString()));
            }
            if (addressTextField.isEmpty()) {
                clinic.setAddress(getClinicAddress());
            } else {
                clinic.setAddress(addressTextField.getValue());
            }
            if (nipNumberField.isEmpty()) {
                clinic.setNip(getClinicNip());
            } else {
                clinic.setNip(Long.parseLong(nipNumberField.getValue()));
            }
            if (phoneNumberField.isEmpty()) {
                clinic.setPhoneNumber(getClinicPhoneNumber());
            } else {
                clinic.setPhoneNumber(phoneNumberField.getValue().toString());
            }
            if (emailField.isEmpty()) {
                clinic.setMail(getClinicMail());
            } else {
                clinic.setMail(emailField.getValue());
            }
            if (passwordField.isEmpty()) {
                clinic.setPassword(getClinicPassword());
            } else {
                clinic.setPassword(passwordField.getValue());
            }

            client.updateClinic(clinic);
            updateAccDialog.close();
            Notification.show("Account has been updated.");
        });

        updateAccDialog.add(verticalLayout, deleteAccButton);
    }

    private void setFormForCompanyNameTextField() {
        companyNameTextField = new TextField();
        companyNameTextField.setLabel("Company name");
    }

    private void setFormForTypeOfAnimalComboBox() {
        typeOfAnimalComboBox = new ComboBox<TypeOfAnimal>();
        typeOfAnimalComboBox.setItems(TypeOfAnimal.values());
        typeOfAnimalComboBox.setLabel("Type of animal");
        typeOfAnimalComboBox.addValueChangeListener(this::typeOfAnimalComboBoxListener);
    }

    private void setFormForAddressTextField() {
        addressTextField = new TextField();
        addressTextField.setLabel("Address");
    }

    private void setFormForNipNumberField() {
        nipNumberField = new TextField();
        nipNumberField.setWidthFull();
        nipNumberField.setLabel("NIP");
    }

    private void setFormForPhoneNumberField() {
        phoneNumberField = new TextField();
        phoneNumberField.setWidthFull();
        phoneNumberField.setLabel("Phone number");
    }

    private void setFormForEmailField() {
        emailField = new EmailField();
        emailField.setLabel("Email");
    }

    private void setFormForPasswordField() {
        passwordField = new PasswordField();
        passwordField.setLabel("Password");
    }

    private void settingsButtonEvent(ClickEvent<Button> event) {
        setFormForUpdateAccDialog();
    }

    private void setFormForInnerUpdateButton() {
        updateAccButton = new Button("UPDATE");
    }

    private void setFormForDeleteAccButton() {
        deleteAccButton = new Button();
        deleteAccButton.setText("DELETE ACCOUNT");
        deleteAccButton.setClassName("delete-button");
        deleteAccButton.addClickListener(this::deleteAccButtonEvent);
    }

    private void deleteAccButtonEvent(ClickEvent<Button> event) {
        client.deleteClinicById(clinicId);
        updateAccDialog.close();
        redirectToHomePage();
        Notification.show("Account has been deleted.");
    }

    private void setFormForAddOperationButton() {
        addOperationButton = new Button();
        addOperationButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        addOperationButton.setClassName("add-button");
        addOperationButton.addClickListener(this::addOperationButtonEvent);
        add(addOperationButton);
    }

    private void addOperationButtonEvent(ClickEvent<Button> event) {
        setFormForPopUpDialog();
        popUpAddDialogEventOpen();
    }

    private void setFormForTypeOfOperationComboBox() {
        typeOfOperationComboBox = new ComboBox<>();
        typeOfOperationComboBox.setAllowCustomValue(false);
        typeOfOperationComboBox.setItems(TypeOfOperation.values());
        typeOfOperationComboBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                costField.setEnabled(true);
                event.getValue();
            } else {
                costField.setEnabled(false);
            }
        });
    }

    private void setFormForCostField() {
        costField = new NumberField();
        costField.setPlaceholder("COST");
        costField.setEnabled(false);
        costField.addValueChangeListener(event -> {
           if (event.getValue() != null) {
               innerAddButton.setEnabled(true);
               event.getValue();
           } else {
               innerAddButton.setEnabled(false);
           }
        });
    }

    private void setFormForInnerAddButton() {
        innerAddButton = new Button("Add");
        innerAddButton.addClickListener(this::innerAddButtonEvent);
        innerAddButton.setEnabled(false);
    }

    private void setFormForPopUpDialog() {
        setFormForTypeOfOperationComboBox();
        setFormForCostField();
        setFormForInnerAddButton();

        popUpAddDialog = new Dialog(new Label("Add operation "));
        popUpAddDialog.setCloseOnOutsideClick(true);
        popUpAddDialog.add(typeOfOperationComboBox);
        popUpAddDialog.add(costField);
        popUpAddDialog.add(innerAddButton);
    }

    private void setFormForTypeOfOperationGrid() {
        typeOfOperationsGrid = new Grid(Operation.class);
        typeOfOperationsGrid.setItems(client.getOperationsByClinicId(clinicId));
        typeOfOperationsGrid.getColumnByKey("id").setVisible(false);
        typeOfOperationsGrid.getColumnByKey("clinicId").setVisible(false);

        typeOfOperationsGrid.setColumns("operations", "cost");
        typeOfOperationsGrid.getColumnByKey("operations").setHeader("OPERATION").setAutoWidth(true);
        typeOfOperationsGrid.getColumnByKey("cost").setHeader("COST").setAutoWidth(true);
        typeOfOperationsGrid.asSingleSelect();

        typeOfOperationsGrid.addSelectionListener(event -> {
            Set<Operation> eventAllSelectedItems = event.getAllSelectedItems();
            operationId = eventAllSelectedItems.stream().map(Operation::getId).collect(Collectors.toSet());
            operationClinicId = eventAllSelectedItems.stream().map(Operation::getClinicId).collect(Collectors.toSet());
            operationName = eventAllSelectedItems.stream().map(Operation::getOperations).collect(Collectors.toSet());
            costOfOperation = eventAllSelectedItems.stream().map(Operation::getCost).collect(Collectors.toSet());

            actId = Long.valueOf(operationId.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
            typeOfOperationName = operationName.toString().replaceAll("\\[", "").replaceAll("\\]", "");
            operationCost = new BigDecimal(costOfOperation.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
        });

        typeOfOperationsGrid.addItemClickListener(event -> {
            setFormForEditOperationDialog();
        });

        typeOfOperationsGrid.setClassName("grid-operation-type");
        HorizontalLayout horizontalLayout = new HorizontalLayout(typeOfOperationsGrid);
        add(horizontalLayout);
    }

    private void setFormForEditOperationDialog() {
        editOperationDialog = new Dialog();
        setFormForInnerTypeOfOperationComboBox();
        setFormForInnerCostOfOperationNumberField();
        setFormForInnerUpdateOperationButton();
        setFormForInnerDeleteOperationButton();
        editOperationDialog.add(innerTypeOfOperationComboBox, innerCostOfOperationNumberField, innerUpdateOperationButton, innerDeleteOperationButton);

        editOperationDialog.open();
    }

    private void setFormForInnerDeleteOperationButton() {
        innerDeleteOperationButton = new Button();
        innerDeleteOperationButton.setText("DELETE");
        innerDeleteOperationButton.setClassName("delete-button");
        innerDeleteOperationButton.addClickListener(this::innerDeleteOperationButtonEvent);
    }

    private void innerDeleteOperationButtonEvent(ClickEvent<Button> event) {
        client.deleteOperationById(actId);
        editOperationDialog.close();
        Notification.show("Successfully deleted. Refresh the page.");
    }

    private void setFormForInnerUpdateOperationButton() {
        innerUpdateOperationButton = new Button();
        innerUpdateOperationButton.setText("UPDATE");
        innerUpdateOperationButton.addClickListener(this::innerUpdateOperationButtonEvent);
        innerUpdateOperationButton.setEnabled(false);
    }

    private void innerUpdateOperationButtonEvent(ClickEvent<Button> event) {
        operationUpdate = new Operation();

        operationUpdate.setId(actId);
        operationUpdate.setClinicId(clinicId);
        operationUpdate.setOperations(innerTypeOfOperationComboBox.getValue());
        operationUpdate.setCost(new BigDecimal(innerCostOfOperationNumberField.getValue()));

        client.updateOperation(operationUpdate);
        editOperationDialog.close();
        Notification.show("Successfully updated. Refresh the page.");
    }

    private void setFormForInnerTypeOfOperationComboBox() {
        innerTypeOfOperationComboBox = new ComboBox<>("Operation");
        innerTypeOfOperationComboBox.setAllowCustomValue(false);
        innerTypeOfOperationComboBox.setItems(TypeOfOperation.values());
        innerTypeOfOperationComboBox.addValueChangeListener(value -> {
            if (value.getValue() != null) {
                innerCostOfOperationNumberField.setEnabled(true);
                value.getValue();
            } else {
                innerCostOfOperationNumberField.setEnabled(false);
            }
        });
    }

    private void setFormForInnerCostOfOperationNumberField() {
        innerCostOfOperationNumberField = new NumberField("Cost");
        innerCostOfOperationNumberField.setEnabled(false);
        innerCostOfOperationNumberField.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                innerUpdateOperationButton.setEnabled(true);
                event.getValue();
            } else {
                innerUpdateOperationButton.setEnabled(false);
            }
        });
    }

    private void setFormForOperationListGrid() {
        operationListGrid = new Grid(OperationConnector.class);
        operationListGrid.setItems(client.getListsByClinicId(clinicId));
        operationListGrid.getColumnByKey("id").setVisible(false);
        operationListGrid.getColumnByKey("clinicId").setVisible(false);
        operationListGrid.getColumnByKey("clinicName").setVisible(false);
        operationListGrid.getColumnByKey("clinicAddress").setVisible(false);
        operationListGrid.getColumnByKey("customerId").setVisible(false);
        operationListGrid.getColumnByKey("operationActId").setVisible(false);

        operationListGrid.setColumns("customerFirstName", "customerLastName", "customerPhoneNumber", "operationName", "operationCost", "date");

        operationListGrid.getColumnByKey("customerFirstName").setHeader("F_NAME");
        operationListGrid.getColumnByKey("customerLastName").setHeader("L_NAME");
        operationListGrid.getColumnByKey("customerPhoneNumber").setHeader("PHONE");
        operationListGrid.getColumnByKey("operationName").setHeader("ACT");
        operationListGrid.getColumnByKey("operationCost").setHeader("COST");
        operationListGrid.getColumnByKey("date").setHeader("DATE");

        operationListGrid.setClassName("grid-operation-list");

        HorizontalLayout horizontalLayout = new HorizontalLayout(operationListGrid);
        add(horizontalLayout);
    }

    private String getClinicCompanyName() {
        Clinic clinic = client.getClinicById(clinicId);
        return clinic.getName();
    }

    private TypeOfAnimal getClinicSpecialization() {
        Clinic clinic = client.getClinicById(clinicId);
        return clinic.getTypeOfAnimal();
    }

    private String getClinicAddress() {
        Clinic clinic = client.getClinicById(clinicId);
        return clinic.getAddress();
    }

    private Long getClinicNip() {
        Clinic clinic = client.getClinicById(clinicId);
        return clinic.getNip();
    }

    private String getClinicPhoneNumber() {
        Clinic clinic = client.getClinicById(clinicId);
        return clinic.getPhoneNumber();
    }

    private String getClinicMail() {
        Clinic clinic = client.getClinicById(clinicId);
        return clinic.getMail();
    }

    private String getClinicLogin() {
        Clinic clinic = client.getClinicById(clinicId);
        return clinic.getLogin();
    }

    private String getClinicPassword() {
        Clinic clinic = client.getClinicById(clinicId);
        return clinic.getPassword();
    }
}
