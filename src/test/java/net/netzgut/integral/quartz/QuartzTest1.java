package net.netzgut.integral.quartz;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

public class QuartzTest1 {

    @Test(timeOut = 5 * 1000)
    public void basicTest() throws InterruptedException {
        RegistryBuilder builder = new RegistryBuilder().add(QuartzTestModule1.class);
        Registry registry = builder.build();
        registry.performRegistryStartup();

        Thread.sleep(2000);

        Assert.assertTrue(TestJob1.RUN);
        TestJob1.RUN = false;
        registry.shutdown();
    }

}
