package net.netzgut.integral.quartz;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.testng.annotations.Test;

public class QuartzTest2 {

    @Test(timeOut = 5 * 1000)
    public void testFailingJob() throws InterruptedException {
        System.out.println("starting error test");
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getLogger("net.netzgut").setLevel(Level.DEBUG);
        RegistryBuilder builder = new RegistryBuilder().add(QuartzTestModule2.class);
        Registry registry = builder.build();
        registry.performRegistryStartup();

        Thread.sleep(2000);

        registry.shutdown();
    }

}
