/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.administration.model.view.LedStateView.OFF;
import static me.hmasrafchi.leddisplay.administration.model.view.LedStateView.ON;
import static me.hmasrafchi.leddisplay.administration.model.view.LedStateView.TRANSPARENT;

import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.hmasrafchi.leddisplay.administration.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.administration.model.view.LedStateView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView;
import me.hmasrafchi.leddisplay.administration.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
public final class TestMest {
	@Test
	public void tests() throws JsonProcessingException {
		final List<List<LedStateView>> overlayRollHorizontallyStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON));
		final OverlayRollHorizontallyView overlayRollHorizontallyToPost = new OverlayRollHorizontallyView(
				overlayRollHorizontallyStates, RgbColorView.GREEN, RgbColorView.BLUE, 5, 1);

		final List<List<LedStateView>> overlayStationaryStates = asList( //
				asList(ON, ON, ON, ON, ON), //
				asList(TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(OFF, TRANSPARENT, TRANSPARENT, TRANSPARENT, OFF), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON));
		final OverlayStationaryView overlayStationaryToPost = new OverlayStationaryView(overlayStationaryStates,
				RgbColorView.RED, RgbColorView.YELLOW, 1);

		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand("PostmanMatrix", 6, 5, asList(
				asList(overlayRollHorizontallyToPost, overlayStationaryToPost), asList(overlayRollHorizontallyToPost)));
		ObjectMapper o = new ObjectMapper();
		String writeValueAsString = o.writeValueAsString(createMatrixCommand);
		System.out.println(writeValueAsString);
	}
}