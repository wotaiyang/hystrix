# 说明:
该项目为自己封装的hystrix的dubbo插件,使用使用方式见下文:

# 1.使用maven将该项目打成jar包

# 2.JAR包引入:
在dubbo项目中需要进行hystrix进行熔断控制的项目的pom文件中引入依赖
```
<dependency>
    <groupId>com.wotaiyang.hystrix</groupId>
    <artifactId>hystrix-dubbo</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

# 3.消费方法中定义自己的hystrix参数
注: 建议fallback参数必须定义,否则调用一旦报错,直接抛出异常,
其他参数可以不定义，使用默认值（参见HystrixCommandPropertiesFactory、HystrixThreadPoolPropertiesFactory类）

示例如下:
```
<dubbo:reference id="booking.ticketInfo"  interface="com.ymy.asiabrand.booking.consumer.api.TicketInfo" check="false">
    <dubbo:method name="queryTicketInfo" timeout="3000" retry="0" >
        <dubbo:parameter key="fallback" value="fallback"/>
        <!--以下参数均有默认值,只示例三个-->
        <dubbo:parameter key="sleepWindowInMilliseconds" value="3000"/>
        <dubbo:parameter key="errorThresholdPercentage" value="40"/>
        <dubbo:parameter key="requestVolumeThreshold" value="30"/>
    </dubbo:method>
</dubbo:reference>
```
# 4.实现Fallback,写具体降级逻辑
示例如下:
```
package com.netease.hystrix.dubbo.rpc.filter.fallback;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.wotaiyang.hystrix.dubbo.rpc.filter.Fallback;

/**
 * <p>TODO</p>
 * <p>
 * <PRE>
 * <BR>    修改记录
 * <BR>-----------------------------------------------
 * <BR>    修改日期         修改人          修改内容
 * </PRE>
 *
 * @author lusp
 * @version 1.0
 * @date Created in 2018/4/28 14:28
 * @since 1.0
 */
@Activate(group = Constants.CONSUMER)
public class FallbackImpl implements Fallback {

    @Override
    public Object invoke() {
        System.out.println("========降级方法开始执行=======");
        //降级逻辑......
        return "服务不通，开始计数降级";
    }
}

```

# 5.创建配置文件让上述实现生效
在resources目录下的META-INF文件夹下创建services文件夹,
然后创建名字为Fallback接口全路径的文件,即com.wotaiyang.hystrix.dubbo.rpc.filter.Fallback
然后在文件中添加
```
fallback=com.netease.hystrix.dubbo.rpc.filter.fallback.FallbackImpl
```

