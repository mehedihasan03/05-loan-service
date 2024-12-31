package com.doer.mraims.loanprocess.core.utils;

public enum DBSchema {

    COMMON("common."),
    TEMPLATE("template.");

    private String schema;

    DBSchema(String schema) {
        this.schema = schema;
    }

    public String getSchema() {
        return schema;
    }
}
