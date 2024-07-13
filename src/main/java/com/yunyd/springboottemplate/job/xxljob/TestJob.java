package com.yunyd.springboottemplate.job.xxljob;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @lyd
 * @date 2024/7/12
 */
@Component
public class TestJob {

    @XxlJob("yunyd123")
    public void TestJobHandler() throws Exception {
        System.out.println("执行定时任务,执行时间:"+new Date());
    }
}
