package com.doer.mraims.loanprocess.auth.repository.impl;

import com.doer.mraims.loanprocess.auth.model.UserInfo;
import com.doer.mraims.loanprocess.auth.repository.UserInfoRepository;
import com.doer.mraims.loanprocess.core.utils.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserInfoRepositoryImpl implements UserInfoRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserInfo findUserInfo(String schemaName, String instituteOid, String userId) {

            UserInfo data = new UserInfo();

            String sql = "SELECT l.user_id, l.institute_oid " +
                    " FROM " + schemaName + Table.SEC_LOGIN + " l " +
                    " WHERE m.user_id = '" + userId + "' AND m.institute_oid = '" + instituteOid + "'";
            try {
                log.debug("findByMemberId SQL: {}", sql);
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
                for (Map<String, Object> row : rows) {
                    data.setUserId((String) row.get("user_id"));
                    data.setInstituteOid((String) row.get("institution_oid"));

                    break;
                }
                log.debug("findByMemberId data: {}", data);
            } catch (Exception e) {
                log.error("An Exception has been occurred while executing findByMemberId SQL: {}, Message: {}", sql, e.getMessage(), e);
            }
            return data;
        }
}
