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

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ContextPathResolver {

    private final Environment environment;

    public ContextPathResolver(Environment environment) {
        this.environment = environment;
    }

    public String resolve() {
        String configuredPath = environment.getProperty("server.servlet.context-path");
        if (!StringUtils.hasText(configuredPath)) {
            configuredPath = environment.getProperty("server.servlet.contextPath");
        }
        return normalize(configuredPath);
    }

    static String normalize(String contextPath) {
        if (!StringUtils.hasText(contextPath) || "/".equals(contextPath.trim())) {
            return "/";
        }

        String normalized = contextPath.trim();
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        while (normalized.endsWith("/") && normalized.length() > 1) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
