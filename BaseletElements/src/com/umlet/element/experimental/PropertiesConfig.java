package com.umlet.element.experimental;

import org.apache.log4j.Logger;

import DefaultGlobalFacet.DefaultGlobalTextFacet.ElementStyleEnum;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet.GlobalSetting;

public class PropertiesConfig {
	
	private static final Logger log = Logger.getLogger(PropertiesConfig.class);

	private AlignHorizontal hAlign;
	private boolean hAlignGloballySet = false;
	private AlignVertical vAlign;
	private boolean vAlignGloballySet = false;
	private Double yPos = null;
	private double elementWidthForAutoresize;
	private int leftBuffer = 0;
	private int rightBuffer = 0;
	private Settings settings;
	private Dimension gridElementSize;
	private ElementStyleEnum elementStyle;
	private Integer layer = Integer.valueOf(GlobalSetting.LAYER.getValue());

	public PropertiesConfig(Settings settings) {
		this.hAlign = settings.getHAlign();
		this.vAlign = settings.getVAlign();
		this.elementStyle = settings.getElementStyle();
		this.elementWidthForAutoresize = settings.getMinElementWidthForAutoresize();
		this.settings = settings;
	}

	public PropertiesConfig(Settings settings, Dimension realSize) {
		this(settings);
		setGridElementSize(realSize);
	}

	public AlignHorizontal gethAlign() {
		return hAlign;
	}

	public void sethAlign(AlignHorizontal hAlign) {
		if (!hAlignGloballySet) this.hAlign = hAlign;
	}

	public void sethAlignGlobally(AlignHorizontal hAlign) {
		hAlignGloballySet = true;
		this.hAlign = hAlign;
	}

	public void setvAlignGlobally(AlignVertical vAlign) {
		vAlignGloballySet = true;
		this.vAlign = vAlign;
	}

	public void resetAlign() {
		if (!hAlignGloballySet) this.hAlign = settings.getHAlign();
		if (!vAlignGloballySet) this.vAlign = settings.getVAlign();
	}

	public AlignVertical getvAlign() {
		return vAlign;
	}

	public void setvAlign(AlignVertical vAlign) {
		if (!vAlignGloballySet) this.vAlign = vAlign;
	}

	public double getyPos() {
		if (yPos == null) {
			yPos = settings.getYPosStart();
		}
		return yPos;
	}

	public void addToYPos(double inc) {
		if (yPos == null) { // get yPos from settings the first time it would be modified, because initialization in constructor would be too early (eg: it could depend on some settings of preparsefacets like fontsize)
			yPos = settings.getYPosStart();
		}
		yPos += inc;
	}

//	private int maxLeftBuffer = 0;
//	private int maxRightBuffer = 0;

	public void addToLeftBuffer(int inc) {
		this.leftBuffer += inc;
//		if (leftBuffer > maxLeftBuffer) maxLeftBuffer = leftBuffer;
	}

	public void addToRightBuffer(int inc) {
		this.rightBuffer += inc;
//		if (rightBuffer > maxRightBuffer) maxRightBuffer = rightBuffer;
	}

//	public int getMaxLeftBuffer() {
//		return maxLeftBuffer;
//	}
//
//	public int getMaxRightBuffer() {
//		return maxRightBuffer;
//	}

	public void addToBuffer(int inc) {
		addToLeftBuffer(inc);
		addToRightBuffer(inc);
	}

	public Dimension getGridElementSize() {
		return gridElementSize;
	}

	public XValues getXLimits(double linePos) {
		XValues xLimits = settings.getXValues(linePos, gridElementSize.height, gridElementSize.width);
		xLimits.addLeft(leftBuffer);
		xLimits.subRight(rightBuffer);
		return xLimits;
	}

	public XValues getXLimitsForArea(double bottomYPos, double areaHeight) {
		XValues xLimitsTop = getXLimits(bottomYPos);
		XValues xLimitsBottom = getXLimits(bottomYPos - areaHeight);
		return xLimitsTop.intersect(xLimitsBottom);
	}

	public double getDividerPos(double f) {
		return getyPos() - f + 2;
	}

	public void updateElementWidthForAutoresize(double width) {
		elementWidthForAutoresize = Math.max(elementWidthForAutoresize, width);
	}

	public double getElementWidthForAutoresize() {
		return elementWidthForAutoresize;
	}

	public void setGridElementSize(Dimension gridElementSize) {
		this.gridElementSize = gridElementSize;
	}

	public ElementStyleEnum getElementStyle() {
		return elementStyle;
	}

	public void setElementStyle(ElementStyleEnum elementStyle) {
		this.elementStyle = elementStyle;
	}

	public void setLayer(String layer) {
		try {
			this.layer = Integer.valueOf(layer);
		} catch (NumberFormatException e) {
			log.info("Invalid value: " + layer + " - " + GlobalSetting.LAYER + " must be an Integer");
		}
	}

	public Integer getLayer() {
		return layer;
	}
	
	public Settings getSettings() {
		return settings;
	}

}
