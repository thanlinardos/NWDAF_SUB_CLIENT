package io.nwdaf.eventsubscription.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.client.requestbuilders.CreateSubscriptionRequestBuilder;

@SpringBootApplication
public class NwdafSubClientApplication {
	
	@Autowired
	private Environment env;
	
	private static final Logger log = LoggerFactory.getLogger(NwdafSubClientApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(NwdafSubClientApplication.class, args);
		
		
	}

//	@Bean
	public CommandLineRunner run() throws JsonProcessingException{
		RestTemplate restTemplate = new RestTemplate();
		String apiRoot = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url");
		CreateSubscriptionRequestBuilder rbuilder = new CreateSubscriptionRequestBuilder();
		NnwdafEventsSubscription bodyObject = rbuilder.InitSubscriptionRequest(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		return args -> {
			HttpEntity<NnwdafEventsSubscription> req = new HttpEntity<>(bodyObject);
			ResponseEntity<NnwdafEventsSubscription> res = restTemplate.postForEntity(
					apiRoot+"/nwdaf-eventsubscription/v1/subscriptions",req, NnwdafEventsSubscription.class);
			System.out.println("Location:"+res.getHeaders().getFirst("Location"));
			NnwdafEventsSubscription body = res.getBody();
			if(body!=null){
				log.info(body.toString());
			}
		};
	}
	
	public static Logger getLogger() {
		return NwdafSubClientApplication.log;
	}
	
}
