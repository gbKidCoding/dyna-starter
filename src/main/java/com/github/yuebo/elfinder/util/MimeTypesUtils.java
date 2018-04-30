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

package com.github.yuebo.elfinder.util;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public abstract class MimeTypesUtils
{
	private static Map<String, String> _map;

	public static final String UNKNOWN_MIME_TYPE = "application/oct-stream";

	static
	{
		_map = new HashMap<String, String>();
		try
		{
			load();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	public static String getMimeType(String ext)
	{
		return _map.get(ext.toLowerCase());
	}

	public static boolean isUnknownType(String mime)
	{
		return mime == null || UNKNOWN_MIME_TYPE.equals(mime);
	}

	private static void load() throws IOException
	{
		InputStream is = new ClassPathResource("/mime.types").getInputStream();
		BufferedReader fr = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = fr.readLine()) != null)
		{
			line = line.trim();
			if (line.startsWith("#") || line.isEmpty())
			{
				continue;
			}

			String[] tokens = line.split("\\s+");
			if (tokens.length < 2)
				continue;

			for (int i = 1; i < tokens.length; i++)
			{
				putMimeType(tokens[i], tokens[0]);
			}
		}

		is.close();
	}

	public static void putMimeType(String ext, String type)
	{
		if (ext == null || type == null)
			return;

		_map.put(ext.toLowerCase(), type);
	}
}
