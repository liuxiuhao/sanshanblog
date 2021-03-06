package xyz.sanshan.main.dao.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class MybatisConfig {


    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().
//                getResource("classpath:mybatis/Configuration.xml"));
//        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().
//                getResources("classpath:mybatis/mapper/*.xml"));
        Properties properties = new Properties();
        PageInterceptor pageHelper = new PageInterceptor();//配置pagehelper分页插件
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "count");
        properties.setProperty("count", "countSql");
        properties.setProperty("autoRuntimeDialect", "true");
        pageHelper.setProperties(properties);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});
        return sqlSessionFactoryBean;
    }


    /**
     * 通用mapper
     * 配置mapper接口扫描,扫描后会自动注入到spring
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer m = new MapperScannerConfigurer();
        m.setBasePackage("xyz.sanshan.main.dao.mybatis");
        return m;
    }


}
