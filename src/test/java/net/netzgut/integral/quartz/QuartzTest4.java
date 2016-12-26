package net.netzgut.integral.quartz;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.testng.annotations.Test;

public class QuartzTest4 {

    @Test(timeOut = 10 * 1000)
    public void testConcurrentJobs() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getLogger("net.netzgut").setLevel(Level.DEBUG);
        try {
            Registry registry = startRegistry();
            Thread.sleep(5000);
            registry.shutdown();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Registry startRegistry() {
        RegistryBuilder builder = new RegistryBuilder().add(QuartzTestModule4.class);
        Registry registry = builder.build();
        registry.performRegistryStartup();
        return registry;
    }

}
