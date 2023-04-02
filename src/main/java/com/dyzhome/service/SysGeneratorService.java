package com.dyzhome.service;

import cn.hutool.core.util.StrUtil;
import com.dyzhome.utils.GenUtils;
import com.dyzhome.utils.JdbcUtils;
import com.dyzhome.utils.PageUtils;
import com.dyzhome.utils.Query;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 *
 * @author Dyz
 */
@Service
public class SysGeneratorService {

    /**
     * 查询表总数
     * @param tableName 表名
     * @return 总数
     */
    public int getTotal(String tableName) throws Exception{
        Connection connection = JdbcUtils.connection;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "count(1) as total\n" +
                "FROM information_schema.TABLES\n" +
                "WHERE table_schema = (SELECT DATABASE())");
        if (StrUtil.isNotBlank(tableName)) {
            sql.append(" AND table_name LIKE concat( '%', ?, '%' )");
        }
        PreparedStatement ps = connection.prepareStatement(sql.toString());
        if (StrUtil.isNotBlank(tableName)) {
            ps.setString(1,tableName);
        }
        ResultSet rs = ps.executeQuery();
        rs.next();
        String s = rs.getString("total");
        return Integer.parseInt(s);
    }

    /**
     * 查询分页列表
     * @param query 条件
     * @return 列表
     */
    public PageUtils queryList(Query query) throws Exception {
        int limit = query.getLimit();
        int page = query.getPage();
        Connection connection = JdbcUtils.connection;
        if (Objects.isNull(connection) || Objects.isNull(query.get("tableName"))){
            return new PageUtils(null, 0, query.getLimit(), query.getPage());
        }
        int total = getTotal(query.get("tableName").toString());
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "table_name AS tableName,\n" +
                "engine AS engine,\n" +
                "table_comment AS tableComment,\n" +
                "create_time AS createTime\n" +
                "FROM information_schema.TABLES\n" +
                "WHERE table_schema = (SELECT DATABASE())");
        if (StrUtil.isNotBlank(query.get("tableName").toString())) {
            sql.append(" AND table_name LIKE concat( '%', ?, '%' )");
        }
        sql.append(" ORDER BY create_time DESC");
        sql.append(" LIMIT ?, ?");
        PreparedStatement ps = connection.prepareStatement(sql.toString());
        if (StrUtil.isNotBlank(query.get("tableName").toString())) {
            ps.setString(1, query.get("tableName").toString());
            ps.setInt(2, ((page - 1) * limit));
            ps.setInt(3, limit);
        } else {
            ps.setInt(1, ((page - 1) * limit));
            ps.setInt(2, limit);
        }
        ResultSet rs = ps.executeQuery();
        List<Map<String, Object>> list = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            map.put("tableName", rs.getString("tableName"));
            map.put("engine", rs.getString("engine"));
            map.put("tableComment", rs.getString("tableComment"));
            map.put("createTime", rs.getString("createTime"));
            list.add(map);
        }
        return new PageUtils(list, total, query.getLimit(), query.getPage());
    }

    /**
     * 生成代码
     * @param tableNames 表名
     * @param connection 数据库连接
     * @return byte数组
     */
    public byte[] generatorCode(String[] tableNames, Connection connection) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = queryTable(tableName, connection);
            //查询列信息
            List<Map<String, String>> columns = queryColumn(tableName, connection);
            //生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }


    /**
     * 根据表名查询表
     * @param tableName 表名
     * @param connection 数据库连接
     * @return 表信息
     */
    public Map<String, String> queryTable(String tableName, Connection connection) throws Exception {
        String sql = "SELECT table_name    AS tableName,\n" +
                "   engine        AS engine,\n" +
                "   table_comment AS tableComment,\n" +
                "   create_time   AS createTime\n" +
                "FROM information_schema.TABLES\n" +
                "   WHERE table_schema = (SELECT DATABASE())\n" +
                "AND table_name = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, tableName);
        ResultSet rs = ps.executeQuery();
        HashMap<String, String> map = new HashMap<>();
        while (rs.next()) {
            map.put("tableName", rs.getString("tableName"));
            map.put("engine", rs.getString("engine"));
            map.put("tableComment", rs.getString("tableComment"));
            map.put("createTime", rs.getString("createTime"));
        }
        return map;
    }

    /**
     * 根据表名查询其所有字段信息
     * @param tableName 表名
     * @param connection 数据库连接
     * @return 结果集
     */
    public List<Map<String, String>> queryColumn(String tableName, Connection connection) throws Exception {
        String sql = "SELECT column_name    AS columnName,\n" +
                "   data_type      AS dataType,\n" +
                "   column_comment AS columnComment,\n" +
                "   column_key     AS columnKey,\n" +
                "   extra\n" +
                "FROM information_schema.COLUMNS\n" +
                "WHERE table_name = ?\n" +
                "   AND table_schema = (SELECT DATABASE())\n" +
                "ORDER BY ordinal_position";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, tableName);
        ResultSet rs = ps.executeQuery();
        ArrayList<Map<String, String>> list = new ArrayList<>();
        while (rs.next()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("columnName", rs.getString("columnName"));
            map.put("dataType", rs.getString("dataType"));
            map.put("columnComment", rs.getString("columnComment"));
            map.put("columnKey", rs.getString("columnKey"));
            map.put("extra", rs.getString("extra"));
            list.add(map);
        }
        return list;
    }
}
