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

import datadog.opentracing.DDTracer;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import javax.inject.Singleton;

@Factory
@Requires(beans = DatadogConfiguration.class)
@Requires(classes = DDTracer.class)
@Requires(classes = GlobalTracer.class)
@Requires(classes = datadog.trace.api.GlobalTracer.class)
public class DatadogTracerFactory {
    private DatadogConfiguration configuration;

    public DatadogTracerFactory(DatadogConfiguration configuration) {
        this.configuration = configuration;
    }

    @Singleton
    @Primary
    public Tracer datadogTracer() {
        if (!GlobalTracer.isRegistered()) {
            DDTracer tracer = new DDTracer(configuration.getConfig());
            GlobalTracer.register(tracer);
            datadog.trace.api.GlobalTracer.registerIfAbsent(tracer);
        }
        return GlobalTracer.get();
    }
}
