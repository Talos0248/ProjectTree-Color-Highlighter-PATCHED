package dev.pnbarx.idea.treecolor.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import dev.pnbarx.idea.treecolor.services.ProjectStateService;
import dev.pnbarx.idea.treecolor.utils.ActionUtils;
import org.jetbrains.annotations.NotNull;

public class ClearAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(ClearAction.class);

    public ClearAction() {
        super(
                "Clear",
                "Remove highlighting",
                null
        );
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        VirtualFile[] files = ActionUtils.getFiles(actionEvent);
        ProjectStateService projectStateService = ProjectStateService.getInstance(actionEvent);

        if (projectStateService != null) {
            projectStateService.files.removeNodes(files);
            projectStateService.saveState();
        }
    }

    @Override
    public void update(@NotNull AnActionEvent actionEvent) {
        ProjectStateService projectStateService = ProjectStateService.getInstance(actionEvent);
        VirtualFile[] files = ActionUtils.getFiles(actionEvent);

        if (projectStateService != null && projectStateService.files.isHighlighted(files)) {
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