package com.hoperun.micro.security.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
public class DruidDBConfig {
	private final static Logger logger = LoggerFactory.getLogger(DruidDBConfig.class); 
	
    @Value("${spring.datasource.url}")
    private String dbUrl;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    
    @Value("${spring.datasource.initialSize}")
    private int initialSize;
    
    @Value("${spring.datasource.minIdle}")
    private int minIdle;
    
    @Value("${spring.datasource.maxActive}")
    private int maxActive;
    
    @Value("${spring.datasource.maxWait}")
    private int maxWait;
    
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    
    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    
    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;
    
    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;
    
    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;
    
    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;
    
    @Value("${spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;
    
    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    
    @Value("${spring.datasource.filters}")
    private String filters;
    
    @Value("{spring.datasource.connectionProperties}")
    private String connectionProperties;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addInitParameter("allow", ""); //ip白名单
        reg.addInitParameter("deny", ""); //ip黑名单
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", "admin");
        reg.addInitParameter("loginPassword", "adminZwj");
        return reg;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }
    
    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource(){
    	DruidDataSource datasource = new DruidDataSource();
    	datasource.setUrl(this.dbUrl);
    	datasource.setUsername(username);
    	datasource.setPassword(password);
    	datasource.setDriverClassName(driverClassName);
    	
    	//configuration
    	datasource.setInitialSize(initialSize); //初始化大小
    	datasource.setMinIdle(minIdle);  //最小
    	datasource.setMaxActive(maxActive); //最大
    	datasource.setMaxWait(maxWait);  //配置获取连接等待超时的时间
    	datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis); //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    	datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis); //配置一个连接在池中最小生存的时间，单位是毫秒
    	datasource.setValidationQuery(validationQuery);
    	datasource.setTestWhileIdle(testWhileIdle);
    	datasource.setTestOnBorrow(testOnBorrow);
    	datasource.setTestOnReturn(testOnReturn);
    	datasource.setPoolPreparedStatements(poolPreparedStatements); //打开PSCache，并且指定每个连接上PSCache的大小
    	datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
    	try {
    		// 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
			datasource.setFilters(filters);
		} catch (SQLException e) {
			logger.error("druid configuration initialization filter", e);
		}
    	// 通过connectProperties属性来打开mergeSql功能；慢SQL记录	
    	datasource.setConnectionProperties(connectionProperties);
    	
    	return datasource;
    }
}

