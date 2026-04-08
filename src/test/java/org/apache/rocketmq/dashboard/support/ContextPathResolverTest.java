/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rocketmq.dashboard.support;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.env.MockEnvironment;

public class ContextPathResolverTest {

    @Test
    public void shouldUseKebabCasePropertyFirst() {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("server.servlet.context-path", "/rocketmq-console")
                .withProperty("server.servlet.contextPath", "/legacy-path");

        ContextPathResolver resolver = new ContextPathResolver(environment);

        Assert.assertEquals("/rocketmq-console", resolver.resolve());
    }

    @Test
    public void shouldSupportCamelCaseProperty() {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("server.servlet.contextPath", "/rocketmq-console");

        ContextPathResolver resolver = new ContextPathResolver(environment);

        Assert.assertEquals("/rocketmq-console", resolver.resolve());
    }

    @Test
    public void shouldNormalizeEmptyAndMissingValuesToRoot() {
        Assert.assertEquals("/", new ContextPathResolver(new MockEnvironment()).resolve());

        MockEnvironment environment = new MockEnvironment()
                .withProperty("server.servlet.contextPath", "");
        Assert.assertEquals("/", new ContextPathResolver(environment).resolve());
    }

    @Test
    public void shouldNormalizeContextPathShape() {
        Assert.assertEquals("/", ContextPathResolver.normalize("/"));
        Assert.assertEquals("/rocketmq-console", ContextPathResolver.normalize("rocketmq-console"));
        Assert.assertEquals("/rocketmq-console", ContextPathResolver.normalize("/rocketmq-console/"));
    }
}
