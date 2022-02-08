package cn.enjoy.transaction;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Component
@EnableTransactionManagement(proxyTargetClass = false)
//@MapperScan(basePackages = {"cn.enjoy.mapper"})
public class EnableTransactionManagementBean {
    /*
    * 这样也可以
    * */
    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager dtm = new DataSourceTransactionManager();
        dtm.setDataSource(dataSource);
        return dtm;
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager platformTransactionManager) {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(platformTransactionManager);
        return transactionTemplate;
    }
}
