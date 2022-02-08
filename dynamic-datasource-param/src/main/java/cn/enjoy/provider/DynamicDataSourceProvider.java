package cn.enjoy.provider;

import java.util.Map;

public interface DynamicDataSourceProvider {
    Map<Object, Object> loadDataSources();
}
