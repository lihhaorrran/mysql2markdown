package com.example.Utils;

import com.alibaba.druid.util.StringUtils;
import com.example.Schema;
import com.example.TableInfo;
import lombok.Data;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by niejinping on 2017/4/27.
 */
@Data
public class DbUtils {
    private String driver;
    private String url;
    private String urlSchema;
    private String user;
    private String password;
    private BasicDataSource dataSource;
    private BasicDataSource dataSourceSchema;
    private Connection connection;
    private Connection connectionSchema;

    public DbUtils(String driver,String url,String urlSchema,String user,String password) {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        dataSourceSchema = new BasicDataSource();

        dataSourceSchema.setDriverClassName(driver);
        dataSourceSchema.setUrl(urlSchema);
        dataSourceSchema.setUsername(user);
        dataSourceSchema.setPassword(password);

        try {
            connection = dataSource.getConnection();
            connectionSchema = dataSourceSchema.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public List<String> getTableNames() {

        try {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, null, new String[] {"TABLE","VIEW"});

            List<String> resultList = new ArrayList<>();

            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");

                if(StringUtils.isEmpty(tableName) == false){
                    resultList.add(tableName);
                }
            }
            return resultList;
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ArrayList<>();

    }

    public String getTableDesc(String tableName){
        String sql = String.format(" SELECT TABLE_COMMENT FROM TABLES  WHERE TABLE_NAME='%s'" , tableName);
        System.out.println(sql);
        PreparedStatement ps;
        try {
            ps = connectionSchema.prepareStatement(sql);

            ResultSet tmpRs = ps.executeQuery();

            while (tmpRs.next()) {
                String tableDesc = tmpRs.getString("TABLE_COMMENT");

                if(StringUtils.isEmpty(tableDesc) == false){
                    return tableDesc;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public TableInfo getTableInfo(String tableName){
        TableInfo tableInfo = new TableInfo();

        tableInfo.setTableName(tableName);
        tableInfo.setTableDesc(getTableDesc(tableName));

        return tableInfo;
    }

    public List<Schema> getTableSchema(String tableName)  {
        try{
            PreparedStatement ps = connection.prepareStatement(" SHOW FULL COLUMNS FROM " + tableName);
            ResultSet rs = ps.executeQuery();
            List<Schema> schemaList = new ArrayList<>();
            while (rs.next()) {
                Schema schema = new Schema();
                String field = rs.getString("Field");
                String type = rs.getString("Type");
                String collation = rs.getString("Collation");
                String aNull = rs.getString("Null");
                String key = rs.getString("Key");
                String aDefault = rs.getString("Default");
                String extra = rs.getString("Extra");
                String comment = rs.getString("Comment");

                schema.setField(field);
                schema.setType(type);
                schema.setCollation(collation);
                schema.setANull(aNull);
                schema.setKey(key);
                schema.setADefault(aDefault);
                schema.setExtra(extra);
                schema.setComment(comment);

                schemaList.add(schema);
                System.out.println(String.format("%s %s %s ",field,type,comment));
            }

            return schemaList;
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public List<List<Schema>> getAllSchema(){
        List<List<Schema>> schemas = new ArrayList<>();

        List<String> tables = getTableNames();


        for(String table : tables){
            List<Schema> schemas1 = getTableSchema(table);

            if(schemas1 != null || schemas1.size() != 0 ){
                schemas.add(schemas1);
            }
        }
        return schemas;
    }
}
