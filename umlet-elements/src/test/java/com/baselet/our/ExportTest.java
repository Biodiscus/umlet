package com.baselet.our;

import com.baselet.standalone.MainStandalone;
import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.awt.image.FileImageSource;
import sun.awt.image.ImageFormatException;
import sun.awt.image.JPEGImageDecoder;
import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * 1. Export diagram
 *
 * © 2017, Gopper
 */
public class ExportTest {
    private File file;
    private File outputFile;
    private File exampleFile1;

	@Before
	public void init() {
	    String name = "example.uxf";
	    String exampleName1 = "example1.jpg";
	    String outputName = "example2.jpg";

	    file = getFile(name);
	    assertNotNull("The example UXF file shouldn't be null", file);

	    exampleFile1 = getFile(exampleName1);
        assertNotNull("The example1 JPG file shouldn't be null", exampleName1);

	    outputFile = new File(
            file.getAbsolutePath().replace(name, outputName)
        );
	    assertNotNull("The output JPG file shouldn't be null", outputFile);
    }

    @After
    public void cleanUp() {
//        assertTrue("The generated output file should be deleted", outputFile.delete());
    }


	@Test(timeout = 1000)
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
		assertImageEquals(outputFile, exampleFile1);

        assertNotCorrupt(outputFile);
	}

	private void assertNotCorrupt(File img) throws IOException, ImageFormatException {
        JPEGImageDecoder jpgDecoder = new JPEGImageDecoder(
                new FileImageSource(img.getAbsolutePath()), new FileInputStream(img)
        );
        jpgDecoder.produceImage();
    }

	private void assertImageEquals(File image1, File image2) throws IOException {
		imageEquals(image1, image2, 0.75F);
	}

    private void imageEquals(File image1, File image2, float minimumPercentage) throws IOException {
		BufferedImage img1 = ImageIO.read(image1);
		BufferedImage img2 = ImageIO.read(image2);

		assertEquals("Heights should be the same", img1.getHeight(), img2.getHeight());
		assertEquals("Widths should be the same", img1.getWidth(), img2.getWidth());

		int len = img1.getWidth() * img1.getHeight();
		int correctMatches = 0;
		for (int i = 0; i < len; i++) {
			int x = i % img1.getWidth();
			int y = (int) Math.floor(i / img1.getWidth());

			if(img1.getRGB(x, y) == img2.getRGB(x, y)) {
				correctMatches ++;
			}
		}

		float percentage = (float)correctMatches / len;
		assertTrue("The correct pixels should equal: "+minimumPercentage+", but was: "+percentage, percentage >= minimumPercentage);
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
