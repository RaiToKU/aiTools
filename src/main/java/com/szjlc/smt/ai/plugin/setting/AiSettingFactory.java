package com.szjlc.smt.ai.plugin.setting;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

/**
 * @author raito
 * @date 2024/07/29
 */
public class AiSettingFactory implements SearchableConfigurable {

    private AiToolsSetting aiToolsSetting = new AiToolsSetting();

    @Override
    public @NotNull @NonNls String getId() {
        return "test.id";
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "ai-assistant-config";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return aiToolsSetting.getSetting();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
