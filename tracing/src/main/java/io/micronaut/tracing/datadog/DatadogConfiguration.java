/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.tracing.datadog;

import datadog.trace.api.Config;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.core.util.Toggleable;
import io.micronaut.runtime.ApplicationConfiguration;

import java.util.Properties;

import static io.micronaut.tracing.datadog.DatadogConfiguration.PREFIX;

@ConfigurationProperties(PREFIX)
@Requires(property = "tracing.datadog.enabled", value = "true")
public class DatadogConfiguration implements Toggleable {
    /**
     * The configuration prefix.
     */
    public static final String PREFIX = "tracing.datadog";

    /**
     * The default enable value.
     */
    @SuppressWarnings("WeakerAccess")
    private static Boolean DEFAULT_ENABLED = false;

    private String agentHost = Config.DEFAULT_AGENT_HOST;
    private Integer agentPort = Config.DEFAULT_TRACE_AGENT_PORT;
    private String serviceName;
    private Boolean enabled = DEFAULT_ENABLED;

    public DatadogConfiguration(ApplicationConfiguration applicationConfiguration) {
        serviceName = applicationConfiguration.getName().orElse(Environment.DEFAULT_NAME);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enable/Disable Datadog. Default value ({@value #DEFAULT_ENABLED}).
     *
     * @param enabled A boolean to enable/disabled Datadog
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Sets the agent port. Default value ({@value Config#DEFAULT_TRACE_AGENT_PORT}).
     *
     * @param agentPort An integer port
     */
    public void setAgentPort(int agentPort) {
        this.agentPort = agentPort;
    }

    /**
     * @return agent port
     */
    public int getAgentPort() {
        return agentPort;
    }

    /**
     * Sets the tracing agent host. Default value ({@value Config#DEFAULT_AGENT_HOST}).
     *
     * @param agentHost agent host
     */
    public void setAgentHost(String agentHost) {
        this.agentHost = agentHost;
    }

    /**
     * @return agent host
     */
    public String getAgentHost() {
        return agentHost;
    }

    public Config getConfig() {
        final Properties properties = new Properties();
        properties.setProperty(Config.TRACE_AGENT_PORT, agentPort.toString());
        properties.setProperty(Config.AGENT_HOST, agentHost);
        properties.setProperty(Config.SERVICE_NAME, serviceName);
        properties.setProperty(Config.TRACE_ENABLED, Boolean.toString(enabled));
        return Config.get(properties);
    }
}
