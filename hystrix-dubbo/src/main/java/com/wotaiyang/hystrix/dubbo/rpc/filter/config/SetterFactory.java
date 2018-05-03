package com.wotaiyang.hystrix.dubbo.rpc.filter.config;

import com.alibaba.dubbo.common.URL;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>HystrixCommand.Setter 工厂</p>
 * <p>
 * <PRE>
 * <BR>    修改记录
 * <BR>-----------------------------------------------
 * <BR>    修改日期         修改人          修改内容
 * </PRE>
 *
 * @author lusp
 * @version 1.0
 * @date Created in 2018/5/3 16:25
 * @since 1.0
 */
public class SetterFactory {

    /**
     * 配置的缓存Map
     */
    private static ConcurrentHashMap<String,HystrixCommand.Setter> setterMap = new ConcurrentHashMap<>();

    /**
     * 根据调用方法的接口名、方法名、以及url获取Setter
     *
     * @author lusp
     * @created 2018年05月03日 16:27
     * @param interfaceName,methodName,url
     * @return Setter
     */
    public static HystrixCommand.Setter create(String interfaceName, String methodName, URL url) {
        String key = String.format("%s.%s", interfaceName, methodName);
        if (setterMap.containsKey(key)) {
            return setterMap.get(key);
        } else {
            setterMap.putIfAbsent(key, doCreate(interfaceName, methodName, url));
            return setterMap.get(key);
        }
    }

    /**
     * 创建Setter的私用方法
     * 默认采用接口名字作为groupKey,方法名字作为commandKey
     *
     *
     * @author lusp
     * @created 2018年05月03日 16:29
     * @param interfaceName,methodName,url
     * @return Setter
     */
    private static HystrixCommand.Setter doCreate(String interfaceName, String methodName, URL url) {
        //线程池按class进行划分，一个class可以理解为一个领域服务，熔断保护按方法维度提供
        return HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(interfaceName))
                .andCommandKey(HystrixCommandKey.Factory.asKey(methodName))
                .andCommandPropertiesDefaults(HystrixCommandPropertiesFactory.create(url, methodName))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolPropertiesFactory.create(url));
    }
}
