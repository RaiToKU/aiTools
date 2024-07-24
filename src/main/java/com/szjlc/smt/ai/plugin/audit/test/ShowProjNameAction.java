package com.szjlc.smt.ai.plugin.audit.test;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * @author lea
 * @description
 * @history 2024-07-24 23:19 created by lea
 * @since 2024-07-24 23:19
 */
public class ShowProjNameAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getProject();
        Messages.showMessageDialog("项目名称为：" + project.getName(), "这是标题", Messages.getInformationIcon());
    }
}
