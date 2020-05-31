package com.bigdata.analysis.dao;

import com.bigdata.analysis.domain.Task;

/**
 * Author:Mid_tsai
 * Date:2020/5/26
 * Description:
 */
public interface ITaskDAO {
    /**
     * 根据主键查询任务
     * @param taskid 主键
     * @return 任务
     */
    Task findById(long taskid);
}
