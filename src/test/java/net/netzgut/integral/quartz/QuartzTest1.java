package net.netzgut.integral.quartz;

import org.apache.tapestry5.ioc.RegistryBuilder;

public class QuartzTest1 {

    public static void main(String[] args) {
        //BasicConfigurator.configure();
        System.out.println("starting basic test");
        new QuartzTest1().start();
    }

    private synchronized void start() {

        RegistryBuilder builder = new RegistryBuilder().add(QuartzTestModule1.class);
        builder.build().performRegistryStartup();
        System.out.println("registry started");

        System.out.println("done?");
    }

}
