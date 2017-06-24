package com.baselet.element.sequence_aio.facet;

/**
 * Interface for the drawing information of a whole diagram.
 */
public interface DrawingInfo extends HorizontalDrawingInfo, VerticalDrawingInfo {

	LifelineDrawingInfo getDrawingInfo(Lifeline lifeline);

}
