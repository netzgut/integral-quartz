package net.netzgut.integral.quartz;

import java.util.Date;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;

import net.netzgut.integral.quartz.builder.QuartzJobSchedulingBundleBuilder;
import net.netzgut.integral.quartz.modules.QuartzModule;

@ImportModule(QuartzModule.class)
public class QuartzTestModule1 {

    @Contribute(QuartzSchedulerManager.class)
    public static void contributeQuartzSchedulerManager(OrderedConfiguration<JobSchedulingBundle> configuration) {

        new QuartzJobSchedulingBundleBuilder() //
                                               .jobClass(TestJob1.class) //
                                               .startDate(new Date()) //
                                               .triggerSecondly(1) //
                                               .build(configuration);

    }
}
