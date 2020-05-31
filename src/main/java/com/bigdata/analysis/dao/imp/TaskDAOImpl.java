package com.bigdata.analysis.dao.imp;

import com.bigdata.analysis.dao.ITaskDAO;
import com.bigdata.analysis.domain.Task;
import com.bigdata.analysis.jdbc.JDBCHelper;

import java.sql.ResultSet;

/**
 * Author:Mid_tsai
 * Date:2020/5/26
 * Description:
 */
public class TaskDAOImpl implements ITaskDAO {

    /**
     * 根据主键查询任务
     * @param taskid 主键
     * @return 任务
     */
    public Task findById(long taskid) {
        final Task task = new Task();

        String sql = "select * from task where id=?";
        Object[] params = new Object[]{taskid};

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            public void process(ResultSet rs) throws Exception {
                if(rs.next()) {
                    long taskid = rs.getLong(1);
                    String taskName = rs.getString(2);
                    String taskParam = rs.getString(3);
                    String createTime = rs.getString(4);
                    //String startTime = rs.getString(4);
                    //String finishTime = rs.getString(5);
                    //String taskType = rs.getString(6);
                    //String taskStatus = rs.getString(7);

                    task.setTaskid(taskid);
                    task.setTaskName(taskName);
                    task.setCreateTime(createTime);
					/*task.setStartTime(startTime);
					task.setFinishTime(finishTime);
					task.setTaskType(taskType);
					task.setTaskStatus(taskStatus);*/
                    task.setTaskParam(taskParam);
                }
            }

        });
        return task;
    }
}
