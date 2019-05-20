package com.hp.ncs.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * app来源的客户端
 *
 * @author dongxing
 */
@Configuration
@ConfigurationProperties(AppSecretProperties.PREFIX)
@Data
public class AppSecretProperties {

	public static final String PREFIX = "app.secret";

	/**
	 * a set of secrets .
	 */
	private List<Config> appConfig;

	@Data
	protected static class Config{
		/**
		 * 来源的id
		 */
		private String appId;

		/**
		 * 分配的秘钥
		 */
		private String secret;
	}

}