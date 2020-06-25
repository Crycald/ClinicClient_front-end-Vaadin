package com.client.frontend.registration.customer;

import com.client.frontend.api.Client;
import com.client.frontend.api.domain.Customer;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import static com.vaadin.flow.component.icon.VaadinIcon.ANGLE_DOUBLE_LEFT;

@Route("customerRegistrationPage")
@StyleSheet("/css/style.css")
@PageTitle("Customer Registration Page")
public class CustomerRegistrationPage extends Div {
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField loginField;
    private PasswordField passwordField;
    private EmailField emailField;
    private TextField phoneNumberField; //textfield cuz numberfield converts input to sth like 34324324230B.431
    private TextField acceptTextField;
    private Text contentText;
    private Button signInButton;
    private Button previousPageButton;
    private Button termsOfServicesButton;
    private Button acceptButton;
    private Dialog contentDialog;
    private Dialog popUpDialog;

    private Client client;
    private Customer customer;

    @Autowired
    public CustomerRegistrationPage(Client client) {
        this.client = client;
        init();
    }

    private void init() {
        setClassName("centered");
        setPopUpDialogForm();
        setFormForRegistrationForm();
    }

    private void signInButtonEvent(ClickEvent<Button> event) {
        if (firstNameField.getValue().isEmpty() || lastNameField.getValue().isEmpty() ||
                loginField.getValue().isEmpty() || passwordField.getValue().isEmpty() ||
                emailField.getValue().isEmpty() || phoneNumberField.getValue().isEmpty())
        {
            Notification.show("All fields have to be filled");
        } else {
            if (client.validateCustomerLogin(loginField.getValue()) == false) {
                customer = new Customer();
                customer.setFirstname(firstNameField.getValue());
                customer.setLastname(lastNameField.getValue());
                customer.setLogin(loginField.getValue());
                customer.setPassword(passwordField.getValue());
                customer.setEmail(emailField.getValue());
                customer.setPhoneNumber(phoneNumberField.getValue());

                client.createCustomer(customer);
                popUpDialog();
            } else {
                Notification.show("Customer with given login already exists!");
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

    private void setPopUpDialogForm() {
        popUpDialog = new Dialog();
        popUpDialog.add(new Label("Now you can log in"));
    }

    private void setFormForRegistrationForm() {
        setFormForFirstNameField();
        setFormFormLastNameField();
        setFormForLoginField();
        setFormForPasswordField();
        setFormForEmailField();
        setFormForPhoneNumberField();
        setFormForSignInButton();
        setFormForPreviousPageButton();
        setFormForTermsOfServicesButton();

        HorizontalLayout horizontalLayout = new HorizontalLayout(previousPageButton, signInButton);
        VerticalLayout verticalLayout = new VerticalLayout(firstNameField, lastNameField, loginField,
                passwordField, emailField, phoneNumberField, termsOfServicesButton, horizontalLayout);

        add(verticalLayout);
    }

    private void setFormForFirstNameField() {
        firstNameField = new TextField("");
        firstNameField.setPlaceholder("First name");
        firstNameField.setRequired(true);
    }

    private void setFormFormLastNameField() {
        lastNameField = new TextField("");
        lastNameField.setPlaceholder("Last name");
        lastNameField.setRequired(true);
    }

    private void setFormForLoginField() {
        loginField = new TextField("");
        loginField.setPlaceholder("Login");
        loginField.setRequired(true);
    }

    private void setFormForPasswordField() {
        passwordField = new PasswordField("");
        passwordField.setPlaceholder("Password");
        passwordField.setRequired(true);
    }

    private void setFormForEmailField() {
        emailField = new EmailField("");
        emailField.setPlaceholder("E-mail");
    }

    private void setFormForPhoneNumberField() {
        phoneNumberField = new TextField("");
        phoneNumberField.setPlaceholder("Phone number");
        phoneNumberField.setWidthFull();
    }

    private void setFormForSignInButton() {
        signInButton = new Button("Sign in");
        signInButton.addClickShortcut(Key.ENTER);
        signInButton.setEnabled(false);
    }

    private void setFormForPreviousPageButton() {
        previousPageButton = new Button();
        previousPageButton.addClassName("previous-page-button");
        previousPageButton.setIcon(new Icon(ANGLE_DOUBLE_LEFT));
        previousPageButton.addClickListener(event -> previousPage());
    }

    private void setFormForTermsOfServicesButton() {
        termsOfServicesButton = new Button();
        termsOfServicesButton.setText("Terms Of Services");
        termsOfServicesButton.addClickListener(this::termsOfServicesButtonEvent);
    }

    private void termsOfServicesButtonEvent(ClickEvent<Button> event) {
        openContentDialog();
    }

    private void openContentDialog() {
        setFormForContentDialog();

        contentDialog.open();
        contentDialog.setCloseOnOutsideClick(true);
    }

    private void setFormForContentDialog() {
        setFormForContentText();
        setFormForAcceptTextField();
        setFormForAcceptButton();

        contentDialog = new Dialog();
        contentDialog.setCloseOnOutsideClick(true);

        HorizontalLayout horizontalLayout = new HorizontalLayout(contentText);
        VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout);
        contentDialog.add(verticalLayout);
        contentDialog.add(acceptTextField);
        contentDialog.add(acceptButton);
    }

    private void setFormForAcceptTextField() {
        acceptTextField = new TextField();
        acceptTextField.setPlaceholder("I accept");
        acceptTextField.setLabel("More then captcha..");
    }

    private void setFormForAcceptButton() {
        acceptButton = new Button("Verify");
        acceptButton.addClickListener(this::acceptButtonEvent);
    }

    private void acceptButtonEvent(ClickEvent<Button> event) {
        if (acceptTextField.isEmpty()) {
            Notification.show("Accept terms!");
        } else {
            if (acceptTextField.getValue().contains("I accept")) {
                signInButton.addClickListener(this::signInButtonEvent);
                signInButton.setEnabled(true);
                contentDialog.close();
            } else {
                Notification.show("Don't take a shortcut");
            }
        }
    }

    private void setFormForContentText() {
        String loripsum = client.getLoripsum();
        contentText = new Text(loripsum);
    }
}
