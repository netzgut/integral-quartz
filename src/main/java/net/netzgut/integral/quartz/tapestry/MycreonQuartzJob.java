package net.netzgut.integral.quartz.tapestry;

/**
 * will be built each time the job is executed!
 * can contain injected services / constructor services
 * @author felix gonschorek
 * @since 21.04.2012
 *
 */
public interface MycreonQuartzJob extends Runnable {
    // this annotation solely has the purpose to make quartz jobs easier to find
}
