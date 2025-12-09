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

package org.dromara.dante.assistant.oss.definition.domain;

import com.google.common.base.MoreObjects;

/**
 * <p>Description: Checksum 通用属性 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/23 11:24
 */
public class ChecksumDomain implements OssDomain {

    private String checksumCRC32;

    private String checksumCRC32C;

    private String checksumSHA1;

    private String checksumSHA256;

    public String getChecksumCRC32() {
        return checksumCRC32;
    }

    public void setChecksumCRC32(String checksumCRC32) {
        this.checksumCRC32 = checksumCRC32;
    }

    public String getChecksumCRC32C() {
        return checksumCRC32C;
    }

    public void setChecksumCRC32C(String checksumCRC32C) {
        this.checksumCRC32C = checksumCRC32C;
    }

    public String getChecksumSHA1() {
        return checksumSHA1;
    }

    public void setChecksumSHA1(String checksumSHA1) {
        this.checksumSHA1 = checksumSHA1;
    }

    public String getChecksumSHA256() {
        return checksumSHA256;
    }

    public void setChecksumSHA256(String checksumSHA256) {
        this.checksumSHA256 = checksumSHA256;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("checksumCRC32", checksumCRC32)
                .add("checksumCRC32C", checksumCRC32C)
                .add("checksumSHA1", checksumSHA1)
                .add("checksumSHA256", checksumSHA256)
                .toString();
    }
}
