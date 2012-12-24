import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

public class DragonBukkitPluginUpdater {

    public static String LINE_SEPERATOR = System.getProperty("line.separator");

    public static void replaceAll(File sourceRoot, String oldVersion,
            String newVersion) {
        List<File> sources = DragonBukkitPluginUpdater.findSources(sourceRoot);
        for (File f : sources) {
            DragonBukkitPluginUpdater.replaceVersion(f, oldVersion, newVersion);
        }
    }

    public static List<File> findSources(File rootFolder) {
        ArrayList<File> sources = new ArrayList<File>();
        File[] files = rootFolder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    sources.addAll(findSources(f));
                } else if (f.getName().endsWith(".java")) {
                    sources.add(f);
                }
            }
        }
        return sources;
    }

    public static void replace(String oldString, String newString,
            File oldFile, File newFile) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(oldFile));
            FileWriter out = new FileWriter(newFile);
            String line = null;
            try {
                while ((line = in.readLine()) != null) {
                    out.append(line.replace(oldString, newString)
                            + LINE_SEPERATOR);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copy(File from, File to) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(from));
            FileWriter out = new FileWriter(to);
            String line = null;
            try {
                while ((line = in.readLine()) != null) {
                    out.append(line + LINE_SEPERATOR);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void replaceVersion(File sourceFile, String oldString,
            String newString) {
        File tmpFile = new File(sourceFile.getAbsoluteFile() + ".tmp");
        DragonBukkitPluginUpdater.copy(sourceFile, tmpFile);
        DragonBukkitPluginUpdater.replace(oldString, newString, tmpFile,
                sourceFile);
        tmpFile.delete();
    }

    public static File getSourceRoot() {
        File f = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Source Folder of plugin");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(null)) {
            f = chooser.getSelectedFile();
        }
        return f;
    }

    public static String detectVersion(File sourceRoot) {
        return detectVersion(DragonBukkitPluginUpdater.findSources(sourceRoot));
    }

    public static String detectVersion(List<File> sourceFiles) {
        String version = null;
        for (File f : sourceFiles) {
            version = detectVersionFromOneFile(f);
            if (version != null) {
                return version;
            }
        }
        return null;
    }

    public static String detectVersionFromOneFile(File f) {
        String version = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader(f));
            String line = null;
            try {
                while ((line = in.readLine()) != null) {
                    version = analyseLine(line);
                    if (version != null) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String analyseLine(String line) {
        String version = null;
        String[] packages = new String[] { "net.minecraft.server.v",
                "org.bukkit.craftbukkit.v" };
        for (int i = 0; i < packages.length && version == null; i++) {
            version = analyseLine(line, packages[i]);
        }
        return version;
    }

    public static String analyseLine(String line, String nms) {
        String version = null;
        int start = line.indexOf(nms);
        if (start > 0) {
            try {
                int end = line.indexOf(".", start + nms.length() - 1);
                version = line.substring(start + nms.length() - 1, end);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return version;
    }

    public static String increaseVersion(String oldVersion) {
        int last = oldVersion.length() - 1;
        char lastNumber = oldVersion.charAt(last);
        lastNumber++;
        return oldVersion.substring(0, last) + lastNumber;
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        setLookAndFeel();
        // System.out.println(getSourceRoot());
        List<File> sources = DragonBukkitPluginUpdater.findSources(new File(
                "D:/test/src"));
        String version = detectVersion(sources);
        System.out.println(version + ":" + increaseVersion(version));
        for (File f : sources) {
            replaceVersion(f, ".v1_4_5.", ".v1_4_6.");
        }
        System.out.println("" + sources.size());
    }
}
