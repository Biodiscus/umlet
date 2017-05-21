package com.baselet.our;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Iterator;

/**
 * © 2017, Gopper
 */
public class Testing {
	@Test
	public void test() {
		Iterator<String> i = mock(Iterator.class);
		when(i.next()).thenReturn("Mockito").thenReturn("rocks");
		String result = i.next()+" "+i.next();
		assertEquals("Mockito rocks", result);
	}
}
