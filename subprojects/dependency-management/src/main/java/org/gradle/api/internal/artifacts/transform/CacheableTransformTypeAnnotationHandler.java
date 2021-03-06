/*
 * Copyright 2019 the original author or authors.
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

package org.gradle.api.internal.artifacts.transform;

import org.gradle.api.artifacts.transform.CacheableTransform;
import org.gradle.api.artifacts.transform.TransformAction;
import org.gradle.api.internal.tasks.properties.annotations.TypeAnnotationHandler;
import org.gradle.internal.reflect.TypeValidationContext;

import java.lang.annotation.Annotation;

import static org.gradle.internal.reflect.TypeValidationContext.Severity.ERROR;

public class CacheableTransformTypeAnnotationHandler implements TypeAnnotationHandler {
    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return CacheableTransform.class;
    }

    @Override
    public void validateTypeMetadata(Class<?> classWithAnnotationAttached, TypeValidationContext visitor) {
        if (!TransformAction.class.isAssignableFrom(classWithAnnotationAttached)) {
            visitor.visitTypeProblem(ERROR,
                classWithAnnotationAttached,
                String.format("Cannot use @%s on type. This annotation can only be used with %s types",
                    getAnnotationType().getSimpleName(), TransformAction.class.getSimpleName()));
        }
    }
}
