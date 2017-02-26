/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist.inmem;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import me.hmasrafchi.leddisplay.rest.persist.MatrixEntity;
import me.hmasrafchi.leddisplay.rest.persist.MatrixRepository;

/**
 * @author michelin
 *
 */
public final class MatrixRepositoryInMem implements MatrixRepository {
	private static int ID_COUNTER = 0;

	private final static List<MatrixEntity> MATRICES = new CopyOnWriteArrayList<>();

	@Override
	public void create(final MatrixEntity matrix) {
		matrix.setId(ID_COUNTER++);
		MATRICES.add(matrix);
		System.out.println(MATRICES);
	}

	@Override
	public Optional<MatrixEntity> get(final Integer id) {
		return MATRICES.stream().filter(matrix -> id.equals(matrix.getId())).findFirst();
	}

	@Override
	public void update(final MatrixEntity matrixEntity) {
		MATRICES.set(MATRICES.indexOf(matrixEntity), matrixEntity);
	}
}