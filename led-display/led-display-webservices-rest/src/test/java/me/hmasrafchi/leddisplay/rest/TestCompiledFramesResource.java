/**
 * 
 */
package me.hmasrafchi.leddisplay.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.junit.Test;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.rest.CompiledFramesResource.MyClass;

/**
 * @author michelin
 *
 */
public final class TestCompiledFramesResource extends TestResource {
	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void get_matrixframes_shoulReturnOKEvenIfNoScenesArePresent(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2) {
		final Response postResponse = sendPostCreatingMatrix(webTarget1, 4, 6);
		final String path = getWebTargetPath(postResponse) + "/frames";

		final Response getResponse = webTarget2.path(path).request(MediaType.APPLICATION_JSON).get();
		final MyClass frames = getResponse.readEntity(MyClass.class);
		final List<List<List<Led>>> actualFrames = frames.getFrames();

		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.OK.getStatusCode())));
	}
}