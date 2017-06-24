package com.baselet.standalone.our;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.baselet.control.Main;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.handler.ConfigHandler;
import com.baselet.control.enums.ElementId;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Path;
import com.baselet.control.util.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.NewGridElement;
import com.baselet.element.elementnew.uml.Class;
import com.baselet.element.interfaces.Diagram;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gui.BaseGUI;
import com.baselet.standalone.MainStandalone;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 3. Build a class diagram via plain text
 *
 * © 2017, Gopper
 */
public class BuildTest {
	private File buildFile;
	private File emptyFile;


	@Before
	public void init() {
		buildFile = getFile("build.uxf");
		emptyFile = getFile("emptyWorkspace.uxf");

		assertNotNull("The example UXF file shouldn't be null", buildFile);

		Utils.BuildInfo buildInfo = Utils.readBuildInfo();
		Program.init(buildInfo.version, RuntimeType.BATCH);
		ConfigHandler.loadConfig();
	}

	@Test(timeout = 10000)
	public void build() {
		DiagramHandler diagram = new DiagramHandler(null);
		Rectangle bounds = new Rectangle(0, 0, 100, 100);
		String attributes = "TestClass\n";
		attributes += "--\n--\n";
		attributes += "- test () : void";
		NewGridElement element = ElementFactorySwing.create(ElementId.UMLClass, bounds, attributes, "", diagram);
		diagram.getDrawPanel().addElement(element);

		List<GridElement> newElements = diagram.getDrawPanel().getGridElements();
		diagram = new DiagramHandler(buildFile);
		List<GridElement> buildElements = diagram.getDrawPanel().getGridElements();

		for(int i = 0; i < newElements.size(); i ++) {
			Class newElement = (Class) newElements.get(i);
			Class buildElement = (Class) buildElements.get(i);

			assertEquals(newElement.getPanelAttributes(), buildElement.getPanelAttributes());
		}
	}

	@Test(timeout = 10000)
	public void buildEmpty() {
		DiagramHandler diagram = new DiagramHandler(null);

		List<GridElement> newElements = diagram.getDrawPanel().getGridElements();
		diagram = new DiagramHandler(emptyFile);
		List<GridElement> buildElements = diagram.getDrawPanel().getGridElements();
		assertEquals("The new elements array shouldn't contain an element", newElements.size(), 0);
		assertEquals("The build elements array shouldn't contain an element", buildElements.size(), 0);
	}

	private File getFile(String name) {
		ClassLoader loader = getClass().getClassLoader();
		URL url = loader.getResource(name);
		assertNotNull("The resource URL shouldn't be null", url);
		return new File(url.getFile());
	}
}
