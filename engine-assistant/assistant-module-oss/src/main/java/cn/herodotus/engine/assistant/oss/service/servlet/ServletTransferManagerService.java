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

package cn.herodotus.engine.assistant.oss.service.servlet;

import cn.herodotus.engine.assistant.oss.converter.argument.*;
import cn.herodotus.engine.assistant.oss.converter.result.CompletedDirectoryUploadToResultConverter;
import cn.herodotus.engine.assistant.oss.converter.result.CompletedFileDownloadToResultConverter;
import cn.herodotus.engine.assistant.oss.converter.result.CompletedFileUploadToResultConverter;
import cn.herodotus.engine.assistant.oss.converter.result.CompletedUploadToResultConverter;
import cn.herodotus.engine.assistant.oss.definition.service.AbstractServletService;
import cn.herodotus.engine.assistant.oss.entity.argument.*;
import cn.herodotus.engine.assistant.oss.entity.result.FailedFileUploadResult;
import cn.herodotus.engine.assistant.oss.entity.result.GetObjectResult;
import cn.herodotus.engine.assistant.oss.entity.result.PutObjectResult;
import cn.herodotus.engine.assistant.oss.service.base.S3TransferManagerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>Description: AWS SDK V2 高阶操作阻塞式 Service </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/29 20:54
 */
@Service
public class ServletTransferManagerService extends AbstractServletService {

    private final S3TransferManagerService transferManagerService;

    public ServletTransferManagerService(S3TransferManagerService transferManagerService) {
        this.transferManagerService = transferManagerService;
    }

    public GetObjectResult downloadFile(DownloadFileArgument argument) {
        return process(argument, new ArgumentToDownloadFileRequestConverter(), new CompletedFileDownloadToResultConverter(), transferManagerService::downloadFile);
    }

    public GetObjectResult resumeDownloadFile(ResumableFileDownloadArgument argument) {
        return process(argument, new ArgumentToResumableFileDownloadConverter(), new CompletedFileDownloadToResultConverter(), transferManagerService::resumeDownloadFile);
    }

    public PutObjectResult uploadFile(UploadFileArgument argument) {
        return process(argument, new ArgumentToUploadFileRequestConverter(), new CompletedFileUploadToResultConverter(), transferManagerService::uploadFile);
    }

    public PutObjectResult resumeUploadFile(ResumableFileUploadArgument argument) {
        return process(argument, new ArgumentToResumableFileUploadConverter(), new CompletedFileUploadToResultConverter(), transferManagerService::resumeUploadFile);
    }

    public PutObjectResult upload(UploadArgument argument) {
        return process(argument, new ArgumentToUploadRequestConverter(), new CompletedUploadToResultConverter(), transferManagerService::upload);
    }

    public List<FailedFileUploadResult> uploadDirectory(UploadDirectoryArgument argument) {
        return process(argument, new ArgumentToUploadDirectoryRequestConverter(), new CompletedDirectoryUploadToResultConverter(), transferManagerService::uploadDirectory);
    }
}
