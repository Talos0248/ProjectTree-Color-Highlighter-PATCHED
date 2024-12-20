package dev.pnbarx.idea.treecolor.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import dev.pnbarx.idea.treecolor.services.ProjectStateService;
import dev.pnbarx.idea.treecolor.state.beans.ColorSettings;
import dev.pnbarx.idea.treecolor.ui.components.ColoredCircleIcon;
import dev.pnbarx.idea.treecolor.utils.ActionUtils;
import dev.pnbarx.idea.treecolor.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class HighlightActionGroup extends ActionGroup {

    private static final Logger LOG = Logger.getInstance(HighlightActionGroup.class);

    @Override
    public void update(@NotNull AnActionEvent actionEvent) {
        Presentation presentation = actionEvent.getPresentation();
        presentation.setEnabledAndVisible(isEnabled(actionEvent));
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent actionEvent) {
        ArrayList<AnAction> actionGroupItems = new ArrayList<>();
        ProjectStateService projectStateService = ProjectStateService.getInstance(actionEvent);

        if (isEnabled(actionEvent) && projectStateService != null) {
            actionGroupItems.addAll(getHighlightActions(projectStateService, actionEvent));
            actionGroupItems.add(new Separator()); // -------
            actionGroupItems.add(new ClearAction());
            actionGroupItems.add(new ClearRecursivelyAction());
            actionGroupItems.add(new Separator()); // -------
            actionGroupItems.add(new DefineColorsAction());
        }

        return actionGroupItems.toArray(new AnAction[0]);
    }

    @NotNull
    private ArrayList<AnAction> getHighlightActions(@NotNull ProjectStateService projectStateService, @NotNull AnActionEvent actionEvent) {
        ArrayList<AnAction> highlightActions = new ArrayList<>();

        VirtualFile[] files = ActionUtils.getFiles(actionEvent);
        boolean isHighlighted = projectStateService.files.isHighlighted(files);
        boolean isHighlightedRecursively = projectStateService.files.isHighlightedRecursively(files);

        projectStateService.colors.getColorSettingsList().stream()
                .filter(ColorSettings::isSetAndEnabled)
                .forEach(colorSettings -> {
                    int colorId = colorSettings.getId();

                    ColoredCircleIcon.Mode iconMode = ColoredCircleIcon.Mode.DEFAULT;
                    if (isHighlighted && projectStateService.files.isHighlightedInColor(files, colorId)) {
                        iconMode = ColoredCircleIcon.Mode.HIGHLIGHTED;
                    } else if (isHighlightedRecursively && projectStateService.files.isHighlightedInColorRecursively(files, colorId)) {
                        iconMode = ColoredCircleIcon.Mode.HIGHLIGHTED_RECURSIVELY;
                    }

                    HighlightAction action = new HighlightAction(
                            StringUtils.getSafeLabelString(colorSettings.getName(), " ", 50),
                            "Highlight in color #" + colorId,
                            ColoredCircleIcon.getInstance(colorSettings.getColor(), iconMode)
                    );
                    action.setColorId(colorId);

                    highlightActions.add(action);
                });

        return highlightActions;
    }

    private boolean isEnabled(@Nullable AnActionEvent actionEvent) {
        return actionEvent != null && ActionUtils.getFiles(actionEvent) != null;
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}