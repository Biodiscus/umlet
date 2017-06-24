package com.baselet.element.relation.helper;

import com.baselet.control.basics.geom.Rectangle;

public interface ResizableObject {
	void setPointMinSize(int index, Rectangle rectFromCenter);

	void resetPointMinSize(int index);
}
