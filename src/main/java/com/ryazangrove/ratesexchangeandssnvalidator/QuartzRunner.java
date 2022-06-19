package com.ryazangrove.ratesexchangeandssnvalidator;


import com.ryazangrove.ratesexchangeandssnvalidator.services.RatesExchangeService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class QuartzRunner {

    public QuartzRunner(){
        try {
            updateAtInitialization();
        } catch (SchedulerException e) {
            System.out.println(e);
        }
    }

    public void updateAtInitialization() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        JobKey jobKey = new JobKey("updateJob");
        TriggerKey cronTriggerKey = new TriggerKey("cronTrigger");
        JobDetail job = newJob(RatesExchangeUpdater.class).withIdentity(jobKey).storeDurably().build();
        CronTrigger trigger = newTrigger()
                .withIdentity(cronTriggerKey)
				.withSchedule(cronSchedule("0 0 * * * ?"))
				.build();

        // run by cron trigger
        scheduler.scheduleJob(job, trigger);
        // initial update
        scheduler.addJob(job,true);
        scheduler.triggerJob(jobKey);
    }

    @PersistJobDataAfterExecution
    @DisallowConcurrentExecution
    public static class RatesExchangeUpdater implements Job {

        @Override
        public void execute(final JobExecutionContext jobExecutionContext) throws JobExecutionException {
            RatesExchangeService.updateExchangeRates();
        }
    }
}
