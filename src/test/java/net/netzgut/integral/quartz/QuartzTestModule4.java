package net.netzgut.integral.quartz;

import java.util.Date;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;

import net.netzgut.integral.quartz.builder.QuartzJobSchedulingBundleBuilder;

@ImportModule(QuartzModule.class)
public class QuartzTestModule4 {

    @Contribute(QuartzSchedulerManager.class)
    public static void contributeQuartzSchedulerManager(OrderedConfiguration<JobSchedulingBundle> configuration) {

        new QuartzJobSchedulingBundleBuilder() //
                                               .jobClass(TestJob2.class) //
                                               .startDate(new Date()) //
                                               .triggerSecondly(2) //
                                               .build(configuration);

        new QuartzJobSchedulingBundleBuilder() //
                                               .jobClass(TestJob3.class) //
                                               .startDate(new Date(new Date().getTime() + 100)) //
                                               .triggerSecondly(2) //
                                               .build(configuration);

    }
}
