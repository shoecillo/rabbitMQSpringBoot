package com.sh.listener;

public interface RabbitListener<T> {

	public void receiveMessage(T msg);
	
}
