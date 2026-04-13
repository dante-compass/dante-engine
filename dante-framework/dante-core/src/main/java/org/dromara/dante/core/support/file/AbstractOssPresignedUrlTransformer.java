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

package org.dromara.dante.core.support.file;

import com.google.common.collect.Range;
import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

/**
 * <p>Description: 基于对象存储预签名 URL 实现文件传输抽象定义 </p>
 * <p>
 * 基于 OSS 实现文件在服务之间传输。A 服务将文件传输到 OSS 服务，B 服务从 OSS 服务中下载使用。
 * 使用 OSS 的预签名 URL 直接上传文件到 OSS 服务，可以避免服务间的资源消耗，降低复杂度。预签名地址安全性较弱，在服务间使用忽略这个安全性问题。
 * <p>
 * 单体版本：只要依赖 <code>oss-spring-boot-starter</code>，使用 <code>LocalOssTransformer</code> 直接上传
 * 微服务版：在主工程 <code>rpc-client-oss-spring-boot-starter</code> 模块中，根据注入的、不同协议的 {@link OssPresignedUrlGenerator} 来实现服务间文件的传输
 *
 * @author : gengwei.zheng
 * @date : 2026/1/5 0:25
 */
public abstract class AbstractOssPresignedUrlTransformer implements OssTransformer {

    private static final Logger log = LoggerFactory.getLogger(AbstractOssPresignedUrlTransformer.class);

    private final OssPresignedUrlGenerator ossPresignedUrlGenerator;

    protected AbstractOssPresignedUrlTransformer(OssPresignedUrlGenerator ossPresignedUrlGenerator) {
        this.ossPresignedUrlGenerator = ossPresignedUrlGenerator;
    }

    @Override
    public boolean upload(String bucketName, Path path) {

        String url = ossPresignedUrlGenerator.generateUploadUrl(bucketName, path.getFileName().toString());

        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .PUT(HttpRequest.BodyPublishers.ofFile(path))
                    .build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

            return response.statusCode() == HttpStatus.SC_OK;
        } catch (FileNotFoundException e) {
            log.error("[Herodotus] |- File [{}] not found on upload.", path, e);
        } catch (IOException e) {
            log.error("[Herodotus] |- File [{}] upload catch io error.", path, e);
        } catch (InterruptedException e) {
            log.error("[Herodotus] |- File [{}] upload catch interrupted error.", path, e);
        }

        return false;
    }

    @Override
    public boolean download(String bucketName, Path path) {

        String url = ossPresignedUrlGenerator.generateDownloadUrl(bucketName, path.getFileName().toString());

        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(path));

            return response.statusCode() == HttpStatus.SC_OK;
        } catch (IOException e) {
            log.error("[Herodotus] |- File [{}] download catch io error.", path, e);
        } catch (InterruptedException e) {
            log.error("[Herodotus] |- File [{}] download catch interrupted error.", path, e);
        }

        return false;
    }

    @Override
    public boolean remove(String bucketName, String fileName) {

        String url = ossPresignedUrlGenerator.generateDeleteUrl(bucketName, fileName);

        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

            Range<Integer> range = Range.closed(HttpStatus.SC_OK, HttpStatus.SC_MULTI_STATUS);
            return range.contains(response.statusCode());
        } catch (IOException e) {
            log.error("[Herodotus] |- File delete catch io error.", e);
        } catch (InterruptedException e) {
            log.error("[Herodotus] |- File delete catch interrupted error.", e);
        }

        return false;
    }
}
