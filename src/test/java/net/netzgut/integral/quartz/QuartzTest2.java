package net.netzgut.integral.quartz;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.RegistryBuilder;

public class QuartzTest2 {

    public static void main(String[] args) {
        System.out.println("starting error test");
        new QuartzTest2().start();
    }

    private synchronized void start() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getLogger("net.netzgut").setLevel(Level.DEBUG);
        RegistryBuilder builder = new RegistryBuilder().add(QuartzTestModule2.class);
        builder.build().performRegistryStartup();
        System.out.println("registry started");

        System.out.println("done?");
    }

}
