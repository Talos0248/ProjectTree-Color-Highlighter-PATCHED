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

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

public class ActionUtils {

    @Nullable
    public static VirtualFile[] getFiles(@Nullable AnActionEvent actionEvent) {
        if (actionEvent == null) {
            return new VirtualFile[0]; // Return empty array if event is null
        }

        // Run the action off the EDT using ReadAction (which is safe to use for read operations)
        return ReadAction.compute(() -> {
            VirtualFile[] files = actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
            if (files == null || files.length == 0) {
                Logger.getInstance(ActionUtils.class).warn("No files selected in the context of the action event.");
                return new VirtualFile[0]; // Return empty array if no files are selected
            }
            return files;
        });
    }


    public static void setActionEnabled(AnActionEvent actionEvent, boolean enabled) {
        Presentation presentation = actionEvent.getPresentation();
        presentation.setEnabled(enabled);
    }
}