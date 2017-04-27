package com.example;

import com.alibaba.druid.util.StringUtils;
import com.example.Utils.DbUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.Date;

/**
 * Created by niejinping on 2017/4/26.
 */
@Controller
public class DB2Controller {

    @Value("${application.message:Hello World}")
    private String message = "Hello World";
    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.url_schema}")
    private String urlSchema;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;
    private DbUtils dbUtils;

    @PostConstruct
    public void init(){
        dbUtils = new DbUtils(driver,url,urlSchema,user,password);
    }

    @RequestMapping("/db")
    public String db(ModelMap model){
        long start = System.currentTimeMillis();
        System.out.println("jdbc start ==========>");

        List<String> tableNames = dbUtils.getTableNames();
        Map<String,List<Schema>> schemasMap = new HashMap<>();
        Map<String,String> tableDescMap = new HashMap<>();

        for(String tableName : tableNames){
            String tableDesc = dbUtils.getTableDesc(tableName);

            tableDescMap.put(tableName,tableDesc);

            List<Schema> schemas = dbUtils.getTableSchema(tableName);
            schemasMap.put(tableName, schemas);
        }

        long end = System.currentTimeMillis();
        System.out.println("jdbc end ==========>");

        model.put("tableNames",tableNames);
        model.put("tableDescMap",tableDescMap);
        model.put("schemasMap",schemasMap);

        return "dbAll";
    }

    @RequestMapping(value = "/db2markdown",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String db2markdown(ModelMap model){

        List<String> tableNames = dbUtils.getTableNames();
        Map<String,List<Schema>> schemasMap = new HashMap<>();
        Map<String,String> tableDescMap = new HashMap<>();

        for(String tableName : tableNames){
            String tableDesc = dbUtils.getTableDesc(tableName);

            tableDescMap.put(tableName,tableDesc);

            List<Schema> schemas = dbUtils.getTableSchema(tableName);
            schemasMap.put(tableName, schemas);
        }

        model.put("tableNames",tableNames);
        model.put("tableDescMap",tableDescMap);
        model.put("schemasMap",schemasMap);

        return "dbAll";
    }


    @ResponseBody
    @RequestMapping("/schemas")
    public List<List<Schema>> schemas(){
        return dbUtils.getAllSchema();
    }

    @ResponseBody
    @RequestMapping("/tables")
    public List<String> tables(){
        return dbUtils.getTableNames();
    }
    @ResponseBody
    @RequestMapping("/tableinfo/{tableName}")
    public TableInfo tableinfo(@PathVariable("tableName")String tableName){
        return dbUtils.getTableInfo(tableName);
    }

    @ResponseBody
    @RequestMapping("/schema/{tableName}")
    public List<Schema> schemas(@PathVariable("tableName")String tableName){
        return dbUtils.getTableSchema(tableName);
    }

    @RequestMapping("/db/{tableName}")
    public String dbTable(@PathVariable("tableName") String tableName, ModelMap model){
        long start = System.currentTimeMillis();
        System.out.println("jdbc start ==========>");

        try {

                System.out.println(tableName + "===========================>");

                List<Schema> schemas =  dbUtils.getTableSchema(tableName);

                model.put("items",schemas);

                System.out.println(tableName + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");


        }catch (Exception e ){
            e.printStackTrace();
        }

        return "db";
    }

    @RequestMapping("/hello")
    public String welcome(ModelMap model) {
        model.put("time", new Date());
        model.put("message", this.message);
        return "hello";
    }
}
