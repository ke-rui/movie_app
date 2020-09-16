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

package org.gradle.internal.vfs.watch;

import org.gradle.internal.snapshot.CompleteFileSystemLocationSnapshot;
import org.gradle.internal.snapshot.SnapshotHierarchy;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

public interface FileWatcherRegistry extends Closeable, SnapshotHierarchy.SnapshotDiffListener {

    interface ChangeHandler {
        void handleChange(Type type, Path path);

        void handleLostState();
    }

    enum Type {
        CREATED,
        MODIFIED,
        REMOVED,
        INVALIDATE
    }

    /**
     * {@inheritDoc}
     *
     * @throws WatchingNotSupportedException when the native watchers can't be updated.
     */
    @Override
    void changed(Collection<CompleteFileSystemLocationSnapshot> removedSnapshots, Collection<CompleteFileSystemLocationSnapshot> addedSnapshots);

    /**
     * Changes the must watch directories, e.g. when the same daemon is used on a different project.
     *
     * @throws WatchingNotSupportedException when the native watchers can't be updated.
     */
    void updateMustWatchDirectories(Collection<File> updatedWatchDirectories);

    /**
     * Get statistics about the received changes.
     */
    FileWatchingStatistics getAndResetStatistics();

    /**
     * Close the watcher registry. Stops watching without handling the changes.
     */
    @Override
    void close() throws IOException;

    interface FileWatchingStatistics {
        Optional<Throwable> getErrorWhileReceivingFileChanges();
        boolean isUnknownEventEncountered();
        int getNumberOfReceivedEvents();
    }
}
