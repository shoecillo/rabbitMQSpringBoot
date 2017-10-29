package com.sh.listener.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.listener.RabbitListener;
import com.sh.messages.CustomerMsg;

/**
 * 
 * @author shoe011
 *
 */
@Component
public class ListenerCustomer implements RabbitListener<CustomerMsg>{

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerCustomer.class);
    
    @Override
    public void receiveMessage(CustomerMsg message) {
    	
    	try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
			
			LOGGER.debug("Receive Nessage: \n"+json);
			
		} catch (JsonProcessingException e) {
			LOGGER.error("Error: ", e);
		}
        
    }

    

}