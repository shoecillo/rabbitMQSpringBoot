package com.sh.producers;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sh.messages.CustomerMsg;
import com.sh.messages.ShopMsg;


@Component
public class MsgProducer {

	@Autowired
	@Qualifier("rabbitTemplateCustomer")
	private RabbitTemplate rabbitCustomer;
	
	@Autowired
	@Qualifier("rabbitTemplateShop")
	private RabbitTemplate rabbitShop;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MsgProducer.class);
	
	public void sendCustomerMsg(CustomerMsg msg)
	{
		LOGGER.debug("SENDING MESSAGE >>>>>>");
		rabbitCustomer.convertAndSend(msg);
		LOGGER.debug(MessageFormat.format("MESSAGE SENDED TO {0} >>>>>>", rabbitCustomer.getRoutingKey()));
	}
	
	public void sendShopMsg(ShopMsg msg)
	{
		LOGGER.debug("SENDING MESSAGE >>>>>>");
		rabbitShop.convertAndSend(msg);
		LOGGER.debug(MessageFormat.format("MESSAGE SENDED TO {0} >>>>>>", rabbitShop.getRoutingKey()));
	}
	
}
