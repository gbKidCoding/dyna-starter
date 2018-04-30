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

package com.ifreelight.dyna.provider.processor;

import com.ifreelight.dyna.AppConstants;
import com.ifreelight.dyna.core.SearchFormProcessor;
import com.ifreelight.dyna.core.SearchResult;
import com.ifreelight.dyna.core.ViewContext;
import com.ifreelight.dyna.service.JDBCService;
import com.mongodb.BasicDBObject;
import org.apache.commons.collections.OrderedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: yuebo
 * Date: 1/6/15
 * Time: 5:03 PM
 */
@Component
public class DefaultSearchFormProcessor implements SearchFormProcessor, AppConstants {
    private static Logger logger = Logger.getLogger(DefaultSearchFormProcessor.class);
    @Autowired
    protected JDBCService jdbcService;

    @Override
    public SearchResult processSearch(ViewContext viewContext, Map filter, OrderedMap sort, int limit, int skip) {
        SearchResult result = new SearchResult();
//        System.out.println(filter);
        //should exclude the params start with $
        Map filterTemp=new HashMap();
        for(Object key:filter.keySet()){
            String k=key.toString();
            if(!k.startsWith($)){
                filterTemp.put(key,filter.get(key));
            }
        }



        String query = viewContext.getQuery();
        if (query != null) {
            List<Map<String, Object>> searchResult = jdbcService.queryForListWithFilter(query, new BasicDBObject(filterTemp), sort, limit, skip);
            int total = jdbcService.queryForCount(query, new BasicDBObject(filterTemp));
            result.setResult(searchResult);
            result.setTotal(total);

        } else {
            List<Map<String, Object>> searchResult = jdbcService.findList(viewContext.getData(), new BasicDBObject(filterTemp), sort, limit, skip);
            int total = jdbcService.count(viewContext.getData(), new BasicDBObject(filterTemp));
            result.setResult(searchResult);
            result.setTotal(total);
        }

        return result;

    }
}
