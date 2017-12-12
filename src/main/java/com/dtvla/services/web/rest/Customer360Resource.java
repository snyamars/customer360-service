package com.dtvla.services.web.rest;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.codahale.metrics.annotation.Timed;
import com.jayway.jsonpath.JsonPath;

/**
 * REST controller for managing Customer360.
 */
@RestController
@RequestMapping("/api/customer360")
public class Customer360Resource {

    private final Logger log = LoggerFactory.getLogger(Customer360Resource.class);

    //private static final String ENTITY_NAME = "customerAgreement";
        
    //private final CustomerAgreementService customerAgreementService;
    
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    
	
	@Autowired
    RestTemplate restTemplate;

    public Customer360Resource() {
        
    }
	
	@Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

	
	@GetMapping("/agreement/{key}/{value:.+}")
    @Timed
    public List<Object> customerAgreement(@PathVariable String key, @PathVariable String value, @RequestHeader(value="Authorization") String authorization) {
        
    	log.debug("REST request to get customerlookup : {}", key);
		log.debug("REST request to get customerlookup : {}", value);
		
		if(!(StringUtils.hasText(key) && (key.equals(KEY_EMAIL) || key.equals(KEY_PHONE)))){
			return Arrays.asList("Key is invalid. Key should be either email or phone");
		}
		
		/*final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", authorization);
		final HttpEntity<String> request = new HttpEntity<String>(headers);*/
		
		//customer lookup from email/phone number
		String customerLookup = null;
		customerLookup = restTemplate.exchange("http://customer-data-service/api/customerlookup/{key}/{value}",                                       
		HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, key, value).getBody();
		
		log.debug("Customer lookup", customerLookup);
		List<String> customerIdList = JsonPath.read(customerLookup, "$..customerId");
		String customerId =  null;
		
		for(String custId : customerIdList){
			customerId = custId; 
		}
		
		//log.debug("Customer lookup", customerId);
		
		log.debug("customer id from customerlookup: {}", customerId);
		
		//pass customerId to get customer360
		String customer360 = restTemplate.exchange("http://customer-data-service/api/customer360?token=1234&i_customer={customerId}&i_requestId=ProdTest&i_systemId=K2V&i_country=AR&format=json&i_sync_on=0",                                       
		HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, customerId).getBody();
		
		//log.debug("After data service call"+customer360);
		
		List<Object> customerAgreement = JsonPath.read(customer360, "$..Agreement.results");
		
		//log.debug("After json parsing "+ customerAgreement);
		
        return customerAgreement;
    }
	
	@GetMapping("/account/{key}/{value:.+}")
    @Timed
    public List<Object> customerAccount(@PathVariable String key, @PathVariable String value, @RequestHeader(value="Authorization") String authorization) {
        
    	log.debug("REST request to get customerlookup : {}", key);
		log.debug("REST request to get customerlookup : {}", value);
		
		if(!(StringUtils.hasText(key) && (key.equals(KEY_EMAIL) || key.equals(KEY_PHONE)))){
			return Arrays.asList("Key is invalid. Key should be either email or phone");
		}
		
		
		/*final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", authorization);
		final HttpEntity<String> request = new HttpEntity<String>(headers);*/
		
		//customer lookup from email/phone number
		String customerLookup = restTemplate.exchange("http://customer-data-service/api/customerlookup/{key}/{value}",                                       
		HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, key, value).getBody();
		
		
		List<String> customerIdList = JsonPath.read(customerLookup, "$..customerId");
		String customerId =  null;
		
		for(String custId : customerIdList){
			customerId = custId; 
		}
		
		log.debug("customer id from customerlookup: {}", customerId);
		
		//pass customerId to get customer360
		String customer360 = restTemplate.exchange("http://customer-data-service/api/customer360?token=1234&i_customer={customerId}&i_requestId=ProdTest&i_systemId=K2V&i_country=AR&format=json&i_sync_on=0",                                       
		HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, customerId).getBody();
		
		List<Object> customerAccount = JsonPath.read(customer360, "$..Account.results");
		
		
        return customerAccount;
    }
	
	@GetMapping("/profile/{key}/{value:.+}")
    @Timed
    public String customerProfile(@PathVariable String key, @PathVariable String value, @RequestHeader(value="Authorization") String authorization) {
        
    	log.debug("REST request to get customerlookup : {}", key);
		log.debug("REST request to get customerlookup : {}", value);
		
		if(!(StringUtils.hasText(key) && (key.equals(KEY_EMAIL) || key.equals(KEY_PHONE)))){
			return "Key is invalid. Key should be either email or phone";
		}
		
		/*final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", authorization);
		final HttpEntity<String> request = new HttpEntity<String>(headers);*/
		
		//customer lookup from email/phone number
		String customerProfile = restTemplate.exchange("http://customer-data-service/api/customerlookup/{key}/{value}",                                       
		HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, key, value).getBody();
		
		
		/*String customerId = JsonPath.read(customerLookup, "$..customerId");
		
		log.debug("customer id from customerlookup: {}", customerId);
		
		//pass customerId to get customer360
		String customer360 = restTemplate.exchange("http://customer-data-service/api/customer360?token=1234&i_customer={customerId}&i_requestId=ProdTest&i_systemId=K2V&i_country=AR&format=json&i_sync_on=0",                                       
		HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, customerId).getBody();
		
		Object customerAgreement = JsonPath.read(customer360, "$..Agreement.results");*/
		
		
        return customerProfile;
    }


	@GetMapping("/{customerId}/agreement")
    @Timed
    public List<Object> customerAgreement(@PathVariable String customerId, @RequestHeader(value="Authorization") String authorization) {
        
    	log.debug("REST request to get customerlookup : {}", customerId);
		
		
		//pass customerId to get customer360
		String customer360 = restTemplate.exchange("http://customer-data-service/api/customer360?token=1234&i_customer={customerId}&i_requestId=ProdTest&i_systemId=K2V&i_country=AR&format=json&i_sync_on=0",                                       
		HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, customerId).getBody();
		
		List<Object> customerAgreement = JsonPath.read(customer360, "$..Agreement.results");
		
		
        return customerAgreement;
    }
	
	@GetMapping("/{customerId}/account")
    @Timed
    public List<Object> customerAccount(@PathVariable String customerId, @RequestHeader(value="Authorization") String authorization) {
        
    	log.debug("REST request to get customerlookup : {}", customerId);
		
		
		//pass customerId to get customer360
		String customer360 = restTemplate.exchange("http://customer-data-service/api/customer360?token=1234&i_customer={customerId}&i_requestId=ProdTest&i_systemId=K2V&i_country=AR&format=json&i_sync_on=0",                                       
		HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, customerId).getBody();
		
		List<Object> customerAccount = JsonPath.read(customer360, "$..Account.results");
		
		
        return customerAccount;
    }
	
	@GetMapping("/{customerId}/profile")
    @Timed
    public String customerProfile(@PathVariable String customerId, @RequestHeader(value="Authorization") String authorization) {
        
    	log.debug("REST request to get customerlookup : {}", customerId);
		
		
		
		/*final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", authorization);
		final HttpEntity<String> request = new HttpEntity<String>(headers);*/
		
		//customer lookup from email/phone number
		String customerProfile = restTemplate.exchange("http://customer-data-service/api/customerlookup/{customerId}/profile",                                       
		HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, customerId).getBody();
		
		
		
		
		
        return customerProfile;
    }


}
