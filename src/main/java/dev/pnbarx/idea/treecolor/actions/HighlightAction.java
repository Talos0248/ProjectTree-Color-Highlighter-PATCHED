package dev.pnbarx.idea.treecolor.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import dev.pnbarx.idea.treecolor.services.ProjectStateService;
import dev.pnbarx.idea.treecolor.utils.ActionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class HighlightAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(HighlightAction.class);

    @SuppressWarnings({"UnusedDeclaration"}) // action must have a no-argument constructor
    public HighlightAction() {
        super();
    }

    public HighlightAction(@Nullable String name, @Nullable String description, @Nullable Icon icon) {
        super(name, description, icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        ProjectStateService projectStateService = ProjectStateService.getInstance(actionEvent);
        if (projectStateService == null) return;
        int colorId = getColorId();
        VirtualFile[] files = ActionUtils.getFiles(actionEvent);
        projectStateService.files.addNodes(files, colorId);
        projectStateService.saveState();
    }

    protected int getColorId() {
        Presentation presentation = getTemplatePresentation();
        try {
            //noinspection ConstantConditions
            return (int) presentation.getClientProperty("colorId");
        } catch (NullPointerException e) {
            LOG.debug("colorIndex is undefined");
            return -1;
        }
    }

    public void setColorId(int colorId) {
        Presentation presentation = getTemplatePresentation();
        presentation.putClientProperty("colorId", colorId);
    }

    @Override
    public ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}