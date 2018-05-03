package com.wotaiyang.hystrix.dubbo.rpc.filter;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcResult;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>command基类</p>
 * <p>
 * <PRE>
 * <BR>    修改记录
 * <BR>-----------------------------------------------
 * <BR>    修改日期         修改人          修改内容
 * </PRE>
 *
 * @author lusp
 * @version 1.0
 * @date Created in 2018/5/3 16:35
 * @since 1.0
 */
public class DubboCommand extends HystrixCommand<Result> {

    private static Logger logger = LoggerFactory.getLogger(DubboCommand.class);

    private Invoker<?> invoker;
    private Invocation invocation;
    private String fallbackName;

    public DubboCommand(Setter setter, Invoker<?> invoker, Invocation invocation, String fallbackName) {
        super(setter);
        this.invoker = invoker;
        this.invocation = invocation;
        this.fallbackName = fallbackName;
    }

    /**
     * command调用execute() 方法时执行
     *
     * @author lusp
     * @created 2018年05月03日 16:43
     * @param
     * @return Result
     */
    @Override
    protected Result run() throws Exception {
        Result result = invoker.invoke(invocation);
        //如果远程调用异常，抛出异常执行降级逻辑
        if (result.hasException()) {
            throw new HystrixRuntimeException(HystrixRuntimeException.FailureType.COMMAND_EXCEPTION, DubboCommand.class, result.getException().getMessage(), result.getException(), null);
        }
        return result;
    }

    /**
     * 降级方法
     *
     * @author lusp
     * @created 2018年05月03日 16:44
     * @param
     * @return Result
     */
    @Override
    protected Result getFallback() {
        if (StringUtils.isEmpty(fallbackName)) {
            //抛出原本的异常
            return super.getFallback();
        }
        try {
            //基于SPI扩展和fallbackName加载Fallback接口实现,并调用invoke()方法返回
            ExtensionLoader<Fallback> loader = ExtensionLoader.getExtensionLoader(Fallback.class);
            Fallback fallback = loader.getExtension(fallbackName);
            Object value = fallback.invoke();
            return new RpcResult(value);
        } catch (RuntimeException ex) {
            logger.error("【Hystrix】降级方法fallback()执行失败,请检查Fallback实现", ex);
            throw ex;
        }

    }
}
