package org.geogebra.common.gui.view.table;

import org.geogebra.common.kernel.ModeSetter;
import org.geogebra.common.kernel.arithmetic.Evaluatable;
import org.geogebra.common.kernel.geos.GProperty;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.main.App;

import java.util.ArrayList;
import java.util.List;

/**
 * The TableValuesView implementation.
 */
public class TableValuesView implements TableValues {

	private float valuesMin = -2.0f;
	private float valuesMax = 2.0f;
	private float valuesStep = 1.0f;

	private TableValuesModel model;
	private List<GeoElement> elements;

	/**
	 * Create a new Table Value View.
	 */
	public TableValuesView() {
		this.elements = new ArrayList<>();
	}

	@Override
	public void showColumn(Evaluatable element) {

	}

	@Override
	public void hideColumn(Evaluatable element) {

	}

	@Override
	public void setValuesMin(float valuesMin) {
		this.valuesMin = valuesMin;
	}

	@Override
	public float getValuesMin() {
		return valuesMin;
	}

	@Override
	public void setValuesMax(float valuesMax) {
		this.valuesMax = valuesMax;
	}

	@Override
	public float getValuesMax() {
		return valuesMax;
	}

	@Override
	public void setValuesStep(float valuesStep) {
		this.valuesStep = valuesStep;
	}

	@Override
	public float getValuesStep() {
		return valuesStep;
	}

	@Override
	public TableValuesModel getTableValuesModel() {
		return model;
	}

	@Override
	public void add(GeoElement geo) {
		elements.add(geo);
	}

	@Override
	public void remove(GeoElement geo) {
		elements.remove(geo);
	}

	@Override
	public void rename(GeoElement geo) {
		// update header
	}

	@Override
	public void update(GeoElement geo) {
		// update column
	}

	@Override
	public void updateVisualStyle(GeoElement geo, GProperty prop) {
	}

	@Override
	public void updateHighlight(GeoElementND geo) {
	}

	@Override
	public void updateAuxiliaryObject(GeoElement geo) {
	}

	@Override
	public void repaintView() {
	}

	@Override
	public boolean suggestRepaint() {
		return false;
	}

	@Override
	public void reset() {
	}

	@Override
	public void clearView() {
	}

	@Override
	public void setMode(int mode, ModeSetter m) {
	}

	@Override
	public int getViewID() {
		return App.VIEW_TABLE_OF_VALUES;
	}

	@Override
	public boolean hasFocus() {
		return false;
	}

	@Override
	public void startBatchUpdate() {
	}

	@Override
	public void endBatchUpdate() {
	}

	@Override
	public void updatePreviewFromInputBar(GeoElement[] geos) {
	}
}