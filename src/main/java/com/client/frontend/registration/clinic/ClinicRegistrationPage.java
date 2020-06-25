package com.client.frontend.registration.clinic;

import com.client.frontend.api.Client;
import com.client.frontend.api.domain.Clinic;
import com.client.frontend.api.domain.enums.TypeOfAnimal;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
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

import static com.vaadin.flow.component.icon.VaadinIcon.ANGLE_DOUBLE_LEFT;

@Route("clinicRegistrationPage")
@StyleSheet("/css/style.css")
@PageTitle("Clinic Registration Page")
public class ClinicRegistrationPage extends Div {
    private TextField companyNameField;
    private ComboBox <TypeOfAnimal> specializationField;
    private TextField addressField;
    private NumberField nipField;
    private TextField phoneNumberField;
    private EmailField emailField;
    private TextField loginField;
    private PasswordField passwordField;
    private Button signInButton;
    private Button previousPageButton;
    private Dialog popUpDialog;

    private Client client;
    private Clinic clinic;


    @Autowired
    public ClinicRegistrationPage(Client client) {
        this.client = client;
        init();
    }

    public void signInButtonEvent(ClickEvent<Button> event) {
        if (companyNameField.getValue().isEmpty() || specializationField.getValue() == null ||
                addressField.getValue().isEmpty() || nipField.getValue() == null ||
                phoneNumberField.getValue().isEmpty() || emailField.getValue().isEmpty() ||
                loginField.getValue().isEmpty() || passwordField.getValue().isEmpty())
        {
            Notification.show("All fields have to be filled");
        } else {
            if (client.validateClinicLogin(loginField.getValue()) == false) {
                clinic = new Clinic();
                clinic.setName(companyNameField.getValue());
                clinic.setTypeOfAnimal(specializationField.getValue());
                clinic.setAddress(addressField.getValue());
                clinic.setNip(Double.doubleToLongBits(nipField.getValue()));
                clinic.setPhoneNumber(phoneNumberField.getValue());
                clinic.setMail(emailField.getValue());
                clinic.setLogin(loginField.getValue());
                clinic.setPassword(passwordField.getValue());

                client.createClinic(clinic);
                popUpDialog();
            } else {
                Notification.show("Clinic with given login already exists!");
            }
        }
    }

    private void popUpDialog() {
        previousPage();
        popUpDialog.open();
        popUpDialog.setCloseOnOutsideClick(true);
    }

    private void previousPage() {
        UI.getCurrent().navigate("");
    }

    private void init() {
        setClassName("centered");

        setFormForPopUpDialog();
        setFormForCompanyNameField();
        setFormForSpecializationField();
        setFormForAddressField();
        setFormForNipField();
        setFormForPhoneNumberField();
        setFormForEmailField();
        setFormForLoginField();
        setFormForPasswordField();
        setFormForSignInButton();
        setFormFormPreviousPageButton();

        HorizontalLayout horizontalLayout = new HorizontalLayout(previousPageButton, signInButton);

        VerticalLayout verticalLayout = new VerticalLayout(companyNameField, specializationField, loginField, passwordField,
                addressField, nipField, phoneNumberField, emailField, phoneNumberField, horizontalLayout);

        add(verticalLayout);
    }

    private void setFormForPopUpDialog() {
        popUpDialog = new Dialog();
        popUpDialog.add(new Label("Now you can log in"));
    }

    private void setFormForCompanyNameField() {
        companyNameField = new TextField("");
        companyNameField.setPlaceholder("Company name");
        companyNameField.setRequired(true);
    }

    private void setFormForSpecializationField() {
        specializationField = new ComboBox("");
        specializationField.setPlaceholder("Specialization");
        specializationField.setRequired(true);
        specializationField.setItems(TypeOfAnimal.values());
    }

    private void setFormForAddressField() {
        addressField = new TextField("");
        addressField.setPlaceholder("Address");
        addressField.setRequired(true);
    }

    private void setFormForNipField() {
        nipField = new NumberField("");
        nipField.setPlaceholder("NIP");
        nipField.setWidthFull();
    }

    private void setFormForPhoneNumberField() {
        phoneNumberField = new TextField("");
        phoneNumberField.setPlaceholder("Phone number");
        phoneNumberField.setRequired(true);
    }

    private void setFormForEmailField() {
        emailField = new EmailField("");
        emailField.setPlaceholder("E-mail");
    }

    private void setFormForLoginField() {
        loginField = new TextField("");
        loginField.setPlaceholder("Login");
        loginField.setRequired(true);
    }

    private void setFormForPasswordField() {
        passwordField = new PasswordField("");
        passwordField.setPlaceholder("Password");
        phoneNumberField.setRequired(true);
    }

    private void setFormForSignInButton() {
        signInButton = new Button("Sign in");
        signInButton.addClickListener(this::signInButtonEvent);
        signInButton.addClickShortcut(Key.ENTER);
    }

    private void setFormFormPreviousPageButton() {
        previousPageButton = new Button();
        previousPageButton.addClassName("previous-page-button");
        previousPageButton.setIcon(new Icon(ANGLE_DOUBLE_LEFT));
        previousPageButton.addClickListener(event -> previousPage());
    }
}
