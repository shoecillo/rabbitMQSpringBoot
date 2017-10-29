package com.sh.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sh.messages.CustomerMsg;
import com.sh.messages.ShopMsg;
import com.sh.producers.MsgProducer;

/**
 * 
 * @author shoe011
 *
 */
@RestController
public class RabbitController {
	
	/**
	 * @see MsgProducer
	 */
	@Autowired
	private MsgProducer producer;
	
	/**
	 * GET Method for send a test message for customer
	 */
	@RequestMapping(value="/sendMsgCustomer.rest",method=RequestMethod.GET)
	public void sendMsgCustomer()
	{
		CustomerMsg msg = new CustomerMsg("Shoe011", "Programmer", "Cool");
		producer.sendCustomerMsg(msg);
	}
	
	/**
	 * GET Method for send a test message for shop
	 */
	@RequestMapping(value="/sendMsgShop.rest",method=RequestMethod.GET)
	public void sendMsgShop()
	{
		ShopMsg msg = new ShopMsg("Games Workshop", "Edimburgh", 20);
		producer.sendShopMsg(msg);
	}
	
	/**
	 * GET Method for send a test message for customer
	 */
	@RequestMapping(value="/sendMsgCustomer.rest",method=RequestMethod.POST)
	public void sendMsgCustomer(@RequestBody CustomerMsg msg)
	{
		producer.sendCustomerMsg(msg);
	}
	
	/**
	 * GET Method for send a test message for shop
	 */
	@RequestMapping(value="/sendMsgShop.rest",method=RequestMethod.POST)
	public void sendMsgShop(@RequestBody ShopMsg msg)
	{
		producer.sendShopMsg(msg);
	}
	
	/**
	 * Information about Rabbit config
	 * @return String - Json Info
	 */
	@RequestMapping(value="/info",method=RequestMethod.GET,produces="application/json")
	public String info()
	{
		ObjectNode root = producer.info();
		
		return root.toString();
	}
	
}
