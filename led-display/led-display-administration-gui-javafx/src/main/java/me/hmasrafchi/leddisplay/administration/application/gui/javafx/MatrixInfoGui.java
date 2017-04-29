/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static java.lang.Integer.valueOf;

import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.administration.application.gui.javafx.component.TextFieldWithLabel;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;

/**
 * @author michelin
 *
 */
public final class MatrixInfoGui extends VBox {
	private TextFieldWithLabel labelId;
	private TextFieldWithLabel labelName;
	private TextFieldWithLabel rowCountLabel;
	private TextFieldWithLabel columnCountLabel;

	public MatrixInfoGui(final MatrixView matrix) {
		final Integer id = matrix.getId();
		this.labelId = new TextFieldWithLabel("matrix id: ", String.valueOf(id), true);

		final String name = matrix.getName();
		this.labelName = new TextFieldWithLabel("name: ", name);

		final int rowCount = matrix.getRowCount();
		this.rowCountLabel = new TextFieldWithLabel("row count: ", String.valueOf(rowCount));

		final int columnCount = matrix.getColumnCount();
		this.columnCountLabel = new TextFieldWithLabel("column count: ", String.valueOf(columnCount));

		getChildren().addAll(this.labelId, this.labelName, this.rowCountLabel, this.columnCountLabel);
	}

	public Integer getMatrixId() {
		return Integer.valueOf(labelId.getText());
	}

	public String getMatrixName() {
		return labelName.getText();
	}

	public Integer getMatrixRowCount() {
		return valueOf(rowCountLabel.getText());
	}

	public Integer getMatrixColumnCount() {
		return valueOf(columnCountLabel.getText());
	}
}