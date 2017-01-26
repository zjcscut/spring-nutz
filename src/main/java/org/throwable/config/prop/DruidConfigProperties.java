package org.throwable.config.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zjc
 * @version 2017/1/25 23:36
 * @description
 */
@ConfigurationProperties(prefix = DruidConfigProperties.DRUID_CONFIG_PREFIX)
public class DruidConfigProperties {

	public static final String DRUID_CONFIG_PREFIX = "druid";

	private String url;
	private String username;
	private String password;
	private String driverClassName;

	public DruidConfigProperties() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
}
