/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.domain.Matrix;

/**
 * @author michelin
 *
 */
public final class TestBeanMapper {
	@Test
	public void test() {
		final MatrixView matrixView = new MatrixView(5, 6);
		final BeanMapperJpa beanMapper = new BeanMapperJpa();
		final Matrix actualMatrix = beanMapper.mapMatrixFromViewToDomainModel(matrixView);

		final Matrix expectedMatrix = new Matrix(5, 6, Optional.empty());

		Assert.assertThat(actualMatrix, equalTo(expectedMatrix));
	}
}