package de.ralfebert.commons.lang.date;

import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

public class DateHelperTest {

	private static DateFormat samplesFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSSS");

	@Test
	public void testGetToday() {
		assertEquals(DateHelper.getDateWithoutTime(new Date()), DateHelper.getToday());
	}

	@Test
	public void testGetTomorrow() {
		assertEquals(DateHelper.getDateWithoutTime(DateUtils.addDays(new Date(), 1)), DateHelper.getTomorrow());
	}

	@Test
	public void testIsEqualOrAfterTomorrow() {
		assertTrue(DateHelper.isEqualOrAfterTomorrow(DateHelper.getDateWithoutTime(DateUtils.addDays(new Date(), 1))));
		assertTrue(DateHelper.isEqualOrAfterTomorrow(DateUtils.addDays(new Date(), 1)));
		assertFalse(DateHelper.isEqualOrAfterTomorrow(DateUtils.addDays(new Date(), -1)));
		assertFalse(DateHelper.isEqualOrAfterTomorrow(new Date()));
	}

	@Test
	public void testGetDateWithoutTime() throws ParseException {
		Calendar c = Calendar.getInstance();
		// 6 - 1 is June (Calendar months start at 0)
		c.set(2006, 6 - 1, 5, 15, 9, 18);
		c.set(Calendar.MILLISECOND, 11);

		assertEquals(DateHelper.getDateWithoutTime(c.getTime()), samplesFormat.parse("05.06.2006 00:00:00:0000"));
		assertEquals("06.06.2006 00:00:00:0000", samplesFormat.format(DateHelper.getDateWithoutTime(samplesFormat
				.parse("06.06.2006 10:20:30:0040"))));
	}

	@Test
	public void testMinMax() throws ParseException {
		Date d1 = samplesFormat.parse("1.1.2003 00:00:00:0000");
		Date d2 = samplesFormat.parse("05.06.2006 00:00:00:0000");
		Date d3 = samplesFormat.parse("06.06.2006 00:00:00:0000");
		assertEquals(d2, DateHelper.max(d1, d2));
		assertEquals(d2, DateHelper.max(d2, d1));
		assertEquals(d3, DateHelper.max(d2, d3));
		assertEquals(d3, DateHelper.max(d3, d2));
		assertEquals(d1, DateHelper.min(d1, d2));
		assertEquals(d1, DateHelper.min(d2, d1));
		assertEquals(d2, DateHelper.min(d2, d3));
		assertEquals(d2, DateHelper.min(d3, d2));
	}

	@Test
	public void testGetDateByDayMonthYear() {
		assertEquals("05.09.2001 00:00:00:0000", samplesFormat.format(DateHelper.getDateByYearMonthDay(2001, 9, 5)));
	}

}