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

public class QuartzTest3 {

    public static void main(String[] args) {

        System.out.println("starting CLUSTER test");
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getLogger("net.netzgut").setLevel(Level.DEBUG);
        initDb();
        new QuartzTest3().start();
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
                if (line != null) {
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

    private synchronized void start() {

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

}
