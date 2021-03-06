package org.geogebra.web.full.gui.toolbarpanel;

import java.util.List;

import org.geogebra.common.awt.GPoint;
import org.geogebra.common.gui.view.table.TableValuesDimensions;
import org.geogebra.common.gui.view.table.TableValuesModel;
import org.geogebra.common.gui.view.table.TableValuesView;
import org.geogebra.common.kernel.kernelND.GeoEvaluatable;
import org.geogebra.web.full.css.MaterialDesignResources;
import org.geogebra.web.full.gui.util.MyToggleButtonW;
import org.geogebra.web.html5.gui.util.NoDragImage;
import org.geogebra.web.html5.main.AppW;
import org.geogebra.web.html5.util.CSSEvents;
import org.geogebra.web.html5.util.StickyTable;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Sticky table of values.
 * 
 * @author laszlo
 *
 */
public class StickyValuesTable extends StickyTable<TVRowData> {

	// margin to align value cells to header - 3dot empty place
	private static final int VALUE_RIGHT_MARGIN = 36;
	private static final int TABLE_HEADER_HEIGHT = 40;
	private static final int X_LEFT_PADDING = 16;
	private static final int MIN_COLUMN_WIDTH = 72;
	private static final int STRICT_ROW_HEIGHT = 40;

	/** Template to create a cell */
	static final CellTemplates TEMPLATES = GWT.create(CellTemplates.class);
	private TableValuesModel model;
	private TableValuesDimensions dimensions;
	private NoDragImage moreImg;
	private TableValuesView view;
	private AppW app;
	private TableValuesDataProvider provider;

	/**
	 * Interface to feed table with data and update provider.
	 * 
	 * @author laszlo
	 *
	 */
	interface TableValuesDataProvider {
		/**
		 * @return see {@link TableValuesView}
		 */
		TableValuesView getView();

		/**
		 * Updates provider panel - when table becomes empty for example.
		 */
		void update();
	}

	/**
	 * Class to wrap callback after column delete.
	 *
	 * @author laszlo.
	 *
	 */
	private class ColumnDelete implements Runnable {
		Runnable cb = null;
		private int column = -1;
		private Element elem;

		/**
		 * Constructor.
		 * 
		 * @param column
		 *            the deleted column number.
		 * @param elem
		 *            the corresponding HTML element.
		 * @param cb
		 *            Callback to run after delete transition.
		 */
		ColumnDelete(int column, Element elem, Runnable cb) {
			this.column = column;
			this.elem = elem;
			this.cb = cb;
		}

		@Override
		public void run() {
			onDeleteColumn(column, elem, cb);
		}
	}

	/**
	 * @param app
	 *            {@link AppW}
	 * @param provider
	 *            to feed table with data.
	 */
	public StickyValuesTable(AppW app, TableValuesDataProvider provider) {
		super();
		this.app = app;
		this.provider = provider;
		this.view = provider.getView();
		this.model = view.getTableValuesModel();
		this.dimensions = view.getTableValuesDimensions();
	}

	@Override
	protected void onHeaderClick(Element source, int column) {
		new ContextMenuTV(app, column > 0 ? view.getGeoAt(column - 1) : null, column - 1)
				.show(new GPoint(source.getAbsoluteLeft(), source.getAbsoluteTop() - 8));
	}

	@Override
	protected void addCells() {
		for (int column = 0; column < model.getColumnCount(); column++) {
			addColumn();
		}
	}

	@Override
	protected void addColumn() {
		int column = getValuesTable().getColumnCount();
		Column<TVRowData, ?> colHeader = getColumnName();
		if (colHeader != null) {
			getHeaderTable().addColumn(colHeader, getHeaderHtml(column));
		}
		Column<TVRowData, ?> colValue = getColumnValue(column, dimensions);
		if (colValue != null) {
			getValuesTable().addColumn(colValue);
		}
	}

	@Override
	protected void fillValues(List<TVRowData> rows) {
		rows.clear();
		for (int row = 0; row < model.getRowCount(); row++) {
			rows.add(new TVRowData(row, model));
		}
	}

	private SafeHtml getHeaderHtml(final int col) {
		FlowPanel p = new FlowPanel();
		p.add(new Label(model.getHeaderAt(col)));
		MyToggleButtonW btn = new MyToggleButtonW(getMoreImage());
		p.add(btn);
		SafeHtml content = SafeHtmlUtils.fromTrustedString(p.getElement().getInnerHTML());
		return makeCell(content, getColumnWidth(dimensions, col), TABLE_HEADER_HEIGHT);
	}

	/**
	 * Makes a cell as SafeHtml.
	 * 
	 * @param content
	 *            of the cell.
	 * @param width
	 *            of the cell.
	 * @param height
	 *            of the cell.
	 * @return SafeHtml of the cell.
	 */
	static SafeHtml makeCell(SafeHtml content, int width, int height) {
		SafeStylesBuilder sb = new SafeStylesBuilder();
		sb.width(width, Unit.PX).height(height, Unit.PX).trustedNameAndValue("line-height", height,
				Unit.PX);
		return TEMPLATES.cell(content, sb.toSafeStyles());
	}

	/**
	 * Gives the preferred width of a column.
	 * 
	 * @param dimensions
	 *            The column sizes
	 * @param column
	 *            particular column index.
	 * @return the calculated width of the column.
	 */
	static int getColumnWidth(TableValuesDimensions dimensions, int column) {
		int w = Math.max(dimensions.getColumnWidth(column), dimensions.getHeaderWidth(column))
				+ VALUE_RIGHT_MARGIN;
		if (column == 0) {
			w += X_LEFT_PADDING;
		}
		return Math.max(w, MIN_COLUMN_WIDTH + X_LEFT_PADDING);
	}

	private NoDragImage getMoreImage() {
		if (moreImg == null) {
			moreImg = new NoDragImage(MaterialDesignResources.INSTANCE.more_vert_black(), 24);
		}
		return moreImg;
	}

	private static Column<TVRowData, SafeHtml> getColumnName() {
		Column<TVRowData, SafeHtml> nameColumn = new Column<TVRowData, SafeHtml>(
				new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(TVRowData object) {
				return SafeHtmlUtils.fromTrustedString(object.getHeader());
			}
		};
		return nameColumn;
	}

	private static Column<TVRowData, SafeHtml> getColumnValue(final int col,
			final TableValuesDimensions dimensions) {
		Column<TVRowData, SafeHtml> column = new Column<TVRowData, SafeHtml>(new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(TVRowData object) {
				String valStr = object.getValue(col);
				boolean empty = "".equals(valStr);
				SafeHtml value = SafeHtmlUtils.fromSafeConstant(valStr);
				int width = empty ? 0 : getColumnWidth(dimensions, col);
				int height = empty ? 0 : STRICT_ROW_HEIGHT;
				return makeCell(value, width, height);
			}
		};
		return column;
	}

	/**
	 * @author Balazs
	 *
	 */
	public interface CellTemplates extends SafeHtmlTemplates {
		/**
		 * @param message
		 *            of the cell.
		 * @param style
		 *            of the cell.
		 * @return HTML representation of the cell content.
		 */
		@SafeHtmlTemplates.Template("<div style=\"{1}\""
				+ "class=\"cell\"><div class=\"content\">{0}</div></div>")
		SafeHtml cell(SafeHtml message, SafeStyles style);
	}

	/**
	 * Deletes the specified column from the table
	 *
	 * @param column
	 *            column to delete.
	 * @param cb
	 *            to run on transition end.
	 */
	public void deleteColumn(int column, Runnable cb) {
		int col = column;
		NodeList<Element> elems = getColumnElements(col);
		Element header = getHeaderElement(col);

		if (elems == null || elems.getLength() == 0 || header == null) {
			return;
		}

		int tableWidth = getValuesTable().getOffsetWidth() - header.getOffsetWidth();

		header.addClassName("delete");
		if (cb != null) {
			CSSEvents.runOnTransition(new ColumnDelete(col, header, cb), header, "delete");
		}

		for (int i = 0; i < elems.getLength(); i++) {
			Element e = elems.getItem(i);
			e.addClassName("delete");
		}
		setHeaderWidth(tableWidth);
	}

	/**
	 * Runs on column delete.
	 *
	 * @param column
	 *            the deleted column number.
	 *
	 * @param header
	 *            The table header HTML element
	 *
	 * @param cb
	 *            custom callback to run on column delete.
	 */
	void onDeleteColumn(int column, Element header, Runnable cb) {
		if (isLastColumnDeleted()) {
			if (cb != null) {
				cb.run();
			}
		} else {
			removeColumn(column);
		}
		if (view.isEmpty()) {
			provider.update();
		}
	}

	private boolean isLastColumnDeleted() {
		return view.getTableValuesModel().getColumnCount() == 0;
	}

	/**
	 * Sets height of the values to be able to scroll.
	 * 
	 * @param height
	 *            to set.
	 */
	public void setHeight(int height) {
		setBodyHeight(height);
	}

	/**
	 * 
	 * Scroll table view to the corresponding column of the geo.
	 * 
	 * @param geo
	 *            to scroll.
	 */
	public void scrollTo(GeoEvaluatable geo) {
		if (geo == null) {
			return;
		}

		int pos = 0;
		int col = view.getColumn(geo);
		for (int i = 0; i < col; i++) {
			pos += dimensions.getColumnWidth(col);
		}
		setHorizontalScrollPosition(pos);
	}

	@Override
	protected void doSyncHeaderSizes() {
		for (int col = 0; col < model.getColumnCount(); col++) {
			getHeaderTable().setColumnWidth(col, dimensions.getColumnWidth(col) + "px");
		}
	}
}
