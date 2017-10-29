package com.sh.events;

import org.springframework.context.ApplicationEvent;

import com.sh.messages.CustomerMsg;

@SuppressWarnings("serial")
public class CustomerEvent extends ApplicationEvent{
	
	private CustomerMsg msg;

	public CustomerEvent(Object source,CustomerMsg msg) {
		super(source);
		this.msg = msg;
	}

	public CustomerMsg getMsg() {
		return msg;
	}
	
	

}
