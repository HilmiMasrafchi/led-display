/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.lang.String.valueOf;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import me.hmasrafchi.leddisplay.administration.infrastructure.MatrixRepository;
import me.hmasrafchi.leddisplay.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.model.view.MatrixView;

/**
 * @author michelin
 *
 */
@RestController("matrices")
public final class MatricesController {
	@Autowired
	private MatrixRepository matrixRepository;

	@PostMapping
	public ResponseEntity<?> createMatrix(final CreateMatrixCommand createMatrixCommand) {
		final MatrixView matrixView = new MatrixView(createMatrixCommand.getName(), createMatrixCommand.getRowCount(),
				createMatrixCommand.getColumnCount(), createMatrixCommand.getScenes());

		final MatrixView matrixViewCreated = matrixRepository.create(matrixView);
		// sendMatrixUpdatedEvent(matrixViewCreated);

		final Integer matrixId = matrixViewCreated.getId();
		final URI createdMatrixLocationURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(valueOf(matrixId)).toUri();

		return ResponseEntity.created(createdMatrixLocationURI).build();
	}
}