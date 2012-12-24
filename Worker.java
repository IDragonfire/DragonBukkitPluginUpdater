import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;

public class Worker {

    private File sourceRoot;
    private String oldVersion;
    private String newVersion;

    private ConfirmFrame frame;

    public Worker() {
        DragonBukkitPluginUpdater.setLookAndFeel();
        this.sourceRoot = DragonBukkitPluginUpdater.getSourceRoot();
        this.oldVersion = DragonBukkitPluginUpdater
                .detectVersion(this.sourceRoot);
        this.newVersion = DragonBukkitPluginUpdater
                .increaseVersion(this.oldVersion);

        this.frame = new ConfirmFrame(this.sourceRoot.getAbsolutePath(),
                this.oldVersion, this.newVersion, new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        replaceAllProgress();
                    }
                });

    }

    private void replaceAllProgress() {
        DragonBukkitPluginUpdater.replaceAll(new File(this.frame.getSources()),
                this.frame.getOldVersion(), this.frame.getNewVersion());
        JOptionPane.showMessageDialog(null, "Done !");
    }

    public static void main(String[] args) {
        new Worker();
    }
}
