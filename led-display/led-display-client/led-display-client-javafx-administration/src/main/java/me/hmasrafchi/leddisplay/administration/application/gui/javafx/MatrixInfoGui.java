/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static java.lang.Integer.valueOf;

import java.math.BigInteger;

import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.model.view.MatrixView;

/**
 * @author michelin
 *
 */
final class MatrixInfoGui extends VBox {
	private TextFieldWithLabel labelId;
	private TextFieldWithLabel labelName;
	private TextFieldWithLabel rowCountLabel;
	private TextFieldWithLabel columnCountLabel;

	MatrixInfoGui(final MatrixView matrix) {
		final BigInteger id = matrix.getId();
		this.labelId = new TextFieldWithLabel("matrix id: ", String.valueOf(id), true);

		final String name = matrix.getName();
		this.labelName = new TextFieldWithLabel("name: ", name);

		final int rowCount = matrix.getRowCount();
		this.rowCountLabel = new TextFieldWithLabel("row count: ", String.valueOf(rowCount));

		final int columnCount = matrix.getColumnCount();
		this.columnCountLabel = new TextFieldWithLabel("column count: ", String.valueOf(columnCount));

		getChildren().addAll(this.labelId, this.labelName, this.rowCountLabel, this.columnCountLabel);
	}

	public BigInteger getMatrixId() {
		return new BigInteger(labelId.getText());
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