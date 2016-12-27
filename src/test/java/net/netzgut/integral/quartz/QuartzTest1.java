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

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

public class QuartzTest1 {

    @Test(timeOut = 5 * 1000)
    public void basicTest() throws InterruptedException {
        RegistryBuilder builder = new RegistryBuilder().add(QuartzTestModule1.class);
        Registry registry = builder.build();
        registry.performRegistryStartup();

        Thread.sleep(2000);

        Assert.assertTrue(TestJob1.RUN);
        TestJob1.RUN = false;
        registry.shutdown();
    }

}
