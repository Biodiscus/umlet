package com.baselet.element.interfaces;

public interface GridElementDeprecatedAddons {

	void doBeforeExport();

	GridElementDeprecatedAddons NONE = new GridElementDeprecatedAddons() {
		@Override
		public void doBeforeExport() {}
	};
}
