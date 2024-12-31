package com.doer.mraims.loanprocess.core.dao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public interface QueryExecutorInterface {

    int saveSingleRow(String sql, Object[] params);

    JSONArray getMultipleRows(Map<String, Object> objectMap);

    JSONObject getSingleRow(Map<String, Object> objectMap);

}
