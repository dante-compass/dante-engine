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

package org.dromara.dante.core.domain;

import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.core.utils.TimeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;

/**
 * <p>Description: 文件属性信息 </p>
 *
 * @author : gengwei.zheng
 * @date : 2026/1/31 16:47
 */
public class FileAttributes implements BaseModel {

    private String fileName;
    private String filePath;
    private LocalDateTime lastModifiedTime;
    private LocalDateTime lastAccessTime;
    private LocalDateTime creationTime;
    private boolean isRegularFile;
    private boolean isDirectory;
    private boolean isSymbolicLink;
    private boolean isOther;
    private long size;
    private String bucketName;

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    private void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    private void setLastModifiedTime(LocalDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public LocalDateTime getLastAccessTime() {
        return lastAccessTime;
    }

    private void setLastAccessTime(LocalDateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    private void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isRegularFile() {
        return isRegularFile;
    }

    private void setRegularFile(boolean regularFile) {
        isRegularFile = regularFile;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    private void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public boolean isSymbolicLink() {
        return isSymbolicLink;
    }

    private void setSymbolicLink(boolean symbolicLink) {
        isSymbolicLink = symbolicLink;
    }

    public boolean isOther() {
        return isOther;
    }

    private void setOther(boolean other) {
        isOther = other;
    }

    public long getSize() {
        return size;
    }

    private void setSize(long size) {
        this.size = size;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public static Builder with(Path path) {
        return new Builder(path);
    }

    public static class Builder {

        private final Path path;
        private String bucketName;

        protected Builder(Path path) {
            this.path = path;
        }

        public Builder bucketName(String bucketName) {
            this.bucketName = bucketName;
            return this;
        }

        private LocalDateTime toLocalDateTime(FileTime fileTime) {
            if (ObjectUtils.isNotEmpty(fileTime)) {
                return TimeUtils.toLocalDateTime(fileTime.toInstant());
            }
            return null;
        }

        public FileAttributes build() throws IOException {
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);

            FileAttributes target = new FileAttributes();
            target.setFileName(path.getFileName().toString());
            target.setFilePath(path.toAbsolutePath().toString());
            target.setLastModifiedTime(toLocalDateTime(attributes.lastModifiedTime()));
            target.setLastAccessTime(toLocalDateTime(attributes.lastModifiedTime()));
            target.setCreationTime(toLocalDateTime(attributes.creationTime()));
            target.setSize(attributes.size());
            target.setRegularFile(attributes.isRegularFile());
            target.setDirectory(attributes.isDirectory());
            target.setSymbolicLink(attributes.isSymbolicLink());
            target.setOther(attributes.isOther());
            target.setBucketName(this.bucketName);
            return target;
        }
    }
}
