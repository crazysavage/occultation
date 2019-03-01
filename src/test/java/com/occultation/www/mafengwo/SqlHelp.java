package com.occultation.www.mafengwo;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.occultation.www.util.FileUtil;

import javax.sql.DataSource;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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


    private List<Field> getFields(Class clazz) {

        Field[] fs = clazz.getDeclaredFields();
        if (fs == null) {
            return null;
        }
        List<Field> fields = new ArrayList<>();
        for (Field f : fs) {
            f.setAccessible(true);
            fields.add(f);
        }

        return fields;

    }

    public <T> List<T> select(String sql,Class<T> clazz) {

        List<Field> fields = getFields(clazz);
        if (fields == null) {
            return null;
        }

        try (Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ResultSet set = ps.executeQuery();

            List<T> res = new ArrayList<>();
            while(set.next()) {
                T t = clazz.newInstance();
                for (Field f : fields) {
                    Object o = set.getString(f.getName());
                    if (f.getType() == Integer.class) {
                        o = Integer.parseInt((String) o);
                    }
                    f.set(t,o);
                }
                res.add(t);
            }
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public int insert(String sql,Object o) {
        List vals = getParamVals(sql,o);
        sql = formatSql(sql);
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            int idx = 1;
            for (Object val : vals) {
                ps.setObject(idx,val);
                idx++;
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(ps.toString());
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

        return 0;
    }











}
