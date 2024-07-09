package com.jerry.up.lala.framework.core.exception;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>Description:线程池配置
 *
 * @author FMJ
 */
@EnableAsync
@Configuration
public class ExceptionThreadConfig {

    /**
     * <p>Description: 异常邮件线程池
     * @see com.jerry.up.lala.framework.core.exception.ExceptionEmailComponent#send(List, ExceptionEmailBO) 
     */
    @Bean("exception-email-executor")
    public Executor exceptionEmailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程
        executor.setCorePoolSize(10);
        // 最大线程
        executor.setMaxPoolSize(20);
        // 缓冲队列
        executor.setQueueCapacity(200);
        // 允许线程的空闲时间
        executor.setKeepAliveSeconds(60);
        // 线程池名的前缀
        executor.setThreadNamePrefix("exception-email-executor-");
        // 用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        // 这样这些异步任务的销毁就会先于Redis线程池的销毁
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

}
