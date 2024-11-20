package dev.pnbarx.idea.treecolor.services;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.xmlb.XmlSerializerUtil;
import dev.pnbarx.idea.treecolor.state.beans.AppState;
import dev.pnbarx.idea.treecolor.state.beans.ColorSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@State(name = "ProjectTreeColorHighlighter", storages = {@Storage("projectTreeColorHighlighter.xml")})
public class AppStateService implements PersistentStateComponent<AppState> {

    private static final Logger LOG = Logger.getInstance(AppStateService.class);

    private final AppState state = new AppState();

    public static AppStateService getInstance() {
        return ApplicationManager.getApplication().getService(AppStateService.class);
    }

    @Nullable
    @Override
    public AppState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull AppState state) {
        XmlSerializerUtil.copyBean(state, this.state);
        LOG.debug("AppState loaded");
    }

    @Nullable
    public List<ColorSettings> getColorSettingsList() {
        return state.colorSettingsList.stream()
                .map(ColorSettings::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void setColorSettingsList(@NotNull List<ColorSettings> colorSettingsList) {
        state.colorSettingsList = colorSettingsList.stream()
                .map(ColorSettings::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Nullable
    public String getMarksForCollapsedHighlights() {
        return state.marksForCollapsedHighlights;
    }

    public void setMarksForCollapsedHighlights(String marks) {
        state.marksForCollapsedHighlights = marks;
    }


}