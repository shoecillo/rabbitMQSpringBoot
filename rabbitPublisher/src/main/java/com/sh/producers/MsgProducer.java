package com.sh.producers;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sh.messages.CustomerMsg;
import com.sh.messages.ShopMsg;

/**
 * 
 * @author shoe011
 *
 */
@Component
public class MsgProducer {

	@Autowired
	@Qualifier("rabbitTemplateCustomer")
	private RabbitTemplate rabbitCustomer;
	
	@Autowired
	@Qualifier("rabbitTemplateShop")
	private RabbitTemplate rabbitShop;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MsgProducer.class);
	
	/**
	 * Convert and send CustomerMsg object to Rabbit 
	 * @param msg - CustomerMsg
	 * @see CustomerMsg
	 */
	public void sendCustomerMsg(CustomerMsg msg)
	{
		try {
			LOGGER.debug("<<<<<< SENDING MESSAGE");
			rabbitCustomer.convertAndSend(msg);
			LOGGER.debug(MessageFormat.format("MESSAGE SENT TO {0} >>>>>>", rabbitCustomer.getRoutingKey()));
		
		} catch (AmqpException e) {
			LOGGER.error("Error sending Customer: ",e);
		}
	}
	
	/**
	 * Convert and send ShopMsg object to Rabbit 
	 * @param msg - ShopMsg
	 * @see ShopMsg
	 */
	public void sendShopMsg(ShopMsg msg)
	{
		try {
			LOGGER.debug("<<<<< SENDING MESSAGE");
			rabbitShop.convertAndSend(msg);
			LOGGER.debug(MessageFormat.format("MESSAGE SENT TO {0} >>>>>>", rabbitShop.getRoutingKey()));
		} catch (AmqpException e) {
			LOGGER.error("Error sending Shop: ",e);
		}
	}
	
	/**
	 * Get info from QUEUES (Only development) in Jackson JSON object
	 * @return ObjectNode
	 */
	public ObjectNode info()
	{
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ObjectNode root = factory.objectNode();
		root.put("host", rabbitCustomer.getConnectionFactory().getHost());
		root.put("port", rabbitCustomer.getConnectionFactory().getPort());
		root.put("Customer UUID", rabbitCustomer.getUUID());
		root.put("Shop UUID", rabbitShop.getUUID());
		root.put("queueCustomer", rabbitCustomer.getRoutingKey());
		root.put("queueShop", rabbitShop.getRoutingKey());
				
		return root;
	}
}
