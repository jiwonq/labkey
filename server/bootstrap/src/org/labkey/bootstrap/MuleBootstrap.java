/*
 * Copyright (c) 2008 LabKey Corporation
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
package org.labkey.bootstrap;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.File;

/*
* User: jeckels
* Date: Jun 26, 2008
*/
public class MuleBootstrap
{
    public static void main(String... rawArgs) throws Exception
    {
        PipelineBootstrapConfig config = null;
        try
        {
            config = new PipelineBootstrapConfig(rawArgs);
        }
        catch (ConfigException e)
        {
            printUsage(e.getMessage());
        }

        ClassLoader classLoader = config.getClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);

        Class runnerClass = classLoader.loadClass("org.labkey.pipeline.mule.MuleStartup");
        Object runner = runnerClass.newInstance();
        Method runMethod = runnerClass.getMethod("run", String[].class, String[].class);

        runMethod.invoke(runner, config.getSpringConfigPaths(), config.getProgramArgs());
    }

    private static void printUsage(String error)
    {
        if (error != null)
        {
            System.err.println(error);
            System.err.println();
        }

        System.err.println("java " + MuleBootstrap.class + " [-" + PipelineBootstrapConfig.MODULES_DIR + "=<MODULE_DIR>] [-" + PipelineBootstrapConfig.WEBAPP_DIR + "=<WEBAPP_DIR>] [-" + PipelineBootstrapConfig.CONFIG_DIR + "=<CONFIG_DIR>]");

        System.exit(1);
    }
}