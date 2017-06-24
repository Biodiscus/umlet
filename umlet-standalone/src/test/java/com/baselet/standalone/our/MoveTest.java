package com.baselet.standalone.our;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URL;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.handler.ConfigHandler;
import com.baselet.control.enums.Direction;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.elementnew.uml.Class;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.relation.Relation;

/**
 *
 * 4. Move elements
 *
 * © 2017, Gopper
 */
public class MoveTest {
	private File file;
	private DrawPanel drawPanel;
	private List<GridElement> elements;

	private Collection<Direction> direction;

	@Before
	public void init() {
		String name = "example.uxf";
		file = getFile(name);

		DiagramHandler diagramHandler = new DiagramHandler(file);
		drawPanel = diagramHandler.getDrawPanel();
		elements = diagramHandler.getDrawPanel().getGridElements();

		Utils.BuildInfo buildInfo = Utils.readBuildInfo();
		Program.init(buildInfo.version, RuntimeType.BATCH);
		ConfigHandler.loadConfig();
	}


	@Test
	public void movePanel() {
		int startX = drawPanel.getX();
		int startY = drawPanel.getX();

		drawPanel.setLocation(100, 100);

		int endX = drawPanel.getX();
		int endY = drawPanel.getY();

		assertNotEquals(startX, endX);
		assertNotEquals(startY, endY);
	}

	@Test
	public void dragClasses() {
		direction = mock(Collection.class);
		when(direction.isEmpty()).thenReturn(true);

		Iterator<GridElement> iterator = elements.iterator();
		while(iterator.hasNext()) {
			GridElement element = iterator.next();
			Rectangle oldRectangle = element.getRectangle();
			Point point = new Point(oldRectangle.getX(), oldRectangle.getY());

			// Elements behave a bit differently
			if (element instanceof Class){
				element.drag(direction, 200, 200, point, false, false, null, false);
				Rectangle newRectangle = element.getRectangle();
				assertNotEquals(oldRectangle.hashCode(), newRectangle.hashCode());
			}
		}
	}

	@Test
	public void dragElements() {
		direction = mock(Collection.class);
		when(direction.isEmpty()).thenReturn(true);

		Iterator<GridElement> iterator = elements.iterator();
		while(iterator.hasNext()) {
			GridElement element = iterator.next();
			Rectangle oldRectangle = element.getRectangle();

			// Elements behave a bit differently
			if(element instanceof Relation) {
				Relation relation = (Relation) element;
				Rectangle dragBox = relation.getRelationPoints().getDragBox();
				Point dragPoint = new Point(dragBox.getX() + 2, dragBox.getY() + 2);
				element.drag(direction, 100, 100, dragPoint, false, true, null, false);
				Rectangle newRectangle = relation.getRectangle();
				assertNotEquals(oldRectangle.hashCode(), newRectangle.hashCode());
			}
		}
	}

	@Test
	public void moveElementBack() {
		Iterator<GridElement> iterator = elements.iterator();
		while(iterator.hasNext()) {
			GridElement element = iterator.next();
			Rectangle oldRectangle = element.getRectangle();
			element.setLocation(oldRectangle.getX() + 100, oldRectangle.getY() + 100);
			element.setLocation(oldRectangle.getX(), oldRectangle.getY());
			Rectangle newRectangle = element.getRectangle();
			assertEquals(oldRectangle.hashCode(), newRectangle.hashCode());
		}
	}

	@Test
	public void moveElement() {
		Iterator<GridElement> iterator = elements.iterator();

		while(iterator.hasNext()) {
			GridElement element = iterator.next();
			Rectangle oldRectangle = element.getRectangle();
			element.setLocation(oldRectangle.getX() + 100, oldRectangle.getY() + 100);
			Rectangle newRectangle = element.getRectangle();

			assertNotEquals(oldRectangle.hashCode(), newRectangle.hashCode());

		}
	}

	private File getFile(String name) {
		ClassLoader loader = getClass().getClassLoader();
		URL url = loader.getResource(name);
		assertNotNull("The resource URL shouldn't be null", url);
		return new File(url.getFile());
	}


}
