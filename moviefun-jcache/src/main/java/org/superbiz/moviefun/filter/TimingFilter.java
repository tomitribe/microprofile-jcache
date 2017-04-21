/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.superbiz.moviefun.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Quick and simple filter
 */
@WebFilter(filterName = "BasicTimingFilter", urlPatterns = "/*")
public class TimingFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {

            final long start = System.currentTimeMillis();

            chain.doFilter(request, response);

            final long end = System.currentTimeMillis();
            final long timeTaken = end - start;

            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            final String method = httpServletRequest.getMethod();
            final String requestURI = httpServletRequest.getRequestURI();

            System.out.println(method + " " + requestURI + " completed in " + timeTaken + "ms");
        }
    }

    @Override
    public void destroy() {

    }
}
