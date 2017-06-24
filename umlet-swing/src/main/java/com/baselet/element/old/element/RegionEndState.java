package com.baselet.element.old.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.Direction;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class RegionEndState extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);

		g2.drawOval(0, 0, getRectangle().getWidth() - 1, getRectangle().getHeight() - 1);
		AffineTransform at = g2.getTransform();
		AffineTransform at2 = (AffineTransform) at.clone();
		at2.rotate(Math.toRadians(45), getRectangle().getWidth() / 2.0, getRectangle().getHeight() / 2.0);
		g2.setTransform(at2);
		g2.drawLine(0, getRectangle().getHeight() / 2, getRectangle().getWidth(), getRectangle().getHeight() / 2);
		g2.drawLine(getRectangle().getWidth() / 2, 0, getRectangle().getWidth() / 2, getRectangle().getHeight());
		g2.setTransform(at);

	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>(); // deny size changes
	}
}
