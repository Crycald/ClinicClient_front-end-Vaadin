package com.client.frontend.home.customer;

import com.client.frontend.api.Client;
import com.client.frontend.api.domain.Clinic;
import com.client.frontend.api.domain.Customer;
import com.client.frontend.api.domain.Operation;
import com.client.frontend.api.domain.OperationConnector;
import com.client.frontend.api.domain.enums.TypeOfAnimal;
import com.client.frontend.home.LoginPage;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Route("customerPage")
@StyleSheet("/css/style.css")
@PageTitle("Customer Page")
public class CustomerPage extends Div {
    private Button logOutButton;
    private Button addButton;
    private Button settingsButton;
    private Button innerUpdateButton;
    private Button saveRegistrationButton;
    private Button deleteAccButton;
    private Grid actualRegistrations;
    private Dialog popUpDialog;
    private Dialog updateAccDialog;
    private TextField firstNameTextfield;
    private TextField lastNameTextfield;
    private PasswordField passwordTextfield;
    private EmailField emailTextfield;
    private TextField phoneNumberTextfield;
    private ComboBox selectAnimalComboBox;
    private ComboBox selectClinicComboBox;
    private ComboBox selectOperationComboBox;
    private DatePicker selectDateDataPicker;
    private Label loginLabel;
    private Long customerId = LoginPage.getCustomerId();
    private Long chosenClinicId;
    private Long chosenOperationId;

    private Client client;
    private Customer customer;
    private OperationConnector operationConnector = new OperationConnector();

    @Autowired
    public CustomerPage(Client client) {
        this.client = client;
        validateCustomerId();
    }

    public void init() {
        setFormForCustomerPage();
    }

    private void validateCustomerId() {
        if (customerId == null) {
            redirectToHomePage();
            UI.getCurrent().getPage().reload();
        } else {
            init();
        }
    }

    private void setFormForCustomerPage() {
        setFormForLoginLabel();
        setFormForLogOutButton();
        setFormForSettingsButton();
        setFormForAddButton();
        setFormForActualRegistrations();

        HorizontalLayout horizontalLayout = new HorizontalLayout(actualRegistrations, logOutButton, settingsButton);
        VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout, addButton);

        add(verticalLayout);
    }

    private void updateAccButtonEvent(ClickEvent<Button> event) {
        setFormForUpdateAccDialog();
    }

    private void setFormForLoginLabel() {
        Label label = new Label("Logged in as: ");
        label.setClassName("label");
        loginLabel = new Label(getCustomerLogin());
        loginLabel.setClassName("login-label");
        add(label, loginLabel);
    }

    private void setFormForDeleteAccButton() {
        deleteAccButton = new Button();
        deleteAccButton.setText("DELETE ACCOUNT");
        deleteAccButton.setClassName("delete-button");
        deleteAccButton.addClickListener(this::deleteAccButtonEvent);
    }

    private void deleteAccButtonEvent(ClickEvent<Button> event) {
        client.deleteCustomerById(customerId);
        updateAccDialog.close();
        redirectToHomePage();
        Notification.show("Account has been deleted.");
    }

    private void setFormForSettingsButton() {
        settingsButton = new Button();
        settingsButton.setIcon(new Icon(VaadinIcon.COGS));
        settingsButton.setClassName("update-acc-button");
        settingsButton.addClickListener(this::updateAccButtonEvent);
    }

    private void setFormForUpdateAccDialog() {
        updateAccDialog = new Dialog();
        updateAccDialog.open();
        updateAccDialog.setCloseOnOutsideClick(true);

        setFormForFirstNameTextfield();
        setFormForLastNameTextfield();
        setFormForPasswordTextfield();
        setFormForEmailTextfield();
        setFormForPhoneNumberTextfield();

        setFormForInnerUpdateButton();
        setFormForDeleteAccButton();

        VerticalLayout verticalLayout = new VerticalLayout(firstNameTextfield, lastNameTextfield, passwordTextfield, emailTextfield, phoneNumberTextfield, innerUpdateButton);

        innerUpdateButton.addClickListener(event -> {
            customer = new Customer();
            customer.setId(customerId);
            customer.setLogin(getCustomerLogin());

            if (firstNameTextfield.isEmpty()) {
                customer.setFirstname(getCustomerFirstName());
            } else {
                customer.setFirstname(firstNameTextfield.getValue());
            }
            if (lastNameTextfield.isEmpty()) {
                customer.setLastname(getCustomerLastName());
            } else {
                customer.setLastname(lastNameTextfield.getValue());
            }
            if (passwordTextfield.isEmpty()) {
                customer.setPassword(getCustomerPassword());
            } else {
                customer.setPassword(passwordTextfield.getValue());
            }
            if (emailTextfield.isEmpty()) {
                customer.setEmail(getCustomerEmail());
            } else {
                customer.setEmail(emailTextfield.getValue());
            }
            if (phoneNumberTextfield.isEmpty()) {
                customer.setPhoneNumber(getCustomerPhoneNumber());
            } else {
                customer.setPhoneNumber(phoneNumberTextfield.getValue().toString());
            }

            client.updateCustomer(customer);
            updateAccDialog.close();
            Notification.show("Account has been updated.");
        });

        updateAccDialog.add(verticalLayout, deleteAccButton);
    }

    private void setFormForFirstNameTextfield() {
        firstNameTextfield = new TextField();
        firstNameTextfield.setLabel("First name");
    }

    private void setFormForLastNameTextfield() {
        lastNameTextfield = new TextField();
        lastNameTextfield.setLabel("Last name");
    }

    private void setFormForPasswordTextfield() {
        passwordTextfield = new PasswordField();
        passwordTextfield.setLabel("Password");
    }

    private void setFormForEmailTextfield() {
        emailTextfield = new EmailField();
        emailTextfield.setLabel("Email");
    }

    private void setFormForPhoneNumberTextfield() {
        phoneNumberTextfield = new TextField();
        phoneNumberTextfield.setLabel("Phone number");
        phoneNumberTextfield.setWidthFull();
    }

    private void setFormForAddButton() {
        addButton = new Button();
        addButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        addButton.setClassName("add-button-customer");
        addButton.addClickListener(this::addButtonEvent);
    }

    private void setFormForInnerUpdateButton() {
        innerUpdateButton = new Button("UPDATE");
    }

    private void setFormForLogOutButton() {
        logOutButton = new Button();
        logOutButton.setIcon(new Icon(VaadinIcon.POWER_OFF));
        logOutButton.setClassName("log-out-button");
        logOutButton.addClickListener(this::logOutButtonEvent);
    }

    private void setFormForActualRegistrations() {
        actualRegistrations = new Grid(OperationConnector.class);
        actualRegistrations.setClassName("grid-operation-list");
        actualRegistrations.setItems(client.getListsByCustomerId(customerId));
        actualRegistrations.removeColumnByKey("id");
        actualRegistrations.removeColumnByKey("clinicId");
        actualRegistrations.removeColumnByKey("customerId");
        actualRegistrations.removeColumnByKey("customerFirstName");
        actualRegistrations.removeColumnByKey("customerLastName");
        actualRegistrations.removeColumnByKey("customerPhoneNumber");
        actualRegistrations.removeColumnByKey("operationActId");
        actualRegistrations.setColumns("clinicName", "clinicAddress", "operationName", "operationCost", "date");

        actualRegistrations.getColumnByKey("clinicName").setHeader("CLINIC");
        actualRegistrations.getColumnByKey("clinicAddress").setHeader("ADDRESS");
        actualRegistrations.getColumnByKey("operationName").setHeader("ACT");
        actualRegistrations.getColumnByKey("operationCost").setHeader("COST");
        actualRegistrations.getColumnByKey("date").setHeader("DATE");
    }

    private void selectOperationComboBoxListener(HasValue.ValueChangeEvent event) {
        List<String> list = new ArrayList();
        String toad = event.getValue().toString();
        list.add(toad);
    }

    private void setFormForSelectOperationComboBox() {
        selectOperationComboBox = new ComboBox();
        selectOperationComboBox.setAllowCustomValue(false);
        selectOperationComboBox.setEnabled(false);
        selectOperationComboBox.setPlaceholder("Available operations");
        selectOperationComboBox.addValueChangeListener(this::selectOperationComboBoxListener);
    }

    private void setFormForSelectClinicComboBox() {
        selectClinicComboBox = new ComboBox();
        selectClinicComboBox.setPlaceholder("Available clinics");
        selectClinicComboBox.setEnabled(false);
    }

    private void setFormForSelectAnimalComboBox() {
        selectAnimalComboBox = new ComboBox();
        selectAnimalComboBox.setPlaceholder("Select animal");
        selectAnimalComboBox.setItems(TypeOfAnimal.values());
    }

    private void setFormForSelectDateDataPicker() {
        selectDateDataPicker = new DatePicker();
        selectDateDataPicker.setLabel("Select date");
        selectDateDataPicker.setVisible(false);
    }

    private void setFormForSaveRegistrationButton() {
        saveRegistrationButton = new Button("Save");
        saveRegistrationButton.setEnabled(false);
    }

    private void setFormForDialog() {
        popUpDialog = new Dialog();
        popUpDialog.open();
        popUpDialog.setCloseOnOutsideClick(true);

        setFormForSelectOperationComboBox();
        setFormForSelectClinicComboBox();
        setFormForSelectAnimalComboBox();
        setFormForSelectDateDataPicker();

        selectAnimalComboBox.addValueChangeListener(animalEvent -> {
            if (selectAnimalComboBox.isEmpty()) {
                Notification.show("Select animal, then select clinic");
                selectClinicComboBox.setEnabled(false);
            } else {
                selectClinicComboBox.clear();
                selectClinicComboBox.setEnabled(true);
                String chosenAnimal = animalEvent.getValue().toString();
                final List<Clinic> list = client.getClinicsByAnimal(TypeOfAnimal.valueOf(chosenAnimal));

                final List<String> clinicList = list
                        .stream()
                        .map(x -> x.getId() + x.getName())
                        .collect(Collectors.toList());
                selectClinicComboBox.setItems(clinicList);

                selectClinicComboBox.addValueChangeListener(clinicIdEvent -> {
                    chosenClinicId = Long.parseLong(clinicIdEvent.getValue().toString().replaceAll("[\\D]", ""));
                });
                selectClinicComboBox.setEnabled(true);

                selectClinicComboBox.addValueChangeListener(clinicEvent -> {
                    if (selectClinicComboBox.isEmpty()) {
                        Notification.show("Select clinic!");
                        selectOperationComboBox.setEnabled(false);
                    } else {
                        selectOperationComboBox.setEnabled(true);
                        final List<Operation> list2 = client.getOperationsByClinicId(chosenClinicId);

                        final List<String> list3 = list2
                                .stream()
                                .map(x -> x.getId() + "." + x.getOperations())
                                .collect(Collectors.toList());

                        selectOperationComboBox.setItems(list3);
                        selectOperationComboBox.setEnabled(true);
                        selectOperationComboBox.addValueChangeListener(operationIdEvent -> {
                            chosenOperationId = Long.parseLong(operationIdEvent.getValue().toString().replaceAll("[\\D]", ""));
                        });

                        selectOperationComboBox.addValueChangeListener(event1 -> {
                            if (selectOperationComboBox.isEmpty() || selectClinicComboBox.isEmpty() || selectAnimalComboBox.isEmpty()) {
                                Notification.show("Select others!");
                            } else {
                                selectDateDataPicker.setVisible(true);
                                saveRegistrationButton.setEnabled(true);

                                selectDateDataPicker.addValueChangeListener(dateEvent -> {
                                    LocalDate date = dateEvent.getValue();

                                    saveRegistrationButton.addClickListener(eventButton -> {

                                        operationConnector.setCustomerId(customerId);
                                        operationConnector.setClinicId(chosenClinicId);
                                        operationConnector.setOperationActId(chosenOperationId);
                                        operationConnector.setDate(date);

                                        client.createOperationConnector(operationConnector);
                                        popUpDialog.close();
                                        Notification.show("Successfully added. Refresh the page");
                                    });
                                });
                            }
                        });
                    }
                });
            }
        });

        setFormForSaveRegistrationButton();
        HorizontalLayout horizontalLayout = new HorizontalLayout(selectDateDataPicker, saveRegistrationButton);
        VerticalLayout innerVerticalLayout = new VerticalLayout(selectAnimalComboBox, selectClinicComboBox, selectOperationComboBox, horizontalLayout);
        popUpDialog.add(innerVerticalLayout);
    }

    private void addButtonEvent(ClickEvent<Button> event) {
        setFormForDialog();
    }

    private void logOutButtonEvent(ClickEvent<Button> event) {
        redirectToHomePage();
    }

    private void redirectToHomePage() {
        UI.getCurrent().navigate("");
    }

    private String getCustomerFirstName() {
        Customer customer1 = client.getCustomerById(customerId);
        return customer1.getFirstname();
    }

    private String getCustomerLastName() {
        Customer customer1 = client.getCustomerById(customerId);
        return customer1.getLastname();
    }

    private String getCustomerPassword() {
        Customer customer1 = client.getCustomerById(customerId);
        return customer1.getPassword();
    }

    private String getCustomerEmail() {
        Customer customer1 = client.getCustomerById(customerId);
        return customer1.getEmail();
    }

    private String getCustomerPhoneNumber() {
        Customer customer1 = client.getCustomerById(customerId);
        return customer1.getPhoneNumber();
    }

    private String getCustomerLogin() {
        Customer customer1 = client.getCustomerById(customerId);
        return customer1.getLogin();
    }
}
