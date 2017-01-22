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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.testng.annotations.Test;

public class QuartzTest3 {

    @Test(timeOut = 20 * 1000)
    public void testCluster() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getLogger("net.netzgut").setLevel(Level.DEBUG);
        initDb();
        try {
            Registry registry = startRegistry();
            Thread.sleep(5000);
            registry.shutdown();
            registry = startRegistry();
            Thread.sleep(5000);
            registry.shutdown();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Registry startRegistry() {
        RegistryBuilder builder = new RegistryBuilder().add(QuartzTestModule3.class);
        Registry registry = builder.build();
        registry.performRegistryStartup();
        return registry;
    }

    private static void initDb() {
        /* initialize database */
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
             BufferedReader reader =
                 new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader()
                                                                .getResourceAsStream("quartz_tables_hsql.sql")));
             Statement st = c.createStatement()) {
            StringBuilder bldr = new StringBuilder();
            String line;
            do {
                line = reader.readLine();
                if (line != null && line.startsWith("--") == false) {
                    bldr.append(line).append("\n");
                }
            }
            while (line != null);
            String[] statements = bldr.toString().split(";");

            for (String statement : Arrays.asList(statements)) {
                if (statement != null && statement.trim().length() > 0) {
                    System.out.println("Executing statement: '" + statement + "'");
                    st.execute(statement);
                }
            }
        }
        catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
