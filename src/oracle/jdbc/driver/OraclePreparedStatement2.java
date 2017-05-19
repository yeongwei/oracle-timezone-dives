package oracle.jdbc.driver;

import java.sql.PreparedStatement;

public class OraclePreparedStatement2 {
	public String toString(PreparedStatement ps) throws Exception {
		OraclePreparedStatementWrapper ops = (OraclePreparedStatementWrapper) ps;
		return ((OraclePreparedStatementWrapper) ps).getOriginalSql();
	}
}
