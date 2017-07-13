/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.shared;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class DateUtilsTest {

    @Test
    public void testToLocalDateTime() {
        LocalDateTime expected = LocalDateTime.of(2017, 7, 6, 21, 32, 18);
        Date date = new Date(1499347938000L);
        LocalDateTime actual = DateUtils.toLocalDateTime(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocalDateTime_whenInputIsNullExpectNull() {
        assertNull(DateUtils.toLocalDateTime(null));
    }

    @Test
    public void testToLocalDate() {
        LocalDate expected = LocalDate.of(2017, 7, 6);
        Date date = new Date(1499347938000L);
        LocalDate actual = DateUtils.toLocalDate(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocalDate_whenInputIsNullExpectNull() {
        assertNull(DateUtils.toLocalDate(null));
    }

    @Test
    public void testToDateFromLocalDateTime() {
        Date expected = new Date(1499347938000L);
        LocalDateTime localDateTime = LocalDateTime.of(2017, 7, 6, 21, 32, 18);
        Date actual = DateUtils.toDate(localDateTime);
        assertEquals(expected, actual);
    }

    @Test
    public void testToDateFromLocalDateTime_whenInputIsNullExpectNull() {
        assertNull(DateUtils.toDate((LocalDateTime) null));
    }

    @Test
    public void testToDateFromLocalDate() {
        Date expected = new Date(1499270400000L);
        LocalDate localDate = LocalDate.of(2017, 7, 6);
        Date actual = DateUtils.toDate(localDate);
        assertEquals(expected, actual);
    }

    @Test
    public void testToDateFromLocalDate_whenInputIsNullExpectNull() {
        assertNull(DateUtils.toDate((LocalDate) null));
    }

}