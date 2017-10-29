package com.sh.app;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.sh.listener.impl.ListenerCustomer;
import com.sh.listener.impl.ListenerShop;


/**
 * 
 * @author shoe011
 *
 */
@ComponentScan("com.sh")
@SpringBootApplication
public class AppRabbitReader {
	
	
	@Value("${queue.customer.name}")
	private String qCustomer;
	
	@Value("${queue.shop.name}")
	private String qShop;
	
	@Value("${broker.host}")
	private String brokerUrl;
	
	@Value("${topic.exchange.name}")
	private String topicName;
	
	@Value("${rabbit.user}")
	private String user;
	
	@Value("${rabbit.pwd}")
	private String pwd;
	
	private static final String LISTENER_METHOD = "receiveMessage";
	
	public static void main(String[] args)
	{
		SpringApplication.run(AppRabbitReader.class, args);
	}
	
	/**
	 * 
	 * @return Queue
	 */
	@Bean(name ="queueCustomer")
	Queue queueCustomer() {
		return new Queue(qCustomer, true);
	}
	
	/**
	 * 
	 * @return TopicExchange
	 */
	@Bean(name="exchangeCustomer")
	TopicExchange exchangeCustomer() {
		return new TopicExchange(topicName);
	}
	/**
	 * 
	 * @param queueCustomer
	 * @param exchangeCustomer
	 * @return Binding
	 */
	@Bean(name="bindingCustomer")
	Binding bindingCustomer(Queue queueCustomer, TopicExchange exchangeCustomer) {
		return BindingBuilder.bind(queueCustomer).to(exchangeCustomer).with(qCustomer);
	}
	/**
	 * 
	 * @return Queue
	 */
	@Bean(name="queueShop")
	Queue queueShop() {
		return new Queue(qShop, true);
	}
	
	/**
	 * 
	 * @return TopicExchange
	 */
	@Bean(name="exchangeShop")
	TopicExchange exchangeShop() {
		return new TopicExchange(topicName);
	}
	
	/**
	 * 
	 * @param queueShop
	 * @param exchangeShop
	 * @return Binding
	 */
	@Bean(name="bindingShop")
	Binding bindingShop(Queue queueShop, TopicExchange exchangeShop) {
		return BindingBuilder.bind(queueShop).to(exchangeShop).with(qShop);
	}
	
	/**
	 * ConnectionFactory configuration for read
	 * @return ConnectionFactory
	 * @see 
	 * {@link org.springframework.amqp.rabbit.connection.ConnectionFactory}
	 * {@link org.springframework.amqp.rabbit.connection.CachingConnectionFactory.CachingConnectionFactory}
	 */
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(brokerUrl);

		connectionFactory.setUsername(user);
		connectionFactory.setPassword(pwd);
		
		return connectionFactory;
	}
	
	/**
	 * JSON message converter
	 * @return MessageConverter
	 */
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	/**
	 * Listener container for Customers
	 * @param connectionFactory
	 * @param listenerAdapterCustomer
	 * @return SimpleMessageListenerContainer
	 */
	@Bean(name="containerCustomer")
	SimpleMessageListenerContainer containerCustomer(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapterCustomer) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageConverter(jsonMessageConverter());
		container.setQueueNames(qCustomer);
		container.setMessageListener(listenerAdapterCustomer);
		return container;
	}

	/**
	 * Configuration of listener adapter for Customers
	 * @param receiver
	 * @return MessageListenerAdapter
	 */
	@Bean(name="listenerAdapterCustomer")
	public MessageListenerAdapter listenerAdapterCustomer(ListenerCustomer receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}
	/**
	 * Listener container for Shops
	 * @param connectionFactory
	 * @param listenerAdapterShop
	 * @return
	 */
	@Bean(name="containerShop")
	SimpleMessageListenerContainer containerShop(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapterShop) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageConverter(jsonMessageConverter());
		container.setQueueNames(qShop);
		container.setMessageListener(listenerAdapterShop);
		return container;
	}
	
	/**
	 * Configuration of listener adapter for Shops
	 * @param receiver
	 * @return MessageListenerAdapter
	 */
	@Bean(name="listenerAdapterShop")
	public MessageListenerAdapter listenerAdapterShop(ListenerShop receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}
	
}
