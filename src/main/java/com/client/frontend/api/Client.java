package com.client.frontend.api;

import com.client.frontend.api.domain.Clinic;
import com.client.frontend.api.domain.Customer;
import com.client.frontend.api.domain.Operation;
import com.client.frontend.api.domain.OperationConnector;
import com.client.frontend.api.domain.enums.TypeOfAnimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class Client {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${clinic.api.endpoint.prod}")
    private String clientApi;

    public void createCustomer(Customer customer) {
        restTemplate.postForObject(clientApi + "/customers/" , (customer), Customer.class);
    }

    public Customer updateCustomer(Customer customer) {
        restTemplate.put(clientApi + "/customers/" , (customer), Customer.class);
        return customer;
    }

    public Customer getCustomerById(Long customerId) {
        Customer customer = restTemplate.getForObject(clientApi + "/customers/" + customerId, Customer.class);
        return customer;
    }

    public Long validateCustomer(String login, String password) {
        Long result = restTemplate.getForObject(clientApi + "/customers/validate/" + login + "&" + password, Long.class);
        return result;
    }

    public Boolean validateCustomerLogin(String customerLogin) {
        Boolean result = restTemplate.getForObject(clientApi + "/customers/validateLogin/" + customerLogin, Boolean.class);
        return result;
    }

    public void deleteCustomerById(Long customerId) {
        restTemplate.delete(clientApi + "/customers/" + customerId, Customer.class);
    }

    public void createClinic(Clinic clinic) {
        restTemplate.postForObject(clientApi + "/clinics/", (clinic), Clinic.class);
    }

    public Clinic updateClinic(Clinic clinic) {
        restTemplate.put(clientApi + "/clinics/" , (clinic), Clinic.class);
        return clinic;
    }

    public Long validateClinic(String login, String password) {
        Long result = restTemplate.getForObject(clientApi + "/clinics/validate/" + login + "&" + password, Long.class);
        return result;
    }

    public Boolean validateClinicLogin(String clinicLogin) {
        Boolean result = restTemplate.getForObject(clientApi + "/clinics/validateLogin/" + clinicLogin, Boolean.class);
        return result;
    }

    public Clinic getClinicById(Long clinicId) {
        Clinic clinic = restTemplate.getForObject(clientApi + "/clinics/" + clinicId, Clinic.class);
        return clinic;
    }

    public void deleteClinicById(Long clinicId) {
        restTemplate.delete(clientApi + "/clinics/" + clinicId, Clinic.class);
    }

    public List<Clinic> getClinicsByAnimal(TypeOfAnimal typeOfAnimal) {
        Clinic[] clinics = restTemplate.getForObject(clientApi + "/clinics/animal/" + typeOfAnimal, Clinic[].class);
        return Arrays.asList(clinics);
    }

    public Operation createOperation(Operation operation) {
        restTemplate.postForObject(clientApi + "/operations", (operation), Operation.class);
        return operation;
    }

    public Operation updateOperation(Operation operation) {
        restTemplate.put(clientApi + "/operations/" , (operation), Operation.class);
        return operation;
    }

    public void deleteOperationById(Long operationId) {
        restTemplate.delete(clientApi + "/operations/" + operationId, Operation.class);
    }

    public List<Operation> getOperationsByClinicId(Long id) {
        Operation[] operations = restTemplate.getForObject(clientApi + "/operations/getByClinicId/" + id, Operation[].class);
        return Arrays.asList(operations);
    }

    public OperationConnector createOperationConnector(OperationConnector operationConnector) {
        restTemplate.postForObject(clientApi + "/operationLists", (operationConnector), OperationConnector.class);
        return operationConnector;
    }

    public List<OperationConnector> getList() {
        OperationConnector[] operationConnectors = restTemplate.getForObject(clientApi + "/operationLists", OperationConnector[].class);
        return Arrays.asList(operationConnectors);
    }

    public List<OperationConnector> getListsByCustomerId(Long customerId) {
        OperationConnector[] operationConnectors = restTemplate.getForObject(clientApi + "/operationLists/customerId/" + customerId, OperationConnector[].class);
        return Arrays.asList(operationConnectors);
    }

    public List<OperationConnector> getListsByClinicId(Long clinicId) {
        OperationConnector[] operationConnectors = restTemplate.getForObject(clientApi + "/operationLists/clinicId/" + clinicId, OperationConnector[].class);
        return Arrays.asList(operationConnectors);
    }

    public List<Operation> getListOfOperation() {
        Operation[] operations = restTemplate.getForObject(clientApi + "/operations", Operation[].class);
        return Arrays.asList(operations);
    }
}
