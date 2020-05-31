package com.bigdata.analysis.dao.factory;

/**
 * Author:Mid_tsai
 * Date:2020/5/26
 * Description:
 */

import com.bigdata.analysis.dao.ISessionAggrStatDAO;
import com.bigdata.analysis.dao.ITaskDAO;
import com.bigdata.analysis.dao.ITop10CategoryDAO;
import com.bigdata.analysis.dao.ITop10SessionDAO;
import com.bigdata.analysis.dao.imp.SessionAggrStatDAOImpl;
import com.bigdata.analysis.dao.imp.TaskDAOImpl;
import com.bigdata.analysis.dao.imp.Top10CategoryDAOImpl;
import com.bigdata.analysis.dao.imp.Top10SessionDAOImpl;

/**
 * DAO工厂类
 */
public class DAOFactory {
    public static ITaskDAO getTaskDAO() {
        return new TaskDAOImpl();
    }
    public static ISessionAggrStatDAO getSessionAggrStatDAO() {
        return new SessionAggrStatDAOImpl();
    }
    public static ITop10CategoryDAO getTop10CategoryDAO() {
        return new Top10CategoryDAOImpl();
    }
    public static ITop10SessionDAO getTop10SessionDAO() {
        return new Top10SessionDAOImpl();
    }
}
