package com.baselet.our;

import com.baselet.standalone.MainStandalone;
import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import sun.awt.image.FileImageSource;
import sun.awt.image.ImageFormatException;
import sun.awt.image.JPEGImageDecoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

/**
 * © 2017, Gopper
 */
public class ExportTest {
    private File file;
    private File outputFile;
    private File exampleFile1;
    private File exampleFile2;

	@Before
	public void init() {
	    String name = "example.uxf";
	    String exampleName1 = "example1.jpg";
	    String exampleName2 = "example2.jpg";
	    String outputName = "test.jpg";

	    file = getFile(name);
	    assertNotNull("The example UXF file shouldn't be null", file);

	    exampleFile1 = getFile(exampleName1);
        assertNotNull("The example JPG file shouldn't be null", exampleName1);

		exampleFile2 = getFile(exampleName2);
		assertNotNull("The example JPG file shouldn't be null", exampleName2);

	    outputFile = new File(
            file.getAbsolutePath().replace(name, outputName)
        );
	    assertNotNull("The output JPG file shouldn't be null", outputFile);
    }

    @After
    public void cleanUp() {
//        assertTrue("The generated output file should be deleted", outputFile.delete());
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
        assertFileEqual(outputFile, exampleFile1, exampleFile2);

        assertNotCorrupt(outputFile);
	}

	private void assertNotCorrupt(File img) throws IOException, ImageFormatException {
        JPEGImageDecoder jpgDecoder = new JPEGImageDecoder(
                new FileImageSource(img.getAbsolutePath()), new FileInputStream(img)
        );
        jpgDecoder.produceImage();
    }

	private void assertFileEqual(File file1, File example1, File example2) throws IOException{
		boolean first = Files.equal(file1, example1);
		boolean second = Files.equal(file1, example2);
		assertTrue("The files should be equal to each other", first || second);
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