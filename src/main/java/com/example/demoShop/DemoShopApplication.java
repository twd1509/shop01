package com.example.demoShop;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@SpringBootApplication
public class DemoShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoShopApplication.class, args);
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		// 1. SqlSessionFactoryBean 객체 생성
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		
		// 2. DataSource 설정, Mapper 파일 위치 설정
		sessionFactory.setDataSource(dataSource);
		Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*Mapper.xml");
		sessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
		sessionFactory.setMapperLocations(res);
		
		// 3. 생성된 SqlSessionFactory 객체 반환
		return sessionFactory.getObject();
	}
}
