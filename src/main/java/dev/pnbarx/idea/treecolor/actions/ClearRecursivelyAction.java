package dev.pnbarx.idea.treecolor.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import dev.pnbarx.idea.treecolor.services.ProjectStateService;
import dev.pnbarx.idea.treecolor.utils.ActionUtils;
import org.jetbrains.annotations.NotNull;

public class ClearRecursivelyAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(ClearRecursivelyAction.class);

    public ClearRecursivelyAction() {
        super(
                "Clear Recursively",
                "Remove highlighting recursively",
                null
        );
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        VirtualFile[] files = ActionUtils.getFiles(actionEvent);
        ProjectStateService projectStateService = ProjectStateService.getInstance(actionEvent);

        if (projectStateService != null) {
            projectStateService.files.removeNodesRecursively(files);
            projectStateService.saveState();
        }
    }

    @Override
    public void update(@NotNull AnActionEvent actionEvent) {
        ProjectStateService projectStateService = ProjectStateService.getInstance(actionEvent);
        VirtualFile[] files = ActionUtils.getFiles(actionEvent);

        if (projectStateService != null && projectStateService.files.isHighlightedRecursively(files)) {
            ActionUtils.setActionEnabled(actionEvent, true);
        } else {
            ActionUtils.setActionEnabled(actionEvent, false);
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}