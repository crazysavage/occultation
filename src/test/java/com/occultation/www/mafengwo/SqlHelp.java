package com.occultation.www.mafengwo;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.occultation.www.util.FileUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-27 10:08
 */
public class SqlHelp {

    private static DataSource dds = null;


    private static class S{
        private static SqlHelp instance = new SqlHelp();
    }


    public static SqlHelp getInstance() {
        return S.instance;
    }


    private SqlHelp() {
        Properties properties = FileUtil.loadPropertyFile("classpath:db.properties");
        try {
            dds = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException("构建数据源失败",e);
        }
    }

    private Connection getConnection() {
        try {
            return dds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private  Pattern sqlPattern = Pattern.compile("#\\{(.*?)}");

    private  List getParamVals(String sql,Object o) {
        List vals = new ArrayList();
        Matcher m = sqlPattern.matcher(sql);
        String json = JSON.toJSONString(o);


        while (m.find()) {
            vals.add(JSONPath.read(json,"$." + m.group(1)));
        }
        return vals;
    }

    private String formatSql(String sql) {
        return sql.replaceAll("#\\{(.*?)}","?");

    }


    public int insert(String sql,Object o) {
        try {
            List vals = getParamVals(sql,o);
            sql = formatSql(sql);
            PreparedStatement ps = getConnection().prepareStatement(sql);
            int idx = 1;
            for (Object val : vals) {
                ps.setObject(idx,val);
                idx++;
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }











}
