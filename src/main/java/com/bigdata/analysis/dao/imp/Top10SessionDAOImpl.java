package com.bigdata.analysis.dao.imp;


import com.bigdata.analysis.dao.ITop10SessionDAO;
import com.bigdata.analysis.domain.Top10Session;
import com.bigdata.analysis.jdbc.JDBCHelper;

/**
 * top10活跃session的DAO实现
 *
 */
public class Top10SessionDAOImpl implements ITop10SessionDAO {

	@Override
	public void insert(Top10Session top10Session) {
		String sql = "insert into top10_session (task_id ,category_id ,session_id,click_count) values(?,?,?,?)";
		
		Object[] params = new Object[]{
				top10Session.getTaskid(),
				top10Session.getCategoryid(),
				top10Session.getSessionid(),
				top10Session.getClickCount()};
		
		JDBCHelper jdbcHelper = JDBCHelper.getInstance();
		jdbcHelper.executeUpdate(sql, params);
	}

}
