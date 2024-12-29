package com.doer.mraims.loanprocess.core.dao;

import org.json.JSONArray;
import org.json.JSONObject;

public interface QueryExecutorInterface {

    int saveSingleRow(String sql, Object[] params);

    JSONArray getMultipleRows(String sql, Object[] params);

    JSONObject getSingleRow(String sql, Object[] params);

}
