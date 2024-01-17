/*
 * Created by JFormDesigner on Mon Aug 28 16:48:10 CST 2023
 */

package zju.cst.aces.IDEA.panel;


import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;
import net.miginfocom.swing.MigLayout;
import zju.cst.aces.IDEA.config.ProjectConfigPersistence;
import zju.cst.aces.IDEA.config.WindowConfig;
import zju.cst.aces.IDEA.config.WindowDefaultConfig;
import zju.cst.aces.IDEA.utils.ConnectUtil;
import zju.cst.aces.IDEA.utils.ProjectConfigFileUtil;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 项目级别的config（非全局）
 */
public class ProjectSettingPanel extends JFrame {
    public static File directory_static;
    public static String fileName_static;
    public ProjectSettingPanel(AnActionEvent event) {
        initComponents();
        setResizable(false);
        Project project = event.getProject();
        String directoryPath = project.getBasePath()+"/.idea"; // 替换为你的目录路径
        String fileName = project.getName()+".xml"; // 替换为要创建的文件名
        fileName_static=fileName;
        File directory = new File(directoryPath);
        directory_static=directory;
        File file = new File(directory, fileName);
        if(!file.exists()){
            try {
                ProjectConfigFileUtil.createConfigFile(directoryPath,fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Path path = Paths.get(directoryPath, fileName);
        ProjectConfigPersistence configPersistence = ProjectConfigFileUtil.loadProjectConfig(path.toFile().getPath());
        String[] apiKeys_per = configPersistence.apiKeys;
        String hostname_per = configPersistence.hostname;
        String port_per = configPersistence.port;
        Integer testNumber_per = configPersistence.testNumber;
        Integer maxRounds_per = configPersistence.maxRounds;
        Integer minErrorTokens_per = configPersistence.minErrorTokens;
        Integer model_index_per = configPersistence.model_index;
        Integer topP_per = configPersistence.topP;
        Double temperature_per = configPersistence.temperature;
        Integer frequencyPenalty_per = configPersistence.frequencyPenalty;
        Integer presencePenalty_per = configPersistence.presencePenalty;
        String tmpOutput_per = configPersistence.tmpOutput;
        Boolean stopWhenSuccess_per = configPersistence.stopWhenSuccess;
        Boolean enableMultithreading_per = configPersistence.enableMultithreading;
        Boolean noExecution_per = configPersistence.noExecution;
        Integer maxThreads_per = configPersistence.maxThreads;
        String testOutput_per = configPersistence.testOutput;
        Integer maxPromptTokens_per = configPersistence.maxPromptTokens;
        Boolean remind_repair_per=configPersistence.remind_repair;
        Boolean remind_regenerate_per=configPersistence.remind_regenerate;
        Boolean remind_compile_per=configPersistence.remind_compile;
        Integer notifyRepair_per=configPersistence.notifyRepair;
        apikey.setText(String.join(",",apiKeys_per));
        hostname.setText(hostname_per);
        port.setText(port_per);
        testNumber.setText(String.valueOf(testNumber_per));
        maxRounds.setText(String.valueOf(maxRounds_per));
        minErrorTokens.setText(String.valueOf(minErrorTokens_per));
        model.setSelectedIndex(model_index_per);
        topP.setText(String.valueOf(topP_per));
        temperature.setText(String.valueOf(temperature_per));
        frequencyPenalty.setText(String.valueOf(frequencyPenalty_per));
        presencePenalty.setText(String.valueOf(presencePenalty_per));
        tmpOutput.setText(tmpOutput_per);
        stopWhenSuccess.setSelected(stopWhenSuccess_per);
        enableMultithreading.setSelected(enableMultithreading_per);
        noExecution.setSelected(noExecution_per);
        maxThreads.setText( String.valueOf(maxThreads_per));
        testOutput.setText(testOutput_per);
        maxPromptTokens.setText(String.valueOf(maxPromptTokens_per));
        compileReminder.setSelected(remind_compile_per);
        regenerateReminder.setSelected(remind_regenerate_per);
        repairReminder.setSelected(remind_repair_per);
        notifyRepair.setSelectedIndex(notifyRepair_per);
        //replace name
        confirm();
    }
    private void confirm() {
        ProjectConfigPersistence configPersistence=new ProjectConfigPersistence();
        configPersistence.apiKeys = apikey.getText().split(",");
        configPersistence.hostname = hostname.getText();
        configPersistence.port = port.getText();
        configPersistence.testNumber = (testNumber.getText().equals("") ? WindowDefaultConfig.testNumber : Integer.parseInt(testNumber.getText()));
        configPersistence.maxRounds = (maxRounds.getText().equals("") ? WindowDefaultConfig.maxRounds : Integer.parseInt(maxRounds.getText()));
        configPersistence.minErrorTokens = (minErrorTokens.getText().equals("") ? WindowDefaultConfig.minErrorTokens : Integer.parseInt(minErrorTokens.getText()));
        configPersistence.topP = (topP.getText().equals("") ? WindowDefaultConfig.topP : Integer.parseInt(topP.getText()));
        configPersistence.temperature = (temperature.getText().equals("") ? WindowDefaultConfig.temperature : Double.parseDouble(temperature.getText()));
        configPersistence.frequencyPenalty = (frequencyPenalty.getText().equals("") ? WindowDefaultConfig.frequencyPenalty : Integer.parseInt(frequencyPenalty.getText()));
        configPersistence.presencePenalty = (presencePenalty.getText().equals("") ? WindowDefaultConfig.presencePenalty : Integer.parseInt(presencePenalty.getText()));
        configPersistence.tmpOutput = (tmpOutput.getText().equals("") ? WindowDefaultConfig.tmpOutput : tmpOutput.getText());
        configPersistence.testOutput = (testOutput.getText().equals("") ? WindowDefaultConfig.testOutput : testOutput.getText());
        configPersistence.maxPromptTokens = (maxPromptTokens.getText().equals("") ? WindowDefaultConfig.maxPromptTokens : Integer.parseInt(maxPromptTokens.getText()));
        configPersistence.model_index = model.getSelectedIndex();
        configPersistence.maxThreads = (maxThreads.getText().equals("") ? WindowDefaultConfig.maxThreads : Integer.parseInt(maxThreads.getText()));
        configPersistence.stopWhenSuccess = stopWhenSuccess.isSelected();
        configPersistence.enableMultithreading = enableMultithreading.isSelected();
        configPersistence.noExecution = noExecution.isSelected();
        configPersistence.remind_compile = compileReminder.isSelected();
        configPersistence.remind_regenerate = regenerateReminder.isSelected();
        configPersistence.remind_repair = repairReminder.isSelected();
        configPersistence.notifyRepair=notifyRepair.getSelectedIndex();
        //持久化project级别的config配置
        ProjectConfigFileUtil.modifyPersistence(configPersistence,new File(directory_static,fileName_static));//TODO:更新loadstate方法，参数为一个configPersistence对象

        WindowConfig.apiKeys = apikey.getText().split(",");
        WindowConfig.hostname = hostname.getText();
        WindowConfig.port = (port.getText().equals("") ? null : (port.getText()));
        WindowConfig.testNumber = (testNumber.getText().equals("") ? WindowDefaultConfig.testNumber : Integer.parseInt(testNumber.getText()));
        WindowConfig.maxRounds = (maxRounds.getText().equals("") ? WindowDefaultConfig.maxRounds : Integer.parseInt(maxRounds.getText()));
        WindowConfig.minErrorTokens = (minErrorTokens.getText().equals("") ? WindowDefaultConfig.minErrorTokens : Integer.parseInt(minErrorTokens.getText()));
        WindowConfig.topP = (topP.getText().equals("") ? WindowDefaultConfig.topP : Integer.parseInt(topP.getText()));
        WindowConfig.temperature = (temperature.getText().equals("") ? WindowDefaultConfig.temperature : Double.parseDouble(temperature.getText()));
        WindowConfig.frequencyPenalty = (frequencyPenalty.getText().equals("") ? WindowDefaultConfig.frequencyPenalty : Integer.parseInt(frequencyPenalty.getText()));
        WindowConfig.presencePenalty = (presencePenalty.getText().equals("") ? WindowDefaultConfig.presencePenalty : Integer.parseInt(presencePenalty.getText()));
        WindowConfig.tmpOutput = (tmpOutput.getText().equals("") ? WindowDefaultConfig.tmpOutput : tmpOutput.getText());
        WindowConfig.testOutput = (testOutput.getText().equals("") ? WindowDefaultConfig.testOutput : testOutput.getText());
        WindowConfig.maxPromptTokens = (maxPromptTokens.getText().equals("") ? WindowDefaultConfig.maxPromptTokens : Integer.parseInt(maxPromptTokens.getText()));
        WindowConfig.model_index = model.getSelectedIndex();
        WindowConfig.maxThreads = (maxThreads.getText().equals("") ? WindowDefaultConfig.maxThreads : Integer.parseInt(maxThreads.getText()));
        WindowConfig.stopWhenSuccess = stopWhenSuccess.isSelected();
        WindowConfig.enableMultithreading = enableMultithreading.isSelected();
        WindowConfig.noExecution = noExecution.isSelected();
        WindowConfig.compileReminder = compileReminder.isSelected();
        WindowConfig.repairReminder = repairReminder.isSelected();
        WindowConfig.regenerateReminder = regenerateReminder.isSelected();
        WindowConfig.notifyRepair = notifyRepair.getSelectedIndex();
        fillPanelInfoByConfig();
    }

    public void fillPanelInfoByConfig() {
        apikey.setText(String.join(",", WindowConfig.apiKeys));
        hostname.setText(WindowConfig.hostname);
        port.setText(WindowConfig.port);
        testNumber.setText(String.valueOf(WindowConfig.testNumber));
        maxRounds.setText(String.valueOf(WindowConfig.maxRounds));
        minErrorTokens.setText(String.valueOf(WindowConfig.minErrorTokens));
        model.setSelectedIndex(WindowConfig.model_index);
        topP.setText(String.valueOf(WindowConfig.topP));
        temperature.setText(String.valueOf(WindowConfig.temperature));
        frequencyPenalty.setText(String.valueOf(WindowConfig.frequencyPenalty));
        presencePenalty.setText(String.valueOf(WindowConfig.presencePenalty));
        tmpOutput.setText(WindowConfig.tmpOutput);
        stopWhenSuccess.setSelected(WindowConfig.stopWhenSuccess);
        enableMultithreading.setSelected(WindowConfig.enableMultithreading);
        noExecution.setSelected(WindowConfig.noExecution);
        maxThreads.setText(String.valueOf(WindowConfig.maxThreads));
        testOutput.setText(WindowConfig.testOutput);
        maxPromptTokens.setText(String.valueOf(WindowConfig.maxPromptTokens));
        repairReminder.setSelected(WindowConfig.repairReminder);
        compileReminder.setSelected(WindowConfig.compileReminder);
        regenerateReminder.setSelected(WindowConfig.regenerateReminder);
        notifyRepair.setSelectedIndex(WindowConfig.notifyRepair);
    }

    private void windowClosing(WindowEvent e) {
        confirm();
    }

    private void testConnection(ActionEvent e) {
        Application application = ApplicationManager.getApplication();
        application.executeOnPooledThread(() -> {
            boolean isConnected = ConnectUtil.testOpenApiConnection(apikey.getText().split(","), hostname.getText(), port.getText());
            SwingUtilities.invokeLater(()->{
                testStatus.setText("");
                if (isConnected) {
                    if(UIUtil.isUnderDarcula()){
                        testStatus.setForeground(Color.white);
                    }
                    else {
                        testStatus.setForeground(Color.GRAY);
                    }
                    testStatus.setText("connection successful");
                   /* ApplicationManager.getApplication().invokeLater(()->{
                        LoggerUtil.info("connect successful", );
                    });*/
                } else {
                    testStatus.setForeground(Color.red);
                    testStatus.setText("connection failed");
                }
            });
        });
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel5 = new JPanel();
        label2 = new JLabel();
        model = new JComboBox<>();
        label5 = new JLabel();
        topP = new JTextField();
        label6 = new JLabel();
        temperature = new JTextField();
        label9 = new JLabel();
        minErrorTokens = new JTextField();
        label10 = new JLabel();
        maxPromptTokens = new JTextField();
        label7 = new JLabel();
        frequencyPenalty = new JTextField();
        label8 = new JLabel();
        presencePenalty = new JTextField();
        panel11 = new JPanel();
        label17 = new JLabel();
        label18 = new JLabel();
        label19 = new JLabel();
        panel4 = new JPanel();
        label1 = new JLabel();
        apikey = new JTextField();
        label3 = new JLabel();
        hostname = new JTextField();
        label4 = new JLabel();
        port = new JTextField();
        testConnectionButton = new JButton();
        testStatus = new JLabel();
        panel6 = new JPanel();
        label14 = new JLabel();
        stopWhenSuccess = new JCheckBox();
        label15 = new JLabel();
        enableMultithreading = new JCheckBox();
        label16 = new JLabel();
        noExecution = new JCheckBox();
        label11 = new JLabel();
        maxThreads = new JTextField();
        label12 = new JLabel();
        testNumber = new JTextField();
        label13 = new JLabel();
        maxRounds = new JTextField();
        label21 = new JLabel();
        tmpOutput = new JTextField();
        label22 = new JLabel();
        testOutput = new JTextField();
        separator1 = new JSeparator();
        separator2 = new JSeparator();
        panel1 = new JPanel();
        label23=new JLabel();
        regenerateReminder = new JCheckBox();
        compileReminder = new JCheckBox();
        repairReminder = new JCheckBox();

        panel2 = new JPanel();
        label24 = new JLabel();
        notifyRepair = new JComboBox<>();
        label20=new JLabel();
        setTitle("ChatUniTest config(Current Project)");
        //======== panel5 ========
        {
            panel5.setLayout(new MigLayout(
                    "insets 0,hidemode 3,align center center,gap 5 5",
                    // columns
                    "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]",
                    // rows
                    "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]"));

            //---- label2 ----
            label2.setText("Model:");
            label2.setToolTipText(" The OpenAI model. Default: gpt-3.5-turbo");
            panel5.add(label2, "cell 0 0 2 1");

            //---- model ----
            model.setModel(new DefaultComboBoxModel<>(new String[] {
                    "gpt-3.5-turbo",
                    "gpt-4",
                    "codellama",
                    "ChatGLM"
            }));
            panel5.add(model, "cell 2 0 3 1");

            //---- label5 ----
            label5.setText("TopP:");
            label5.setToolTipText(" (Optional) The OpenAI API parameters. Default: 1");
            panel5.add(label5, "cell 0 1");
            panel5.add(topP, "cell 1 1 2 1");

            //---- label6 ----
            label6.setText("Temperature:");
            label6.setToolTipText("(Optional) The OpenAI API parameters. Default: 0.5");
            panel5.add(label6, "cell 3 1");
            panel5.add(temperature, "cell 4 1");

            //---- label9 ----
            label9.setText("MinErrorTokens:");
            label9.setToolTipText("(Optional) The minimum tokens of error message in the repair process. Default: 500");
            panel5.add(label9, "cell 0 2 2 1");
            panel5.add(minErrorTokens, "cell 2 2 3 1");

            //---- label10 ----
            label10.setText("MaxPromptTokens:");
            label10.setToolTipText("(Optional) The maximum tokens of prompt in the process. Default: 2600");
            panel5.add(label10, "cell 0 3 2 1");
            panel5.add(maxPromptTokens, "cell 2 3 3 1");

            //---- label7 ----
            label7.setText("FrequencyPenalty:");
            label7.setToolTipText("(Optional) The OpenAI API parameters. Default: 0");
            panel5.add(label7, "cell 0 4 2 1");
            panel5.add(frequencyPenalty, "cell 2 4 3 1");

            //---- label8 ----
            label8.setText("PresencePenalty:");
            label8.setToolTipText("(Optional) The OpenAI API parameters. Default: 0");
            panel5.add(label8, "cell 0 5 2 1");
            panel5.add(presencePenalty, "cell 2 5 3 1");
        }

        //======== this ========
        setIconImage(new ImageIcon(getClass().getResource("/icons/chatunitest.png")).getImage());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ProjectSettingPanel.this.windowClosing(e);
            }
        });
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel11 ========
        {
            if(UIUtil.isUnderDarcula()){
                panel11.setBackground(new Color(62, 67, 76));
            }
            else {
                panel11.setBackground(new Color(230, 235, 240));
            }
            panel11.setLayout(new MigLayout(
                    "filly,hidemode 3,align left center",
                    // columns
                    "[fill]",
                    // rows
                    "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]"));

            //---- label17 ----
            label17.setText("GPT Connection");
            label17.setFont(label17.getFont().deriveFont(label17.getFont().getStyle() | Font.BOLD));
            label17.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mouseClickButton1(e);
                }
            });
            panel11.add(label17, "cell 0 3,alignx left,growx 0");

            //---- label18 ----
            label18.setText("GPT Configuration");
            label18.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mouseClickButton2(e);
                }
            });
            panel11.add(label18, "cell 0 5,alignx left,growx 0");

            //---- label19 ----
            label19.setText("Test Settings");
            label19.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mouseClickButton3(e);
                }
            });
            panel11.add(label19, "cell 0 7,alignx left,growx 0");

            //---- label23 ----
            label23.setText("Confirmation");
            label23.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mouseClickButton4(e);
                }
            });
            panel11.add(label23, "cell 0 9");

            //---- label20 ----
            label20.setText("Notification");
            label20.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mouseClickButton5(e);
                }
            });
            panel11.add(label20, "cell 0 11");
        }
        contentPane.add(panel11);
        panel11.setBounds(0, 0, panel11.getPreferredSize().width, 293);

        //======== panel4 ========
        {
            panel4.setLayout(new MigLayout(
                    "hidemode 3,align center center",
                    // columns
                    "[]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]",
                    // rows
                    "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]"));

            //---- label1 ----
            label1.setText("Apikeys:");
            label1.setToolTipText("(Required) Apikeys of OpenAI, please split each apikey with ','");
            panel4.add(label1, "cell 4 2");
            panel4.add(apikey, "cell 5 2 9 1,growy");

            //---- label3 ----
            label3.setText("Host:");
            label3.setToolTipText("( Optional ) Host number of your proxy if you need");
            panel4.add(label3, "cell 4 3");
            panel4.add(hostname, "cell 5 3 9 1");

            //---- label4 ----
            label4.setText("Port:");
            label4.setToolTipText("( Optional ) Port number of your proxy if you need");
            panel4.add(label4, "cell 4 4");
            panel4.add(port, "cell 5 4 4 1");

            //---- testConnectionButton ----
            testConnectionButton.setText("test connection");
            testConnectionButton.addActionListener(e -> testConnection(e));
            panel4.add(testConnectionButton, "cell 4 6 3 1");
            panel4.add(testStatus, "cell 7 6 7 1");
        }
        contentPane.add(panel4);
        panel4.setBounds(121, 0, 347, 293);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right+40;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());

        //======== panel6 ========
        {
            panel6.setLayout(new MigLayout(
                    "insets 0,hidemode 3,align center center,gap 5 5",
                    // columns
                    "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]",
                    // rows
                    "[]" +
                            "[]" +
                            "[fill]" +
                            "[fill]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[fill]" +
                            "[]" +
                            "[]" +
                            "[]"));

            //---- label14 ----
            label14.setText("StopWhenSuccess");
            label14.setToolTipText("(Optional) Stop the repair process when the test passes");
            panel6.add(label14, "cell 0 2");

            //---- stopWhenSuccess ----
            stopWhenSuccess.setSelected(true);
            panel6.add(stopWhenSuccess, "cell 1 2,align center center,grow 0 0");

            //---- label15 ----
            label15.setText("EnableMultithreading");
            label15.setToolTipText(" (Optional) Enable multi-threaded execution ");
            panel6.add(label15, "cell 2 2");
            panel6.add(enableMultithreading, "cell 3 2,align center center,grow 0 0");

            //---- label16 ----
            label16.setText("NoExecution");
            label16.setToolTipText(" (Optional) Skip the execution verification step of generated tests");
            panel6.add(label16, "cell 0 3");
            panel6.add(noExecution, "cell 1 3,align center center,grow 0 0");
            panel6.add(separator1, "cell 0 4 4 1,gapx 0 0,gapy 5 5");

            //---- label11 ----
            label11.setText("MaxThreads:");
            label11.setToolTipText("(Optional) The maximum number of threads ");
            panel6.add(label11, "cell 0 5");
            panel6.add(maxThreads, "cell 1 5");

            //---- label12 ----(原来的TestNumber)
            label12.setText("TestRounds:");
            label12.setToolTipText("(Optional) The number of generating test rounds for each method. Default: 3");
            panel6.add(label12, "cell 2 5");
            panel6.add(testNumber, "cell 3 5");

            //---- label13 ----(原来的MaxRounds，更改为RepairRounds)
            label13.setText("RepairRounds:");
            label13.setToolTipText(" (Optional) The maximum rounds of the repair process. Default: 3");
            panel6.add(label13, "cell 0 6");
            panel6.add(maxRounds, "cell 1 6");
            panel6.add(separator2, "cell 0 7 4 1,gapx 0 0,gapy 5 5");

            //---- label21 ----
            label21.setText("tmpOutput:");
            label21.setToolTipText("(Optional) The output path for parsed information. Default: /tmp/chatunitest-info");
            panel6.add(label21, "cell 0 8");
            panel6.add(tmpOutput, "cell 1 8 2 1");

            //---- label22 ----
            label22.setText("testOutput:");
            label22.setToolTipText("(Optional) The output path for tests generated by ChatUniTest. Default: /chatunitest");
            panel6.add(label22, "cell 0 9");
            panel6.add(testOutput, "cell 1 9 2 1");
        }
        //panel1
        {
            panel1.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]",
                    // rows
                    "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]"));


            //---- checkBox1 ----
            compileReminder.setText(" Always remind to compile the entire project.");
            panel1.add(compileReminder, "cell 2 2");

            //---- checkBox2 ----
            regenerateReminder.setText(" Auto start new round when test fails. ");
            panel1.add(regenerateReminder, "cell 2 4");

            //---- checkBox3 ----
            repairReminder.setText(" Always ask to repair when test generate fails. ");
            panel1.add(repairReminder, "cell 2 6");
        }
        //======== panel2 ========
        {
            panel2.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]",
                    // rows
                    "[]" +
                            "[]" +
                            "[]" +
                            "[]"));

            //---- label24 ----
            label24.setText("Notify repair process:");
            panel2.add(label24, "cell 2 2");

            //---- comboBox1 ----
            notifyRepair.setModel(new DefaultComboBoxModel<>(new String[] {
                    "Always",
                    "Never"
            }));
            panel2.add(notifyRepair, "cell 3 2");
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel5;
    private JLabel label2;
    private JComboBox<String> model;
    private JLabel label5;
    private JTextField topP;
    private JLabel label6;
    private JTextField temperature;
    private JLabel label9;
    private JTextField minErrorTokens;
    private JLabel label10;
    private JTextField maxPromptTokens;
    private JLabel label7;
    private JTextField frequencyPenalty;
    private JLabel label8;
    private JTextField presencePenalty;
    private JPanel panel11;
    private JLabel label17;
    private JLabel label18;
    private JLabel label19;
    private JPanel panel4;
    private JLabel label1;
    private JTextField apikey;
    private JLabel label3;
    private JTextField hostname;
    private JLabel label4;
    private JTextField port;
    private JButton testConnectionButton;
    private JLabel testStatus;
    private JPanel panel6;
    private JLabel label14;
    private JCheckBox stopWhenSuccess;
    private JLabel label15;
    private JCheckBox enableMultithreading;
    private JLabel label16;
    private JCheckBox noExecution;
    private JLabel label11;
    private JTextField maxThreads;
    private JLabel label12;
    private JTextField testNumber;
    private JLabel label13;
    private JTextField maxRounds;
    private JLabel label21;
    private JTextField tmpOutput;
    private JLabel label22;
    private JTextField testOutput;
    private JSeparator separator1;
    private JSeparator separator2;

    private JLabel label23;
    private JPanel panel1;
    private JCheckBox regenerateReminder;
    private JCheckBox compileReminder;
    private JCheckBox repairReminder;

    private JPanel panel2;
    private JLabel label24;
    private JComboBox<String> notifyRepair;
    private JLabel label20;

    private void mouseClickButton1(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
            label18.setFont(label18.getFont().deriveFont(Font.PLAIN)); // 恢复为正常字体
            label19.setFont(label19.getFont().deriveFont(Font.PLAIN)); // 恢复为正常字体
            label17.setFont(label17.getFont().deriveFont(Font.BOLD)); // 设置为粗体
            label23.setFont(label23.getFont().deriveFont(Font.PLAIN)); // 设置为粗体
            label20.setFont(label20.getFont().deriveFont(Font.PLAIN));


            // 移除其他面板
            getContentPane().remove(panel5);
            getContentPane().remove(panel6);
            getContentPane().remove(panel1);
            getContentPane().remove(panel2);

            // 设置 panel4 的位置和大小
            panel4.setBounds(140, 0, 347, 293);

            // 添加 panel4
            getContentPane().add(panel4);

            revalidate();
            repaint();
        });
    }

    private void mouseClickButton2(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
            label17.setFont(label17.getFont().deriveFont(Font.PLAIN)); // 恢复为正常字体
            label19.setFont(label19.getFont().deriveFont(Font.PLAIN)); // 恢复为正常字体
            label18.setFont(label18.getFont().deriveFont(Font.BOLD)); // 设置为粗体
            label23.setFont(label23.getFont().deriveFont(Font.PLAIN)); // 设置为粗体
            label20.setFont(label20.getFont().deriveFont(Font.PLAIN));


            // 移除其他面板
            getContentPane().remove(panel4);
            getContentPane().remove(panel6);
            getContentPane().remove(panel1);
            getContentPane().remove(panel2);

            // 设置 panel5 的位置和大小
            panel5.setBounds(60, 0, 500, 293);

            // 添加 panel5
            getContentPane().add(panel5);

            revalidate();
            repaint();
        });
    }

    private void mouseClickButton3(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
            label17.setFont(label17.getFont().deriveFont(Font.PLAIN)); // 恢复为正常字体
            label18.setFont(label18.getFont().deriveFont(Font.PLAIN)); // 恢复为正常字体
            label19.setFont(label19.getFont().deriveFont(Font.BOLD)); // 设置为粗体
            label23.setFont(label23.getFont().deriveFont(Font.PLAIN)); // 设置为粗体
            label20.setFont(label20.getFont().deriveFont(Font.PLAIN));


            // 移除其他面板
            getContentPane().remove(panel4);
            getContentPane().remove(panel5);
            getContentPane().remove(panel1);
            getContentPane().remove(panel2);

            // 设置 panel6 的位置和大小
            panel6.setBounds(80, 0, 480, 293);

            // 添加 panel6
            getContentPane().add(panel6);

            revalidate();
            repaint();
        });
    }
    private void mouseClickButton4(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
            label17.setFont(label17.getFont().deriveFont(Font.PLAIN)); // 恢复为正常字体
            label18.setFont(label18.getFont().deriveFont(Font.PLAIN)); // 恢复为正常字体
            label19.setFont(label19.getFont().deriveFont(Font.PLAIN)); // 恢复为正常字体
            label23.setFont(label23.getFont().deriveFont(Font.BOLD)); // 设置为粗体
            label20.setFont(label20.getFont().deriveFont(Font.PLAIN));

            // 移除其他面板
            getContentPane().remove(panel4);
            getContentPane().remove(panel5);
            getContentPane().remove(panel6);
            getContentPane().remove(panel2);

            // 设置 panel1 的位置和大小
            panel1.setBounds(130, 50, 350, 293);

            // 添加 panel6
            getContentPane().add(panel1);

            revalidate();
            repaint();
        });
    }
    private void mouseClickButton5(MouseEvent e){
        label17.setFont(label17.getFont().deriveFont(Font.PLAIN)); // 恢复为正常字体
        label18.setFont(label18.getFont().deriveFont(Font.PLAIN)); // 恢复为正常字体
        label19.setFont(label19.getFont().deriveFont(Font.PLAIN)); // 恢复为正常字体
        label23.setFont(label23.getFont().deriveFont(Font.PLAIN)); // 设置为粗体
        label20.setFont(label20.getFont().deriveFont(Font.BOLD));
        getContentPane().remove(panel4);
        getContentPane().remove(panel5);
        getContentPane().remove(panel6);
        getContentPane().remove(panel1);
        // 设置 panel1 的位置和大小
        panel2.setBounds(130, 50, 350, 293);

        // 添加 panel6
        getContentPane().add(panel2);

        revalidate();
        repaint();
    }
}
