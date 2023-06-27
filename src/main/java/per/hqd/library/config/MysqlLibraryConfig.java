package per.hqd.library.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.FileNotFoundException;

/**
 * @author 520156723@qq.com
 * @date 2023/6/26 16:37
 */
@Slf4j
@Configuration
@MapperScan(basePackages = "per.hqd.library.dao.mapper", sqlSessionFactoryRef = "librarySqlSessionFactory")
public class MysqlLibraryConfig {

    private final String mapperLocation = "classpath*:mappers/*Mapper.xml";

    @Bean
    @Primary
    @ConfigurationProperties("mysql.library")
    public DataSourceProperties libraryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("mysql.library")
    public DataSource libraryDataSource(@Qualifier("libraryDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @Primary
    public SqlSessionFactory librarySqlSessionFactory(@Qualifier("libraryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = null;
        try {
            resources = resourceResolver.getResources(mapperLocation);
        } catch (FileNotFoundException e) {
            log.info("no mapper file found in mapperLocation:{}", mapperLocation);
        }
        if (resources != null) {
            sessionFactoryBean.setMapperLocations(resources);
        }
        return sessionFactoryBean.getObject();
    }
}
