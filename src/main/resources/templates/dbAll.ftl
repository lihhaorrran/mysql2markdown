<!DOCTYPE html>

<html lang="en">
<body>

<#list schemasMap?keys as tableName>
    <#assign tableDesc=tableDescMap[tableName] >
## ${tableName} ${tableDesc} <br>
    <#assign mapUser=schemasMap[tableName] >
|    字段名    | 类型 | 字符集 | 主键 | 默认值 | 其它 | 说明 |<br>
| ---------- | --- | --- | --- | --- | --- | --- |<br>
    <#list mapUser as item>
    |${item.field!}|${item.type!}|${item.collation!}|${item.key!}|${item.aDefault!}|${item.extra!}|${item.comment!}|<br>
    </#list>
<br>
<br>
</#list>
</body>
</html>