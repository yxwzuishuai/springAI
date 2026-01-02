package com.yxw.study.springaidemo.intercept;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Properties;

/**
 * MyBatis 查询 SQL 拦截器（只拦截 SELECT 语句）
 */
@Component // 交给 Spring 管理（若不用 Spring，需手动注册到 MyBatis 配置）
@Intercepts({
        // 拦截 StatementHandler 的 prepare 方法（参数：Connection.class, Integer.class）
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class}
        )
})
public class QuerySqlInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(QuerySqlInterceptor.class);

    /**
     * 核心拦截逻辑：拦截 SQL 预编译，提取查询 SQL 和参数
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 1. 获取 StatementHandler（被代理对象）
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        
        // 2. 通过 MyBatis 元对象工具获取 MappedStatement（包含 SQL 类型、映射信息等）
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        
        // 3. 只拦截 SELECT 类型的 SQL（排除 INSERT/UPDATE/DELETE）
        if (SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            // 4. 获取 BoundSql（包含原始 SQL 和参数）
            BoundSql boundSql = statementHandler.getBoundSql();
            String originalSql = boundSql.getSql(); // 原始 SQL（含 ? 占位符）
            Object parameterObject = boundSql.getParameterObject(); // SQL 参数（可能是单个值、Map、实体类）

            // 5. 格式化 SQL（替换 ? 为实际参数，便于日志查看）
            String formattedSql = formatSql(originalSql, parameterObject);

            // 6. 输出拦截结果（可替换为日志框架、监控系统、数据脱敏等）
            log.info("【拦截查询 SQL】\n原始 SQL：{}\n格式化 SQL：{}", originalSql, formattedSql);
        }

        // 7. 执行原方法（不阻断 SQL 执行）
        return invocation.proceed();
    }

    /**
     * 生成代理对象（MyBatis 内部调用，无需修改）
     */
    @Override
    public Object plugin(Object target) {
        // 只对 StatementHandler 生成代理（避免对其他组件无效代理）
        return Plugin.wrap(target, this);
    }

    /**
     * 读取拦截器配置（可通过 @PropertySource 或 yml 配置参数）
     */
    @Override
    public void setProperties(Properties properties) {
        // 示例：读取配置的日志开关（yml 中配置 mybatis.interceptor.log-enabled=true）
        String logEnabled = properties.getProperty("log-enabled", "true");
        log.info("查询 SQL 拦截器初始化，日志开关：{}", logEnabled);
    }

    /**
     * 格式化 SQL：将 ? 替换为实际参数（支持基本类型、字符串、实体类、Map）
     * 扩展点：可在此处添加 SQL 脱敏（如手机号、身份证）、SQL 语法校验等逻辑
     */
    private String formatSql(String originalSql, Object parameterObject) {
        if (parameterObject == null) {
            return originalSql;
        }

        // 简化实现：替换 ? 为参数（实际项目可使用 MyBatis 工具类或自定义逻辑）
        String sql = originalSql.replaceAll("\\?", "'{}'");
        // 这里仅做示例，复杂参数（如实体类、Map）需解析参数名和值，推荐使用 MyBatis 的 ParameterHandler 辅助解析
        // 完整参数解析可参考：org.apache.ibatis.scripting.defaults.DefaultParameterHandler

        // 示例：如果参数是字符串/数字，直接替换
        if (parameterObject instanceof String || parameterObject instanceof Number) {
            return String.format(sql, parameterObject);
        }

        // 复杂参数（实体类/Map）：此处简化输出，实际项目需解析参数（参考下方扩展说明）
        return String.format(sql, parameterObject.toString());
    }
}