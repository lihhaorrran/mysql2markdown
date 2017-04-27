<!DOCTYPE html>

<html lang="en">
<body>
|    字段名    | 类型 | 字符集 | 主键 | 默认值 | 其它 | 说明 |
| ---------- | --- | --- | --- | --- | --- | --- |
<#list items as item>
|${item.field!}|${item.type!}|${item.collation!}|${item.key!}|${item.aDefault!}|${item.extra!}|${item.comment!}|
</#list>
</body>
</html>