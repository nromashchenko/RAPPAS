package test.integration;

import com.sun.tools.javac.Main;
import main_v2.ArgumentsParser_v2;
import main_v2.Main_v2;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.swing.*;
import java.io.*;

import static main_v2.Main_v2.consoleVersion;


@Category(IntegrationTest.class)
public class Main_v2IT {

    private InputStream originalInputStream;
    private final String inputStreamFilename = "temp/tempStreamFile.dat";

    private void setUp() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException,
            IllegalAccessException, FileNotFoundException {

        System.setProperty("viromeplacer_version", consoleVersion);
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

        // save context
        originalInputStream = System.in;
        final FileInputStream fileInputStream = new FileInputStream(new File(inputStreamFilename));
        System.setIn(fileInputStream);
    }

    private void tearDown() {
        // restore context
        System.setIn(originalInputStream);
    }

    @Test
    public void buildDatabaseTest() throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException,
            InstantiationException, IllegalAccessException {

        setUp();

        String[] arguments = {
                "-m", "JC69",
                "-s", "nucl",
                "-b", "bindep/phyml_x64",
                "-w ", "temp",
                "-r", "data/6_leaves_test_set.aln",
                "-t", "data/6_leaves_test_set.tree",
                "-k", "3",
                "-p", "b"
        };
        ArgumentsParser_v2 argsParser = new ArgumentsParser_v2(arguments, consoleVersion);
        Main_v2.buildDatabase(argsParser);

        tearDown();
    }
}