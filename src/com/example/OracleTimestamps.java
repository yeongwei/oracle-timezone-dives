package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.TimeZone;

public class OracleTimestamps {
	public static void main(String[] args) throws Exception {
		System.out.println("Current Client timezone is " + TimeZone.getDefault());
		System.out.println("Current Client timezone offset is " + TimeZone.getDefault().getRawOffset() / 60 / 1000);
		String sql = "select \n"
				+ "timestamp '1970-01-01 00:00:00', \n"
				+ "timestamp '1970-01-01 00:00:00 UTC', \n"
				+ "timestamp '1970-01-01 00:00:00 America/New_York', \n"
				+ "CURRENT_TIMESTAMP "
				+ "from dual";
		Connection connection = DriverManager
				.getConnection("jdbc:oracle:thin:ncim/ncim@//tnpminlnx13.persistent.co.in:1521/NCIM");
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getTimestamp(1) + "|_|" + rs.getTimestamp(1).getTimezoneOffset() + "|_|" +  rs.getTimestamp(1).toGMTString() + "|_|");
			System.out.println(rs.getTimestamp(2) + "|_|" + rs.getTimestamp(2).getTimezoneOffset() + "|_|" + rs.getTimestamp(2).toGMTString() + "|_|");
			System.out.println(rs.getTimestamp(3) + "|_|" + rs.getTimestamp(3).getTimezoneOffset() + "|_|" + rs.getTimestamp(3).toGMTString() + "|_|");
			System.out.println(rs.getTimestamp(4) + "|_|" + rs.getTimestamp(4).getTimezoneOffset() + "|_|" + rs.getTimestamp(4).toGMTString() + "|_|");
		}
		
		/*
		 * timestamp '1970-01-01 00:00:00' - Oracle Driver assumes that the date/time is at local timezone and toGMTString attempts to undo the local timezone offset
		 * timestmap '1970-01-01 00:00:00 UTC' - Oracle Driver knows that the date/time is at UTC timezone and java.sql.Timestamp#toString reports date/time based on local timezone 
		 * 	and toGMTString will return the correct GMT time because the timestmap '1970-01-01 00:00:00 UTC' tells that the value is already at GMT timezone
		 * timestamp '1970-01-01 00:00:00 America/New_York' - Oracle Driver is being informed that the date/time here is at America/New York timezone
		 * 	java.sql.Timestamp#toString returns the date/time shifted to the local timezone and the toGMTString will compute the GMT date/time based on the date/time shifted to local timezone
		 *  CURRENT_TIMESTAMP - Oracle driver returns date/time with local timezone and toGMTString returns the correct UTC
		 */
	}
}
