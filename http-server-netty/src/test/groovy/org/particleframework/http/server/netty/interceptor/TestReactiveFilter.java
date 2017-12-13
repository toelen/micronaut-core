/*
 * Copyright 2017 original authors
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
package org.particleframework.http.server.netty.interceptor;

import io.reactivex.Flowable;
import org.particleframework.http.HttpRequest;
import org.particleframework.http.MutableHttpResponse;
import org.particleframework.http.annotation.Filter;
import org.particleframework.http.filter.HttpServerFilter;
import org.reactivestreams.Publisher;

/**
 * @author Graeme Rocher
 * @since 1.0
 */
@Filter("/secure**")
public class TestReactiveFilter implements HttpServerFilter{

    @Override
    public int getOrder() {
        return TestSecurityFilter.POSITION - 10;
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        SomeService someService = new SomeService();
        return someService
                    .getSomething()
                    .switchMap(s -> {
                        request.getAttributes().put("SomeServiceValue", s);
                        return chain.proceed(request);
                    });
    }

    class SomeService {
        Flowable<String> getSomething() {
            return Flowable.just("Test");
        }
    }
}
