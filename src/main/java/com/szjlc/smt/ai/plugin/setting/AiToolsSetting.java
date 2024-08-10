package com.szjlc.smt.ai.plugin.setting;

import lombok.Data;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * @author raito
 * @date 2024/07/28
 */
@Data
public class AiToolsSetting {
    private JPanel       setting;
    private JRadioButton aliRadioButton;
    private JTextField alihostTextField;
    private JTextField aliApiKeyTextField;

    private JRadioButton baiduRadioButton;
    private JTextField   baiduAuthHostTestField;
    private JTextField baiduApiKeyTextField;
    private JTextField baidduApiHostTextField;
    private JTextField baiduApiSecertKey;

}
