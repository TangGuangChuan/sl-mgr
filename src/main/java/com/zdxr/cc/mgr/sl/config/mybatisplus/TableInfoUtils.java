package com.zdxr.cc.mgr.sl.config.mybatisplus;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

/**
 * mybatis plus 拼接foreach时有问题
 */
public class TableInfoUtils {

    /**
     * 获取 insert 时候字段 sql 脚本片段
     * <p>insert into table (字段) values (值)</p>
     * <p>位于 "字段" 部位</p>
     *
     * <li> 自动选部位,根据规则会生成 if 标签 </li>
     *
     * @return sql 脚本片段
     */
    public static String getAllInsertSqlColumnMaybeIf(TableInfo tableInfo) {
        return tableInfo.getKeyInsertSqlColumn(true) + tableInfo.getFieldList().stream().map(f -> TableInfoUtils.getInsertSqlColumnMaybeIf(f))
                .filter(Objects::nonNull).collect(joining(TableInfo.NEWLINE));
    }

    /**
     * 获取 insert 时候字段 sql 脚本片段
     * <p>insert into table (字段) values (值)</p>
     * <p>位于 "字段" 部位</p>
     *
     * <li> 根据规则会生成 if 标签 </li>
     *
     * @return sql 脚本片段
     */
    public static String getInsertSqlColumnMaybeIf(TableFieldInfo fieldInfo) {
        final String sqlScript = fieldInfo.getInsertSqlColumn();
        if (fieldInfo.getFieldFill() == FieldFill.INSERT || fieldInfo.getFieldFill() == FieldFill.INSERT_UPDATE) {
            return sqlScript;
        }
        return TableInfoUtils.convertColumn(sqlScript, fieldInfo.getProperty(), fieldInfo.getInsertStrategy(), fieldInfo);
    }


    /**
     * 获取所有 insert 时候插入值 sql 脚本片段
     * <p>insert into table (字段) values (值)</p>
     * <p>位于 "值" 部位</p>
     *
     * <li> 自动选部位,根据规则会生成 if 标签 </li>
     *
     * @return sql 脚本片段
     */
    public static String getAllInsertSqlPropertyMaybeIf(final String prefix, TableInfo tableInfo) {
        final String newPrefix = prefix == null ? TableInfo.EMPTY : prefix;
        return tableInfo.getKeyInsertSqlProperty(newPrefix, true) + tableInfo.getFieldList().stream()
                .map(i -> TableInfoUtils.getInsertSqlPropertyMaybeIf(newPrefix, i)).filter(Objects::nonNull).collect(joining(TableInfo.NEWLINE));
    }

    /**
     * 获取 insert 时候插入值 sql 脚本片段
     * <p>insert into table (字段) values (值)</p>
     * <p>位于 "值" 部位</p>
     *
     * <li> 根据规则会生成 if 标签 </li>
     *
     * @return sql 脚本片段
     */
    public static String getInsertSqlPropertyMaybeIf(final String prefix, TableFieldInfo fieldInfo) {
        String sqlScript = fieldInfo.getInsertSqlProperty(prefix);
        if (fieldInfo.getFieldFill() == FieldFill.INSERT || fieldInfo.getFieldFill() == FieldFill.INSERT_UPDATE) {
            return sqlScript;
        }
        return TableInfoUtils.convertColumn(sqlScript, prefix + fieldInfo.getProperty(), fieldInfo.getInsertStrategy(), fieldInfo);
    }

    /**
     * 转换成 if 标签的脚本片段
     *
     * @param sqlScript     sql 脚本片段
     * @param property      字段名
     * @param fieldStrategy 验证策略
     * @return if 脚本片段
     */
    public static String convertColumn(final String sqlScript, final String property, final FieldStrategy fieldStrategy, TableFieldInfo fieldInfo) {
        final FieldStrategy targetStrategy = Optional.ofNullable(fieldStrategy).orElse(fieldInfo.getFieldStrategy());
        if (targetStrategy == FieldStrategy.NEVER) {
            return null;
        }
        return sqlScript;
    }
}
