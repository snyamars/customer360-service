package com.dtvla.services.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CustomerAgreement entity.
 */
public class CustomerAgreementDTO implements Serializable {

    private Long id;

    private Integer AgreementId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getAgreementId() {
        return AgreementId;
    }

    public void setAgreementId(Integer AgreementId) {
        this.AgreementId = AgreementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerAgreementDTO customerAgreementDTO = (CustomerAgreementDTO) o;

        if ( ! Objects.equals(id, customerAgreementDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerAgreementDTO{" +
            "id=" + id +
            ", AgreementId='" + AgreementId + "'" +
            '}';
    }
}
