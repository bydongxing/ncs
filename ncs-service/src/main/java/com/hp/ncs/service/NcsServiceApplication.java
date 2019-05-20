package com.hp.ncs.service;

import com.hp.ncs.service.api.MessageSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;


/**
 * @author dongxing
 */
@SpringBootApplication
@EnableBinding({MessageSource.class})
public class NcsServiceApplication{

    public static void main(String[] args) {
        SpringApplication.run(NcsServiceApplication.class, args);
    }

}
