package com.baselet.element.sticking.polygon;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.element.sticking.StickingPolygon;

public class SimpleStickingPolygonGenerator implements StickingPolygonGenerator {

	public static final SimpleStickingPolygonGenerator INSTANCE = new SimpleStickingPolygonGenerator();

	private SimpleStickingPolygonGenerator() {}

	@Override
	public StickingPolygon generateStickingBorder(Rectangle rect) {
		StickingPolygon p = new StickingPolygon(rect.getX(), rect.getY());
		p.addRectangle(0, 0, rect.getWidth(), rect.getHeight());
		return p;
	}
}
