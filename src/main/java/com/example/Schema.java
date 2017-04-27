package com.example;

import lombok.Data;

import javax.annotation.sql.DataSourceDefinition;

/**
 * Created by niejinping on 2017/4/26.
 */
@Data
public class Schema {
    String field ;
    String type;
    String collation ;
    String aNull ;
    String key;
    String aDefault;
    String extra;
    String comment;
}
