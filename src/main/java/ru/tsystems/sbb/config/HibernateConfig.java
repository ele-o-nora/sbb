package ru.tsystems.sbb.config;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "ru.tsystems.sbb.model")
@PropertySource(value = "classpath:db.properties")
public class HibernateConfig {

    @Autowired
    private Environment env;

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect",
                env.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql",
                env.getRequiredProperty("hibernate.show_sql"));
        return properties;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
        bds.setUrl(env.getRequiredProperty("jdbc.url"));
        bds.setUsername(env.getRequiredProperty("jdbc.username"));
        bds.setPassword(env.getRequiredProperty("jdbc.password"));
        return bds;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sf = new LocalSessionFactoryBean();
        sf.setDataSource(dataSource());
        sf.setPackagesToScan("ru.tsystems.sbb.model");
        sf.setHibernateProperties(hibernateProperties());
        return sf;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager tm = new HibernateTransactionManager();
        tm.setSessionFactory(sessionFactory().getObject());
        return tm;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
