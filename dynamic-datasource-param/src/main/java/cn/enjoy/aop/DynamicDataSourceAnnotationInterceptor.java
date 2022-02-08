package cn.enjoy.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

/**
 * @Classname DynamicDataSourceAnnotationInterceptor
 * @Description TODO
 * @Author Jack
 * Date 2020/11/24 14:42
 * Version 1.0
 */
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {

    /**
     * 规定第一个参数就是路由key
    * @param
    * @author Jack
    * @date 2020/11/24
    * @throws Exception
    * @return
    * @version
    */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            Object argument = invocation.getArguments()[0];
            if(StringUtils.isEmpty(argument)) {
                DynamicDataSourceContextHolder.push("master");
            }
            DynamicDataSourceContextHolder.push(String.valueOf(argument));
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }
}
