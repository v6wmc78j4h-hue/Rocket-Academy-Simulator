import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
//The following block of code was made by Codex as part of the coding challenge. It is a complete Java Swing application for designing and simulating rockets. The code includes a main class `RocketDesignerGame` that sets up the GUI and handles user interactions. The application allows users to select different rocket bodies, engines, planets, and design parameters, then simulates the flight and provides a report with scores and feedback. The code is structured with inner classes for the main frame and various components, and it uses custom classes like `RocketDesign`, `FlightResult`, `RocketPhysics`, etc., which are not included in this snippet but are essential for the application's functionality.
public class RocketDesignerGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new RocketDesignerFrame().setVisible(true);
        });
    }
}
class RocketDesignerFrame extends JFrame {
        private static final BodyType[] MODEL_BODIES = {
        new BodyType(RocketScale.MODEL, "Tiny Explorer Model", 0.095, 0.020, 0.74, 0.0030, 40, 0.13, 84, 28, 0.84),
        new BodyType(RocketScale.MODEL, "Egg Carrier Model", 0.160, 0.034, 0.80, 0.0044, 65, 0.07, 96, 34, 0.83),
        new BodyType(RocketScale.MODEL, "Big Schoolyard Model", 0.320, 0.075, 0.78, 0.0066, 95, 0.03, 116, 42, 0.82)
    };
    private static final BodyType[] ORBITAL_BODIES = {
        new BodyType(RocketScale.ORBITAL, "Saturn V", 180000, 2800000, 0.42, 78.5, 1850000, 0.10, 168, 48, 1.22),
        new BodyType(RocketScale.ORBITAL, "Space Launch System Block 1", 245000, 2550000, 0.46, 70.0, 2200000, 0.11, 160, 46, 1.23),
        new BodyType(RocketScale.ORBITAL, "Delta IV Heavy", 67000, 668000, 0.39, 60.0, 950000, 0.08, 148, 46, 1.29),
        new BodyType(RocketScale.ORBITAL, "Custom Heavy-Lift Rocket", 125000, 1450000, 0.44, 65.0, 1100000, 0.06, 156, 44, 1.15)
    };
    private static final EngineType[] MODEL_ENGINES = {
        new EngineType(RocketScale.MODEL, "A8 School Engine", 2.6, 0.016, 80, 80, 12, 0.96, 0.92, "Gentle low-power model engine"),
        new EngineType(RocketScale.MODEL, "B6 Park Flyer", 6.0, 0.020, 82, 82, 16, 0.94, 0.88, "Balanced model rocket engine"),
        new EngineType(RocketScale.MODEL, "C6 High Hop", 10.0, 0.025, 85, 85, 22, 0.92, 0.82, "Higher altitude model engine"),
        new EngineType(RocketScale.MODEL, "D12 Big Model Motor", 24.0, 0.055, 90, 90, 32, 0.88, 0.74, "Powerful engine for larger models")
    };
    private static final EngineType[] ORBITAL_ENGINES = {
        new EngineType(RocketScale.ORBITAL, "Saturn V F-1/J-2 Stack", 34500000, 87000, 263, 421, 500000, 0.88, 0.08, "F-1 first stage plus J-2 upper stages"),
        new EngineType(RocketScale.ORBITAL, "SLS RS-25 + Boosters", 39100000, 120000, 366, 452, 650000, 0.92, 0.12, "RS-25 core engines with solid rocket boosters"),
        new EngineType(RocketScale.ORBITAL, "Delta IV RS-68A/RL10 Stack", 9400000, 45000, 362, 462, 390000, 0.90, 0.16, "RS-68A boosters and RL10 upper stage"),
        new EngineType(RocketScale.ORBITAL, "Reusable Methalox Cluster", 18000000, 62000, 330, 370, 420000, 0.86, 0.58, "Fictional reusable booster engine cluster")
    };
    private static final Planet[] PLANETS = {
        new Planet("Earth", 9.81, 1.225, 8500, 9400, 100000, 300,
            new Color(84, 174, 235), new Color(214, 239, 255), new Color(91, 152, 97), new Color(79, 134, 84),
            "Thick lower atmosphere and familiar schoolyard gravity"),
        new Planet("Moon", 1.62, 0.0, 1, 1700, 100000, 900,
            new Color(6, 9, 18), new Color(14, 16, 28), new Color(142, 142, 146), new Color(103, 103, 108),
            "No air, low gravity, and a black sky"),
        new Planet("Mars", 3.71, 0.020, 11100, 4100, 100000, 650,
            new Color(190, 126, 91), new Color(239, 177, 128), new Color(166, 85, 62), new Color(121, 68, 56),
            "Thin air and rusty dust make rockets coast farther"),
        new Planet("Venus", 8.87, 65.0, 15900, 10300, 100000, 90,
            new Color(209, 132, 54), new Color(247, 205, 99), new Color(119, 82, 44), new Color(95, 59, 38),
            "Crushingly thick air makes drag a serious problem"),
        new Planet("Mercury", 3.70, 0.0, 1, 4300, 100000, 800,
            new Color(8, 9, 16), new Color(24, 25, 33), new Color(124, 113, 101), new Color(92, 84, 76),
            "No air and lower gravity, but a rocky surface"),
        new Planet("Titan", 1.35, 5.30, 40000, 2900, 100000, 260,
            new Color(171, 129, 69), new Color(231, 189, 104), new Color(91, 75, 66), new Color(62, 53, 51),
            "Low gravity with a thick orange atmosphere")
    };
    private final JComboBox<RocketScale> scalePicker = new JComboBox<>(RocketScale.values());
    private final JComboBox<Planet> planetPicker = new JComboBox<>(PLANETS);
    private final JComboBox<BodyType> bodyPicker = new JComboBox<>();
    private final JComboBox<EngineType> enginePicker = new JComboBox<>();
    private final JComboBox<GameTheme> themePicker = new JComboBox<>(GameTheme.values());
    private final JComboBox<StageMode> stagingModePicker = new JComboBox<>(StageMode.values());
    private final JComboBox<CustomStageRole> stageOneRolePicker = new JComboBox<>(CustomStageRole.values());
    private final JComboBox<CustomStageRole> stageTwoRolePicker = new JComboBox<>(CustomStageRole.values());
    private final JComboBox<CustomStageRole> stageThreeRolePicker = new JComboBox<>(CustomStageRole.values());
    private final JComboBox<CustomStageRole> stageFourRolePicker = new JComboBox<>(CustomStageRole.values());
    private final JSlider fuelSlider = new JSlider(20, 120, 85);
    private final JSlider finSlider = new JSlider(0, 6, 3);
    private final JSlider stageCountSlider = new JSlider(1, 4, 3);
    private final JCheckBox parachuteBox = new JCheckBox("Pack a parachute", true);
    private final JCheckBox landingLegsBox = new JCheckBox("Add landing legs", true);
    private final JCheckBox heatShieldBox = new JCheckBox("Use a heat shield", false);
    private final JCheckBox eggPayloadBox = new JCheckBox("Fly an egg passenger", false);
    private final JCheckBox atmosphereLayersBox = new JCheckBox("Show atmosphere layers", false);
    private final JButton launchButton = new JButton("Launch Simulation");
    private final JButton randomButton = new JButton("Try Challenge Design");
    private final JTextArea designStatsArea = new JTextArea();
    private final JTextArea reportArea = new JTextArea();
    private final JProgressBar scoreBar = new JProgressBar(0, 100);
    private final JProgressBar heightBar = new JProgressBar(0, 100);
    private final JProgressBar reuseBar = new JProgressBar(0, 100);
    private final JProgressBar deltaVBar = new JProgressBar(0, 100);
    private final RocketCanvas rocketCanvas = new RocketCanvas();
    private JPanel contentPanel;
    private JPanel controlsPanel;
    private JPanel reportPanel;
    private JScrollPane controlsScrollPane;
    private JScrollPane reportScrollPane;
    private GameTheme currentTheme = GameTheme.LIGHT;
    private FlightResult latestResult;
    private Timer animationTimer;
    private int animationTick;
    private boolean updatingChoices;
    RocketDesignerFrame() {
        super("Rocket Builder Academy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1230, 760));
        setLocationByPlatform(true);
        resetCustomStageRoles();
        updateRocketChoices();
        contentPanel = new JPanel(new BorderLayout(14, 14));
        contentPanel.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(contentPanel);
        contentPanel.add(createControls(), BorderLayout.WEST);
        contentPanel.add(rocketCanvas, BorderLayout.CENTER);
        contentPanel.add(createReportPanel(), BorderLayout.EAST);
        wireEvents();
        applyTheme();
        refreshPreview();
        pack();
    }
    private JScrollPane createControls() {
        JPanel panel = new JPanel();
        controlsPanel = panel;
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        JLabel title = new JLabel("Design Lab");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        panel.add(title);
        JLabel subtitle = new JLabel("Build, launch, learn, improve.");
        subtitle.setForeground(new Color(83, 94, 104));
        subtitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(18));
        addLabeledControl(panel, "Theme", themePicker);
        addLabeledControl(panel, "Rocket type", scalePicker);
        addLabeledControl(panel, "Launch world", planetPicker);
        addLabeledControl(panel, "Rocket design", bodyPicker);
        addLabeledControl(panel, "Engine", enginePicker);
        addLabeledControl(panel, "Staging plan", stagingModePicker);
        addSlider(panel, "Fuel load", fuelSlider, "Light", "Packed");
        addSlider(panel, "Stability fins / control", finSlider, "Loose", "Steady");
        addSlider(panel, "Custom stages", stageCountSlider, "1", "4");
        addStageRoleControl(panel, "Stage 1 role", stageOneRolePicker);
        addStageRoleControl(panel, "Stage 2 role", stageTwoRolePicker);
        addStageRoleControl(panel, "Stage 3 role", stageThreeRolePicker);
        addStageRoleControl(panel, "Stage 4 role", stageFourRolePicker);
        parachuteBox.setBackground(Color.WHITE);
        landingLegsBox.setBackground(Color.WHITE);
        heatShieldBox.setBackground(Color.WHITE);
        eggPayloadBox.setBackground(Color.WHITE);
        atmosphereLayersBox.setBackground(Color.WHITE);
        panel.add(parachuteBox);
        panel.add(Box.createVerticalStrut(8));
        panel.add(landingLegsBox);
        panel.add(Box.createVerticalStrut(8));
        panel.add(heatShieldBox);
        panel.add(Box.createVerticalStrut(8));
        panel.add(eggPayloadBox);
        panel.add(Box.createVerticalStrut(8));
        panel.add(atmosphereLayersBox);
        panel.add(Box.createVerticalStrut(14));
        designStatsArea.setWrapStyleWord(true);
        designStatsArea.setLineWrap(true);
        designStatsArea.setEditable(false);
        designStatsArea.setBackground(new Color(248, 251, 253));
        designStatsArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 229, 236)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        designStatsArea.setFont(designStatsArea.getFont().deriveFont(13f));
        designStatsArea.setAlignmentX(JTextArea.LEFT_ALIGNMENT);
        panel.add(designStatsArea);
        panel.add(Box.createVerticalStrut(14));
        launchButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        launchButton.setFont(launchButton.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(launchButton);
        panel.add(Box.createVerticalStrut(8));
        randomButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        panel.add(randomButton);
        panel.add(Box.createVerticalStrut(14));
        JTextArea tip = new JTextArea(
            "Coach tip: Delta-v is your rocket's speed-changing budget. "
                + "Higher Isp engines squeeze more push from the same propellant."
        );
        tip.setWrapStyleWord(true);
        tip.setLineWrap(true);
        tip.setEditable(false);
        tip.setOpaque(false);
        tip.setForeground(new Color(64, 78, 92));
        tip.setFont(tip.getFont().deriveFont(13f));
        tip.setAlignmentX(JTextArea.LEFT_ALIGNMENT);
        panel.add(tip);
        controlsScrollPane = new JScrollPane(panel);
        controlsScrollPane.setPreferredSize(new Dimension(350, 660));
        controlsScrollPane.setBorder(null);
        controlsScrollPane.getVerticalScrollBar().setUnitIncrement(12);
        return controlsScrollPane;
    }
    private void addLabeledControl(JPanel panel, String labelText, JComboBox<?> control) {
        JLabel label = new JLabel(labelText);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(4));
        control.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        control.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        panel.add(control);
        panel.add(Box.createVerticalStrut(14));
    }
    private void addSlider(JPanel panel, String labelText, JSlider slider, String leftText, String rightText) {
        JLabel label = new JLabel(labelText);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        panel.add(label);
        slider.setBackground(Color.WHITE);
        slider.setMajorTickSpacing(labelText.equals("Fuel load") ? 20 : 2);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setAlignmentX(JSlider.LEFT_ALIGNMENT);
        panel.add(slider);
        JPanel hintRow = new JPanel(new BorderLayout());
        hintRow.setOpaque(false);
        hintRow.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        JLabel left = new JLabel(leftText);
        left.setForeground(new Color(95, 108, 120));
        JLabel right = new JLabel(rightText, SwingConstants.RIGHT);
        right.setForeground(new Color(95, 108, 120));
        hintRow.add(left, BorderLayout.WEST);
        hintRow.add(right, BorderLayout.EAST);
        panel.add(hintRow);
        panel.add(Box.createVerticalStrut(12));
    }
    private void addStageRoleControl(JPanel panel, String labelText, JComboBox<CustomStageRole> control) {
        JLabel label = new JLabel(labelText);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));
        label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(3));
        control.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        control.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        panel.add(control);
        panel.add(Box.createVerticalStrut(10));
    }
    private JPanel createReportPanel() {
        JPanel panel = new JPanel();
        reportPanel = panel;
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(365, 660));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        JLabel title = new JLabel("Mission Report");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(12));
        addScoreBar(panel, "Overall grade", scoreBar);
        addScoreBar(panel, "Altitude score", heightBar);
        addScoreBar(panel, "Reuse score", reuseBar);
        addScoreBar(panel, "Delta-v score", deltaVBar);
        reportArea.setEditable(false);
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);
        reportArea.setFont(reportArea.getFont().deriveFont(14f));
        reportArea.setBackground(new Color(248, 251, 253));
        reportArea.setBorder(new EmptyBorder(12, 12, 12, 12));
        reportArea.setText(
            "Welcome, rocket designer!\n\n"
                + "Choose model rockets or real-life orbital rockets, pick a planet, then launch. "
                + "The report now includes delta-v, engine Isp, altitude, reuse, safety, and egg survival."
        );
        reportScrollPane = new JScrollPane(reportArea);
        reportScrollPane.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
        panel.add(reportScrollPane);
        return panel;
    }
    private void addScoreBar(JPanel panel, String labelText, JProgressBar bar) {
        JLabel label = new JLabel(labelText);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(4));
        bar.setValue(0);
        bar.setStringPainted(true);
        bar.setAlignmentX(JProgressBar.LEFT_ALIGNMENT);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        panel.add(bar);
        panel.add(Box.createVerticalStrut(12));
    }
    private void applyTheme() {
        currentTheme = (GameTheme) themePicker.getSelectedItem();
        if (currentTheme == null) {
            currentTheme = GameTheme.LIGHT;
        }
        contentPanel.setBackground(currentTheme.appBackground);
        themeComponentTree(contentPanel, currentTheme);
        controlsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(currentTheme.border),
            new EmptyBorder(16, 16, 16, 16)
        ));
        reportPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(currentTheme.border),
            new EmptyBorder(16, 16, 16, 16)
        ));
        if (controlsScrollPane != null) {
            controlsScrollPane.getViewport().setBackground(currentTheme.panelBackground);
        }
        if (reportScrollPane != null) {
            reportScrollPane.setBorder(BorderFactory.createLineBorder(currentTheme.border));
            reportScrollPane.getViewport().setBackground(currentTheme.cardBackground);
        }
        rocketCanvas.setTheme(currentTheme);
        repaint();
    }
    private void themeComponentTree(Component component, GameTheme theme) {
        if (component instanceof JPanel) {
            component.setBackground(theme.panelBackground);
            component.setForeground(theme.text);
        } else if (component instanceof JTextArea) {
            JTextArea area = (JTextArea) component;
            area.setBackground(theme.cardBackground);
            area.setForeground(theme.cardText);
            area.setCaretColor(theme.cardText);
            area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(theme.softBorder),
                new EmptyBorder(10, 10, 10, 10)
            ));
        } else if (component instanceof JLabel) {
            component.setForeground(theme.text);
        } else if (component instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) component;
            checkBox.setBackground(theme.panelBackground);
            checkBox.setForeground(theme.text);
        } else if (component instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) component;
            comboBox.setBackground(theme.cardBackground);
            comboBox.setForeground(theme.cardText);
        } else if (component instanceof JSlider) {
            JSlider slider = (JSlider) component;
            slider.setBackground(theme.panelBackground);
            slider.setForeground(theme.text);
        } else if (component instanceof JButton) {
            JButton button = (JButton) component;
            button.setBackground(theme.accent);
            button.setForeground(theme.buttonText);
            button.setFocusPainted(false);
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(true);
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(theme.buttonBorder),
                new EmptyBorder(6, 10, 6, 10)
            ));
        } else if (component instanceof JProgressBar) {
            JProgressBar bar = (JProgressBar) component;
            bar.setBackground(theme.cardBackground);
            bar.setForeground(theme.accent);
            bar.setBorder(BorderFactory.createLineBorder(theme.softBorder));
        } else if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            scrollPane.setBackground(theme.panelBackground);
            scrollPane.setBorder(BorderFactory.createLineBorder(theme.softBorder));
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                themeComponentTree(child, theme);
            }
        }
    }
    private void wireEvents() {
        themePicker.addActionListener(event -> applyTheme());
        scalePicker.addActionListener(event -> {
            if (!updatingChoices) {
                updateRocketChoices();
                latestResult = null;
                refreshPreview();
            }
        });
        planetPicker.addActionListener(event -> refreshPreview());
        bodyPicker.addActionListener(event -> refreshPreview());
        enginePicker.addActionListener(event -> refreshPreview());
        stagingModePicker.addActionListener(event -> {
            updateStagingControls();
            refreshPreview();
        });
        fuelSlider.addChangeListener(event -> refreshPreview());
        finSlider.addChangeListener(event -> refreshPreview());
        stageCountSlider.addChangeListener(event -> {
            if (!updatingChoices) {
                syncCustomStageRolesToStageCount();
            }
            updateStagingControls();
            refreshPreview();
        });
        stageOneRolePicker.addActionListener(event -> refreshPreview());
        stageTwoRolePicker.addActionListener(event -> refreshPreview());
        stageThreeRolePicker.addActionListener(event -> refreshPreview());
        stageFourRolePicker.addActionListener(event -> refreshPreview());
        parachuteBox.addActionListener(event -> refreshPreview());
        landingLegsBox.addActionListener(event -> refreshPreview());
        heatShieldBox.addActionListener(event -> refreshPreview());
        eggPayloadBox.addActionListener(event -> refreshPreview());
        atmosphereLayersBox.addActionListener(event -> refreshPreview());
        launchButton.addActionListener(this::launchRocket);
        randomButton.addActionListener(this::tryChallengeDesign);
    }
    private void updateRocketChoices() {
        updatingChoices = true;
        RocketScale scale = (RocketScale) scalePicker.getSelectedItem();
        BodyType previousBody = (BodyType) bodyPicker.getSelectedItem();
        EngineType previousEngine = (EngineType) enginePicker.getSelectedItem();
        BodyType[] bodies = scale == RocketScale.MODEL ? MODEL_BODIES : ORBITAL_BODIES;
        EngineType[] engines = scale == RocketScale.MODEL ? MODEL_ENGINES : ORBITAL_ENGINES;
        bodyPicker.setModel(new DefaultComboBoxModel<>(bodies));
        enginePicker.setModel(new DefaultComboBoxModel<>(engines));
        selectMatchingBody(previousBody, bodies);
        selectMatchingEngine(previousEngine, engines);
        boolean modelRocket = scale == RocketScale.MODEL;
        eggPayloadBox.setEnabled(modelRocket);
        if (!modelRocket) {
            eggPayloadBox.setSelected(false);
            heatShieldBox.setSelected(true);
            parachuteBox.setSelected(false);
            landingLegsBox.setSelected(false);
            fuelSlider.setValue(Math.max(fuelSlider.getValue(), 75));
            stagingModePicker.setSelectedItem(StageMode.PRESET);
            stageCountSlider.setValue(3);
        } else {
            parachuteBox.setSelected(true);
            landingLegsBox.setSelected(true);
            heatShieldBox.setSelected(false);
            fuelSlider.setValue(Math.min(fuelSlider.getValue(), 100));
            stageCountSlider.setValue(Math.min(stageCountSlider.getValue(), 2));
        }
        resetCustomStageRoles();
        updateStagingControls();
        updatingChoices = false;
    }
    private void resetCustomStageRoles() {
        int stageCount = stageCountSlider.getValue();
        stageOneRolePicker.setSelectedItem(CustomStageRole.TAKEOFF_BOOSTER);
        stageTwoRolePicker.setSelectedItem(stageCount <= 2
            ? CustomStageRole.SPACE_UPPER_STAGE
            : CustomStageRole.UPPER_ATMOSPHERE_CORE);
        stageThreeRolePicker.setSelectedItem(stageCount <= 3
            ? CustomStageRole.SPACE_UPPER_STAGE
            : CustomStageRole.UPPER_ATMOSPHERE_CORE);
        stageFourRolePicker.setSelectedItem(CustomStageRole.SPACE_UPPER_STAGE);
    }
    private void syncCustomStageRolesToStageCount() {
        int stageCount = stageCountSlider.getValue();
        if (stageCount == 1) {
            stageOneRolePicker.setSelectedItem(CustomStageRole.TAKEOFF_BOOSTER);
            return;
        }
        stageRolePicker(stageCount).setSelectedItem(CustomStageRole.SPACE_UPPER_STAGE);
        if (stageCount >= 2) {
            stageOneRolePicker.setSelectedItem(CustomStageRole.TAKEOFF_BOOSTER);
        }
        if (stageCount >= 3 && stageTwoRolePicker.getSelectedItem() == CustomStageRole.SPACE_UPPER_STAGE) {
            stageTwoRolePicker.setSelectedItem(CustomStageRole.UPPER_ATMOSPHERE_CORE);
        }
        if (stageCount >= 4 && stageThreeRolePicker.getSelectedItem() == CustomStageRole.SPACE_UPPER_STAGE) {
            stageThreeRolePicker.setSelectedItem(CustomStageRole.UPPER_ATMOSPHERE_CORE);
        }
    }
    private JComboBox<CustomStageRole> stageRolePicker(int stageNumber) {
        if (stageNumber == 1) {
            return stageOneRolePicker;
        }
        if (stageNumber == 2) {
            return stageTwoRolePicker;
        }
        if (stageNumber == 3) {
            return stageThreeRolePicker;
        }
        return stageFourRolePicker;
    }
    private void updateStagingControls() {
        boolean customMode = stagingModePicker.getSelectedItem() == StageMode.CUSTOM;
        int stageCount = stageCountSlider.getValue();
        stageCountSlider.setEnabled(customMode);
        stageOneRolePicker.setEnabled(customMode && stageCount >= 1);
        stageTwoRolePicker.setEnabled(customMode && stageCount >= 2);
        stageThreeRolePicker.setEnabled(customMode && stageCount >= 3);
        stageFourRolePicker.setEnabled(customMode && stageCount >= 4);
    }
    private void selectMatchingBody(BodyType previous, BodyType[] bodies) {
        if (previous == null) {
            bodyPicker.setSelectedIndex(0);
            return;
        }
        for (BodyType body : bodies) {
            if (body.name.equals(previous.name)) {
                bodyPicker.setSelectedItem(body);
                return;
            }
        }
        bodyPicker.setSelectedIndex(0);
    }
    private void selectMatchingEngine(EngineType previous, EngineType[] engines) {
        if (previous == null) {
            enginePicker.setSelectedIndex(0);
            return;
        }
        for (EngineType engine : engines) {
            if (engine.name.equals(previous.name)) {
                enginePicker.setSelectedItem(engine);
                return;
            }
        }
        enginePicker.setSelectedIndex(0);
    }
    private void refreshPreview() {
        if (bodyPicker.getItemCount() == 0 || enginePicker.getItemCount() == 0) {
            return;
        }
        RocketDesign design = currentDesign();
        rocketCanvas.setDesign(design);
        updateDesignStats(design);
        if (latestResult == null) {
            rocketCanvas.setResult(null, 0);
        }
    }
    private void updateDesignStats(RocketDesign design) {
        DesignEstimate estimate = RocketPhysics.estimate(design);
        StagePlan stagePlan = StagePlanner.planFor(design);
        StringBuilder stats = new StringBuilder();
        stats.append("Engine Isp: ")
            .append(Math.round(design.engine.seaLevelIsp)).append(" s sea level / ")
            .append(Math.round(design.engine.vacuumIsp)).append(" s vacuum\n");
        stats.append("Delta-v: ").append(formatMetersPerSecond(estimate.deltaV)).append("\n");
        stats.append("Useful delta-v: ").append(formatMetersPerSecond(estimate.effectiveDeltaV)).append("\n");
        stats.append("Target: ").append(formatMetersPerSecond(estimate.targetDeltaV)).append("\n");
        stats.append("Thrust/weight: ").append(String.format("%.2f", estimate.thrustToWeight)).append("\n");
        stats.append("Stages: ").append(stagePlan.shortDescription()).append("\n");
        stats.append("Build cost: ").append(formatDollars(RocketPhysics.cost(design))).append("\n");
        stats.append("Parachute power: ")
            .append(Math.round(design.planet.parachuteEffectiveness() * 100)).append("%\n");
        stats.append(design.planet.name).append(": ")
            .append(String.format("%.2f", design.planet.gravity)).append(" m/s^2 gravity, ")
            .append(String.format("%.2f", design.planet.airDensityRatio())).append("x Earth air");
        designStatsArea.setText(stats.toString());
    }
    private RocketDesign currentDesign() {
        return new RocketDesign(
            (RocketScale) scalePicker.getSelectedItem(),
            (Planet) planetPicker.getSelectedItem(),
            (BodyType) bodyPicker.getSelectedItem(),
            (EngineType) enginePicker.getSelectedItem(),
            fuelSlider.getValue(),
            finSlider.getValue(),
            parachuteBox.isSelected(),
            landingLegsBox.isSelected(),
            heatShieldBox.isSelected(),
            eggPayloadBox.isSelected() && eggPayloadBox.isEnabled(),
            atmosphereLayersBox.isSelected(),
            (StageMode) stagingModePicker.getSelectedItem(),
            stageCountSlider.getValue(),
            (CustomStageRole) stageOneRolePicker.getSelectedItem(),
            (CustomStageRole) stageTwoRolePicker.getSelectedItem(),
            (CustomStageRole) stageThreeRolePicker.getSelectedItem(),
            (CustomStageRole) stageFourRolePicker.getSelectedItem()
        );
    }
    private void launchRocket(ActionEvent event) {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        RocketDesign design = currentDesign();
        latestResult = RocketPhysics.simulate(design);
        showReport(latestResult);
        animationTick = 0;
        launchButton.setEnabled(false);
        rocketCanvas.setResult(latestResult, 0);
        animationTimer = new Timer(28, timerEvent -> {
            animationTick++;
            double progress = Math.min(1.0, animationTick / 165.0);
            rocketCanvas.setResult(latestResult, progress);
            if (progress >= 1.0) {
                animationTimer.stop();
                launchButton.setEnabled(true);
            }
        });
        animationTimer.start();
    }
    private void showReport(FlightResult result) {
        scoreBar.setValue(result.overallScore);
        scoreBar.setString(result.letterGrade + "  " + result.overallScore + "/100");
        heightBar.setValue(result.altitudeScore);
        heightBar.setString(result.altitudeScore + "/100");
        reuseBar.setValue(result.reuseScore);
        reuseBar.setString(result.reuseScore + "/100");
        deltaVBar.setValue(result.deltaVScore);
        deltaVBar.setString(result.deltaVScore + "/100");
        StringBuilder report = new StringBuilder();
        report.append("Grade: ").append(result.letterGrade).append(" (")
            .append(result.overallScore).append("/100)\n\n");
        report.append("Rocket: ").append(result.design.body.name).append("\n");
        report.append("Engine: ").append(result.design.engine.name).append("\n");
        report.append("Planet: ").append(result.design.planet.name).append("\n");
        report.append("Engine Isp: ")
            .append(Math.round(result.design.engine.seaLevelIsp)).append(" s sea level / ")
            .append(Math.round(result.design.engine.vacuumIsp)).append(" s vacuum\n");
        report.append("Delta-v: ").append(formatMetersPerSecond(result.estimate.deltaV)).append("\n");
        report.append("Useful delta-v: ").append(formatMetersPerSecond(result.estimate.effectiveDeltaV)).append("\n");
        report.append("Delta-v target: ").append(formatMetersPerSecond(result.estimate.targetDeltaV)).append("\n\n");
        report.append("Peak altitude: ").append(formatAltitude(result.maxAltitude)).append("\n");
        report.append("Top speed: ").append(formatMetersPerSecond(result.topSpeed)).append("\n");
        report.append("Landing speed: ").append(formatMetersPerSecond(result.landingSpeed)).append("\n");
        report.append("Max acceleration: ").append(String.format("%.1f", result.maxG)).append(" g\n");
        report.append("Build cost: ").append(formatDollars(result.cost)).append("\n");
        report.append("Parachute effectiveness: ")
            .append(Math.round(result.design.planet.parachuteEffectiveness() * 100)).append("%\n");
        report.append("Reached space: ").append(result.reachedSpace ? "yes" : "not yet").append("\n");
        if (result.design.scale == RocketScale.ORBITAL) {
            report.append("Orbit-capable delta-v: ").append(result.orbitCapable ? "yes" : "not yet").append("\n");
        }
        if (result.design.eggPayload) {
            report.append("Egg passenger: ").append(result.eggBroke ? "cracked" : "safe").append("\n");
        }
        report.append("\nStaging events:\n");
        for (StageEvent stageEvent : result.stagePlan.events) {
            report.append("- ").append(stageEvent.name).append("\n");
        }
        report.append("\nWhat went well:\n");
        for (String praise : result.praises) {
            report.append("- ").append(praise).append("\n");
        }
        report.append("\nTry next:\n");
        for (String advice : result.advice) {
            report.append("- ").append(advice).append("\n");
        }
        report.append("\nMission note: ").append(result.summary);
        reportArea.setText(report.toString());
        reportArea.setCaretPosition(0);
    }
    private void tryChallengeDesign(ActionEvent event) {
        RocketScale scale = Math.random() > 0.55 ? RocketScale.MODEL : RocketScale.ORBITAL;
        scalePicker.setSelectedItem(scale);
        updateRocketChoices();
        BodyType[] bodies = scale == RocketScale.MODEL ? MODEL_BODIES : ORBITAL_BODIES;
        EngineType[] engines = scale == RocketScale.MODEL ? MODEL_ENGINES : ORBITAL_ENGINES;
        bodyPicker.setSelectedItem(bodies[(int) (Math.random() * bodies.length)]);
        enginePicker.setSelectedItem(engines[(int) (Math.random() * engines.length)]);
        planetPicker.setSelectedItem(PLANETS[(int) (Math.random() * PLANETS.length)]);
        fuelSlider.setValue(45 + (int) (Math.random() * 70));
        finSlider.setValue(1 + (int) (Math.random() * 6));
        parachuteBox.setSelected(scale == RocketScale.MODEL && Math.random() > 0.2);
        landingLegsBox.setSelected(Math.random() > 0.45);
        heatShieldBox.setSelected(scale == RocketScale.ORBITAL || Math.random() > 0.5);
        eggPayloadBox.setSelected(scale == RocketScale.MODEL && Math.random() > 0.55);
        atmosphereLayersBox.setSelected(Math.random() > 0.5);
        stagingModePicker.setSelectedItem(scale == RocketScale.ORBITAL && Math.random() > 0.25 ? StageMode.PRESET : StageMode.CUSTOM);
        stageCountSlider.setValue(1 + (int) (Math.random() * (scale == RocketScale.MODEL ? 2 : 4)));
        resetCustomStageRoles();
        updateStagingControls();
        latestResult = null;
        refreshPreview();
        JOptionPane.showMessageDialog(
            this,
            "New challenge design loaded. Can you tune the delta-v and earn an A?",
            "Design Challenge",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    private static String formatMetersPerSecond(double value) {
        if (value >= 1000) {
            return String.format("%.2f km/s", value / 1000.0);
        }
        return Math.round(value) + " m/s";
    }
    private static String formatAltitude(double value) {
        if (value >= 1000000) {
            return String.format("%.2f Mm", value / 1000000.0);
        }
        if (value >= 1000) {
            return String.format("%.1f km", value / 1000.0);
        }
        return Math.round(value) + " m";
    }
    private static String formatDollars(int value) {
        return String.format("$%,d", value);
    }
}
class RocketCanvas extends JPanel {
    private RocketDesign design;
    private FlightResult result;
    private double progress;
    private GameTheme theme = GameTheme.LIGHT;
    RocketCanvas() {
        setBackground(new Color(222, 238, 251));
        setBorder(BorderFactory.createLineBorder(new Color(174, 197, 216)));
    }
    void setTheme(GameTheme theme) {
        this.theme = theme == null ? GameTheme.LIGHT : theme;
        setBorder(BorderFactory.createLineBorder(this.theme.border));
        repaint();
    }
    void setDesign(RocketDesign design) {
        this.design = design;
        repaint();
    }
    void setResult(FlightResult result, double progress) {
        this.result = result;
        this.progress = progress;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        Planet planet = design == null ? Planet.earthLike() : design.planet;
        boolean spaceView = isSpaceView();
        if (spaceView) {
            drawSpaceScene(g, width, height, planet);
        } else if (design != null && design.showAtmosphereLayers) {
            drawAtmosphereLayers(g, width, height, planet);
        } else {
            drawPlanetSky(g, width, height, planet);
        }
        if (!spaceView) {
            drawGround(g, width, height, planet);
            drawLaunchPad(g, width, height, planet);
        }
        RocketPose pose = currentPose(width, height);
        drawTrail(g, pose, width, height);
        drawStageSeparationPieces(g, pose);
        drawRecoverySystem(g, pose);
        drawRocket(g, pose);
        drawHud(g, width, height);
        g.dispose();
    }
    private boolean isSpaceView() {
        return result != null && result.reachedSpace && progress > 0.58;
    }
    private void drawSpaceScene(Graphics2D g, int width, int height, Planet planet) {
        for (int y = 0; y < height; y++) {
            float mix = y / (float) Math.max(1, height);
            g.setColor(mix(new Color(2, 5, 18), new Color(18, 28, 62), mix));
            g.drawLine(0, y, width, y);
        }
        drawStars(g, width, height, 110);
        drawPlanetLimb(g, width, height, planet);
        drawSmallPlanet(g, width - 126, 72, 32, new Color(206, 91, 66), false);
        drawSmallPlanet(g, width - 72, 148, 42, new Color(234, 189, 92), false);
        g.setColor(new Color(255, 255, 255, 210));
        g.setFont(g.getFont().deriveFont(Font.BOLD, 15f));
        g.drawString("Space view", 18, height - 110);
    }
    private void drawPlanetLimb(Graphics2D g, int width, int height, Planet planet) {
        int limbHeight = 135;
        g.setColor(planet.groundTop);
        g.fillArc(-width / 4, height - limbHeight, width + width / 2, limbHeight * 2, 0, 180);
        g.setColor(new Color(255, 255, 255, 70));
        if (planet.surfaceDensityKgM3 > 0.01) {
            g.setStroke(new BasicStroke(6f));
            g.drawArc(-width / 4, height - limbHeight - 10, width + width / 2, limbHeight * 2, 0, 180);
        }
    }
    private void drawPlanetSky(Graphics2D g, int width, int height, Planet planet) {
        for (int y = 0; y < height; y++) {
            float mix = y / (float) Math.max(1, height);
            g.setColor(mix(planet.skyTop, planet.skyBottom, mix));
            g.drawLine(0, y, width, y);
        }
        if (planet.surfaceDensityKgM3 > 0.01) {
            g.setColor(new Color(255, 255, 255, planet.name.equals("Venus") ? 90 : 160));
            drawCloud(g, width / 5, height / 5, planet.name.equals("Titan") ? 82 : 60);
            drawCloud(g, width * 3 / 4, height / 4, planet.name.equals("Venus") ? 88 : 48);
            drawCloud(g, width / 2, height / 7, 36);
        } else {
            drawStars(g, width, height, 38);
        }
        Color sunColor = planet.name.equals("Mars") ? new Color(255, 210, 145) : new Color(255, 225, 95);
        g.setColor(sunColor);
        g.fillOval(width - 110, 34, 58, 58);
        if (planet.name.equals("Moon") || planet.name.equals("Mercury")) {
            drawSmallPlanet(g, width - 170, 48, 32, new Color(70, 136, 205), false);
        }
    }
    private void drawAtmosphereLayers(Graphics2D g, int width, int height, Planet planet) {
        if (planet.surfaceDensityKgM3 <= 0.01) {
            drawAirlessLayerView(g, width, height, planet);
            return;
        }
        Color[] colors = {
            new Color(5, 7, 18),
            new Color(15, 20, 55),
            new Color(32, 47, 111),
            new Color(55, 93, 170),
            new Color(92, 151, 221),
            new Color(124, 197, 240)
        };
        String[] labels = {"Space", "Exosphere", "Thermosphere", "Mesosphere", "Stratosphere", "Troposphere"};
        int bandHeight = Math.max(1, height / colors.length);
        for (int i = 0; i < colors.length; i++) {
            int y = i * bandHeight;
            g.setColor(colors[i]);
            g.fillRect(0, y, width, i == colors.length - 1 ? height - y : bandHeight);
            g.setColor(new Color(255, 255, 255, 170));
            g.drawLine(0, y, width, y);
        }
        drawStars(g, width, height, 65);
        drawSmallPlanet(g, width - 92, 80, 34, new Color(206, 91, 66), false);
        drawSmallPlanet(g, width - 160, 130, 40, new Color(234, 189, 92), false);
        drawSmallPlanet(g, width - 72, 196, 54, new Color(189, 147, 102), true);
        g.setColor(new Color(255, 255, 255, 220));
        g.setFont(g.getFont().deriveFont(Font.BOLD, 13f));
        for (int i = 0; i < labels.length; i++) {
            int y = i * bandHeight;
            int labelX = i < 2 && width > 430 ? 300 : 18;
            g.drawString(labels[i], labelX, y + 24);
        }
        g.setColor(new Color(255, 255, 255, 180));
        g.setStroke(new BasicStroke(2f));
        int spaceLineY = bandHeight;
        g.drawLine(0, spaceLineY, width, spaceLineY);
        g.drawString("Space boundary", width - 140, spaceLineY + 18);
        g.setColor(new Color(255, 255, 255, 215));
        g.setFont(g.getFont().deriveFont(Font.BOLD, 14f));
        g.drawString("Layer view from " + planet.name, 18, height - 104);
    }
    private void drawAirlessLayerView(Graphics2D g, int width, int height, Planet planet) {
        for (int y = 0; y < height; y++) {
            float mix = y / (float) Math.max(1, height);
            g.setColor(mix(new Color(2, 4, 13), new Color(17, 19, 31), mix));
            g.drawLine(0, y, width, y);
        }
        drawStars(g, width, height, 95);
        int boxWidth = Math.min(430, Math.max(260, width - 330));
        int boxX = width > 620 ? width - boxWidth - 24 : 300;
        int boxY = 22;
        if (boxX + boxWidth > width - 12) {
            boxX = Math.max(18, width - boxWidth - 18);
            boxY = 142;
        }
        g.setColor(new Color(255, 255, 255, 226));
        g.fillRoundRect(boxX, boxY, boxWidth, 72, 12, 12);
        g.setColor(new Color(108, 126, 156));
        g.drawRoundRect(boxX, boxY, boxWidth, 72, 12, 12);
        g.setColor(new Color(31, 43, 66));
        g.setFont(g.getFont().deriveFont(Font.BOLD, 18f));
        g.drawString(planet.name + ": no atmosphere", boxX + 16, boxY + 27);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 13f));
        g.drawString("Vacuum all the way down.", boxX + 16, boxY + 49);
        g.drawString("Parachutes cannot work here.", boxX + 16, boxY + 65);
    }
    private void drawStars(Graphics2D g, int width, int height, int count) {
        g.setColor(new Color(255, 255, 255, 210));
        for (int i = 0; i < count; i++) {
            int x = (i * 73 + 31) % Math.max(1, width);
            int y = (i * 47 + 19) % Math.max(1, height - 100);
            int size = 1 + (i % 3 == 0 ? 1 : 0);
            g.fillOval(x, y, size, size);
        }
    }
    private void drawSmallPlanet(Graphics2D g, int x, int y, int size, Color color, boolean ring) {
        if (ring) {
            g.setColor(new Color(226, 213, 175, 160));
            g.setStroke(new BasicStroke(3f));
            g.drawOval(x - size / 3, y + size / 4, size + size / 2, size / 3);
        }
        g.setColor(color);
        g.fillOval(x, y, size, size);
        g.setColor(new Color(255, 255, 255, 45));
        g.fillOval(x + size / 5, y + size / 8, size / 3, size / 3);
    }
    private Color mix(Color a, Color b, float amount) {
        int r = (int) (a.getRed() + (b.getRed() - a.getRed()) * amount);
        int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * amount);
        int bl = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * amount);
        return new Color(r, g, bl);
    }
    private void drawCloud(Graphics2D g, int x, int y, int size) {
        g.fillOval(x, y, size, size / 2);
        g.fillOval(x + size / 4, y - size / 4, size / 2, size / 2);
        g.fillOval(x + size / 2, y, size, size / 2);
    }
    private void drawGround(Graphics2D g, int width, int height, Planet planet) {
        int groundY = height - 80;
        g.setColor(planet.groundTop);
        g.fillRect(0, groundY, width, height - groundY);
        g.setColor(planet.groundBottom);
        g.fillRect(0, groundY + 28, width, height - groundY - 28);
        if (planet.surfaceDensityKgM3 <= 0.01) {
            g.setColor(new Color(255, 255, 255, 45));
            for (int i = 0; i < 9; i++) {
                int x = (i * 97 + 24) % Math.max(1, width);
                int y = groundY + 8 + (i * 17) % 38;
                g.drawOval(x, y, 32 + i * 3, 9 + i);
            }
        }
    }
    private void drawLaunchPad(Graphics2D g, int width, int height, Planet planet) {
        int padY = height - 94;
        int padX = width / 2 - 78;
        g.setColor(new Color(88, 98, 110));
        g.fillRoundRect(padX, padY, 156, 16, 8, 8);
        g.setColor(new Color(66, 75, 85));
        g.fillRect(padX + 18, padY + 16, 14, 38);
        g.fillRect(padX + 124, padY + 16, 14, 38);
        if (design != null && design.scale == RocketScale.ORBITAL) {
            g.setColor(new Color(76, 84, 94));
            g.fillRect(padX - 18, padY - 118, 12, 134);
            g.setStroke(new BasicStroke(2f));
            for (int y = padY - 108; y < padY; y += 22) {
                g.drawLine(padX - 18, y, padX + 14, y + 16);
            }
        }
    }
    private RocketPose currentPose(int width, int height) {
        if (isSpaceView()) {
            int x = (int) (width * (0.38 + Math.min(0.30, (progress - 0.58) * 0.55)));
            int y = (int) (height * (0.52 - Math.min(0.18, (progress - 0.58) * 0.22)));
            double angle = design != null && design.scale == RocketScale.ORBITAL ? -0.55 : -0.18;
            return new RocketPose(x, y, angle);
        }
        double ease = 1 - Math.pow(1 - progress, 3);
        double flightShape = result == null ? 0 : Math.sin(progress * Math.PI);
        double launchRise = result == null ? 0 : altitudeScale(result.maxAltitude);
        int startY = height - 118;
        int y = (int) (startY - ease * (height - 210) * (0.34 + launchRise * 0.66));
        if (result != null && result.design.scale == RocketScale.MODEL && progress > 0.66) {
            y += (int) ((progress - 0.66) * 135);
        }
        int x = width / 2 + (int) (Math.sin(progress * 5.5) * 28 * flightShape * (1.12 - stability()));
        double angle = Math.sin(progress * 8) * 0.18 * flightShape * (1.14 - stability());
        if (result != null && result.design.scale == RocketScale.ORBITAL && result.orbitCapable && progress > 0.55) {
            x += (int) ((progress - 0.55) * width * 0.22);
            angle -= 0.22 * (progress - 0.55);
        }
        return new RocketPose(x, y, angle);
    }
    private double altitudeScale(double altitude) {
        if (design != null && design.scale == RocketScale.ORBITAL) {
            return Math.min(1.0, altitude / Math.max(1, design.planet.spaceAltitudeM));
        }
        return Math.min(1.0, altitude / Math.max(1, design.planet.modelTargetAltitude));
    }
    private double stability() {
        if (design == null) {
            return 1.0;
        }
        return Math.max(0.45, Math.min(1.15, 0.65 + design.fins * 0.08 + design.body.stabilityBonus));
    }
    private void drawTrail(Graphics2D g, RocketPose pose, int width, int height) {
        if (progress <= 0.02 || result == null || !hasActiveExhaust()) {
            return;
        }
        int flameLength = progress < 0.52 ? (design.scale == RocketScale.ORBITAL ? 76 : 46) : 18;
        int flameWidth = design.scale == RocketScale.ORBITAL ? 42 : 30;
        int exhaustY = pose.y + exhaustAnchorOffset();
        g.setColor(new Color(255, 142, 48, 210));
        g.fillOval(pose.x - flameWidth / 2, exhaustY, flameWidth, flameLength);
        g.setColor(new Color(255, 222, 86, 225));
        g.fillOval(pose.x - flameWidth / 4, exhaustY, flameWidth / 2, Math.max(12, flameLength - 16));
        if (isSpaceView()) {
            return;
        }
        g.setColor(new Color(230, 235, 238, design.scale == RocketScale.ORBITAL ? 115 : 135));
        int smokeCount = design.scale == RocketScale.ORBITAL ? 18 : 12;
        for (int i = 0; i < smokeCount; i++) {
            double p = Math.max(0, progress - i * 0.021);
            int smokeY = exhaustY + 44 + i * (design.scale == RocketScale.ORBITAL ? 16 : 22);
            int smokeX = pose.x + (int) (Math.sin(p * 18 + i) * (design.scale == RocketScale.ORBITAL ? 42 : 28));
            int size = (design.scale == RocketScale.ORBITAL ? 30 : 20) + i * 3;
            if (smokeY < height - 70) {
                g.fillOval(smokeX - size / 2, smokeY - size / 2, size, size);
            }
        }
    }
    private boolean hasActiveExhaust() {
        return design == null || design.scale != RocketScale.ORBITAL || !stageHasHappened(StagePiece.UPPER_STAGE);
    }
    private int exhaustAnchorOffset() {
        if (design == null) {
            return 34;
        }
        if (design.scale == RocketScale.MODEL) {
            return design.body.visualHeight / 2 + 6;
        }
        if (stageHasHappened(StagePiece.CORE)) {
            return 24;
        }
        return design.body.visualHeight / 2 + 10;
    }
    private void drawStageSeparationPieces(Graphics2D g, RocketPose pose) {
        if (result == null || result.stagePlan == null || design == null) {
            return;
        }
        for (int i = 0; i < result.stagePlan.events.size(); i++) {
            StageEvent event = result.stagePlan.events.get(i);
            double age = progress - event.progress;
            if (age < 0 || age > 0.16 || event.piece == StagePiece.PAYLOAD) {
                continue;
            }
            int alpha = Math.max(35, (int) (220 * (1.0 - age / 0.16)));
            int drift = (int) (age * 420);
            int y = pose.y + 42 + (int) (age * 130);
            boolean pairedBoosters = event.piece == StagePiece.BOOSTER
                && design.scale == RocketScale.ORBITAL
                && (design.body.name.contains("Delta") || design.body.name.contains("Space Launch"));
            if (pairedBoosters) {
                drawSeparatedStagePiece(g, event.piece, pose.x - 38 - drift, y, -1, alpha);
                drawSeparatedStagePiece(g, event.piece, pose.x + 38 + drift, y, 1, alpha);
                drawStageLabel(g, event.name, pose.x, y - 20, alpha);
            } else {
                int side = i % 2 == 0 ? -1 : 1;
                int x = pose.x + side * (34 + drift);
                drawSeparatedStagePiece(g, event.piece, x, y, side, alpha);
                drawStageLabel(g, event.name, x, y - 18, alpha);
            }
        }
    }
    private void drawSeparatedStagePiece(Graphics2D g, StagePiece piece, int x, int y, int side, int alpha) {
        Graphics2D p = (Graphics2D) g.create();
        p.translate(x, y);
        p.rotate(side * 0.35);
        if (piece == StagePiece.BOOSTER) {
            boolean slsBooster = design != null && design.body.name.contains("Space Launch");
            p.setColor(slsBooster ? new Color(244, 244, 238, alpha) : new Color(231, 120, 54, alpha));
            p.fillRoundRect(-8, -34, 16, 68, 8, 8);
            p.setColor(new Color(80, 88, 96, alpha));
            p.drawRoundRect(-8, -34, 16, 68, 8, 8);
        } else if (piece == StagePiece.UPPER_STAGE) {
            p.setColor(new Color(235, 238, 242, alpha));
            p.fillRoundRect(-10, -24, 20, 48, 7, 7);
            p.setColor(new Color(66, 80, 95, alpha));
            p.drawRoundRect(-10, -24, 20, 48, 7, 7);
        } else {
            p.setColor(new Color(221, 226, 232, alpha));
            p.fillRoundRect(-14, -38, 28, 76, 8, 8);
            p.setColor(new Color(72, 83, 95, alpha));
            p.drawRoundRect(-14, -38, 28, 76, 8, 8);
        }
        p.dispose();
    }
    private void drawStageLabel(Graphics2D g, String label, int x, int y, int alpha) {
        g.setFont(g.getFont().deriveFont(Font.BOLD, 12f));
        FontMetrics metrics = g.getFontMetrics();
        int width = metrics.stringWidth(label) + 14;
        g.setColor(new Color(255, 255, 255, Math.min(230, alpha + 20)));
        g.fillRoundRect(x - width / 2, y - 16, width, 22, 8, 8);
        g.setColor(new Color(53, 65, 78, alpha));
        g.drawString(label, x - metrics.stringWidth(label) / 2, y);
    }
    private void drawRecoverySystem(Graphics2D g, RocketPose pose) {
        if (result == null || design == null || !design.parachute) {
            return;
        }
        if (design.planet.parachuteEffectiveness() <= 0.01) {
            if (design.scale == RocketScale.MODEL && progress > 0.55) {
                drawPoppedNoseCone(g, pose.x + 52, pose.y - 92);
            }
            return;
        }
        if (design.scale == RocketScale.MODEL && progress > 0.55) {
            int chuteY = pose.y - 150;
            g.setColor(new Color(244, 248, 252, 235));
            g.fillArc(pose.x - 70, chuteY, 140, 86, 0, 180);
            g.setColor(new Color(196, 62, 72));
            g.setStroke(new BasicStroke(2f));
            g.drawArc(pose.x - 70, chuteY, 140, 86, 0, 180);
            g.setColor(new Color(91, 107, 122));
            g.drawLine(pose.x - 54, chuteY + 42, pose.x - 16, pose.y - 26);
            g.drawLine(pose.x, chuteY + 42, pose.x, pose.y - 26);
            g.drawLine(pose.x + 54, chuteY + 42, pose.x + 16, pose.y - 26);
            drawPoppedNoseCone(g, pose.x + 52, pose.y - 92);
        } else if (design.scale == RocketScale.ORBITAL && progress > 0.78 && result.landedSafely) {
            int chuteY = pose.y - 108;
            g.setColor(new Color(244, 248, 252, 225));
            g.fillArc(pose.x - 62, chuteY, 124, 76, 0, 180);
            g.setColor(new Color(91, 107, 122));
            g.drawLine(pose.x - 45, chuteY + 38, pose.x - 16, pose.y - 4);
            g.drawLine(pose.x + 45, chuteY + 38, pose.x + 16, pose.y - 4);
        }
    }
    private void drawPoppedNoseCone(Graphics2D g, int x, int y) {
        g.setColor(new Color(222, 67, 67));
        g.fillPolygon(new int[] {x - 12, x + 14, x + 2}, new int[] {y + 18, y + 14, y - 10}, 3);
        g.setColor(new Color(160, 49, 54));
        g.drawPolygon(new int[] {x - 12, x + 14, x + 2}, new int[] {y + 18, y + 14, y - 10}, 3);
    }
    private void drawRocket(Graphics2D g, RocketPose pose) {
        if (design == null) {
            return;
        }
        Graphics2D r = (Graphics2D) g.create();
        r.translate(pose.x, pose.y);
        r.rotate(pose.angle);
        if (design.scale == RocketScale.ORBITAL) {
            drawOrbitalRocket(r);
        } else {
            drawModelRocket(r);
        }
        r.dispose();
    }
    private void drawModelRocket(Graphics2D r) {
        int bodyHeight = design.body.visualHeight;
        int bodyWidth = design.body.visualWidth;
        r.setColor(new Color(232, 239, 246));
        r.fillRoundRect(-bodyWidth / 2, -bodyHeight / 2, bodyWidth, bodyHeight, 18, 18);
        r.setColor(new Color(80, 96, 112));
        r.setStroke(new BasicStroke(2f));
        r.drawRoundRect(-bodyWidth / 2, -bodyHeight / 2, bodyWidth, bodyHeight, 18, 18);
        boolean nosePopped = result != null && design.parachute && progress > 0.55;
        if (!nosePopped) {
            r.setColor(new Color(222, 67, 67));
            int[] noseX = {-bodyWidth / 2, 0, bodyWidth / 2};
            int[] noseY = {-bodyHeight / 2 + 8, -bodyHeight / 2 - 30, -bodyHeight / 2 + 8};
            r.fillPolygon(noseX, noseY, 3);
            r.setColor(new Color(160, 49, 54));
            r.drawPolygon(noseX, noseY, 3);
        }
        r.setColor(new Color(75, 163, 210));
        r.fillOval(-10, -bodyHeight / 2 + 22, 20, 20);
        r.setColor(new Color(29, 83, 120));
        r.drawOval(-10, -bodyHeight / 2 + 22, 20, 20);
        if (design.eggPayload) {
            r.setColor(new Color(249, 242, 213));
            r.fillOval(-9, 5, 18, 24);
            r.setColor(new Color(143, 117, 72));
            r.drawOval(-9, 5, 18, 24);
        }
        r.setColor(new Color(247, 190, 71));
        r.fillRect(-bodyWidth / 2 + 4, -2, bodyWidth - 8, 12);
        r.setColor(new Color(46, 138, 122));
        int finSize = 12 + design.fins * 2;
        r.fillPolygon(
            new int[] {-bodyWidth / 2, -bodyWidth / 2 - finSize, -bodyWidth / 2},
            new int[] {bodyHeight / 2 - 24, bodyHeight / 2 + 14, bodyHeight / 2},
            3
        );
        r.fillPolygon(
            new int[] {bodyWidth / 2, bodyWidth / 2 + finSize, bodyWidth / 2},
            new int[] {bodyHeight / 2 - 24, bodyHeight / 2 + 14, bodyHeight / 2},
            3
        );
        r.setColor(new Color(74, 80, 88));
        r.fillRect(-bodyWidth / 2 + 7, bodyHeight / 2 - 2, bodyWidth - 14, 12);
        if (design.landingLegs) {
            r.setStroke(new BasicStroke(3f));
            r.drawLine(-bodyWidth / 2 + 4, bodyHeight / 2 + 8, -bodyWidth / 2 - 12, bodyHeight / 2 + 24);
            r.drawLine(bodyWidth / 2 - 4, bodyHeight / 2 + 8, bodyWidth / 2 + 12, bodyHeight / 2 + 24);
        }
    }
    private void drawOrbitalRocket(Graphics2D r) {
        int bodyHeight = design.body.visualHeight;
        int bodyWidth = design.body.visualWidth;
        boolean delta = design.body.name.contains("Delta");
        boolean sls = design.body.name.contains("Space Launch");
        boolean saturn = design.body.name.contains("Saturn");
        boolean boostersSeparated = stageHasHappened(StagePiece.BOOSTER);
        boolean coreSeparated = stageHasHappened(StagePiece.CORE);
        boolean upperSeparated = stageHasHappened(StagePiece.UPPER_STAGE);
        if (delta) {
            if (upperSeparated) {
                drawPayloadCapsule(r, 0, -18, "Payload");
                return;
            }
            if (coreSeparated) {
                drawUpperStageModule(r, 0, -bodyHeight / 2 + 12, 24, 74, "DCSS");
                drawPayloadFairing(r, 0, -bodyHeight / 2 - 34, 32, 54);
                return;
            }
            int coreWidth = 24;
            int coreTop = -bodyHeight / 2;
            if (!boostersSeparated) {
                drawDeltaCommonBoosterCore(r, -coreWidth - 18, coreTop + 12, coreWidth, bodyHeight - 12, true);
                drawDeltaCommonBoosterCore(r, coreWidth + 18, coreTop + 12, coreWidth, bodyHeight - 12, true);
            }
            drawDeltaCommonBoosterCore(r, 0, coreTop, coreWidth, bodyHeight, false);
            drawUpperStageModule(r, 0, coreTop - 30, 22, 48, "DCSS");
            drawPayloadFairing(r, 0, coreTop - 74, 34, 48);
            return;
        }
        if (sls) {
            if (upperSeparated) {
                drawPayloadCapsule(r, 0, -20, "Orion");
                return;
            }
            if (coreSeparated) {
                drawUpperStageModule(r, 0, -bodyHeight / 2 + 16, 22, 70, "ICPS");
                drawPayloadCapsule(r, 0, -bodyHeight / 2 + 2, "Orion");
                return;
            }
            if (!boostersSeparated) {
                drawSolidRocketBooster(r, -bodyWidth / 2 - 14, -bodyHeight / 2 + 28, 14, bodyHeight - 42);
                drawSolidRocketBooster(r, bodyWidth / 2 + 14, -bodyHeight / 2 + 28, 14, bodyHeight - 42);
            }
            drawSlsCoreStage(r, -bodyWidth / 2, -bodyHeight / 2 + 6, bodyWidth, bodyHeight - 4);
            drawUpperStageModule(r, 0, -bodyHeight / 2 - 18, 24, 44, "ICPS");
            drawPayloadCapsule(r, 0, -bodyHeight / 2 - 38, "Orion");
            return;
        }
        if (saturn && upperSeparated) {
            drawPayloadCapsule(r, 0, -22, "Apollo");
            return;
        }
        Color stripe = saturn ? new Color(40, 45, 52) : new Color(235, 118, 58);
        if (!coreSeparated || saturn) {
            drawCore(r, -bodyWidth / 2, -bodyHeight / 2, bodyWidth, bodyHeight, new Color(232, 236, 240), stripe);
        } else {
            drawCore(r, -bodyWidth / 3, -bodyHeight / 2 + 26, bodyWidth * 2 / 3, 78, new Color(232, 236, 240), stripe);
        }
        r.setColor(new Color(226, 59, 56));
        int[] noseX = {-bodyWidth / 2, 0, bodyWidth / 2};
        int[] noseY = {-bodyHeight / 2 + 10, -bodyHeight / 2 - 34, -bodyHeight / 2 + 10};
        r.fillPolygon(noseX, noseY, 3);
        r.setColor(new Color(114, 54, 58));
        r.drawPolygon(noseX, noseY, 3);
        r.setColor(new Color(52, 59, 67));
        r.fillRect(-bodyWidth / 2 + 5, bodyHeight / 2 - 6, bodyWidth - 10, 16);
        if (design.landingLegs) {
            r.setStroke(new BasicStroke(4f));
            r.drawLine(-bodyWidth / 2 + 4, bodyHeight / 2 + 5, -bodyWidth / 2 - 20, bodyHeight / 2 + 30);
            r.drawLine(bodyWidth / 2 - 4, bodyHeight / 2 + 5, bodyWidth / 2 + 20, bodyHeight / 2 + 30);
        }
    }
    private void drawDeltaCommonBoosterCore(Graphics2D r, int centerX, int topY, int width, int height, boolean sideBooster) {
        int x = centerX - width / 2;
        drawCore(r, x, topY, width, height, new Color(224, 111, 45), new Color(238, 238, 232));
        r.setColor(new Color(242, 229, 202));
        r.fillRect(x + 3, topY + 12, Math.max(4, width - 6), 9);
        r.setColor(new Color(83, 91, 101));
        r.fillRect(x + 4, topY + height - 16, Math.max(4, width - 8), 12);
        drawEngineBell(r, centerX, topY + height + 7, width - 6);
        if (sideBooster) {
            r.setColor(new Color(82, 89, 96));
            r.fillRect(centerX < 0 ? x + width : x - 5, topY + height / 3, 5, 18);
        }
    }
    private void drawSolidRocketBooster(Graphics2D r, int centerX, int topY, int width, int height) {
        int x = centerX - width / 2;
        drawCore(r, x, topY, width, height, new Color(244, 244, 238), new Color(48, 55, 64));
        r.setColor(new Color(48, 55, 64));
        r.fillRect(x + 2, topY + 16, Math.max(4, width - 4), 8);
        r.fillRect(x + 2, topY + height - 28, Math.max(4, width - 4), 8);
        drawEngineBell(r, centerX, topY + height + 6, width - 4);
    }
    private void drawSlsCoreStage(Graphics2D r, int x, int y, int width, int height) {
        drawCore(r, x, y, width, height, new Color(213, 111, 45), new Color(238, 238, 232));
        r.setColor(new Color(236, 238, 235));
        r.fillRect(x + 4, y + height - 24, Math.max(6, width - 8), 18);
        r.setColor(new Color(74, 82, 92));
        r.drawRect(x + 4, y + height - 24, Math.max(6, width - 8), 18);
        for (int i = 0; i < 4; i++) {
            int engineX = x + 7 + i * Math.max(5, (width - 14) / 3);
            drawEngineBell(r, engineX, y + height + 6, 8);
        }
    }
    private void drawUpperStageModule(Graphics2D r, int centerX, int topY, int width, int height, String label) {
        int x = centerX - width / 2;
        drawCore(r, x, topY, width, height, new Color(235, 238, 242), new Color(77, 86, 96));
        r.setColor(new Color(53, 62, 72));
        r.setFont(r.getFont().deriveFont(Font.BOLD, 7f));
        FontMetrics metrics = r.getFontMetrics();
        r.drawString(label, centerX - metrics.stringWidth(label) / 2, topY + height / 2 + 3);
        drawEngineBell(r, centerX, topY + height + 5, Math.max(9, width - 8));
    }
    private void drawPayloadFairing(Graphics2D r, int centerX, int topY, int width, int height) {
        int half = width / 2;
        int baseY = topY + height;
        r.setColor(new Color(238, 241, 245));
        r.fillRoundRect(centerX - half, topY + height / 3, width, height * 2 / 3, 12, 12);
        r.fillPolygon(
            new int[] {centerX - half, centerX, centerX + half},
            new int[] {topY + height / 3 + 4, topY - 12, topY + height / 3 + 4},
            3
        );
        r.setColor(new Color(72, 82, 92));
        r.drawLine(centerX, topY + 2, centerX, baseY - 3);
        r.drawRoundRect(centerX - half, topY + height / 3, width, height * 2 / 3, 12, 12);
    }
    private void drawEngineBell(Graphics2D r, int centerX, int baseY, int width) {
        int halfTop = Math.max(3, width / 3);
        int halfBottom = Math.max(5, width / 2);
        r.setColor(new Color(52, 58, 66));
        r.fillPolygon(
            new int[] {centerX - halfTop, centerX + halfTop, centerX + halfBottom, centerX - halfBottom},
            new int[] {baseY - 8, baseY - 8, baseY + 6, baseY + 6},
            4
        );
    }
    private boolean stageHasHappened(StagePiece piece) {
        if (result == null || result.stagePlan == null) {
            return false;
        }
        for (StageEvent event : result.stagePlan.events) {
            if (event.piece == piece && progress >= event.progress) {
                return true;
            }
        }
        return false;
    }
    private void drawPayloadCapsule(Graphics2D r, int centerX, int noseY, String label) {
        r.setColor(new Color(238, 241, 245));
        r.fillOval(centerX - 13, noseY + 14, 26, 24);
        r.setColor(new Color(204, 67, 59));
        r.fillPolygon(new int[] {centerX - 13, centerX, centerX + 13}, new int[] {noseY + 18, noseY - 14, noseY + 18}, 3);
        r.setColor(new Color(72, 82, 92));
        r.drawOval(centerX - 13, noseY + 14, 26, 24);
        r.drawPolygon(new int[] {centerX - 13, centerX, centerX + 13}, new int[] {noseY + 18, noseY - 14, noseY + 18}, 3);
    }
    private void drawCore(Graphics2D r, int x, int y, int width, int height, Color bodyColor, Color stripe) {
        r.setColor(bodyColor);
        r.fillRoundRect(x, y, width, height, 12, 12);
        r.setColor(new Color(73, 82, 92));
        r.setStroke(new BasicStroke(2f));
        r.drawRoundRect(x, y, width, height, 12, 12);
        r.setColor(stripe);
        r.fillRect(x + 3, y + height / 2 - 8, Math.max(4, width - 6), 16);
    }
    private void drawHud(Graphics2D g, int width, int height) {
        g.setColor(theme.hudBackground);
        g.fillRoundRect(18, 18, 260, 110, 10, 10);
        g.setColor(theme.hudText);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 16f));
        g.drawString("Rocket Builder Academy", 32, 44);
        g.setFont(g.getFont().deriveFont(13f));
        if (design == null) {
            g.drawString("Pick parts, then launch.", 32, 68);
            g.drawString("Your rocket is on the pad.", 32, 88);
            return;
        }
        g.drawString("World: " + design.planet.name, 32, 68);
        if (result == null) {
            DesignEstimate estimate = RocketPhysics.estimate(design);
            g.drawString("Delta-v: " + formatMetersPerSecond(estimate.deltaV), 32, 88);
            g.drawString("Engine Isp: " + Math.round(design.engine.vacuumIsp) + " s vac", 32, 108);
            return;
        }
        int shownAltitude = (int) Math.round(result.maxAltitude * Math.sin(progress * Math.PI));
        if (progress > 0.5) {
            shownAltitude = (int) Math.round(result.maxAltitude * (1 - (progress - 0.5) * 0.12));
        }
        shownAltitude = Math.max(0, shownAltitude);
        g.drawString("Altitude: " + formatAltitude(shownAltitude), 32, 88);
        g.drawString("Delta-v: " + formatMetersPerSecond(result.estimate.deltaV), 32, 108);
        if (progress > 0.95) {
            drawBottomMessage(g, width, height, landingMessage());
        }
    }
    private void drawBottomMessage(Graphics2D g, int width, int height, String message) {
        int boxWidth = Math.max(180, Math.min(width - 54, 520));
        g.setFont(g.getFont().deriveFont(Font.BOLD, 17f));
        FontMetrics metrics = g.getFontMetrics();
        List<String> lines = wrapText(metrics, message, boxWidth - 28);
        int lineHeight = metrics.getHeight();
        int boxHeight = Math.max(48, lines.size() * lineHeight + 22);
        int boxX = (width - boxWidth) / 2;
        int boxY = height - 170;
        g.setColor(theme.hudBackground);
        g.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 14, 14);
        g.setColor(theme.softBorder);
        g.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 14, 14);
        g.setColor(result.landedSafely || result.orbitCapable ? new Color(36, 128, 93) : new Color(170, 81, 40));
        int firstBaseline = boxY + (boxHeight - lines.size() * lineHeight) / 2 + metrics.getAscent();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int textX = boxX + (boxWidth - metrics.stringWidth(line)) / 2;
            int textY = firstBaseline + i * lineHeight;
            g.drawString(line, Math.max(boxX + 14, textX), textY);
        }
    }
    private List<String> wrapText(FontMetrics metrics, String message, int maxWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        for (String word : message.split(" ")) {
            String candidate = currentLine.length() == 0 ? word : currentLine + " " + word;
            if (metrics.stringWidth(candidate) > maxWidth && currentLine.length() > 0) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                currentLine = new StringBuilder(candidate);
            }
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        if (lines.isEmpty()) {
            lines.add(message);
        }
        return lines;
    }
    private String landingMessage() {
        if (result.design.eggPayload) {
            return result.eggBroke ? "Egg cracked. Try a softer landing." : "Egg survived the mission!";
        }
        if (result.orbitCapable) {
            return "Space mission ready!";
        }
        return result.landedSafely ? "Soft landing!" : "Needs a gentler landing.";
    }
    private static String formatMetersPerSecond(double value) {
        if (value >= 1000) {
            return String.format("%.2f km/s", value / 1000.0);
        }
        return Math.round(value) + " m/s";
    }
    private static String formatAltitude(double value) {
        if (value >= 1000000) {
            return String.format("%.2f Mm", value / 1000000.0);
        }
        if (value >= 1000) {
            return String.format("%.1f km", value / 1000.0);
        }
        return Math.round(value) + " m";
    }
}
class RocketPhysics {
    private static final double G0 = 9.80665;
    static DesignEstimate estimate(RocketDesign design) {
        StagePlan stagePlan = StagePlanner.planFor(design);
        double gearMass = design.scale == RocketScale.MODEL ? modelGearMass(design) : orbitalGearMass(design);
        double dryMass = design.body.dryMassKg + design.engine.massKg + gearMass;
        double propellantMass = design.body.defaultPropellantKg * (design.fuelPercent / 100.0);
        double startMass = dryMass + propellantMass;
        double finalMass = Math.max(0.0001, dryMass);
        double averageIsp = design.engine.averageIsp(design.planet);
        double massFlow = design.engine.thrustNewtons / Math.max(0.0001, averageIsp * G0);
        double burnTime = propellantMass / Math.max(0.0001, massFlow);
        double deltaV = stagedDeltaV(design, dryMass, propellantMass, stagePlan.propulsionStages)
            * design.body.stageEfficiency
            * stagePlan.efficiencyMultiplier;
        double stability = calculateStability(design);
        double stagingLossFactor = Math.max(0.72, 1.0 - Math.max(0, stagePlan.propulsionStages - 1) * 0.06);
        double gravityLoss = design.planet.gravity * burnTime * (design.scale == RocketScale.ORBITAL ? 0.42 : 0.16) * stagingLossFactor;
        double dragLoss = design.planet.airDensityRatio() * design.body.dragCoefficient
            * (design.scale == RocketScale.ORBITAL ? 260 : 18)
            * (design.scale == RocketScale.ORBITAL ? Math.max(0.72, 1.0 - Math.max(0, stagePlan.propulsionStages - 1) * 0.05) : 1.0)
            * Math.max(0.35, 1.2 - stability);
        double effectiveDeltaV = Math.max(0, deltaV - gravityLoss - dragLoss);
        double thrustToWeight = design.engine.thrustNewtons / Math.max(0.0001, startMass * Math.max(0.05, design.planet.gravity));
        double targetDeltaV = design.scale == RocketScale.ORBITAL
            ? design.planet.lowOrbitDeltaVMps
            : Math.sqrt(2 * Math.max(0.1, design.planet.gravity) * design.planet.modelTargetAltitude) / 0.52;
        return new DesignEstimate(
            dryMass,
            propellantMass,
            startMass,
            finalMass,
            deltaV,
            effectiveDeltaV,
            targetDeltaV,
            burnTime,
            massFlow,
            thrustToWeight,
            stability
        );
    }
    private static double stagedDeltaV(RocketDesign design, double dryMass, double propellantMass, int propulsionStages) {
        int stages = Math.max(1, Math.min(4, propulsionStages));
        if (stages == 1) {
            double finalMass = Math.max(0.0001, dryMass);
            return design.engine.averageIsp(design.planet) * G0 * Math.log((dryMass + propellantMass) / finalMass);
        }
        double payloadDry = dryMass * (design.scale == RocketScale.MODEL ? 0.45 : 0.16);
        double stageDryTotal = Math.max(0.0001, dryMass - payloadDry);
        double dryWeightSum = stages * (stages + 1) / 2.0;
        double propWeightSum = dryWeightSum;
        double currentMass = dryMass + propellantMass;
        double deltaV = 0;
        for (int i = 0; i < stages; i++) {
            double weight = stages - i;
            double stageDry = stageDryTotal * weight / dryWeightSum;
            double stagePropellant = propellantMass * weight / propWeightSum;
            double massAfterBurn = Math.max(0.0001, currentMass - stagePropellant);
            deltaV += stageIsp(design, i, stages) * G0 * Math.log(currentMass / massAfterBurn);
            currentMass = Math.max(payloadDry, massAfterBurn - stageDry);
        }
        return deltaV;
    }
    private static double stageIsp(RocketDesign design, int stageIndex, int stages) {
        if (design.scale == RocketScale.MODEL) {
            return design.engine.averageIsp(design.planet);
        }
        if (design.planet.surfaceDensityKgM3 <= 0.01) {
            return design.engine.vacuumIsp;
        }
        if (stageIndex == 0) {
            return design.engine.seaLevelIsp * 0.62 + design.engine.vacuumIsp * 0.38;
        }
        if (stageIndex == stages - 1) {
            return design.engine.vacuumIsp;
        }
        return design.engine.averageIsp(design.planet);
    }
    static FlightResult simulate(RocketDesign design) {
        DesignEstimate estimate = estimate(design);
        StagePlan stagePlan = StagePlanner.planFor(design);
        double fuel = estimate.propellantMass;
        double altitude = 0;
        double velocity = 0;
        double maxAltitude = 0;
        double topSpeed = 0;
        double maxG = 0;
        double dt = design.scale == RocketScale.ORBITAL ? 0.25 : 0.02;
        double maxTime = design.scale == RocketScale.ORBITAL ? 1300 : 95;
        double time = 0;
        double thrustLoss = estimate.stability < 0.78 ? (0.78 - estimate.stability) * 0.34 : 0;
        while (time < maxTime) {
            boolean burning = fuel > 0.000001;
            double mass = estimate.dryMass + Math.max(0, fuel);
            double thrust = burning ? design.engine.thrustNewtons * (1.0 - thrustLoss) : 0;
            if (burning) {
                fuel -= estimate.massFlowKgPerSecond * dt;
            }
            double density = design.planet.airDensityAt(altitude);
            double drag = 0.5 * density * design.body.dragCoefficient * design.body.referenceAreaM2 * velocity * velocity;
            double dragForce = velocity > 0 ? -drag : drag;
            double acceleration = (thrust - mass * design.planet.gravity + dragForce) / mass;
            if (altitude <= 0 && velocity <= 0 && acceleration < 0) {
                acceleration = 0;
                velocity = 0;
            }
            velocity += acceleration * dt;
            altitude += velocity * dt;
            maxAltitude = Math.max(maxAltitude, altitude);
            topSpeed = Math.max(topSpeed, Math.abs(velocity));
            maxG = Math.max(maxG, Math.abs(acceleration) / G0);
            time += dt;
            if (!burning && altitude <= 0 && velocity < 0) {
                break;
            }
            if (altitude < 0) {
                altitude = 0;
            }
        }
        double ballisticAltitude = estimate.effectiveDeltaV * estimate.effectiveDeltaV
            / (2 * Math.max(0.1, design.planet.gravity))
            * (design.scale == RocketScale.ORBITAL ? 0.18 : 0.32);
        maxAltitude = Math.max(maxAltitude, ballisticAltitude);
        double landingSpeed = estimateLandingSpeed(design, estimate.dryMass, topSpeed);
        boolean landedSafely = landingSpeed < (design.scale == RocketScale.MODEL ? 12.5 : 22.0);
        boolean reachedSpace = maxAltitude >= design.planet.spaceAltitudeM;
        boolean orbitCapable = design.scale == RocketScale.ORBITAL && estimate.effectiveDeltaV >= design.planet.lowOrbitDeltaVMps;
        boolean eggBroke = design.eggPayload && (landingSpeed > 6.5 || maxG > 16.0 || !landedSafely);
        int altitudeScore = altitudeScore(design, maxAltitude, reachedSpace);
        int deltaVScore = clampScore((int) Math.round(estimate.effectiveDeltaV / Math.max(1, estimate.targetDeltaV) * 100));
        int reuseScore = reuseScore(design, landingSpeed, landedSafely);
        int safetyScore = safetyScore(design, estimate, topSpeed, maxG, landedSafely, eggBroke);
        int budgetScore = budgetScore(design);
        int fuelSmartScore = fuelSmartScore(design);
        int overallScore = clampScore((int) Math.round(
            design.scale == RocketScale.ORBITAL
                ? altitudeScore * 0.25 + deltaVScore * 0.28 + reuseScore * 0.15 + safetyScore * 0.17 + budgetScore * 0.07 + fuelSmartScore * 0.08
                : altitudeScore * 0.25 + deltaVScore * 0.10 + reuseScore * 0.25 + safetyScore * 0.20 + budgetScore * 0.10 + fuelSmartScore * 0.10
        ));
        if (design.parachute && design.planet.parachuteEffectiveness() <= 0.01) {
            overallScore = clampScore(overallScore - (design.scale == RocketScale.MODEL ? 10 : 6));
        }
        int cost = cost(design);
        List<String> praises = new ArrayList<>();
        List<String> advice = new ArrayList<>();
        buildFeedback(design, estimate, maxAltitude, reachedSpace, orbitCapable, landedSafely, eggBroke, altitudeScore,
            deltaVScore, budgetScore, fuelSmartScore, praises, advice);
        String letterGrade = gradeFor(overallScore);
        String summary = summaryFor(design, overallScore, reachedSpace, orbitCapable, landedSafely);
        return new FlightResult(
            design,
            stagePlan,
            estimate,
            maxAltitude,
            topSpeed,
            landingSpeed,
            maxG,
            altitudeScore,
            deltaVScore,
            reuseScore,
            safetyScore,
            budgetScore,
            fuelSmartScore,
            overallScore,
            cost,
            letterGrade,
            landedSafely,
            reachedSpace,
            orbitCapable,
            eggBroke,
            praises,
            advice,
            summary
        );
    }
    private static double modelGearMass(RocketDesign design) {
        double mass = design.fins * 0.006;
        if (design.parachute) {
            mass += 0.012;
        }
        if (design.landingLegs) {
            mass += 0.018;
        }
        if (design.heatShield) {
            mass += 0.010;
        }
        if (design.eggPayload) {
            mass += 0.057;
        }
        return mass;
    }
    private static double orbitalGearMass(RocketDesign design) {
        double mass = design.fins * 350.0;
        if (design.parachute) {
            mass += 1500.0;
        }
        if (design.landingLegs) {
            mass += 8000.0;
        }
        if (design.heatShield) {
            mass += 4500.0;
        }
        return mass;
    }
    private static double calculateStability(RocketDesign design) {
        double stability = 0.58 + design.fins * 0.085 + design.body.stabilityBonus;
        if (design.engine.thrustNewtons / Math.max(1, design.body.dryMassKg) > (design.scale == RocketScale.MODEL ? 95 : 165)) {
            stability -= 0.10;
        }
        if (design.fuelPercent > 105) {
            stability -= 0.06;
        }
        if (design.fuelPercent < 35) {
            stability -= 0.04;
        }
        if (design.scale == RocketScale.ORBITAL) {
            stability += 0.12;
        }
        return Math.max(0.45, Math.min(1.16, stability));
    }
    private static double estimateLandingSpeed(RocketDesign design, double dryMass, double topSpeed) {
        double speed = Math.sqrt(dryMass) * (design.scale == RocketScale.MODEL ? 3.1 : 0.40)
            + topSpeed * (design.scale == RocketScale.MODEL ? 0.018 : 0.002);
        speed *= Math.sqrt(Math.max(0.08, design.planet.gravity / 9.81));
        speed *= 1.0 + design.planet.airDensityRatio() * (design.scale == RocketScale.MODEL ? 0.04 : 0.015);
        if (design.parachute) {
            double parachutePower = design.planet.parachuteEffectiveness();
            double reduction = design.scale == RocketScale.MODEL ? 0.64 : 0.42;
            speed *= 1.0 - parachutePower * reduction;
            if (parachutePower <= 0.01) {
                speed *= 1.05;
            }
        }
        if (design.landingLegs) {
            speed *= design.scale == RocketScale.MODEL ? 0.74 : 0.68;
        }
        if (design.heatShield) {
            speed *= 0.88;
        }
        return speed;
    }
    private static int altitudeScore(RocketDesign design, double maxAltitude, boolean reachedSpace) {
        if (design.scale == RocketScale.ORBITAL) {
            int score = clampScore((int) Math.round(maxAltitude / Math.max(1, design.planet.spaceAltitudeM) * 100));
            return reachedSpace ? Math.max(82, score) : score;
        }
        return clampScore((int) Math.round(maxAltitude / Math.max(1, design.planet.modelTargetAltitude) * 100));
    }
    private static int reuseScore(RocketDesign design, double landingSpeed, boolean landedSafely) {
        double base = design.scale == RocketScale.MODEL ? 18 : 8;
        double parachuteBonus = design.parachute
            ? design.planet.parachuteEffectiveness() * (design.scale == RocketScale.MODEL ? 27 : 18)
            : 0;
        double score = base
            + design.engine.reuseScore * (design.scale == RocketScale.MODEL ? 28 : 34)
            + parachuteBonus
            + (design.landingLegs ? (design.scale == RocketScale.MODEL ? 16 : 28) : 0)
            + (design.heatShield ? (design.scale == RocketScale.MODEL ? 8 : 18) : 0)
            - Math.max(0, landingSpeed - (design.scale == RocketScale.MODEL ? 9 : 16)) * (design.scale == RocketScale.MODEL ? 4 : 2);
        if (design.parachute && design.planet.parachuteEffectiveness() <= 0.01) {
            score -= design.scale == RocketScale.MODEL ? 8 : 12;
        }
        if (landedSafely) {
            score += 8;
        }
        return clampScore((int) Math.round(score));
    }
    private static int safetyScore(
        RocketDesign design,
        DesignEstimate estimate,
        double topSpeed,
        double maxG,
        boolean landedSafely,
        boolean eggBroke
    ) {
        double score = estimate.stability * 48
            + (estimate.thrustToWeight >= 1.0 ? 18 : -12)
            + (design.parachute ? 8 * design.planet.parachuteEffectiveness() : 0)
            + (design.heatShield ? 10 : 0)
            + (landedSafely ? 12 : -10);
        if (design.parachute && design.planet.parachuteEffectiveness() <= 0.01) {
            score -= 8;
        }
        if (topSpeed > (design.scale == RocketScale.MODEL ? 150 : 8000)) {
            score -= 8;
        }
        if (maxG > (design.scale == RocketScale.MODEL ? 18 : 6)) {
            score -= 10;
        }
        if (design.eggPayload && !eggBroke) {
            score += 12;
        } else if (eggBroke) {
            score -= 15;
        }
        return clampScore((int) Math.round(score));
    }
    private static int budgetScore(RocketDesign design) {
        int cost = cost(design);
        int friendlyBudget = design.scale == RocketScale.MODEL ? 230 : 1850000;
        int divisor = design.scale == RocketScale.MODEL ? 4 : 18000;
        return clampScore(100 - Math.max(0, cost - friendlyBudget) / divisor);
    }
    private static int fuelSmartScore(RocketDesign design) {
        double ideal = design.scale == RocketScale.MODEL
            ? 70 + design.body.dryMassKg * 40 + (design.planet.gravity - 3) * 1.5
            : 88 + (design.planet.gravity - 9.81) * 1.3 + design.planet.airDensityRatio() * 2.5;
        if (design.planet.surfaceDensityKgM3 <= 0.01) {
            ideal -= design.scale == RocketScale.MODEL ? 12 : 8;
        }
        ideal = Math.max(35, Math.min(112, ideal));
        return clampScore((int) Math.round(100 - Math.abs(design.fuelPercent - ideal) * 1.35));
    }
    static int cost(RocketDesign design) {
        int stageCount = StagePlanner.planFor(design).propulsionStages;
        double accessoryCost = design.fins * (design.scale == RocketScale.MODEL ? 10 : 12000)
            + (design.parachute ? (design.scale == RocketScale.MODEL ? 25 : 90000) : 0)
            + (design.landingLegs ? (design.scale == RocketScale.MODEL ? 30 : 170000) : 0)
            + (design.heatShield ? (design.scale == RocketScale.MODEL ? 24 : 140000) : 0)
            + (design.eggPayload ? 4 : 0)
            + Math.max(0, stageCount - 1) * (design.scale == RocketScale.MODEL ? 35 : 180000);
        return (int) Math.round(design.body.cost + design.engine.cost + design.fuelPercent * (design.scale == RocketScale.MODEL ? 1.8 : 3200) + accessoryCost);
    }
    private static void buildFeedback(
        RocketDesign design,
        DesignEstimate estimate,
        double maxAltitude,
        boolean reachedSpace,
        boolean orbitCapable,
        boolean landedSafely,
        boolean eggBroke,
        int altitudeScore,
        int deltaVScore,
        int budgetScore,
        int fuelSmartScore,
        List<String> praises,
        List<String> advice
    ) {
        if (design.scale == RocketScale.ORBITAL) {
            if (reachedSpace) {
                praises.add("This huge rocket crossed the space line on " + design.planet.name + ".");
            } else {
                advice.add("Add fuel, choose a higher-Isp engine, or launch from a lower-gravity world to reach space.");
            }
            if (orbitCapable) {
                praises.add("The useful delta-v meets the low-orbit target for this world.");
            } else {
                advice.add("Watch useful delta-v. It needs to beat the target after gravity and drag losses.");
            }
        } else {
            if (altitudeScore >= 75) {
                praises.add("The model rocket made a strong climb for " + design.planet.name + ".");
            } else {
                advice.add("Try more useful delta-v: reduce mass, add fuel carefully, or pick a stronger engine.");
            }
            if (design.parachute && design.planet.parachuteEffectiveness() > 0.05) {
                praises.add("The nose cone popped and the parachute had room to open above the rocket.");
            } else if (design.parachute) {
                advice.add("Parachutes need air. On " + design.planet.name + ", the parachute adds weight but cannot slow the rocket.");
            } else if (design.planet.parachuteEffectiveness() <= 0.01) {
                praises.add("Skipping the parachute avoided dead weight on an airless world.");
            } else {
                advice.add("A parachute helps the model rocket drift down gently.");
            }
        }
        if (design.stagingMode == StageMode.PRESET) {
            praises.add("The staging plan follows the real vehicle's major separation events.");
        } else if (StagePlanner.planFor(design).propulsionStages > 1) {
            praises.add("Custom staging drops empty hardware instead of carrying it all the way up.");
        }
        if (estimate.stability >= 0.94) {
            praises.add("The stability setup kept the flight path tidy.");
        } else {
            advice.add("Add fins or reduce overpowering thrust to improve stability.");
        }
        if (landedSafely) {
            praises.add("The recovery gear made the landing reusable.");
        } else {
            advice.add("A slower landing improves reuse and protects payloads.");
        }
        if (design.eggPayload && !eggBroke) {
            praises.add("The egg passenger survived. That is gentle engineering.");
        } else if (eggBroke) {
            if (design.planet.parachuteEffectiveness() <= 0.01) {
                advice.add("The egg cracked. On an airless world, lower landing speed with less mass, gentler thrust, or landing legs.");
            } else {
                advice.add("The egg cracked. Lower landing speed and max g with a parachute or lighter engine.");
            }
        }
        if (budgetScore >= 75) {
            praises.add("The budget stayed friendly for the mission.");
        } else {
            advice.add("Try fewer extras or a smaller rocket if you want a better cost grade.");
        }
        if (deltaVScore < 70) {
            advice.add("Delta-v is low for this goal. Higher Isp, less dry mass, or more propellant can help.");
        } else {
            praises.add("The delta-v budget is healthy for this mission.");
        }
        if (fuelSmartScore < 65) {
            advice.add("Fuel has a sweet spot: too little limits burn time, too much becomes heavy.");
        }
        if (praises.isEmpty()) {
            praises.add("The rocket team collected useful test data.");
        }
        if (advice.isEmpty()) {
            advice.add("Challenge yourself: keep the grade while lowering cost or protecting an egg.");
        }
    }
    private static int clampScore(int value) {
        return Math.max(0, Math.min(100, value));
    }
    private static String gradeFor(int score) {
        if (score >= 98) {
            return "A+";
        }
        if (score >= 90) {
            return "A";
        }
        if (score >= 80) {
            return "B";
        }
        if (score >= 70) {
            return "C";
        }
        if (score >= 60) {
            return "D";
        }
        return "F";
    }
    private static String summaryFor(RocketDesign design, int score, boolean reachedSpace, boolean orbitCapable, boolean landedSafely) {
        if (design.scale == RocketScale.ORBITAL && reachedSpace && orbitCapable) {
            return "Excellent heavy-lift mission. This design has enough useful delta-v for space.";
        }
        if (score >= 85 && landedSafely) {
            return "Excellent engineering. This design is ready for another mission.";
        }
        if (score >= 70) {
            return "Good work. A few smart changes could turn this into a star flyer.";
        }
        if (score >= 50) {
            return "Promising start. Every test teaches the team something new.";
        }
        return "Back to the design lab. Tinker, test, and try again.";
    }
}
enum RocketScale { MODEL("Model rocket"), ORBITAL("Real-life orbital rocket"); private final String label; RocketScale(String label){this.label=label;} public String toString(){return label;} }
enum StageMode { PRESET("Predetermined staging"), CUSTOM("Custom staging"); private final String label; StageMode(String label){this.label=label;} public String toString(){return label;} }
enum GameTheme {
    LIGHT("Light theme", new Color(242,247,251), Color.WHITE, new Color(248,251,253), new Color(54,70,84), new Color(54,70,84), new Color(83,94,104), new Color(211,222,232), new Color(220,229,236), new Color(37,56,102), Color.WHITE, new Color(29,43,78), new Color(255,255,255,224), new Color(54,70,84)),
    BLUE("Rocket Academy blue", new Color(21,32,61), new Color(37,56,102), new Color(236,242,251), new Color(246,249,255), new Color(34,48,88), new Color(211,222,241), new Color(97,125,177), new Color(144,166,208), new Color(244,40,67), Color.WHITE, new Color(255,188,199), new Color(236,242,251,228), new Color(34,48,88)),
    DARK("Dark mode", new Color(13,17,23), new Color(24,31,42), new Color(238,242,248), new Color(235,241,248), new Color(31,41,55), new Color(174,185,201), new Color(69,82,104), new Color(103,119,145), new Color(77,139,245), Color.WHITE, new Color(149,185,255), new Color(238,242,248,232), new Color(25,36,54));
    final String label; final Color appBackground,panelBackground,cardBackground,text,cardText,mutedText,border,softBorder,accent,buttonText,buttonBorder,hudBackground,hudText;
    GameTheme(String label, Color appBackground, Color panelBackground, Color cardBackground, Color text, Color cardText, Color mutedText, Color border, Color softBorder, Color accent, Color buttonText, Color buttonBorder, Color hudBackground, Color hudText){this.label=label;this.appBackground=appBackground;this.panelBackground=panelBackground;this.cardBackground=cardBackground;this.text=text;this.cardText=cardText;this.mutedText=mutedText;this.border=border;this.softBorder=softBorder;this.accent=accent;this.buttonText=buttonText;this.buttonBorder=buttonBorder;this.hudBackground=hudBackground;this.hudText=hudText;}
    public String toString(){return label;}
}
enum CustomStageRole { TAKEOFF_BOOSTER("Takeoff booster","takeoff",StagePiece.BOOSTER), CORE_STAGE("Core stage","middle-atmosphere",StagePiece.CORE), UPPER_ATMOSPHERE_CORE("Upper-atmosphere stage","upper atmosphere",StagePiece.CORE), SPACE_UPPER_STAGE("Space/vacuum stage","space",StagePiece.UPPER_STAGE); private final String label; final String flightRegion; final StagePiece piece; CustomStageRole(String label,String flightRegion,StagePiece piece){this.label=label;this.flightRegion=flightRegion;this.piece=piece;} public String toString(){return label;} }
enum StagePiece { BOOSTER, CORE, UPPER_STAGE, PAYLOAD }
class StageEvent { final String name; final double progress; final StagePiece piece; StageEvent(String name,double progress,StagePiece piece){this.name=name;this.progress=progress;this.piece=piece;} }
class StagePlan { final String name; final int propulsionStages; final List<StageEvent> events; final double efficiencyMultiplier; StagePlan(String name,int propulsionStages,List<StageEvent> events){this(name,propulsionStages,events,1.0);} StagePlan(String name,int propulsionStages,List<StageEvent> events,double efficiencyMultiplier){this.name=name;this.propulsionStages=propulsionStages;this.events=events;this.efficiencyMultiplier=efficiencyMultiplier;} String shortDescription(){return propulsionStages+" stage"+(propulsionStages==1?"":"s")+" - "+name;} }
class StagePlanner {
    static StagePlan planFor(RocketDesign design){return design.stagingMode==StageMode.PRESET&&design.scale==RocketScale.ORBITAL?presetPlan(design.body.name):customPlan(design);}
    private static StagePlan presetPlan(String bodyName){
        if(bodyName.contains("Space Launch")) return new StagePlan("SLS Block 1 preset",3,events(new StageEvent("Booster separation",0.30,StagePiece.BOOSTER),new StageEvent("Core stage separation",0.60,StagePiece.CORE),new StageEvent("ICPS separation",0.80,StagePiece.UPPER_STAGE),new StageEvent("Orion module continues",0.88,StagePiece.PAYLOAD)));
        if(bodyName.contains("Delta IV")) return new StagePlan("Delta IV Heavy preset",3,events(new StageEvent("Twin side CBC booster separation",0.32,StagePiece.BOOSTER),new StageEvent("Center CBC core stage separation",0.62,StagePiece.CORE),new StageEvent("Delta cryogenic/Centaur-style upper stage separation",0.82,StagePiece.UPPER_STAGE),new StageEvent("Payload continues",0.90,StagePiece.PAYLOAD)));
        if(bodyName.contains("Saturn")) return new StagePlan("Saturn V preset",3,events(new StageEvent("S-IC first stage separation",0.30,StagePiece.CORE),new StageEvent("S-II second stage separation",0.56,StagePiece.CORE),new StageEvent("S-IVB third stage separation",0.78,StagePiece.UPPER_STAGE),new StageEvent("Apollo spacecraft continues",0.88,StagePiece.PAYLOAD)));
        return new StagePlan("Heavy-lift preset",3,events(new StageEvent("Takeoff booster separation",0.32,StagePiece.BOOSTER),new StageEvent("Core stage separation",0.62,StagePiece.CORE),new StageEvent("Space stage separation",0.82,StagePiece.UPPER_STAGE),new StageEvent("Payload continues",0.90,StagePiece.PAYLOAD)));
    }
    private static StagePlan customPlan(RocketDesign design){int stages=Math.max(1,Math.min(4,design.customStageCount));List<StageEvent> events=new ArrayList<>();for(int i=1;i<=stages;i++){double progress=0.24+i*(0.58/Math.max(1,stages));CustomStageRole role=design.customStageRole(i);events.add(new StageEvent("Custom stage "+i+" separation ("+role.flightRegion+")",progress,role.piece));}events.add(new StageEvent(design.scale==RocketScale.MODEL?"Recovery payload continues":"Payload continues",0.90,StagePiece.PAYLOAD));return new StagePlan("custom "+stages+"-stage stack",stages,events,customStageEfficiency(design,stages));}
    private static double customStageEfficiency(RocketDesign design,int stages){double score=1.0;for(int i=1;i<=stages;i++){CustomStageRole role=design.customStageRole(i);boolean first=i==1,last=i==stages;if(first&&role==CustomStageRole.TAKEOFF_BOOSTER)score+=0.025;else if(last&&role==CustomStageRole.SPACE_UPPER_STAGE)score+=0.025;else if(!first&&!last&&(role==CustomStageRole.CORE_STAGE||role==CustomStageRole.UPPER_ATMOSPHERE_CORE))score+=0.015;else score-=0.035;}return Math.max(0.88,Math.min(1.07,score));}
    private static List<StageEvent> events(StageEvent... stageEvents){List<StageEvent> list=new ArrayList<>();for(StageEvent stageEvent:stageEvents)list.add(stageEvent);return list;}
}
class RocketDesign { final RocketScale scale; final Planet planet; final BodyType body; final EngineType engine; final int fuelPercent,fins,customStageCount; final boolean parachute,landingLegs,heatShield,eggPayload,showAtmosphereLayers; final StageMode stagingMode; final CustomStageRole stageOneRole,stageTwoRole,stageThreeRole,stageFourRole;
    RocketDesign(RocketScale scale,Planet planet,BodyType body,EngineType engine,int fuelPercent,int fins,boolean parachute,boolean landingLegs,boolean heatShield,boolean eggPayload,boolean showAtmosphereLayers,StageMode stagingMode,int customStageCount,CustomStageRole stageOneRole,CustomStageRole stageTwoRole,CustomStageRole stageThreeRole,CustomStageRole stageFourRole){this.scale=scale;this.planet=planet;this.body=body;this.engine=engine;this.fuelPercent=fuelPercent;this.fins=fins;this.parachute=parachute;this.landingLegs=landingLegs;this.heatShield=heatShield;this.eggPayload=eggPayload;this.showAtmosphereLayers=showAtmosphereLayers;this.stagingMode=stagingMode;this.customStageCount=customStageCount;this.stageOneRole=stageOneRole==null?CustomStageRole.TAKEOFF_BOOSTER:stageOneRole;this.stageTwoRole=stageTwoRole==null?CustomStageRole.UPPER_ATMOSPHERE_CORE:stageTwoRole;this.stageThreeRole=stageThreeRole==null?CustomStageRole.SPACE_UPPER_STAGE:stageThreeRole;this.stageFourRole=stageFourRole==null?CustomStageRole.SPACE_UPPER_STAGE:stageFourRole;}
    CustomStageRole customStageRole(int stageNumber){return stageNumber<=1?stageOneRole:stageNumber==2?stageTwoRole:stageNumber==3?stageThreeRole:stageFourRole;}
}
class BodyType { final RocketScale scale; final String name; final double dryMassKg,defaultPropellantKg,dragCoefficient,referenceAreaM2,stabilityBonus,stageEfficiency; final int cost,visualHeight,visualWidth; BodyType(RocketScale scale,String name,double dryMassKg,double defaultPropellantKg,double dragCoefficient,double referenceAreaM2,int cost,double stabilityBonus,int visualHeight,int visualWidth,double stageEfficiency){this.scale=scale;this.name=name;this.dryMassKg=dryMassKg;this.defaultPropellantKg=defaultPropellantKg;this.dragCoefficient=dragCoefficient;this.referenceAreaM2=referenceAreaM2;this.cost=cost;this.stabilityBonus=stabilityBonus;this.visualHeight=visualHeight;this.visualWidth=visualWidth;this.stageEfficiency=stageEfficiency;} public String toString(){return name;} }
class EngineType { final RocketScale scale; final String name,notes; final double thrustNewtons,massKg,seaLevelIsp,vacuumIsp,reliability,reuseScore; final int cost; EngineType(RocketScale scale,String name,double thrustNewtons,double massKg,double seaLevelIsp,double vacuumIsp,int cost,double reliability,double reuseScore,String notes){this.scale=scale;this.name=name;this.thrustNewtons=thrustNewtons;this.massKg=massKg;this.seaLevelIsp=seaLevelIsp;this.vacuumIsp=vacuumIsp;this.cost=cost;this.reliability=reliability;this.reuseScore=reuseScore;this.notes=notes;} double averageIsp(Planet planet){if(planet.surfaceDensityKgM3<=0.01)return vacuumIsp;double seaLevelWeight=Math.min(0.62,0.34+planet.airDensityRatio()*0.07);return seaLevelIsp*seaLevelWeight+vacuumIsp*(1.0-seaLevelWeight);} public String toString(){return name+"  Isp "+Math.round(seaLevelIsp)+"/"+Math.round(vacuumIsp)+" s";} }
class Planet { final String name,note; final double gravity,surfaceDensityKgM3,scaleHeightM,lowOrbitDeltaVMps,spaceAltitudeM,modelTargetAltitude; final Color skyTop,skyBottom,groundTop,groundBottom; Planet(String name,double gravity,double surfaceDensityKgM3,double scaleHeightM,double lowOrbitDeltaVMps,double spaceAltitudeM,double modelTargetAltitude,Color skyTop,Color skyBottom,Color groundTop,Color groundBottom,String note){this.name=name;this.gravity=gravity;this.surfaceDensityKgM3=surfaceDensityKgM3;this.scaleHeightM=scaleHeightM;this.lowOrbitDeltaVMps=lowOrbitDeltaVMps;this.spaceAltitudeM=spaceAltitudeM;this.modelTargetAltitude=modelTargetAltitude;this.skyTop=skyTop;this.skyBottom=skyBottom;this.groundTop=groundTop;this.groundBottom=groundBottom;this.note=note;} double airDensityAt(double altitudeM){return surfaceDensityKgM3<=0||scaleHeightM<=1?0:surfaceDensityKgM3*Math.exp(-Math.max(0,altitudeM)/scaleHeightM);} double airDensityRatio(){return surfaceDensityKgM3/1.225;} double parachuteEffectiveness(){return surfaceDensityKgM3<=0.0001?0:Math.max(0,Math.min(1,airDensityRatio()*2.2));} static Planet earthLike(){return new Planet("Earth",9.81,1.225,8500,9400,100000,300,new Color(84,174,235),new Color(214,239,255),new Color(91,152,97),new Color(79,134,84),"Earth-like default");} public String toString(){return name+"  "+String.format("%.2f",gravity)+" m/s^2";} }
class DesignEstimate { final double dryMass,propellantMass,startMass,finalMass,deltaV,effectiveDeltaV,targetDeltaV,burnTimeSeconds,massFlowKgPerSecond,thrustToWeight,stability; DesignEstimate(double dryMass,double propellantMass,double startMass,double finalMass,double deltaV,double effectiveDeltaV,double targetDeltaV,double burnTimeSeconds,double massFlowKgPerSecond,double thrustToWeight,double stability){this.dryMass=dryMass;this.propellantMass=propellantMass;this.startMass=startMass;this.finalMass=finalMass;this.deltaV=deltaV;this.effectiveDeltaV=effectiveDeltaV;this.targetDeltaV=targetDeltaV;this.burnTimeSeconds=burnTimeSeconds;this.massFlowKgPerSecond=massFlowKgPerSecond;this.thrustToWeight=thrustToWeight;this.stability=stability;} }
class FlightResult { final RocketDesign design; final StagePlan stagePlan; final DesignEstimate estimate; final double maxAltitude,topSpeed,landingSpeed,maxG; final int altitudeScore,deltaVScore,reuseScore,safetyScore,budgetScore,fuelSmartScore,overallScore,cost; final String letterGrade,summary; final boolean landedSafely,reachedSpace,orbitCapable,eggBroke; final List<String> praises,advice; FlightResult(RocketDesign design,StagePlan stagePlan,DesignEstimate estimate,double maxAltitude,double topSpeed,double landingSpeed,double maxG,int altitudeScore,int deltaVScore,int reuseScore,int safetyScore,int budgetScore,int fuelSmartScore,int overallScore,int cost,String letterGrade,boolean landedSafely,boolean reachedSpace,boolean orbitCapable,boolean eggBroke,List<String> praises,List<String> advice,String summary){this.design=design;this.stagePlan=stagePlan;this.estimate=estimate;this.maxAltitude=maxAltitude;this.topSpeed=topSpeed;this.landingSpeed=landingSpeed;this.maxG=maxG;this.altitudeScore=altitudeScore;this.deltaVScore=deltaVScore;this.reuseScore=reuseScore;this.safetyScore=safetyScore;this.budgetScore=budgetScore;this.fuelSmartScore=fuelSmartScore;this.overallScore=overallScore;this.cost=cost;this.letterGrade=letterGrade;this.landedSafely=landedSafely;this.reachedSpace=reachedSpace;this.orbitCapable=orbitCapable;this.eggBroke=eggBroke;this.praises=praises;this.advice=advice;this.summary=summary;} }
class RocketPose { final int x,y; final double angle; RocketPose(int x,int y,double angle){this.x=x;this.y=y;this.angle=angle;} }
