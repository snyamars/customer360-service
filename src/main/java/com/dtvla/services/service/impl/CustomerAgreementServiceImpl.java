package com.dtvla.services.service.impl;

import com.dtvla.services.service.CustomerAgreementService;
import com.dtvla.services.domain.CustomerAgreement;
import com.dtvla.services.repository.CustomerAgreementRepository;
import com.dtvla.services.service.dto.CustomerAgreementDTO;
import com.dtvla.services.service.mapper.CustomerAgreementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CustomerAgreement.
 */
@Service
@Transactional
public class CustomerAgreementServiceImpl implements CustomerAgreementService{

    private final Logger log = LoggerFactory.getLogger(CustomerAgreementServiceImpl.class);
    
    private final CustomerAgreementRepository customerAgreementRepository;

    private final CustomerAgreementMapper customerAgreementMapper;

    public CustomerAgreementServiceImpl(CustomerAgreementRepository customerAgreementRepository, CustomerAgreementMapper customerAgreementMapper) {
        this.customerAgreementRepository = customerAgreementRepository;
        this.customerAgreementMapper = customerAgreementMapper;
    }

    /**
     * Save a customerAgreement.
     *
     * @param customerAgreementDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CustomerAgreementDTO save(CustomerAgreementDTO customerAgreementDTO) {
        log.debug("Request to save CustomerAgreement : {}", customerAgreementDTO);
        CustomerAgreement customerAgreement = customerAgreementMapper.customerAgreementDTOToCustomerAgreement(customerAgreementDTO);
        customerAgreement = customerAgreementRepository.save(customerAgreement);
        CustomerAgreementDTO result = customerAgreementMapper.customerAgreementToCustomerAgreementDTO(customerAgreement);
        return result;
    }

    /**
     *  Get all the customerAgreements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerAgreementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerAgreements");
        Page<CustomerAgreement> result = customerAgreementRepository.findAll(pageable);
        return result.map(customerAgreement -> customerAgreementMapper.customerAgreementToCustomerAgreementDTO(customerAgreement));
    }

    /**
     *  Get one customerAgreement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerAgreementDTO findOne(Long id) {
        log.debug("Request to get CustomerAgreement : {}", id);
        CustomerAgreement customerAgreement = customerAgreementRepository.findOne(id);
        CustomerAgreementDTO customerAgreementDTO = customerAgreementMapper.customerAgreementToCustomerAgreementDTO(customerAgreement);
        return customerAgreementDTO;
    }

    /**
     *  Delete the  customerAgreement by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerAgreement : {}", id);
        customerAgreementRepository.delete(id);
    }
}
