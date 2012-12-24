import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConfirmFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel main;

    private String sources;
    private String oldVersion;
    private String newVersion;

    private JTextField field_sources;
    private JTextField field_oldVersion;
    private JTextField field_newVersion;

    private ActionListener listener;

    public ConfirmFrame(String sourcesRoot, String oldVersion,
            String newVersion, ActionListener listener) {
        this.sources = sourcesRoot;
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
        this.listener = listener;
        init();
    }

    private void init() {
        initMainPanel();

        super.setTitle("Dragon Bukkit Plugin Updater");
        super.setMinimumSize(new Dimension(300, 0));
        super.pack();
        super.setLocationRelativeTo(null);
        super.setVisible(true);
        super.toFront();
        super.setAlwaysOnTop(true);
        super.setAlwaysOnTop(false);

    }

    private void initMainPanel() {
        this.main = new JPanel();
        this.main.setLayout(new GridBagLayout());
        this.main.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        GridBagConstraints left = new GridBagConstraints();
        left.insets = new Insets(3, 0, 0, 3);
        left.weightx = 0;
        left.anchor = GridBagConstraints.LINE_START;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(3, 0, 3, 3);
        right.weightx = 1.0;
        right.fill = 1;
        right.gridwidth = GridBagConstraints.REMAINDER;

        this.main.add(new JLabel("Sources"), left);
        this.field_sources = new JTextField(this.sources);
        this.main.add(this.field_sources, right);

        this.main.add(new JLabel("old Version"), left);
        this.field_oldVersion = new JTextField(this.oldVersion);
        this.main.add(this.field_oldVersion, right);

        this.main.add(new JLabel("new Version"), left);
        this.field_newVersion = new JTextField(this.newVersion);
        this.main.add(this.field_newVersion, right);

        JButton ok = new JButton("Replace all");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                replaceAllAction();
            }
        });
        this.main.add(ok, right);

        super.setContentPane(this.main);
    }

    public void replaceAllAction() {
        this.sources = this.field_sources.getText();
        this.oldVersion = this.field_oldVersion.getText();
        this.newVersion = this.field_newVersion.getText();
        if (this.listener != null) {
            this.listener.actionPerformed(null);
        }
        super.setVisible(false);
    }

    public String getSources() {
        return this.sources;
    }

    public String getOldVersion() {
        return this.oldVersion;
    }

    public String getNewVersion() {
        return this.newVersion;
    }

    public static void main(String[] args) {
        DragonBukkitPluginUpdater.setLookAndFeel();
        new ConfirmFrame("D:/test", "v1.4", "v1.5", null);
    }
}
