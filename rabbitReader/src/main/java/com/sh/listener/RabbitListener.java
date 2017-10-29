package com.sh.listener;

/**
 * 
 * @author shoe011
 *
 * @param <T> - Message Type Bean
 */
@FunctionalInterface
public interface RabbitListener<T> {
	
	/**
	 * Method that receive the message from rabbit queue
	 * @param msg - generic type
	 */
	public void receiveMessage(T msg);
	
}
