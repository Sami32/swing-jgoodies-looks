/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.jgoodies.looks.demo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.jgoodies.clearlook.ClearLookManager;
import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.windows.ExtWindowsLookAndFeel;

/** 
 * Demonstrates how to use the jGoodies Looks. Therefore, 
 * it provides several panels, that comprise a variety of 
 * Swing widgets in different configurations.<p>
 * 
 * Also, this frame contains examples for Swing misuse,
 * that can be automatically corrected by ClearLook.
 * 
 * @author Karsten Lentzsch
 */
public class DemoFrame extends JFrame {

    protected static final Dimension PREFERRED_SIZE =
        LookUtils.isLowRes ? new Dimension(650, 510) : new Dimension(730, 560);

    /** Describes optional settings of the JGoodies Looks */
    private final Settings settings;

    /**
     * Constructs a <code>DemoFrame</code>, configures the UI, 
     * and builds the content.
     */
    protected DemoFrame(Settings settings) {
        this.settings = settings;
        configureUI();
        build();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        DemoFrame instance = new DemoFrame(createSettings());
        instance.setSize(PREFERRED_SIZE);
        instance.locateOnScreen(instance);
        instance.setVisible(true);
    }
    
    private static Settings createSettings() {
        Settings settings = Settings.createDefault();
        
        // Configure the settings here.
        
        return settings;
    }
    

    /**
     * Configures the user interface; requests Swing settings and 
     * jGoodies Looks options from the launcher.
     */
    private void configureUI() {
        Options.setDefaultIconSize(new Dimension(18, 18));

        // Set font options		
        UIManager.put(
            Options.USE_SYSTEM_FONTS_APP_KEY,
            settings.isUseSystemFonts());
        Options.setGlobalFontSizeHints(settings.getFontSizeHints());
        Options.setUseNarrowButtons(settings.isUseNarrowButtons());
        Options.setTabIconsEnabled(settings.isTabIconsEnabled());
        ClearLookManager.setMode(settings.getClearLookMode());
        ClearLookManager.setPolicy(settings.getClearLookPolicyName());

        // Swing Settings
        LookAndFeel selectedLaf = settings.getSelectedLookAndFeel();
        if (selectedLaf instanceof PlasticLookAndFeel) {
            PlasticLookAndFeel.setMyCurrentTheme(settings.getSelectedTheme());
            PlasticLookAndFeel.setTabStyle(settings.getPlasticTabStyle());
            PlasticLookAndFeel.setHighContrastFocusColorsEnabled(
                settings.isPlasticHighContrastFocusEnabled());
        } else if (selectedLaf.getClass() == MetalLookAndFeel.class) {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
        }

        // Workaround caching in MetalRadioButtonUI
        JRadioButton radio = new JRadioButton();
        radio.getUI().uninstallUI(radio);
        JCheckBox checkBox = new JCheckBox();
        checkBox.getUI().uninstallUI(checkBox);

        try {
            UIManager.setLookAndFeel(selectedLaf);
        } catch (Exception e) {
            System.out.println("Can't change L&F: " + e);
        }

    }

    /**
     * Builds the <code>DemoFrame</code> using Options from the Launcher.
     */
    private void build() {
        setContentPane(buildContentPane());
        setTitle(getWindowTitle());
        setJMenuBar(
            new MenuBuilder().buildMenuBar(
                settings,
                createHelpActionListener(),
                createAboutActionListener()));
        setIconImage(readImageIcon("eye_16x16.gif").getImage());
    }

    /**
     * Builds and answers the content.
     */
    private JComponent buildContentPane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buildToolBar(), BorderLayout.NORTH);
        panel.add(buildMainPanel(), BorderLayout.CENTER);
        return panel;
    }

    // Tool Bar *************************************************************

    /**
     * Builds, configures, and answers the toolbar. Requests
     * HeaderStyle, look-specific BorderStyles, and Plastic 3D Hint from Launcher.
     */
    private Component buildToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        // Swing
        toolBar.putClientProperty(
            Options.HEADER_STYLE_KEY,
            settings.getToolBarHeaderStyle());
        toolBar.putClientProperty(
            PlasticLookAndFeel.BORDER_STYLE_KEY,
            settings.getToolBarPlasticBorderStyle());
        toolBar.putClientProperty(
            ExtWindowsLookAndFeel.BORDER_STYLE_KEY,
            settings.getToolBarWindowsBorderStyle());
        toolBar.putClientProperty(
            PlasticLookAndFeel.IS_3D_KEY,
            settings.getToolBar3DHint());

        AbstractButton button;

        toolBar.add(createToolBarButton("backward.gif"));
        button = createToolBarButton("forward.gif");
        button.setEnabled(false);
        toolBar.add(button);
        toolBar.add(createToolBarButton("home.gif"));
        toolBar.addSeparator();
        toolBar.add(createOpenButton());
        toolBar.add(createToolBarButton("print.gif"));
        toolBar.add(createToolBarButton("refresh.gif"));
        toolBar.addSeparator();

        ButtonGroup group = new ButtonGroup();
        button = createToolBarRadioButton("pie_mode.png");
        button.setSelectedIcon(readImageIcon("pie_mode_selected.gif"));
        group.add(button);
        button.setSelected(true);
        toolBar.add(button);

        button = createToolBarRadioButton("bar_mode.png");
        button.setSelectedIcon(readImageIcon("bar_mode_selected.gif"));
        group.add(button);
        toolBar.add(button);

        button = createToolBarRadioButton("table_mode.png");
        button.setSelectedIcon(readImageIcon("table_mode_selected.gif"));
        group.add(button);
        toolBar.add(button);
        toolBar.addSeparator();

        button = createToolBarButton("help.gif");
        button.addActionListener(createHelpActionListener());
        toolBar.add(button);

        toolBar.add(Box.createGlue());

        button = new RolloverCheckButton();
        button.setToolTipText("Shall show border when mouse is over");
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        return toolBar;
    }

    private AbstractButton createOpenButton() {
        AbstractButton button = createToolBarButton("open.gif");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new JFileChooser().showOpenDialog(DemoFrame.this);
            }

        });
        return button;
    }

    /** Defines the margin used in toolbar buttons. */
    private static final Insets TOOLBAR_BUTTON_MARGIN = new Insets(1, 1, 1, 1);

    /**
     * Creates and answers a <code>JButton</code> 
     * configured for use in a JToolBar.<p>
     * 
     * Superceded by ToolBarButton from the jGoodies UI framework.
     */
    private AbstractButton createToolBarButton(String iconName) {
        JButton button = new JButton(readImageIcon(iconName));
        button.setFocusPainted(false);
        button.setMargin(TOOLBAR_BUTTON_MARGIN);
        return button;
    }

    /**
     * Creates and answers a <code>JToggleButton</code> 
     * configured for use in a JToolBar.<p>
     * 
     * Superceded by ToolBarToggleButton from the jGoodies UI framework.
     */
    private AbstractButton createToolBarRadioButton(String iconName) {
        JToggleButton button = new JToggleButton(readImageIcon(iconName));
        button.setFocusPainted(false);
        button.setMargin(TOOLBAR_BUTTON_MARGIN);
        return button;
    }

    // Tabbed Pane **********************************************************

    /**
     * Builds and answers the tabbed pane.
     */
    private Component buildMainPanel() {
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
        //tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        addTabs(tabbedPane);

        tabbedPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        return tabbedPane;
    }

    protected void addTabs(JTabbedPane tabbedPane) {
        tabbedPane.addTab("Desktop", new DesktopTab().build());
        tabbedPane.addTab("States", new StatesTab().build());
        tabbedPane.addTab("HTML Labels", new HtmlTab().build());
        tabbedPane.addTab("Dialogs", new DialogsTab().build(tabbedPane));
        tabbedPane.addTab("Narrow Test", new NarrowTab().build());
        tabbedPane.addTab("Alignment", new AlignmentTab().build());
        tabbedPane.addTab("ClearLook", new ClearLookTab().build());
    }
    
    protected String getWindowTitle() {
        return "Simple Looks Demo";
    }
    

    // Helper Code **********************************************************************

    /*
     * Looks up and answers an icon for the specified filename suffix.<p>
     */
    private static ImageIcon readImageIcon(String filename) {
        URL url =
            DemoFrame.class.getClassLoader().getResource("images/" + filename);
        return new ImageIcon(url);
    }

    /**
     * Locates the given component on the screen's center.
     */
    protected void locateOnScreen(Component component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation(
            (screenSize.width  - paneSize.width)  / 2,
            (screenSize.height - paneSize.height) / 2);
    }

    /**
     * Creates and answers an ActionListener that opens the help viewer.
     */
    protected ActionListener createHelpActionListener() {
        return null;
    }

    /**
     * Creates and answers an ActionListener that opens the about dialog.
     */
    protected ActionListener createAboutActionListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    DemoFrame.this,
                    "The simple Looks Demo Application\n"
                        + "\n\u00a9 2003 JGoodies Karsten Lentzsch. All Rights Reserved.\n\n");
            }
        };
    }

    // Checks that all tool bar buttons have a UIResource border
    private static class RolloverCheckButton extends JButton {

        private boolean checked = false;

        public void paint(Graphics g) {
            if (!checked) {
                checkAndSetResult();
            }
            super.paint(g);
        }

        private void checkAndSetResult() {
            Icon passedIcon = readImageIcon("passed.gif");
            Icon failedIcon = readImageIcon("failed.gif");

            boolean passed = allButtonBordersAreUIResources();
            setIcon(passed ? passedIcon : failedIcon);
            setText(passed ? "Can Swap L&F" : "Can't Swap L&F");

            checked = true;
        }

        /**
         * Checks and answers whether all button borders implement UIResource.
         */
        private boolean allButtonBordersAreUIResources() {
            JToolBar bar = (JToolBar) getParent();
            for (int i = bar.getComponentCount() - 1; i >= 0; i--) {
                Component child = bar.getComponent(i);
                if (child instanceof JButton) {
                    Border b = ((JButton) child).getBorder();
                    if (!(b instanceof UIResource))
                        return false;
                }
            }
            return true;
        }

    }

}