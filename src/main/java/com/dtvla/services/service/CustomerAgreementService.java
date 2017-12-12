package com.dtvla.services.service;

import com.dtvla.services.service.dto.CustomerAgreementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing CustomerAgreement.
 */
public interface CustomerAgreementService {

    /**
     * Save a customerAgreement.
     *
     * @param customerAgreementDTO the entity to save
     * @return the persisted entity
     */
    CustomerAgreementDTO save(CustomerAgreementDTO customerAgreementDTO);

    /**
     *  Get all the customerAgreements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CustomerAgreementDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" customerAgreement.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CustomerAgreementDTO findOne(Long id);

    /**
     *  Delete the "id" customerAgreement.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
