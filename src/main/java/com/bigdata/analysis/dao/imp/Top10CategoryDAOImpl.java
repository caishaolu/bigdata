package com.bigdata.analysis.dao.imp;


import com.bigdata.analysis.dao.ITop10CategoryDAO;
import com.bigdata.analysis.domain.Top10Category;
import com.bigdata.analysis.jdbc.JDBCHelper;

/**
 * top10品类DAO实现

 *
 */
public class Top10CategoryDAOImpl implements ITop10CategoryDAO {

	@Override
	public void insert(Top10Category category) {
		String sql = "insert into top10_category (task_id ,cate_id ,click_cnt ,pay_cnt ,order_cnt) values(?,?,?,?,?)";
		
		Object[] params = new Object[]{category.getTaskid(),
				category.getCategoryid(),
				category.getClickCount(),
				category.getOrderCount(),
				category.getPayCount()};  
		
		JDBCHelper jdbcHelper = JDBCHelper.getInstance();
		jdbcHelper.executeUpdate(sql, params);
	}

}
