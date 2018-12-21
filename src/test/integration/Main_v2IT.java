package test.integration;

import main_v2.ArgumentsParser_v2;
import main_v2.Main_v2;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;

import javax.swing.*;
import java.io.*;

import static main_v2.Main_v2.consoleVersion;


@Category(IntegrationTest.class)
public class Main_v2IT {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder(new File("temp"));

    private InputStream originalInputStream;

    private void setUp() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException,
            IllegalAccessException, IOException {

        temporaryFolder.create();

        // main_v2.Main_v2.main(...) related things
        System.setProperty("viromeplacer_version", consoleVersion);
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

        // save I/O context
        originalInputStream = System.in;
        File inputStreamFile = temporaryFolder.newFile("input_stream.dat");
        FileInputStream fileInputStream = new FileInputStream(inputStreamFile);
        System.setIn(fileInputStream);
    }

    private void tearDown() {
        // restore context
        System.setIn(originalInputStream);

        // NOTE:
        // Comment this line in case of failed tests to analyze the outputs
        // temporaryFolder.delete();
    }

    @Test
    public void buildDatabaseTest() throws Exception {
        setUp();

        String[] arguments = {
                "-m", "JC69",
                "-s", "nucl",
                "-b", "bindep/phyml_x64",
                "-w ", "temp",
                "-r", "data/test/6_leaves/6_leaves_test_set.aln",
                "-t", "data/test/6_leaves/6_leaves_test_set.tree",
                "-k", "3",
                "-p", "b",
                "-w", temporaryFolder.getRoot().getAbsolutePath()
        };
        ArgumentsParser_v2 argsParser = new ArgumentsParser_v2(arguments, consoleVersion);
        Main_v2.buildDatabase(argsParser);

        tearDown();
    }
}