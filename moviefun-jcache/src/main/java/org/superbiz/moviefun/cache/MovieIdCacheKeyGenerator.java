/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.superbiz.moviefun.cache;

import org.jsr107.ri.annotations.DefaultGeneratedCacheKey;
import org.superbiz.moviefun.persistence.Movie;

import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.annotation.Annotation;

public class MovieIdCacheKeyGenerator implements CacheKeyGenerator {
    @Override
    public GeneratedCacheKey generateCacheKey(final CacheKeyInvocationContext<? extends Annotation> cacheKeyInvocationContext) {


        final CacheInvocationParameter[] allParameters = cacheKeyInvocationContext.getAllParameters();
        for (final CacheInvocationParameter parameter : allParameters) {
            if (Movie.class.equals(parameter.getRawType())) {
                final Movie movie = Movie.class.cast(parameter.getValue());
                return new DefaultGeneratedCacheKey(new Object[] { movie.getId() });
            }
        }

        throw new IllegalArgumentException("No movie argument found in method signature");
    }
}
