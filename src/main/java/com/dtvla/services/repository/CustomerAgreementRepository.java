package com.dtvla.services.repository;

import com.dtvla.services.domain.CustomerAgreement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerAgreement entity.
 */
@SuppressWarnings("unused")
public interface CustomerAgreementRepository extends JpaRepository<CustomerAgreement,Long> {

}
