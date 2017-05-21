/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.infrastructure.jpa;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.hmasrafchi.leddisplay.consumer.data.jpa.FrameJpa;
import me.hmasrafchi.leddisplay.consumer.data.jpa.LedJpa;
import me.hmasrafchi.leddisplay.consumer.data.jpa.LedRowJpa;
import me.hmasrafchi.leddisplay.consumer.data.jpa.MatrixJpa;
import me.hmasrafchi.leddisplay.consumer.data.jpa.RgbColorJpa;
import me.hmasrafchi.leddisplay.consumer.infrastructure.MatrixRepository;
import me.hmasrafchi.leddisplay.model.event.MatrixUpdatedEvent;
import me.hmasrafchi.leddisplay.model.view.LedView;
import me.hmasrafchi.leddisplay.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
@Repository
public class MatrixRepositoryJpa implements MatrixRepository {
	@Autowired
	private MatrixRepositorySpringJpa matrixRepositorySpring;

	@Override
	public MatrixUpdatedEvent save(final MatrixUpdatedEvent matrixEvent) {
		final MatrixJpa matrixEntity = mapEventToDataModel(matrixEvent);
		final MatrixJpa matrixEntitySaved = matrixRepositorySpring.save(matrixEntity);

		return mapFromDataModelToEventModel(matrixEntitySaved);
	}

	private MatrixJpa mapEventToDataModel(final MatrixUpdatedEvent matrixUpdatedEvent) {
		final Integer matrixId = matrixUpdatedEvent.getMatrixId();
		final int rowCount = matrixUpdatedEvent.getRowCount();
		final int columnCount = matrixUpdatedEvent.getColumnCount();
		final List<FrameJpa> mapCompiledFramesFromEventToJpaModel = mapCompiledFramesFromEventToDataModel(
				matrixUpdatedEvent.getCompiledFrames());

		return new MatrixJpa(matrixId, rowCount, columnCount, mapCompiledFramesFromEventToJpaModel);
	}

	private List<FrameJpa> mapCompiledFramesFromEventToDataModel(final List<List<List<LedView>>> compiledFrames) {
		return compiledFrames.stream().map(frame -> {
			final List<LedRowJpa> frameJpaList = frame.stream().map(ledRow -> {
				final List<LedJpa> ledRowJpaList = ledRow.stream().map(led -> {
					final String text = led.getText();
					final RgbColorJpa rgbColor = mapRgbColorFromEventToJpa(led.getRgbColor());

					return new LedJpa(text, rgbColor);
				}).collect(Collectors.toList());
				return new LedRowJpa(ledRowJpaList);
			}).collect(Collectors.toList());
			return new FrameJpa(frameJpaList);
		}).collect(Collectors.toList());
	}

	private RgbColorJpa mapRgbColorFromEventToJpa(RgbColorView rgbColor) {
		return new RgbColorJpa(rgbColor.getR(), rgbColor.getG(), rgbColor.getB());
	}

	@Override
	public MatrixUpdatedEvent findByMatrixId(final Integer matrixId) {
		final MatrixJpa matrixEntity = matrixRepositorySpring.findOne(matrixId);
		return mapFromDataModelToEventModel(matrixEntity);
	}

	private MatrixUpdatedEvent mapFromDataModelToEventModel(final MatrixJpa matrixJpa) {
		if (matrixJpa == null) {
			return null;
		}

		final Integer matrixId = matrixJpa.getMatrixId();
		final int rowCount = matrixJpa.getRowCount();
		final int columnCount = matrixJpa.getColumnCount();
		final List<List<List<LedView>>> collect = matrixJpa.getCompiledFrames().stream().map(frame -> {
			return frame.getLedRows().stream().map(ledRow -> {
				return ledRow.getLeds().stream().map(led -> {
					final RgbColorView rgbColor = new RgbColorView(led.getRgbColor().getR(), led.getRgbColor().getG(),
							led.getRgbColor().getB());
					return new LedView(led.getText(), rgbColor);
				}).collect(Collectors.toList());
			}).collect(Collectors.toList());
		}).collect(Collectors.toList());

		return new MatrixUpdatedEvent(matrixId, rowCount, columnCount, collect);
	}
}