## RabbitMQ SpringBoot Example

Example of how to create reader and writer SpringBoot applications implementing rabbitMQ.  

Dependencies:


```xml

<dependencies>
     <dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
     </dependency>
     <dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web-services</artifactId>
     </dependency>
     <dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-amqp</artifactId>
     </dependency>
     <dependency>
	<groupId>com.sh</groupId>
	<artifactId>rabbitBeanMessages</artifactId>
	<version>0.0.1</version>
     </dependency>
</dependencies>

```

We have 3 modules:
* [rabbit Publisher](#rabbit-publisher) - write in rabbit queues
* [rabbit Reader](#rabbit-reader)  - read the rabbit queues
* [rabbit Bean Messages](#rabbit-bean-messages) - Common Beans shared  between both modules

## rabbit Bean Messages

This module have 2 POJO Beans,for customers and for shops.SpringBoot send and receive this type of objects.  
Look shop bean:

```java

package com.sh.messages;

public class ShopMsg {

	private String shopName;

	private String city;

	private int sales;



	public ShopMsg() {
		super();

	}
	public ShopMsg(String shopName, String city, int sales) {
		super();
		this.shopName = shopName;
		this.city = city;
		this.sales = sales;

	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getSales() {
		return sales;
	}
	public void setSales(int sales) {
		this.sales = sales;
	}
}
```
## rabbit Publisher

This module have the functionality of publish messages on rabbitMQ.  
Also have REST controllers for publish messages with HTTP calls.

This is the module structure:

```
│   pom.xml
├───src
    ├───main
    ├───java
    │   └───com
    │       └───sh
    │           ├───app
    │           │       AppRabbitPublisher.java
    │           │
    │           ├───ctrl
    │           │       RabbitController.java
    │           │
    │           └───producers
    │                   MsgProducer.java
    │
    └───resources
            application.properties

```


Look the configuration.

With this we declare a queue,a topic exchange and a binder of both:

```java

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

```

Next step is declare a connectionFactory with the rabbitMQ host(brokerUrl) and configure a json message converter:

```java
        @Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(brokerUrl);
		connectionFactory.setUsername(user);
		connectionFactory.setPassword(pwd);

		return connectionFactory;
	}
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
```
Last step is configure the rabbitTemplate:

```java
        @Bean(name="rabbitTemplateCustomer")
	public RabbitTemplate rabbitTemplateCustomer() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setRoutingKey(qCustomer);
		template.setMessageConverter(jsonMessageConverter());
		return template;
	}
```
This configuration is in [AppRabbitPublisher.java](https://github.com/shoecillo/rabbitMQSpringBoot/blob/master/rabbitPublisher/src/main/java/com/sh/app/AppRabbitPublisher.java)


Class MsgProducer is the component that publish in rabbit:

```java

        @Autowired
	@Qualifier("rabbitTemplateCustomer")
	private RabbitTemplate rabbitCustomer;

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

```

The operation [rabbitCustomer.convertAndSend(msg)] convert msg in JSON and send to rabbitMQ configured queue.

The other class is a REST controller with little API for publish messages.

## rabbit Reader

This module have the functionality of listen messages from rabbitMQ.  
Create 2 listeners for 2 different queues but sharing topic exchange.

This is the project structure:

```
│   pom.xml
├───src
    ├───main
    ├───java
    │   └───com
    │       └───sh
    │           ├───app
    │           │       AppRabbitReader.java
    │           │
    │           └───listener
    │               │   RabbitListener.java
    │               │
    │               └───impl
    │                       ListenerCustomer.java
    │                       ListenerShop.java
    │
    └───resources
            application.properties
```

For configure the listeners we have to declare the same that in publisher:Queue,Topic exchange,bind and connectionFactory configured for rabbitMQ host.  
Look listener configuration:

```java
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

	@Bean(name="listenerAdapterCustomer")
	public MessageListenerAdapter listenerAdapterCustomer(ListenerCustomer receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}
```

It's easy to configure listeners,we need a container that configure destination,converter,connectionFactory and listener.
we need a message listener adapter,where configure conversion and inject the class that implements the message reception operation, and we set which is the method listener,in my case is a constant.

Now look a interface that all my receivers must implements:

```java

@FunctionalInterface
public interface RabbitListener<T> {

	public void receiveMessage(T msg);
}

```

Using generic types, we can implements any bean as Message.  
Look one of the implementations:

```java
@Component
public class ListenerCustomer implements RabbitListener<CustomerMsg>{

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerCustomer.class);

    @Override
    public void receiveMessage(CustomerMsg message) {

    	try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);

			LOGGER.debug("Receive Nessage: \n"+json);

		} catch (JsonProcessingException e) {
			LOGGER.error("Error: ", e);
		}   
    }
}
```

RabbitListener<CustomerMsg> assign message bean type,in this case CustomerMsg (in the common project).  
Only show the message received in log.

Only with this steps we can write and read easily working with rabbitMQ.  
Complete example have 2 queues for show how to configure multiple listeners and multiple writers.


***

Thanks to :  
[RabbitMQ](https://www.rabbitmq.com/)  
[SpringBoot](https://projects.spring.io/spring-boot/)
