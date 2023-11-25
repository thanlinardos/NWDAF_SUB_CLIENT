package io.nwdaf.eventsubscription.client.config;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.client.requestbuilders.CreateSubscriptionRequestBuilder;
import io.nwdaf.eventsubscription.client.requestbuilders.RequestEventModel;
import io.nwdaf.eventsubscription.client.requestbuilders.RequestNotificationModel;
import io.nwdaf.eventsubscription.client.requestbuilders.RequestSubscriptionModel;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
//@EnableWebMvc
@RegisterReflectionForBinding({RequestSubscriptionModel.class,RequestNotificationModel.class,RequestEventModel.class,CreateSubscriptionRequestBuilder.class,NotificationMethodEnum.class})
public class ClientConfig{

}
