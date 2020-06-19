/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.plugin.use

import org.gradle.integtests.fixtures.ToBeFixedForInstantExecution
import org.gradle.test.fixtures.file.LeaksFileHandles
import spock.lang.Issue

@LeaksFileHandles
class PluginUseClassLoadingIntegrationSpec extends AbstractPluginSpec {

    @ToBeFixedForInstantExecution
    def "plugin classes are reused if possible"() {
        given:
        publishPlugin()
        settingsFile << """
            include "p1"
            include "p2"
        """

        when:
        file("p1/build.gradle") << USE
        file("p2/build.gradle") << USE

        buildScript """
            evaluationDependsOnChildren()
            task verify {
                doLast {
                    project(":p1").pluginClass.is(project(":p2").pluginClass)
                }
            }
        """

        then:
        succeeds "verify"
    }

    @Issue("GRADLE-3503")
    @ToBeFixedForInstantExecution
    def "Context classloader contains plugin classpath during application"() {
        publishPlugin("""
            def className = getClass().getName()
            Thread.currentThread().getContextClassLoader().loadClass(className)
            project.task("verify")
        """)

        buildScript USE

        expect:
        succeeds("verify")
    }

    void publishPlugin() {
        publishPlugin("project.ext.pluginApplied = true; project.ext.pluginClass = getClass()")
    }
}
