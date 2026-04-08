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

const STORAGE_KEY = 'contextPath';

const normalizeContextPath = (contextPath) => {
    if (!contextPath || !contextPath.trim() || contextPath.trim() === '/') {
        return '';
    }

    let normalized = contextPath.trim();
    if (!normalized.startsWith('/')) {
        normalized = `/${normalized}`;
    }
    while (normalized.endsWith('/') && normalized.length > 1) {
        normalized = normalized.slice(0, -1);
    }
    return normalized;
};

const deriveBasePathFromLocation = () => normalizeContextPath(window.location.pathname);

const getStoredBasePath = () => normalizeContextPath(window.sessionStorage.getItem(STORAGE_KEY) || '');

export const setRuntimeBasePath = (contextPath) => {
    const normalized = normalizeContextPath(contextPath);
    if (normalized) {
        window.sessionStorage.setItem(STORAGE_KEY, normalized);
    } else {
        window.sessionStorage.removeItem(STORAGE_KEY);
    }
    return normalized;
};

export const getRuntimeBasePath = (contextPath) => {
    const explicitPath = normalizeContextPath(contextPath || '');
    if (explicitPath) {
        return explicitPath;
    }

    const locationPath = deriveBasePathFromLocation();
    if (locationPath) {
        return locationPath;
    }
    return getStoredBasePath();
};

export const buildAppUrl = (path = '/', contextPath) => {
    const basePath = getRuntimeBasePath(contextPath);
    const normalizedPath = path === '/' ? '/' : `/${path.replace(/^\/+/, '')}`;
    return basePath ? `${basePath}${normalizedPath}` : normalizedPath;
};

export const buildApiUrl = (path, contextPath) => {
    const normalizedPath = path.startsWith('/') ? path : `/${path}`;
    return `${window.location.origin}${buildAppUrl(normalizedPath, contextPath)}`;
};
