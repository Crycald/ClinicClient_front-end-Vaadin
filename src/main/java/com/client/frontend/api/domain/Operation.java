package com.client.frontend.api.domain;

import com.client.frontend.api.domain.enums.TypeOfOperation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class Operation {
    private Long id;
    private Long clinicId;
    private TypeOfOperation operations;
    private BigDecimal cost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeOfOperation getOperations() {
        return operations;
    }

    public void setOperations(TypeOfOperation operations) {
        this.operations = operations;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Long getClinicId() {
        return clinicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", clinicId=" + clinicId +
                ", operations=" + operations +
                ", cost=" + cost +
                '}';
    }
}
