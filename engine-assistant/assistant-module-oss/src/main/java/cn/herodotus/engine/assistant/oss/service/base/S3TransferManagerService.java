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

import cn.herodotus.engine.assistant.oss.definition.service.AbstractS3TransferManagerService;
import cn.herodotus.engine.assistant.oss.pool.S3AsyncClientObjectPool;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.transfer.s3.model.*;

import java.util.concurrent.CompletableFuture;

/**
 * <p>Description: AWS SDK V2 高阶操作 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/28 18:02
 */
@Service
public class S3TransferManagerService extends AbstractS3TransferManagerService {

    public S3TransferManagerService(S3AsyncClientObjectPool objectPool) {
        super(objectPool);
    }


    public <ResultT> CompletableFuture<CompletedDownload<ResultT>> download(DownloadRequest<ResultT> downloadRequest) {
        Download<ResultT> download = process(s3TransferManager -> s3TransferManager.download(downloadRequest));
        return download.completionFuture();
    }

    public CompletableFuture<CompletedFileDownload> downloadFile(DownloadFileRequest downloadRequest) {
        return fileDownload(s3TransferManager -> s3TransferManager.downloadFile(downloadRequest));
    }

    public CompletableFuture<CompletedFileDownload> resumeDownloadFile(ResumableFileDownload resumableFileDownload) {
        return fileDownload(s3TransferManager -> s3TransferManager.resumeDownloadFile(resumableFileDownload));
    }

    public CompletableFuture<CompletedDirectoryDownload> downloadDirectory(DownloadDirectoryRequest downloadDirectoryRequest) {
        DirectoryDownload directoryDownload = process(s3TransferManager -> s3TransferManager.downloadDirectory(downloadDirectoryRequest));
        return directoryDownload.completionFuture();
    }

    public CompletableFuture<CompletedUpload> upload(UploadRequest uploadRequest) {
        Upload upload = process(s3TransferManager -> s3TransferManager.upload(uploadRequest));
        return upload.completionFuture();
    }

    public CompletableFuture<CompletedFileUpload> uploadFile(UploadFileRequest uploadFileRequest) {
        return fileUpload(s3TransferManager -> s3TransferManager.uploadFile(uploadFileRequest));
    }

    public CompletableFuture<CompletedFileUpload> resumeUploadFile(ResumableFileUpload resumableFileUpload) {
        return fileUpload(s3TransferManager -> s3TransferManager.resumeUploadFile(resumableFileUpload));
    }

    public CompletableFuture<CompletedDirectoryUpload> uploadDirectory(UploadDirectoryRequest uploadDirectoryRequest) {
        DirectoryUpload directoryUpload = process(s3TransferManager -> s3TransferManager.uploadDirectory(uploadDirectoryRequest));
        return directoryUpload.completionFuture();
    }
}
