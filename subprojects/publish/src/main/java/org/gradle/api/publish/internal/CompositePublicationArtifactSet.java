/*
 * Copyright 2018 the original author or authors.
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

package org.gradle.api.publish.internal;

import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.CollectionCallbackActionDecorator;
import org.gradle.api.internal.CompositeDomainObjectSet;
import org.gradle.api.internal.DelegatingDomainObjectSet;
import org.gradle.api.internal.collections.DomainObjectCollectionFactory;
import org.gradle.api.internal.file.UnionFileCollection;
import org.gradle.api.publish.PublicationArtifact;

import java.util.List;

public class CompositePublicationArtifactSet<T extends PublicationArtifact> extends DelegatingDomainObjectSet<T> implements PublicationArtifactSet<T> {

    private final FileCollection files;

    public CompositePublicationArtifactSet(Class<T> type, DomainObjectCollectionFactory collectionFactory, CollectionCallbackActionDecorator callbackActionDecorator, List<? extends PublicationArtifactSet<T>> artifactSets) {
        super(CompositeDomainObjectSet.create(type, collectionFactory, callbackActionDecorator, artifactSets));
        FileCollection[] fileCollections = new FileCollection[artifactSets.size()];
        for (int i = 0; i < artifactSets.size(); i++) {
            fileCollections[i] = artifactSets.get(i).getFiles();
        }
        files = new UnionFileCollection(fileCollections);
    }

    @Override
    public FileCollection getFiles() {
        return files;
    }
}
