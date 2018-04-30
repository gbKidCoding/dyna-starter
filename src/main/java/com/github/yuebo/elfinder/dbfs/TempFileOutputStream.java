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

package com.github.yuebo.elfinder.dbfs;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by yuebo on 2018/2/12.
 */
public class TempFileOutputStream extends FileOutputStream {
    File file;
    Callable callable;
    public TempFileOutputStream(File file, Callable callable) throws IOException {
        super(file);
        this.file=file;
        this.callable=callable;
    }
    @Override
    public void close() throws IOException {
        try {
            super.close();
            try {
                callable.call();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }finally {
            FileUtils.deleteQuietly(file);
        }

    }


}
