package com.baselet.element.sticking.polygon;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.element.sticking.StickingPolygon;

public interface StickingPolygonGenerator {
	StickingPolygon generateStickingBorder(Rectangle rect);
}
