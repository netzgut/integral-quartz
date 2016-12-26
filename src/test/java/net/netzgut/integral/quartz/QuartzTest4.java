package net.netzgut.integral.quartz;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

public class QuartzTest4 {

    public static void main(String[] args) {
        System.out.println("starting CLUSTER test");
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getLogger("net.netzgut").setLevel(Level.DEBUG);
        new QuartzTest4().start();
    }

    private synchronized void start() {

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
