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

package com.ifreelight.dyna.provider.processor.process;

import com.ifreelight.dyna.core.ViewContext;
import com.ifreelight.dyna.provider.processor.DefaultFormProcessor;
import org.activiti.engine.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuebo on 17/11/2017.
 */
@Component
public class ProcessInitFormProcessor extends DefaultFormProcessor {
    @Autowired
    FormService formService;

    @Override
    public Map<String, Object> load(ViewContext viewContext, Map condition) {
        String id = (String) condition.get(DB_FIELD__ID);
        String formKey = formService.getStartFormKey(id);
        HashMap map = new HashMap();
        viewContext.getViewMap().put("view", "redirect:" + formKey);
        return map;
    }
}
