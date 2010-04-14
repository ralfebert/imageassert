/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Ralf Ebert - initial API and implementation
 *******************************************************************************/
package de.ralfebert.commons.lang.date;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

/**
 * Helper methods for dealing with java.util.Date
 */
public class DateHelper {

	/**
	 * Returns true if the given date equals or is after tomorrow (ignoring the
	 * time)
	 * 
	 * @return true if the given date is tomorrow or is after tomorrow
	 */
	public static boolean isEqualOrAfterTomorrow(Date d) {
		return (d != null && !d.before(getTomorrow()));
	}

	/**
	 * Returns the date representing today (by system time) without time
	 * 
	 * @return
	 */
	public static Date getToday() {
		Calendar c = Calendar.getInstance();
		truncateTime(c);
		return c.getTime();
	}

	/**
	 * Returns the date representing tomorrow (by system time) without time
	 * 
	 * @return Date object representing tomorrow 00:00:00
	 */
	public static Date getTomorrow() {
		Calendar c = Calendar.getInstance();
		truncateTime(c);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}

	/**
	 * Returns date without time (hour, minute, second, millisecond set to 0)
	 * 
	 * @param date
	 *            date
	 * @return date without time
	 */
	public static Date getDateWithoutTime(Date date) {
		return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
	}

	/**
	 * Sets hour, minute, second, millisecond to 0 for the given Calendar object
	 * 
	 * @param c
	 *            Calendar object
	 */
	private static void truncateTime(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * Returns the later date of the given dates
	 * 
	 * @param a
	 * @param b
	 * @return latest date of the given dates
	 */
	public static Date max(Date a, Date b) {
		if (a == null) {
			return b;
		}
		if (b == null) {
			return a;
		}
		return (a.after(b) ? a : b);
	}

	/**
	 * Returns the earlier date of the given dates
	 * 
	 * @param a
	 *            date
	 * @param b
	 *            date
	 * @return earliest date of the given dates
	 */
	public static Date min(Date a, Date b) {
		if (a == null) {
			return b;
		}
		if (b == null) {
			return a;
		}
		return (a.before(b) ? a : b);
	}

	/**
	 * Adds nDays to the given date and returns a new date
	 * 
	 * @param date
	 *            date
	 * @param numberOfDays
	 *            number of days to add
	 * @return date plus numberOfDays
	 * @deprecated use {@link DateUtils}#addDays from Apache Commons Lang
	 */
	public static Date addDays(Date date, int numberOfDays) {
		return DateUtils.addDays(date, numberOfDays);
	}

	/**
	 * @deprecated use getDateByYearMonthDay()
	 */
	public static Date getDateByDayMonthYear(int day, int month, int year) {
		return getDateByYearMonthDay(year, month, day);
	}

	/**
	 * This method creates a new Date object with the given year, month, day and zero time
	 * 
	 * @param year
	 *            year for digits, e.g. 2002
	 * @param month
	 *            month of the year 1..12
	 * @param day
	 *            day of the month 1..31
	 * @return a Date based on input parameters (with time zeroed)
	 */
	public static Date getDateByYearMonthDay(int year, int month, int day) {
		if (day < 1 || day > 31) {
			throw new IllegalArgumentException("day is out of range 1..31:" + day);
		}
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("month is out of range 1..12:" + month);
		}

		Calendar c = Calendar.getInstance();
		// For Calendar 0 is January.
		c.set(year, month - 1, day);
		truncateTime(c);
		return c.getTime();
	}
}