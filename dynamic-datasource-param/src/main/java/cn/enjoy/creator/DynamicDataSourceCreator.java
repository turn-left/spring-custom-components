package cn.enjoy.creator;

import cn.enjoy.properties.DataSourceProperty;
import cn.enjoy.properties.DruidConfig;
import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Classname DynamicDataSourceCreator
 * @Description TODO
 * @Author Jack
 * Date 2020/11/26 15:09
 * Version 1.0
 */
@Slf4j
public class DynamicDataSourceCreator {
    /**
     * DRUID数据源类
     */
    private static final String DRUID_DATASOURCE = "com.alibaba.druid.pool.DruidDataSource";
    /**
     * HikariCp数据源
     */
    private static final String HIKARI_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";
    /**
     * JNDI数据源查找
     */
    private static final JndiDataSourceLookup JNDI_DATA_SOURCE_LOOKUP = new JndiDataSourceLookup();

    private Method createMethod;
    private Method typeMethod;
    private Method urlMethod;
    private Method usernameMethod;
    private Method passwordMethod;
    private Method driverClassNameMethod;
    private Method buildMethod;

    /**
     * 是否存在druid
     */
    private Boolean druidExists = false;
    /**
     * 是否存在hikari
     */
    private Boolean hikariExists = false;

    @Setter
    private DruidConfig druidGlobalConfig;

    public DynamicDataSourceCreator() {
        Class<?> builderClass = null;
        try {
            builderClass = Class.forName("org.springframework.boot.jdbc.DataSourceBuilder");
        } catch (Exception e) {
            try {
                builderClass = Class.forName("org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder");
            } catch (Exception e1) {
            }
        }
        try {
            createMethod = builderClass.getDeclaredMethod("create");
            typeMethod = builderClass.getDeclaredMethod("type", Class.class);
            urlMethod = builderClass.getDeclaredMethod("url", String.class);
            usernameMethod = builderClass.getDeclaredMethod("username", String.class);
            passwordMethod = builderClass.getDeclaredMethod("password", String.class);
            driverClassNameMethod = builderClass.getDeclaredMethod("driverClassName", String.class);
            buildMethod = builderClass.getDeclaredMethod("build");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class.forName(DRUID_DATASOURCE);
            log.info("动态数据源-检测到druid存在,如配置中未指定type,druid会默认配置");
            druidExists = true;
        } catch (ClassNotFoundException e) {
        }
        try {
            Class.forName(HIKARI_DATASOURCE);
            hikariExists = true;
        } catch (ClassNotFoundException e) {
        }
    }

    /**
     * 创建数据源
     *
     * @param dataSourceProperty 数据源信息
     * @return 数据源
     */
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        Class<? extends DataSource> type = dataSourceProperty.getType();
        if (type == null) {
            if (druidExists) {
                return createDruidDataSource(dataSourceProperty);
            }
        } else if (DRUID_DATASOURCE.equals(type.getName())) {
            return createDruidDataSource(dataSourceProperty);
        }
        return createBasicDataSource(dataSourceProperty);
    }

    /**
     * 创建基础数据源
     *
     * @param dataSourceProperty 数据源参数
     * @return 数据源
     */
    public DataSource createBasicDataSource(DataSourceProperty dataSourceProperty) {
        try {
            Object o1 = createMethod.invoke(null);
            Object o2 = typeMethod.invoke(o1, dataSourceProperty.getType());
            Object o3 = urlMethod.invoke(o2, dataSourceProperty.getUrl());
            Object o4 = usernameMethod.invoke(o3, dataSourceProperty.getUsername());
            Object o5 = passwordMethod.invoke(o4, dataSourceProperty.getPassword());
            Object o6 = driverClassNameMethod.invoke(o5, dataSourceProperty.getDriverClassName());
            return (DataSource) buildMethod.invoke(o6);
        } catch (Exception e) {
            throw new RuntimeException("多数据源创建数据源失败");
        }
    }

    /**
     * 创建JNDI数据源
     *
     * @param jndiName jndi数据源名称
     * @return 数据源
     */
    public DataSource createJNDIDataSource(String jndiName) {
        return JNDI_DATA_SOURCE_LOOKUP.getDataSource(jndiName);
    }

    /**
     * 创建DRUID数据源
     *
     * @param dataSourceProperty 数据源参数
     * @return 数据源
     */
    public DataSource createDruidDataSource(DataSourceProperty dataSourceProperty) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(dataSourceProperty.getUsername());
        dataSource.setPassword(dataSourceProperty.getPassword());
        dataSource.setUrl(dataSourceProperty.getUrl());
        dataSource.setDriverClassName(dataSourceProperty.getDriverClassName());
        dataSource.setName(dataSourceProperty.getPollName());
        DruidConfig config = dataSourceProperty.getDruid();
        Properties properties = config.toProperties(druidGlobalConfig);
        String filters = properties.getProperty("druid.filters");
        List<Filter> proxyFilters = new ArrayList<>(2);
        if (!StringUtils.isEmpty(filters) && filters.contains("stat")) {
            StatFilter statFilter = new StatFilter();
            statFilter.configFromProperties(properties);
            proxyFilters.add(statFilter);
        }
        if (!StringUtils.isEmpty(filters) && filters.contains("wall")) {
            WallConfig wallConfig = dataSourceProperty.getDruid().getWall().toWallConfig(druidGlobalConfig.getWall());
            WallFilter wallFilter = new WallFilter();
            wallFilter.setConfig(wallConfig);
            proxyFilters.add(wallFilter);
        }
        dataSource.setProxyFilters(proxyFilters);
        dataSource.configFromPropety(properties);
        //连接参数单独设置
        dataSource.setConnectProperties(config.getConnectionProperties());
        //设置druid内置properties不支持的的参数
        Boolean testOnReturn = config.getTestOnReturn() == null ? druidGlobalConfig.getTestOnReturn() : config.getTestOnReturn();
        if (testOnReturn != null && testOnReturn.equals(true)) {
            dataSource.setTestOnReturn(true);
        }
        Integer validationQueryTimeout = config.getValidationQueryTimeout() == null ? druidGlobalConfig.getValidationQueryTimeout() : config.getValidationQueryTimeout();
        if (validationQueryTimeout != null && !validationQueryTimeout.equals(-1)) {
            dataSource.setValidationQueryTimeout(validationQueryTimeout);
        }

        Boolean sharePreparedStatements = config.getSharePreparedStatements() == null ? druidGlobalConfig.getSharePreparedStatements() : config.getSharePreparedStatements();
        if (sharePreparedStatements != null && sharePreparedStatements.equals(true)) {
            dataSource.setSharePreparedStatements(true);
        }
        Integer connectionErrorRetryAttempts = config.getConnectionErrorRetryAttempts() == null ? druidGlobalConfig.getConnectionErrorRetryAttempts() : config.getConnectionErrorRetryAttempts();
        if (connectionErrorRetryAttempts != null && !connectionErrorRetryAttempts.equals(1)) {
            dataSource.setConnectionErrorRetryAttempts(connectionErrorRetryAttempts);
        }
        Boolean breakAfterAcquireFailure = config.getBreakAfterAcquireFailure() == null ? druidGlobalConfig.getBreakAfterAcquireFailure() : config.getBreakAfterAcquireFailure();
        if (breakAfterAcquireFailure != null && breakAfterAcquireFailure.equals(true)) {
            dataSource.setBreakAfterAcquireFailure(true);
        }
        try {
            dataSource.init();
        } catch (SQLException e) {
            log.error("druid数据源启动失败", e);
        }
        return dataSource;
    }
}
