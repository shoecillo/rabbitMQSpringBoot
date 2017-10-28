package com.sh.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sh.messages.CustomerMsg;
import com.sh.messages.ShopMsg;
import com.sh.producers.MsgProducer;

@RestController
public class RabbitController {

	@Autowired
	private MsgProducer producer;
	
	
	@RequestMapping("/sendMsgCustomer.rest")
	public void sendMsgCustomer()
	{
		CustomerMsg msg = new CustomerMsg("Shoe011", "Programmer", "Cool");
		producer.sendCustomerMsg(msg);
	}
	
	@RequestMapping("/sendMsgShop.rest")
	public void sendMsgShop()
	{
		ShopMsg msg = new ShopMsg("Games Workshop", "Edimburgh", 20);
		producer.sendShopMsg(msg);
	}
	
}
