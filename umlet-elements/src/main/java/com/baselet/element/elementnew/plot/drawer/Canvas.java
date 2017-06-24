package com.baselet.element.elementnew.plot.drawer;

import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;

/**
 * <pre>
 * | GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
 * | G                                G
 * | G  OOOOOOOOOOOOOOO               G
 * | G  O             O               G
 * | G  O  IIIIIIIII  O               G
 * | G  O  I       I  O               G
 * | G  O  I       I  O               G
 * | G  O  IIIIIIIII  O               G
 * | G  O             O               G
 * | G  OOOOOOOOOOOOOOO               G
 * | G                                G
 * | G                                G
 * | G                                G
 * | G                                G
 * | G                                G
 * | G                                G
 * | GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
 * </pre>
 */
public class Canvas {

	private int borderspace;
	private final Rectangle outerBorder; // Nothing is drawn between outerBorder and the GridElement-Border
	private final Rectangle innerBorder; // Only axis are drawn between outerBorder and innerBorder; inside the innerBorder the plot is drawn

	private final Dimension gridElementSize;

	public Canvas(Dimension gridElementSize) {
		super();
		this.gridElementSize = gridElementSize;
		outerBorder = new Rectangle();
		innerBorder = new Rectangle();
		setBorder(0, 0, 0, 0, 0);
	}

	public void setBorder(int x, int y, int width, int height, int borderspace) {
		this.borderspace = borderspace;
		outerBorder.setBounds(x, y, width, height);
		updateInnerBorder();
	}

	public void setBorderX(int x) {
		outerBorder.setBounds(x, outerBorder.getY(), outerBorder.getWidth(), outerBorder.getHeight());
		updateInnerBorder();
	}

	public void setBorderY(int y) {
		outerBorder.setBounds(outerBorder.getX(), y, outerBorder.getWidth(), outerBorder.getHeight());
		updateInnerBorder();
	}

	public void setBorderWidth(int width) {
		outerBorder.setBounds(outerBorder.getX(), outerBorder.getY(), width, outerBorder.getHeight());
		updateInnerBorder();
	}

	public void setBorderHeight(int height) {
		outerBorder.setBounds(outerBorder.getX(), outerBorder.getY(), outerBorder.getWidth(), height);
		updateInnerBorder();
	}

	private void updateInnerBorder() {
		innerBorder.setBounds(outerBorder.getX() + borderspace, outerBorder.getY() + borderspace, outerBorder.getWidth() + borderspace, outerBorder.getHeight() + borderspace);
	}

	/**
	 * <pre>
	 * {@literal
	 * | <----->
	 * | G  O  I       I  O               G
	 * }
	 * </pre>
	 */
	public int getInnerLeftPos() {
		return innerBorder.getX();
	}

	public int getInnerUpPos() {
		return innerBorder.getY();
	}

	/**
	 * <pre>
	 * {@literal
	 * |               <------------------>
	 * | G  O  I       I  O               G
	 * }
	 * </pre>
	 */
	public int getInnerRightBorderWidth() {
		return innerBorder.getWidth();
	}

	public int getInnerDownBorderHeight() {
		return innerBorder.getHeight();
	}

	/**
	 * <pre>
	 * {@literal
	 * | <------------->
	 * | G  O  I       I  O               G
	 * }
	 * </pre>
	 */
	public int getInnerRightPos() {
		return gridElementSize.width - getInnerRightBorderWidth();
	}

	public int getInnerDownPos() {
		return gridElementSize.height - getInnerDownBorderHeight();
	}

	/**
	 * <pre>
	 * {@literal
	 * | <----->       <------------------>
	 * | G  O  I       I  O               G
	 * }
	 * </pre>
	 */
	public int getInnerHorizontalSum() {
		return getInnerLeftPos() + getInnerRightBorderWidth();
	}

	public int getInnerVerticalSum() {
		return getInnerUpPos() + getInnerDownBorderHeight();
	}

	/**
	 * <pre>
	 * {@literal
	 * |       <------->
	 * | G  O  I       I  O               G
	 * }
	 * </pre>
	 */
	public int getInnerHorizontalDrawspace() {
		return getInnerRightPos() - getInnerLeftPos();
	}

	public int getInnerVerticalDrawspace() {
		return getInnerDownPos() - getInnerUpPos();
	}

	public int getOuterLeftPos() {
		return outerBorder.getX();
	}

	public int getOuterUpPos() {
		return outerBorder.getY();
	}

	public int getOuterRightBorderWidth() {
		return outerBorder.getWidth();
	}

	public int getOuterDownBorderHeight() {
		return outerBorder.getHeight();
	}

	public int getOuterRightPos() {
		return gridElementSize.width - getOuterRightBorderWidth();
	}

	public int getOuterDownPos() {
		return gridElementSize.height - getOuterDownBorderHeight();
	}

	public int getOuterHorizontalSum() {
		return getOuterLeftPos() + getOuterRightBorderWidth();
	}

	public int getOuterVerticalSum() {
		return getOuterUpPos() + getOuterDownBorderHeight();
	}

	public boolean hasHorizontalDrawspace() {
		return gridElementSize.width > getOuterHorizontalSum();
	}

	public boolean hasVerticalDrawspace() {
		return gridElementSize.width > getOuterHorizontalSum();
	}

	public void draw(DrawHandler baseDrawHandler) {
		baseDrawHandler.setBackgroundColor(ColorOwn.TRANSPARENT);
		baseDrawHandler.setForegroundColor(ColorOwn.RED.transparency(Transparency.BACKGROUND));
		baseDrawHandler.drawRectangle(getOuterLeftPos(), getOuterUpPos(), getOuterRightPos() - getOuterLeftPos() - 1, getOuterDownPos() - getOuterUpPos());
		baseDrawHandler.setForegroundColor(ColorOwn.BLUE);
		baseDrawHandler.drawRectangle(getInnerLeftPos(), getInnerUpPos(), getInnerRightPos() - getInnerLeftPos(), getInnerDownPos() - getInnerUpPos());
	}

}
