package com.promo.management.system.promomanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.promo.management.system.promomanagement.config.properties")
public class PromoManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromoManagementApplication.class, args);
	}

}
