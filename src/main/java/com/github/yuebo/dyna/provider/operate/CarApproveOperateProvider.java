/*
 *
 *  *
 *  *  * Copyright 2002-2017 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 *
 */

package com.github.yuebo.dyna.provider.operate;

import com.github.yuebo.dyna.core.OperateContext;
import com.github.yuebo.dyna.core.ViewContext;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by yuebo on 2017/12/21.
 */
@Component
public class CarApproveOperateProvider extends DefaultOperateProvider {
    @Override
    protected boolean doOperate(ViewContext viewContext, OperateContext operateContext) {
        String id=request.getParameter("_id");

        Map<String,Object> test= jdbcService.find("tbl_car_topup",new BasicDBObject("_id",id).append("status","待审核"));

        if(test!=null){
            jdbcService.update("tbl_car_topup",new BasicDBObject("_id",id),new BasicDBObject("status",getStatus()));
        }

        return super.doOperate(viewContext, operateContext);
    }

    protected Object getStatus() {
        return "审核通过";
    }
}
