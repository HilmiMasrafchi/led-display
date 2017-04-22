/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.administration.model.view.LedStateView.OFF;
import static me.hmasrafchi.leddisplay.administration.model.view.LedStateView.ON;
import static me.hmasrafchi.leddisplay.administration.model.view.LedStateView.TRANSPARENT;
import static me.hmasrafchi.leddisplay.administration.model.view.LedStateView.UNRECOGNIZED;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.hmasrafchi.leddisplay.administration.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.administration.model.view.LedStateView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView;
import me.hmasrafchi.leddisplay.domain.event.RgbColorView;

/**
 * @author michelin
 *
 */
public final class TestMest {
	@Test
	public void tests() throws JsonProcessingException {
		final List<List<LedStateView>> overlayStationaryStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationaryView overlay = new OverlayStationaryView(overlayStationaryStates,
				new RgbColorView(255, 0, 0), new RgbColorView(0, 255, 0), 10);
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand(5, 6,
				Arrays.asList(Arrays.asList(overlay)));

		ObjectMapper o = new ObjectMapper();
		String writeValueAsString = o.writeValueAsString(createMatrixCommand);
		System.out.println(writeValueAsString);
	}
}