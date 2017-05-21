/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.lang.String.valueOf;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import me.hmasrafchi.leddisplay.administration.infrastructure.MatrixRepository;
import me.hmasrafchi.leddisplay.model.event.MatrixUpdatedEvent;
import me.hmasrafchi.leddisplay.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.model.view.LedView;
import me.hmasrafchi.leddisplay.model.view.MatrixView;

/**
 * @author michelin
 *
 */
@RestController
@Slf4j
public final class MatrixController {
	@Autowired
	private MatrixRepository matrixRepository;

	@Autowired
	private JmsTemplate jmsTemplate;

	@PostMapping("/matrices")
	public ResponseEntity<?> createMatrix(@RequestBody final CreateMatrixCommand createMatrixCommand) {
		final MatrixView matrixView = new MatrixView(createMatrixCommand.getName(), createMatrixCommand.getRowCount(),
				createMatrixCommand.getColumnCount(), createMatrixCommand.getScenes());

		final MatrixView matrixViewCreated = matrixRepository.create(matrixView);
		sendMatrixUpdatedEvent(matrixViewCreated);

		final Integer matrixId = matrixViewCreated.getId();
		final URI createdMatrixLocationURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(valueOf(matrixId)).toUri();

		return ResponseEntity.created(createdMatrixLocationURI).build();
	}

	private void sendMatrixUpdatedEvent(final MatrixView matrixView) {
		log.info("sending: " + matrixView.toString());
		final List<List<List<LedView>>> compiledFrames = matrixView.getCompiledFrames();

		if (compiledFrames != null && !compiledFrames.isEmpty()) {
			final Integer id = matrixView.getId();
			final int rowCount = matrixView.getRowCount();
			final int columnCount = matrixView.getColumnCount();
			final MatrixUpdatedEvent matrixUpdatedEvent = new MatrixUpdatedEvent(BigInteger.valueOf(id), rowCount,
					columnCount, compiledFrames);
			jmsTemplate.convertAndSend("domainEvents", matrixUpdatedEvent);
		}
	}

	@GetMapping("/matrices/{matrixId}")
	public ResponseEntity<MatrixView> get(@PathVariable final int matrixId) {
		return matrixRepository.find(matrixId).map(matrix -> ResponseEntity.ok(matrix))
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/matrices")
	public ResponseEntity<List<MatrixView>> getAll() {
		final List<MatrixView> allMatrices = matrixRepository.findAll();
		return ResponseEntity.ok(allMatrices);
	}

	@PutMapping("/matrices")
	public ResponseEntity<?> updateMatrix(@RequestBody final MatrixView matrix) {
		final MatrixView matrixUpdated = matrixRepository.update(matrix);
		sendMatrixUpdatedEvent(matrixUpdated);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/matrices/{matrixId}")
	public ResponseEntity<?> deleteMatrix(@PathVariable final int matrixId) {
		if (matrixRepository.delete(matrixId)) {
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.notFound().build();
	}
}