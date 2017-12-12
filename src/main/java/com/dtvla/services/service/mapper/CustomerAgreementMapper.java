package com.dtvla.services.service.mapper;

import com.dtvla.services.domain.*;
import com.dtvla.services.service.dto.CustomerAgreementDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CustomerAgreement and its DTO CustomerAgreementDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerAgreementMapper {

    CustomerAgreementDTO customerAgreementToCustomerAgreementDTO(CustomerAgreement customerAgreement);

    List<CustomerAgreementDTO> customerAgreementsToCustomerAgreementDTOs(List<CustomerAgreement> customerAgreements);

    CustomerAgreement customerAgreementDTOToCustomerAgreement(CustomerAgreementDTO customerAgreementDTO);

    List<CustomerAgreement> customerAgreementDTOsToCustomerAgreements(List<CustomerAgreementDTO> customerAgreementDTOs);
}
