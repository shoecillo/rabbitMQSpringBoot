package com.sh.listener.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.listener.RabbitListener;
import com.sh.messages.ShopMsg;

@Component
public class ListenerShop implements RabbitListener<ShopMsg> {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(ListenerShop.class);

	@Override
	public void receiveMessage(ShopMsg msg) {
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(msg);
			
			LOGGER.debug("Receive Nessage: \n"+json);
			
		} catch (JsonProcessingException e) {
			LOGGER.error("Error: ", e);
		}
	}

}
