package pers.sample.sb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = true)
public class SbDemoConfiguration {

    @Bean
	public TransactionTemplate transactionTemplate(PlatformTransactionManager manager) {
		return new TransactionTemplate(manager);
	}

}
