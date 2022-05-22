package com.kkanggogo.facealbum.scheduler.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskExecutor=new ThreadPoolTaskScheduler();

        threadPoolTaskExecutor.setPoolSize(2);
        threadPoolTaskExecutor.setThreadGroupName("scheduler thread pool");
        threadPoolTaskExecutor.setThreadNamePrefix("scheduler-thread-");
        threadPoolTaskExecutor.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskExecutor);
    }
}
