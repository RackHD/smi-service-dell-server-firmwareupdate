/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.firmwareupdate;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
@EnableAsync
@EnableSwagger2
@ComponentScan("com.dell.isg")
@EnableDiscoveryClient
public class ServiceServerFirmwareupdateApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceServerFirmwareupdateApplication.class, args);
    }

	@Autowired
	private BuildInfo buildInfo;

    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("serverFirmware").apiInfo(new ApiInfoBuilder().title("SMI Microservice :  Dell Firware Update Service").version(buildInfo.toString()).build()).select().paths(regex("/api.*")).build();
    }

}
