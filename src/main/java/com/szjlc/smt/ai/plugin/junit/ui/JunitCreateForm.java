package com.szjlc.smt.ai.plugin.junit.ui;

import lombok.Data;

import javax.swing.JButton;
import javax.swing.JTextArea;

/**
 * @author raito
 * @date 2024/08/03
 */
@Data
public class JunitCreateForm {
    private JTextArea NeededDescriptionTestArea;
    private JTextArea JunitDesciptionTextArea;
    private JButton   ReviseJunitDescription;
    private JTextArea JunitCreateDesciption;
    private JButton ReviseJunitButton;
    private JButton JunitAssistantButton;
}
