package net.netzgut.integral.quartz;

/**
 * Extend your jobs from this interface. <br />
 * The Job will be autobuild each time it is executed, you can use constructor or field based injection.
 * Please take care, that you don't use services that rely on Response/Request objects, since we are not running
 * in an HTTP request.
 *
 * @author Felix Gonschorek
 * @since 2012-04-24
 *
 */
public interface IntegralQuartzJob extends Runnable {
    // this marker interface solely has the purpose to make quartz jobs easier to find
}
