package com.dtvla.services.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CustomerAgreement.
 */
@Entity
@Table(name = "CustomerAgreement")
public class CustomerAgreement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agreement_id")
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

    public CustomerAgreement AgreementId(Integer AgreementId) {
        this.AgreementId = AgreementId;
        return this;
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
        CustomerAgreement customerAgreement = (CustomerAgreement) o;
        if (customerAgreement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, customerAgreement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerAgreement{" +
            "id=" + id +
            ", AgreementId='" + AgreementId + "'" +
            '}';
    }
}
