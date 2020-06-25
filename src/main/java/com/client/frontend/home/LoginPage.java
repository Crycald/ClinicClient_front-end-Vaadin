package com.client.frontend.home;

import com.client.frontend.api.Client;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


@Route("")
@StyleSheet("/css/style.css")
@PageTitle("HOME")
public class LoginPage extends Div {
    private TextField loginField;
    private PasswordField pwdField;
    private Button logInButton;
    private Anchor registrationLinkCustomer;
    private Anchor registrationLinkClinic;
    private Dialog notFoundDialog;
    private Image img;
    public static Long customerId;
    public static Long clinicId;

    private Client client;

    @Autowired
    public LoginPage(Client client) {
        this.client = client;
        init();
    }

    private void logInButtonEvent(ClickEvent<Button> event) {
        validateUser();
    }

    private void init() {
        setFormForLogin();
        setFormForDialog();
    }

    private void redirectToCustomerPage() {
        UI.getCurrent().navigate("customerPage");
    }

    private void redirectToClinicPage() {
        UI.getCurrent().navigate("clinicPage");
    }

    private void setFormForDialog() {
        notFoundDialog = new Dialog();
        notFoundDialog.add(new Label("login or password is wrong please try again"));
    }

    private void setFormForLogin() {
        setClassName("centered");
        setFormForImage();
        setFormForInputs();
        setFormForLogInButton();
        setFormForLinks();

        HorizontalLayout innerHorizontalLayout = new HorizontalLayout(registrationLinkCustomer,registrationLinkClinic);
        VerticalLayout verticalLayout = new VerticalLayout(img, loginField, pwdField, innerHorizontalLayout,logInButton);

        add(verticalLayout);
    }

    private void setFormForImage() {
        img = new Image("https://media.giphy.com/media/kuWN0iF9BLQKk/giphy.gif", "pikachu.gif");
        img.setMaxWidth("200px");
        img.setMaxHeight("200px");
    }

    private void setFormForInputs() {
        loginField = new TextField();
        loginField.setPlaceholder("Login");

        pwdField = new PasswordField();
        pwdField.setPlaceholder("Password");
    }

    private void setFormForLogInButton() {
        logInButton = new Button();
        logInButton.setText("Log in");
        logInButton.addClickShortcut(Key.ENTER);
        logInButton.addClickListener(this::logInButtonEvent);
    }

    private void setFormForLinks() {
        registrationLinkCustomer = new Anchor();
        registrationLinkCustomer.setHref("customerRegistrationPage");
        registrationLinkCustomer.add("REG_Customer");
        registrationLinkCustomer.addClassName("links");

        registrationLinkClinic = new Anchor();
        registrationLinkClinic.setHref("clinicRegistrationPage");
        registrationLinkClinic.add("REG_Clinic");
        registrationLinkClinic.addClassName("links");
    }

    private void validateUser() {
        customerId = client.validateCustomer(loginField.getValue(), pwdField.getValue());
        clinicId = client.validateClinic(loginField.getValue(), pwdField.getValue());
        if (customerId != null) {
            redirectToCustomerPage();
        } else if (clinicId != null) {
            redirectToClinicPage();
        } else {
            notFoundDialog.open();
        }
    }

    public static Long getCustomerId() {
        return customerId;
    }

    public static Long getClinicId() {
        return clinicId;
    }
}
