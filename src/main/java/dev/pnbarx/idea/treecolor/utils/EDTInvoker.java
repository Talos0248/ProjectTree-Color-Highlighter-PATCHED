/*
 * Copyright (c) 2018-2020 Pavel Barykin.
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

package dev.pnbarx.idea.treecolor.utils;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.concurrency.Invoker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.concurrency.CancellablePromise;

public class EDTInvoker {

    private static final Logger LOG = Logger.getInstance(EDTInvoker.class);

    public static CancellablePromise<?> invoke(@NotNull Runnable task) {
        return invokeLater(task, 0); // Default to no delay
    }

    public static CancellablePromise<?> invokeLater(@NotNull Runnable task, int delay) {
        // Using ApplicationManager's invokeLater to schedule the task on the EDT
        if (delay > 0) {
            // If a delay is specified, use invokeLater with delay
            ApplicationManager.getApplication().invokeLater(task);
        } else {
            // Without delay
            ApplicationManager.getApplication().invokeLater(task);
        }
        return null;  // This method doesn't return a CancellablePromise, since invokeLater doesn't directly support promises.
    }
}
