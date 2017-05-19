package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.TimeZone;

public class Db2Timestamps {
	public static void main(String[] args) throws Exception {
		System.out.println("Current Client timezone is " + TimeZone.getDefault());
		System.out.println("Current Client timezone offset is " + TimeZone.getDefault().getRawOffset() / 60 / 1000);
		String sql = "select \n" +
				"Timestamp('1970-01-01', '00:00:00'), \n" +
				"Timestamp('1970-01-01', '00:00:00') + current timezone, \n" +
				"current_timestamp as local_time , \n" +
				"current_timezone as db2_application_server_timezone, \n" +
				"current_timestamp - current timezone as UTC, \n" +
				"Timestamp('1970-01-01', '00:00:00') + 1491004800 seconds, \n" + // Sat, 01 Apr 2017 00:00:00 GMT
				"Timestamp('1970-01-01', '00:00:00') + current timezone + 1491004800 seconds \n" +
				"from sysibm.sysdummy1";
		System.out.println(sql);
		Connection connection = DriverManager
				.getConnection("jdbc:db2://tnpminlnx08.persistent.co.in:50000/NCIM", "db2inst1","db2inst1");
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getTimestamp(1) + "|_|" + rs.getTimestamp(1).getTimezoneOffset() + "|_|" + rs.getTimestamp(1).toGMTString() + "|_|"); 
			System.out.println(rs.getTimestamp(2) + "|_|" + rs.getTimestamp(2).getTimezoneOffset() + "|_|" + rs.getTimestamp(2).toGMTString() + "|_|");
			System.out.println(rs.getTimestamp(3) + "|_|" + rs.getTimestamp(3).getTimezoneOffset() + "|_|" + rs.getTimestamp(3).toGMTString() + "|_|");
			System.out.println(rs.getString(4));
			System.out.println(rs.getTimestamp(5) + "|_|" + rs.getTimestamp(5).getTimezoneOffset() + "|_|" + rs.getTimestamp(5).toGMTString() + "|_|");
			System.out.println(rs.getTimestamp(6) + "|_|" + rs.getTimestamp(6).getTimezoneOffset() + "|_|" + rs.getTimestamp(6).toGMTString() + "|_|");
			System.out.println(rs.getTimestamp(7) + "|_|" + rs.getTimestamp(7).getTimezoneOffset() + "|_|" + rs.getTimestamp(7).toGMTString() + "|_|");
		}
		/*
		 * Timestamp('1970-01-01', '00:00:00') - Db2 Driver assumes local timezone and toGMTString computes UTC time based on local timezone regardless of Db2 server application timezone
		 * Timestamp('1970-01-01', '00:00:00') + current timezone - Db2 driver knows that the date/time is shifted by Db2 server application timezone
		 * 	but toGMTString is computed with offset of local timezone
		 * current_timestamp as local_time - Db2 Driver returns current date/time based on Db2 server application timezone but toGMTString is computed based on local timezone
		 * current_timezone - Timezone of Db2 server application
		 * current_timestamp - current timezone as UTC - Db2 Driver returns date/time in UTC timezone but toGMTString is computed based on local timezone
		 * Timestamp('1970-01-01', '00:00:00') + 1491004800 seconds - Db2 Druver returns date/time with added unixtimestamp but not sure what is the timezone here
		 * 	and the toGMTString is computed based on local timezone
		 * Timestamp('1970-01-01', '00:00:00') + current timezone + 1491004800 seconds - Db2 Driver returns the data/time in Db2 server application timezone 
		 * and toGMTString is computed based on local timezone
		 */
	}
}
