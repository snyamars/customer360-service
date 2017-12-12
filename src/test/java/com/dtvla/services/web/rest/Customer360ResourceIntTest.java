package com.dtvla.services.web.rest;

import com.dtvla.services.Customer360ServiceApp;

import com.dtvla.services.domain.CustomerAgreement;
import com.dtvla.services.repository.CustomerAgreementRepository;
import com.dtvla.services.service.CustomerAgreementService;
import com.dtvla.services.service.dto.CustomerAgreementDTO;
import com.dtvla.services.service.mapper.CustomerAgreementMapper;
import com.dtvla.services.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the Customer360Resource REST controller.
 *
 * @see Customer360Resource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Customer360ServiceApp.class)
public class Customer360ResourceIntTest {

    private static final Integer DEFAULT_AGREEMENT_ID = 1;
    private static final Integer UPDATED_AGREEMENT_ID = 2;

    @Autowired
    private CustomerAgreementRepository customerAgreementRepository;

    @Autowired
    private CustomerAgreementMapper customerAgreementMapper;

    @Autowired
    private CustomerAgreementService customerAgreementService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomer360MockMvc;

    private CustomerAgreement customerAgreement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Customer360Resource customer360Resource = new Customer360Resource();
        this.restCustomer360MockMvc = MockMvcBuilders.standaloneSetup(customer360Resource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerAgreement createEntity(EntityManager em) {
        CustomerAgreement customerAgreement = new CustomerAgreement()
                .AgreementId(DEFAULT_AGREEMENT_ID);
        return customerAgreement;
    }

    @Before
    public void initTest() {
        customerAgreement = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerAgreement() throws Exception {
        int databaseSizeBeforeCreate = customerAgreementRepository.findAll().size();

        // Create the CustomerAgreement
        CustomerAgreementDTO customerAgreementDTO = customerAgreementMapper.customerAgreementToCustomerAgreementDTO(customerAgreement);

        restCustomer360MockMvc.perform(post("/api/customer-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAgreementDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomerAgreement in the database
        List<CustomerAgreement> customerAgreementList = customerAgreementRepository.findAll();
        assertThat(customerAgreementList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerAgreement testCustomerAgreement = customerAgreementList.get(customerAgreementList.size() - 1);
        assertThat(testCustomerAgreement.getAgreementId()).isEqualTo(DEFAULT_AGREEMENT_ID);
    }

    @Test
    @Transactional
    public void createCustomerAgreementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerAgreementRepository.findAll().size();

        // Create the CustomerAgreement with an existing ID
        CustomerAgreement existingCustomerAgreement = new CustomerAgreement();
        existingCustomerAgreement.setId(1L);
        CustomerAgreementDTO existingCustomerAgreementDTO = customerAgreementMapper.customerAgreementToCustomerAgreementDTO(existingCustomerAgreement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomer360MockMvc.perform(post("/api/customer-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCustomerAgreementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CustomerAgreement> customerAgreementList = customerAgreementRepository.findAll();
        assertThat(customerAgreementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCustomerAgreements() throws Exception {
        // Initialize the database
        customerAgreementRepository.saveAndFlush(customerAgreement);

        // Get all the customerAgreementList
        restCustomer360MockMvc.perform(get("/api/customer-agreements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAgreement.getId().intValue())))
            .andExpect(jsonPath("$.[*].AgreementId").value(hasItem(DEFAULT_AGREEMENT_ID)));
    }

    @Test
    @Transactional
    public void getCustomerAgreement() throws Exception {
        // Initialize the database
        customerAgreementRepository.saveAndFlush(customerAgreement);

        // Get the customerAgreement
        restCustomer360MockMvc.perform(get("/api/customer-agreements/{id}", customerAgreement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerAgreement.getId().intValue()))
            .andExpect(jsonPath("$.AgreementId").value(DEFAULT_AGREEMENT_ID));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerAgreement() throws Exception {
        // Get the customerAgreement
        restCustomer360MockMvc.perform(get("/api/customer-agreements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerAgreement() throws Exception {
        // Initialize the database
        customerAgreementRepository.saveAndFlush(customerAgreement);
        int databaseSizeBeforeUpdate = customerAgreementRepository.findAll().size();

        // Update the customerAgreement
        CustomerAgreement updatedCustomerAgreement = customerAgreementRepository.findOne(customerAgreement.getId());
        updatedCustomerAgreement
                .AgreementId(UPDATED_AGREEMENT_ID);
        CustomerAgreementDTO customerAgreementDTO = customerAgreementMapper.customerAgreementToCustomerAgreementDTO(updatedCustomerAgreement);

        restCustomer360MockMvc.perform(put("/api/customer-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAgreementDTO)))
            .andExpect(status().isOk());

        // Validate the CustomerAgreement in the database
        List<CustomerAgreement> customerAgreementList = customerAgreementRepository.findAll();
        assertThat(customerAgreementList).hasSize(databaseSizeBeforeUpdate);
        CustomerAgreement testCustomerAgreement = customerAgreementList.get(customerAgreementList.size() - 1);
        assertThat(testCustomerAgreement.getAgreementId()).isEqualTo(UPDATED_AGREEMENT_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerAgreement() throws Exception {
        int databaseSizeBeforeUpdate = customerAgreementRepository.findAll().size();

        // Create the CustomerAgreement
        CustomerAgreementDTO customerAgreementDTO = customerAgreementMapper.customerAgreementToCustomerAgreementDTO(customerAgreement);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomer360MockMvc.perform(put("/api/customer-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAgreementDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomerAgreement in the database
        List<CustomerAgreement> customerAgreementList = customerAgreementRepository.findAll();
        assertThat(customerAgreementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCustomerAgreement() throws Exception {
        // Initialize the database
        customerAgreementRepository.saveAndFlush(customerAgreement);
        int databaseSizeBeforeDelete = customerAgreementRepository.findAll().size();

        // Get the customerAgreement
        restCustomer360MockMvc.perform(delete("/api/customer-agreements/{id}", customerAgreement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerAgreement> customerAgreementList = customerAgreementRepository.findAll();
        assertThat(customerAgreementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerAgreement.class);
    }
}
