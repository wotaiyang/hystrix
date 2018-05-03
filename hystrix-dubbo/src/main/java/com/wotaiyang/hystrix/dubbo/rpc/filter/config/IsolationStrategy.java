package com.wotaiyang.hystrix.dubbo.rpc.filter.config;

import com.alibaba.dubbo.common.URL;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * <p>隔离策略</p>
 * <p>
 * <PRE>
 * <BR>    修改记录
 * <BR>-----------------------------------------------
 * <BR>    修改日期         修改人          修改内容
 * </PRE>
 *
 * @author lusp
 * @version 1.0
 * @date Created in 2018/5/3 16:14
 * @since 1.0
 */
public class IsolationStrategy {

    /**
     * 线程池隔离
     */
    public static final String THREAD = "THREAD";
    /**
     * 信号量隔离
     */
    public static final String SEMAPHORE = "SEMAPHORE";

    /**
     * 获取隔离策略，默认使用线程池隔离
     *
     * @author lusp
     * @created 2018年05月03日 16:14
     * @param url
     * @return ExecutionIsolationStrategy
     */
    public static HystrixCommandProperties.ExecutionIsolationStrategy getIsolationStrategy(URL url) {
        String isolation = url.getParameter("isolation", THREAD);
        if (!THREAD.equalsIgnoreCase(isolation) && !SEMAPHORE.equalsIgnoreCase(isolation)) {
            isolation = THREAD;
        }
        if (isolation.equalsIgnoreCase(THREAD)) {
            return HystrixCommandProperties.ExecutionIsolationStrategy.THREAD;
        } else {
            return HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE;
        }
    }
}
