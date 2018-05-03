package com.wotaiyang.hystrix.dubbo.rpc.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.netflix.hystrix.HystrixCommand;
import com.wotaiyang.hystrix.dubbo.rpc.filter.config.SetterFactory;

/**
 * <p>Hystrix消费请求拦截器</p>
 * <p>
 * <PRE>
 * <BR>    修改记录
 * <BR>-----------------------------------------------
 * <BR>    修改日期         修改人          修改内容
 * </PRE>
 *
 * @author lusp
 * @version 1.0
 * @date Created in 2018/5/3 16:51
 * @since 1.0
 */
@Activate(group= Constants.CONSUMER)
public class HystrixFilter implements Filter{
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url = invoker.getUrl();
        String methodName = invocation.getMethodName();
        String interfaceName = invoker.getInterface().getName();
        //获取相关熔断配置
        HystrixCommand.Setter setter = SetterFactory.create(interfaceName, methodName, url);
        //获取降级方法名称
        String fallback = url.getMethodParameter(methodName, "fallback");
        DubboCommand command = new DubboCommand(setter, invoker, invocation, fallback);
        Result result = command.execute();
        return result;
    }
}
