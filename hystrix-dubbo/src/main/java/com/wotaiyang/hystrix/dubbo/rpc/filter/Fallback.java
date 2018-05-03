package com.wotaiyang.hystrix.dubbo.rpc.filter;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * <p>降级处理SPI接口</p>
 * <p>
 * <PRE>
 * <BR>    修改记录
 * <BR>-----------------------------------------------
 * <BR>    修改日期         修改人          修改内容
 * </PRE>
 *
 * @author lusp
 * @version 1.0
 * @date Created in 2018/5/3 16:41
 * @since 1.0
 */
@SPI
public interface Fallback {
    Object invoke();
}
