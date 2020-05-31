package com.bigdata.analysis.util;

import com.alibaba.fastjson.JSONObject;;
import com.bigdata.analysis.conf.ConfigurationManager;
import com.bigdata.analysis.constant.Constants;
import com.bigdata.analysis.model.User;
import com.bigdata.analysis.model.UserVisitLog;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;

/**
 * Spark工具类
 *
 * @author Administrator
 */
public class SparkUtils {

    /**
     * 根据当前是否本地测试的配置
     * 决定，如何设置SparkConf的master
     */
    public static void setMaster(SparkConf conf) {
        boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);
        if (local) {
            conf.setMaster("local");
        }
    }

    /**
     * 获取SQLContext
     * 如果spark.local设置为true，那么就创建SQLContext；否则，创建HiveContext
     *
     * @param sc
     * @return
     */
    public static SQLContext getSQLContext(SparkContext sc) {
        boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);
        if (local) {
            return new SQLContext(sc);
        } else {
            return new HiveContext(sc);
        }
    }


    /**
     * 获取指定日期范围内的用户行为数据RDD
     *
     * @param sqlContext
     * @param taskParam
     * @return
     */
    public static JavaRDD<Row> getActionRDDByDateRange(
            SQLContext sqlContext, JSONObject taskParam) {
        String startDate = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE);
        String endDate = ParamUtils.getParam(taskParam, Constants.PARAM_END_DATE);

        String sql =
                "select * "
                        + "from user_visit_action "
                        + "where date>='" + startDate + "' "
                        + "and date<='" + endDate + "'";
//				+ "and session_id not in('','','')"

        Dataset<Row> actionDF = sqlContext.sql(sql);

        /**
         * 这里就很有可能发生上面说的问题
         * 比如说，Spark SQl默认就给第一个stage设置了20个task，但是根据你的数据量以及算法的复杂度
         * 实际上，你需要1000个task去并行执行
         *
         * 所以说，在这里，就可以对Spark SQL刚刚查询出来的RDD执行repartition重分区操作
         */

//		return actionDF.javaRDD().repartition(1000);

        return actionDF.javaRDD();
    }

    /**
     * 加载文件数据并注册成表
     * @param sc
     * @param sqlContext
     */
    public static void loadData(JavaSparkContext sc,
                                SQLContext sqlContext) {
        JavaRDD<String> lines = sc.textFile(" ");  // 输入行为日志的文件路径
        // 行格式
        JavaRDD<UserVisitLog> userVisitJavaRDD = lines.map(new Function<String, UserVisitLog>() {
            UserVisitLog log = null;

            @Override
            public UserVisitLog call(String v1) throws Exception {
                String[] strs = v1.split(",");
                log = new UserVisitLog();
                log.setDate(strs[0]);
                log.setUserId(strs[1]);
                log.setSessionId(strs[2]);
                log.setPageId(strs[3]);
                log.setActionTime(strs[4]);
                log.setSearchKeyword(strs[5]);
                log.setClickCategoryId(strs[6]);
                log.setClickProductId(strs[7]);
                log.setOrderCategoryIds(strs[8]);
                log.setOrderProductIds(strs[9]);
                log.setPayCategoryIds(strs[10]);
                log.setPayProductIds(strs[11]);
                log.setCityId(strs[12]);
                return log;
            }
        });

        Dataset<Row> df = sqlContext.createDataFrame(userVisitJavaRDD, UserVisitLog.class);
        df.registerTempTable("user_visit_action");


        JavaRDD<String> userLines = sc.textFile(" ");  // 输入用户文件的路径

        // 行格式
        JavaRDD<User> userJavaRDD = userLines.map(new Function<String, User>() {
            User user = null;

            @Override
            public User call(String v1) throws Exception {
                String[] strs = v1.split(",");
                user = new User();
                user.setUserId(strs[0]);
                user.setUserName(strs[1]);
                user.setName(strs[2]);
                user.setAge(strs[3]);
                user.setProfessional(strs[4]);
                user.setCity(strs[5]);
                user.setSex(strs[6]);
                return user;
            }
        });

        Dataset<Row> userDf = sqlContext.createDataFrame(userJavaRDD, User.class);
        userDf.registerTempTable("user_info");
    }

}
