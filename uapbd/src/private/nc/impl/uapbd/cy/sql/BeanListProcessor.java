package nc.impl.uapbd.cy.sql;

import nc.jdbc.framework.processor.ResultSetProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanListProcessor<T> implements ResultSetProcessor {
	private static final long serialVersionUID = -4376896948725349897L;

	private Class<? extends T> type;
	
	public BeanListProcessor(Class<? extends T> type) {
		this.type = type;
	}

	@Override
	public Object handleResultSet(ResultSet rs) throws SQLException {
		BeanListHandler<T> handler = new BeanListHandler<T>(type);
		return handler.handle(rs);
	}

}
