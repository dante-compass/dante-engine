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

package cn.herodotus.engine.assistant.oss.service.base;

import cn.herodotus.engine.assistant.oss.config.AssistantOssConfiguration;
import cn.herodotus.engine.assistant.oss.pool.AwsConfigurer;
import cn.herodotus.engine.assistant.oss.pool.S3AsyncClientObjectPool;
import cn.herodotus.engine.assistant.oss.properties.OssProperties;
import cn.herodotus.engine.assistant.oss.service.utils.OssTestUtils;
import cn.herodotus.engine.core.foundation.utils.ResourceResolverUtils;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.BlockingInputStreamAsyncRequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.transfer.s3.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

/**
 * <p>Description: TransferManager 测试 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/19 18:23
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class S3TransferManagerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(S3TransferManagerServiceTest.class);

    private S3TransferManagerService s3TransferManagerService;

    @BeforeEach
    public void setup() throws Exception {
        AssistantOssConfiguration configuration = OssTestUtils.getConfiguration();

        OssProperties ossProperties = OssTestUtils.getOssProperties();

        AwsConfigurer awsConfigurer = configuration.awsConfigurer(ossProperties);

        S3AsyncClientObjectPool s3AsyncClientObjectPool = configuration.s3AsyncClientObjectPool(awsConfigurer);
        s3TransferManagerService = new S3TransferManagerService(s3AsyncClientObjectPool);
    }

    @Test
    @Order(1)
    void testUploadFileByPath() throws Exception {

        File file = ResourceResolverUtils.getFile("classpath:oss/test-file.sql");

        UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
                .putObjectRequest(b -> b
                        .bucket(OssTestUtils.BASE_BUCKET)
                        .key(file.getName()))
                .source(file.toPath())
                .build();

        CompletedFileUpload uploadResult = s3TransferManagerService.uploadFile(uploadFileRequest).join();
        String etag = uploadResult.response().eTag();

        System.out.println(etag);

        Assertions.assertNotNull(etag, "上传本地文件至 OSS出错");
    }

    @Test
    @Order(2)
    void testDownloadFileByPath() throws Exception {

        File file = new File(OssTestUtils.BASE_PATH + File.separator + "test-file2.sql");

        DownloadFileRequest downloadFileRequest = DownloadFileRequest.builder()
                .getObjectRequest(b -> b.bucket(OssTestUtils.BASE_BUCKET).key("test-file.sql"))
                .destination(Paths.get(file.getPath()))
                .build();

        CompletedFileDownload downloadResult = s3TransferManagerService.downloadFile(downloadFileRequest).join();
        Long length = downloadResult.response().contentLength();

        System.out.println(length);

        Assertions.assertEquals(length, 11042L, "下载 OSS 至本地出错");
    }

    @Test
    @Order(3)
    void testUploadDirectory() throws Exception {

        File directory = new File(OssTestUtils.BASE_PATH + File.separator + "dtest");

        UploadDirectoryRequest request = UploadDirectoryRequest.builder()
                .source(Paths.get(directory.getPath()))
                .bucket(OssTestUtils.BASE_BUCKET)
                .build();

        CompletedDirectoryUpload completedDirectoryUpload = s3TransferManagerService.uploadDirectory(request).join();
        completedDirectoryUpload.failedTransfers()
                .forEach(fail -> log.warn("Object [{}] failed to transfer by upload", fail.toString()));

        int size = completedDirectoryUpload.failedTransfers().size();

        System.out.println(size);

        Assertions.assertEquals(size, 0, "上传目录至OSS出错");
    }

    @Test
    @Order(4)
    void testDownloadDirectory() throws Exception {

        File directory = new File(OssTestUtils.BASE_PATH + File.separator + "dtest2");

        DownloadDirectoryRequest request = DownloadDirectoryRequest.builder()
                .destination(Paths.get(directory.getPath()))
                .bucket(OssTestUtils.BASE_BUCKET)
                .build();

        CompletedDirectoryDownload completedDirectoryDownload = s3TransferManagerService.downloadDirectory(request).join();
        completedDirectoryDownload.failedTransfers()
                .forEach(fail -> log.warn("Object [{}] failed to transfer by download", fail.toString()));

        int size = completedDirectoryDownload.failedTransfers().size();

        System.out.println(size);

        Assertions.assertEquals(size, 0, "从OSS下载目录至本地出错");
    }

    @Test
    void testUploadFileByStream() throws Exception {
        File file = new File(OssTestUtils.BASE_PATH + File.separator + "dtest/aaa.pdf");

        BlockingInputStreamAsyncRequestBody body =
                AsyncRequestBody.forBlockingInputStream(null); // 'null' indicates a stream will be provided later.

        UploadRequest uploadRequest = UploadRequest.builder()
                .requestBody(body)
                .putObjectRequest(r -> r.bucket(OssTestUtils.BASE_BUCKET).key("test-file.pdf"))
                .build();

        CompletableFuture<CompletedUpload> future = s3TransferManagerService.upload(uploadRequest);

        body.writeInputStream(new FileInputStream(file));

        String etag = future.join().response().eTag();
        System.out.println(etag);
    }

    @Test
    void testDownloadFileByStream() throws Exception {
        File file = new File(OssTestUtils.BASE_PATH + File.separator + "dtest/aaa.pdf");

        DownloadRequest<ResponseInputStream<GetObjectResponse>> request = DownloadRequest.builder()
                .getObjectRequest(b -> b.bucket(OssTestUtils.BASE_BUCKET).key("微服务：从设计到部署中文完整版.pdf"))
                .responseTransformer(AsyncResponseTransformer.toBlockingInputStream())
                .build();

        CompletedDownload<ResponseInputStream<GetObjectResponse>> response = s3TransferManagerService.download(request).join();
        long result = response.result().transferTo(new FileOutputStream(file));
        System.out.println(result);
    }
}
