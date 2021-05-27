package com.sellermatch.config.querydsl;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomMysqlDialect extends MySQL5Dialect {
    public CustomMysqlDialect() {
        super();
        // register custom/inner function here
        registerFunction("GROUP_CONCAT", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
        registerFunction("IF", new StandardSQLFunction("if", StandardBasicTypes.STRING));
        registerFunction("INSTR", new StandardSQLFunction("instr", StandardBasicTypes.STRING));

    }
}