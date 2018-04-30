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

package com.ifreelight.elfinder.controller.executors;

import com.ifreelight.elfinder.controller.executor.AbstractJsonCommandExecutor;
import com.ifreelight.elfinder.controller.executor.CommandExecutor;
import com.ifreelight.elfinder.controller.executor.FsItemEx;
import com.ifreelight.elfinder.service.FsService;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;

public class PutCommandExecutor extends AbstractJsonCommandExecutor implements CommandExecutor
{
	@Override
	public void execute(FsService fsService, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception
	{
		String target = request.getParameter("target");

		FsItemEx fsi = super.findItem(fsService, target);
		OutputStream os = fsi.openOutputStream();
		IOUtils.write(request.getParameter("content"), os, "utf-8");
		os.close();
		json.put("changed", new Object[] { super.getFsItemInfo(request, fsi) });
	}
}
