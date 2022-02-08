package cn.enjoy.datasource;

import cn.enjoy.aop.DynamicDataSourceContextHolder;
import cn.enjoy.provider.DynamicDataSourceProvider;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @Classname DynamicRoutingDataSource
 * @Description TODO
 * @Author Jack
 * Date 2020/11/24 17:05
 * Version 1.0
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    @Setter
    private DynamicDataSourceProvider provider;
    @Setter
    private String primary;
    private boolean p6spy;

    @Override
    protected Object determineCurrentLookupKey() {
        log.debug("从默认数据源中返回数据");
        return DynamicDataSourceContextHolder.peek();
    }

    @PostConstruct
    public void init() {
        Map<Object, Object> dataSources = provider.loadDataSources();
        log.info("初始共加载 {} 个数据源", dataSources.size());
        this.setTargetDataSources(dataSources);
        this.setDefaultTargetDataSource(primary);
    }
}
