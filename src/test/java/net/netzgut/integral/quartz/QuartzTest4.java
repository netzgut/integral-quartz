/**
 * Copyright 2016 Netzgut GmbH <info@netzgut.net>
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
