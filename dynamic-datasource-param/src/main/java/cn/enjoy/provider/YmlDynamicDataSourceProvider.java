package cn.enjoy.provider;

import cn.enjoy.creator.DynamicDataSourceCreator;
import cn.enjoy.properties.DataSourceProperty;
import cn.enjoy.properties.DynamicDataSourceProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname YmlDynamicDataSourceProvider
 * @Description TODO
 * @Author Jack
 * Date 2020/11/26 15:08
 * Version 1.0
 */
public class YmlDynamicDataSourceProvider implements DynamicDataSourceProvider {
    /**
     * 多数据源参数
     */
    private DynamicDataSourceProperties properties;
    /**
     * 多数据源创建器
     */
    private DynamicDataSourceCreator dynamicDataSourceCreator;

    public YmlDynamicDataSourceProvider(DynamicDataSourceProperties properties, DynamicDataSourceCreator dynamicDataSourceCreator) {
        this.properties = properties;
        this.dynamicDataSourceCreator = dynamicDataSourceCreator;
    }

    @Override
    public Map<Object, Object> loadDataSources() {
        Map<String, DataSourceProperty> dataSourcePropertiesMap = properties.getDatasource();
        Map<Object, Object> dataSourceMap = new HashMap<>(dataSourcePropertiesMap.size());
        for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()) {
            String pollName = item.getKey();
            DataSourceProperty dataSourceProperty = item.getValue();
            dataSourceProperty.setPollName(pollName);
            dataSourceMap.put(pollName, dynamicDataSourceCreator.createDataSource(dataSourceProperty));
        }
        return dataSourceMap;
    }
}
