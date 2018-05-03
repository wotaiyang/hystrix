package com.wotaiyang.hystrix.dubbo.rpc.filter.config;

import com.alibaba.dubbo.common.URL;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * <p>Hystrix线程池相关配置</p>
 * <p>
 * <PRE>
 * <BR>    修改记录
 * <BR>-----------------------------------------------
 * <BR>    修改日期         修改人          修改内容
 * </PRE>
 *
 * @author lusp
 * @version 1.0
 * @date Created in 2018/5/3 16:18
 * @since 1.0
 */
public class HystrixThreadPoolPropertiesFactory {

    /**
     * 从请求的URL中获取线程池隔离的相关配置
     * 其中coreSize默认值为10、最大线程数为20、线程保持存活时间默认我1分钟
     *
     * @author lusp
     * @created 2018年05月03日 16:19
     * @param url
     * @return Setter
     */
    public static HystrixThreadPoolProperties.Setter create(URL url) {
        return HystrixThreadPoolProperties.Setter().withCoreSize(url.getParameter("coreSize", 10))
                .withAllowMaximumSizeToDivergeFromCoreSize(true)
                .withMaximumSize(url.getParameter("maximumSize", 20))
                .withMaxQueueSize(-1)
                .withKeepAliveTimeMinutes(url.getParameter("keepAliveTimeMinutes", 1));
    }
}
