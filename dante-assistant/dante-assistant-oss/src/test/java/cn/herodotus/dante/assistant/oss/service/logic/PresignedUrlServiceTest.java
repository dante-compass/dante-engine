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

package cn.herodotus.dante.assistant.oss.service.logic;

import cn.herodotus.dante.assistant.oss.config.AssistantOssConfiguration;
import cn.herodotus.dante.assistant.oss.pool.AwsConfigurer;
import cn.herodotus.dante.assistant.oss.pool.S3PresignerObjectPool;
import cn.herodotus.dante.assistant.oss.properties.OssProperties;
import cn.herodotus.dante.assistant.oss.service.utils.OssTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

/**
 * <p>Description: 预签名 URL 服务测试 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/23 21:50
 */
public class PresignedUrlServiceTest {

    private final static String BASE_BUCKET = "herodotus-cloud";

    private PresignedUrlService presignedUrlService;

    @BeforeEach
    public void setup() throws Exception {
        AssistantOssConfiguration configuration = OssTestUtils.getConfiguration();

        OssProperties ossProperties = OssTestUtils.getOssProperties();

        AwsConfigurer awsConfigurer = configuration.awsConfigurer(ossProperties);

        S3PresignerObjectPool s3PresignerObjectPool = configuration.s3PresignerObjectPool(awsConfigurer);
        presignedUrlService = new PresignedUrlService(s3PresignerObjectPool, ossProperties);
    }

    @Test
    @Order(1)
    void testUpload() throws Exception {

        String url = presignedUrlService.createUploadPresignedUrl(OssTestUtils.BASE_BUCKET, "test-file.sql");

        Assertions.assertNotNull(url, "创建预签名上传出错");
    }
}
