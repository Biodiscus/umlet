package com.baselet.element.sequence_aio.facet;

/**
 * Interface for the drawing information of a whole diagram.
 */
public interface HorizontalDrawingInfo {

	LifelineHorizontalDrawingInfo getHDrawingInfo(Lifeline lifeline);

	/**
	 * Returns the distance between the starting point of the lifeline which starts first and the end point of the
	 * other.
	 * I.e. with no loss of generality {@code ll1.getIndex() <= ll2.getIndex()} then
	 * <pre>
	 * {@code
	 * getHDrawingInfo(ll1).getSymmetricHorizontalEnd(tick)
	 * -getHDrawingInfo(ll2).getSymmetricHorizontalStart(tick)
	 * }
	 * </pre>
	 * is returned.
	 * @param ll1
	 * @param ll2
	 * @param tick
	 * @return the symmetric width which is spanned by the two lifelines at the given tick
	 */
	double getSymmetricWidth(Lifeline ll1, Lifeline ll2, int tick);

	double getHorizontalStart(Container container);

	double getHorizontalEnd(Container container);

	double getWidth(Container container);

	double getDiagramHorizontalStart();

	double getDiagramHorizontalEnd();

	double getDiagramWidth();
}
