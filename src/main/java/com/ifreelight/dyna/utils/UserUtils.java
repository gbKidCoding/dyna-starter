/*
 *
 *  * Copyright 2002-2017 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.ifreelight.dyna.utils;

import com.ifreelight.dyna.AppConstants;
import com.ifreelight.dyna.service.JDBCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuebo on 20/11/2017.
 */
@Component
public class UserUtils implements AppConstants{
    @Autowired
    HttpServletRequest request;
    @Autowired
    JDBCService jdbcService;

    public Map currentUser() {
        Map user = (Map) request.getSession().getAttribute("user");
        if (user == null) {
            return new HashMap();
        }
        return user;
    }

    public List<String> currentRoles() {
        ArrayList<String> arrayList = new ArrayList();
        Map user = (Map) request.getSession().getAttribute("user");
        if (user != null) {
            Map map = new HashMap();
            map.put("userid", user.get("_id"));
            List<Map<String, Object>> result = jdbcService.queryForListWithFilter("select r.*,ur.userid from "+TBL_ROLE+" r join "+TBL_USER_ROLE+" ur on r._id=ur.roleid", map, null, 0, 0);
            for (Map role : result) {
                arrayList.add(String.valueOf(role.get("rolename")));
            }
        }
        return arrayList;
    }
}
