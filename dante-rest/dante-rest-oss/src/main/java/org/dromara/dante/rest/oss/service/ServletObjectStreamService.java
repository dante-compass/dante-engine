/*
 * Copyright 2020-2030 码匠君<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 是 Dante Cloud 系统核心组件库，采用 APACHE LICENSE 2.0 开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1. 请不要删除和修改根目录下的LICENSE文件。
 * 2. 请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3. 请保留源码和相关描述文件的项目出处，作者声明等。
 * 4. 分发源码时候，请注明软件出处 <https://gitee.com/dromara/dante-cloud>
 * 5. 在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/dromara/dante-cloud>
 * 6. 若您的项目无法满足以上几点，可申请商业授权
 */

package org.dromara.dante.rest.oss.service;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.dromara.dante.assistant.oss.entity.argument.GetObjectArgument;
import org.dromara.dante.assistant.oss.entity.domain.PutObjectDomain;
import org.dromara.dante.assistant.oss.entity.result.GetObjectResult;
import org.dromara.dante.assistant.oss.service.servlet.ServletObjectService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * <p>Description: 阻塞式环境文件流式下载 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/1/22 12:21
 */
@Service
public class ServletObjectStreamService {

    private final ServletObjectService objectService;

    public ServletObjectStreamService(ServletObjectService objectService) {
        this.objectService = objectService;
    }

    /**
     * 流式文件下载
     *
     * @param argument 请求参数 {@link GetObjectArgument}
     * @param isOnline true 在线显示，false 直接下载
     * @param response {@link HttpServletResponse}
     * @throws IOException 输入输出错误
     */
    private void stream(GetObjectArgument argument, boolean isOnline, HttpServletResponse response) throws IOException {

        String type = isOnline ? "inline" : "attachment";

        GetObjectResult result = objectService.getObject(argument);
        PutObjectDomain domain = result.getMetadata();

        response.setContentType(domain.getContentType());
        response.setCharacterEncoding(domain.getContentEncoding());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, type + ";filename=" + URLEncoder.encode(argument.getObjectName(), StandardCharsets.UTF_8));

        InputStream is = result.getInputStream();
        IOUtils.copy(is, response.getOutputStream());
        IOUtils.closeQuietly(is);
    }

    /**
     * 以流的方式返回响应内容，前端可直接下载
     *
     * @param argument 请求参数 {@link GetObjectArgument}
     * @param response {@link HttpServletResponse}
     * @throws IOException 输入输出错误
     */
    public void download(GetObjectArgument argument, HttpServletResponse response) throws IOException {
        stream(argument, false, response);
    }

    /**
     * 以流的方式返回响应内容，前端可直接展示
     *
     * @param argument 请求参数 {@link GetObjectArgument}
     * @param response {@link HttpServletResponse}
     * @throws IOException 输入输出错误
     */
    public void display(GetObjectArgument argument, HttpServletResponse response) throws IOException {
        stream(argument, true, response);
    }
}
