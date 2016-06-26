/**
 * 
 */
package me.hmasrafchi.leddisplay.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author michelin
 *
 */
public final class TestModularArithmetic {
	@Test
	public void test() {
		final int modulo = 12;
		final ModularArithmetic modularArithmetic = new ModularArithmetic(modulo);

		final List<Integer> expectedList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 0, 1, 2, 3, 4, 5, 6, 7,
				8, 9, 10, 11, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
		final List<Integer> actualList = new ArrayList<>(modulo * 3);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < modulo; j++) {
				final int currentValue = modularArithmetic.count();
				actualList.add(currentValue);
			}
		}

		Assert.assertThat(actualList, CoreMatchers.is(expectedList));
	}

}