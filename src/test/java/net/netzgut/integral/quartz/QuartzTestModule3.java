/**
 * Copyright 2017 Netzgut GmbH <info@netzgut.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.netzgut.integral.quartz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.services.ServiceOverride;
import org.quartz.SchedulerFactory;
import org.quartz.utils.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.netzgut.integral.quartz.builder.QuartzJobSchedulingBundleBuilder;
import net.netzgut.integral.quartz.builder.QuartzSchedulerFactoryBuilder;

@ImportModule(QuartzModule.class)
public class QuartzTestModule3 {

    private static final Logger log = LoggerFactory.getLogger(QuartzTestModule3.class);

    @Contribute(ServiceOverride.class)
    public static void contributeServiceOverride(@SuppressWarnings("rawtypes") MappedConfiguration<Class, Object> conf,
                                                 @Local SchedulerFactory schedulerFactory) {
        conf.add(SchedulerFactory.class, schedulerFactory);
    }

    public static SchedulerFactory buildTestClusterSchedulerFactory() {
        ConnectionProvider connectionProvider = new ConnectionProvider() {

            @Override
            public void shutdown() throws SQLException {
                // NOOP
            }

            @Override
            public void initialize() throws SQLException {
                // NOOP
            }

            @Override
            public Connection getConnection() throws SQLException {
                Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
                return c;
            }
        };

        return QuartzSchedulerFactoryBuilder.withJdbcDefaultSettings(connectionProvider).build();
    }

    @Contribute(QuartzSchedulerManager.class)
    public static void contributeQuartzSchedulerManager(OrderedConfiguration<JobSchedulingBundle> configuration) {

        new QuartzJobSchedulingBundleBuilder() //
                                               .jobClass(TestJob3.class) //
                                               .startDate(new Date()) //
                                               .triggerSecondly(1) //
                                               .build(configuration);

    }
}
