package com.baselet.our;

import com.baselet.standalone.MainStandalone;
import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

/**
 * © 2017, Gopper
 */
public class ExportTest {
    private File file;
    private File outputFile;
    private File exampleFile;

	@Before
	public void init() {
	    String name = "example.uxf";
	    String exampleName = "example.jpg";
	    String outputName = "test.jpg";

	    file = getFile(name);
	    assertNotNull("The example UXF file shouldn't be null", file);

	    exampleFile = getFile(exampleName);
        assertNotNull("The example JPG file shouldn't be null", file);

	    outputFile = new File(
            file.getAbsolutePath().replace(name, outputName)
        );
	    assertNotNull("The output JPG file shouldn't be null", outputFile);
    }

    @After
    public void cleanUp() {
        assertTrue("The generated output file should be deleted", outputFile.delete());
    }


	@Test
	public void testJpgExport() throws Exception {
	    String input = file.getAbsolutePath();
	    String output = outputFile.getAbsolutePath().replace(".jpg", "");

        assertNotNull(file);
        MainStandalone.main(new String[] {
            "-action=convert",
            "-format=jpg",
            "-filename=" + input,
            "-output=" + output
        });
        assertFileExists(output+".jpg");
        assertFileEqual(outputFile, exampleFile);
	}

	private void assertFileEqual(File file1, File file2) throws IOException{
        assertTrue("The files should be equal to each other", Files.equal(file1, file2));
    }

	private void assertFileExists(String loc) {
        // Check if the location is set by checking for it's null value and if the length is at least larger than 0.
        assertNotNull(loc);
        assertNotEquals(loc.length(), 0);

        File file = new File(loc);
        assertNotNull(file);
        assertTrue(file.exists());
    }

    private File getFile(String name) {
        ClassLoader loader = getClass().getClassLoader();
        URL url = loader.getResource(name);
        assertNotNull("The resource URL shouldn't be null", url);
        return new File(url.getFile());
    }
}
