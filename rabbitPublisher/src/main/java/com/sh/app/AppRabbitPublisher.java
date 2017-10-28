package com.sh.app;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("com.sh")
@Configuration
@SpringBootApplication
public class AppRabbitPublisher {

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

	public static void main(String[] args) {

		SpringApplication.run(AppRabbitPublisher.class, args);
	}

	@Bean(name ="queueCustomer")
	Queue queueCustomer() {
		return new Queue(qCustomer, true);
	}

	@Bean(name="exchangeCustomer")
	TopicExchange exchangeCustomer() {
		return new TopicExchange(topicName);
	}

	@Bean(name="bindingCustomer")
	Binding bindingCustomer(Queue queueCustomer, TopicExchange exchangeCustomer) {
		return BindingBuilder.bind(queueCustomer).to(exchangeCustomer).with(qCustomer);
	}
	
	@Bean(name="queueShop")
	Queue queueShop() {
		return new Queue(qShop, true);
	}

	@Bean(name="exchangeShop")
	TopicExchange exchangeShop() {
		return new TopicExchange(topicName);
	}

	@Bean(name="bindingShop")
	Binding bindingShop(Queue queueShop, TopicExchange exchangeShop) {
		return BindingBuilder.bind(queueShop).to(exchangeShop).with(qShop);
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(brokerUrl);
		connectionFactory.setUsername(user);
		connectionFactory.setPassword(pwd);
		
		return connectionFactory;
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new JsonMessageConverter();
	}

	@Bean(name="rabbitTemplateCustomer")
	public RabbitTemplate rabbitTemplateCustomer() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setRoutingKey(qCustomer);
		template.setMessageConverter(jsonMessageConverter());
		return template;
	}
	@Bean(name="rabbitTemplateShop")
	public RabbitTemplate rabbitTemplateShop() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setRoutingKey(qShop);
		template.setMessageConverter(jsonMessageConverter());
		return template;
	}

}
