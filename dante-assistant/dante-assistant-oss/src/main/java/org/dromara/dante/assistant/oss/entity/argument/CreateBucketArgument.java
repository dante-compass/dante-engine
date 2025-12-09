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

package org.dromara.dante.assistant.oss.entity.argument;

import org.dromara.dante.assistant.oss.definition.argument.AbstractBucketArgument;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>Description: 创建存储桶请求参数实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/21 22:27
 */
@Schema(name = "创建存储桶请求参数实体", title = "创建存储桶请求参数实体")
public class CreateBucketArgument extends AbstractBucketArgument {

    private String acl;

    private String grantFullControl;

    private String grantRead;

    private String grantReadACP;

    private String grantWrite;

    private String grantWriteACP;

    private Boolean objectLockEnabledForBucket;

    private String objectOwnership;

    public String getAcl() {
        return acl;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }

    public String getGrantFullControl() {
        return grantFullControl;
    }

    public void setGrantFullControl(String grantFullControl) {
        this.grantFullControl = grantFullControl;
    }

    public String getGrantRead() {
        return grantRead;
    }

    public void setGrantRead(String grantRead) {
        this.grantRead = grantRead;
    }

    public String getGrantReadACP() {
        return grantReadACP;
    }

    public void setGrantReadACP(String grantReadACP) {
        this.grantReadACP = grantReadACP;
    }

    public String getGrantWrite() {
        return grantWrite;
    }

    public void setGrantWrite(String grantWrite) {
        this.grantWrite = grantWrite;
    }

    public String getGrantWriteACP() {
        return grantWriteACP;
    }

    public void setGrantWriteACP(String grantWriteACP) {
        this.grantWriteACP = grantWriteACP;
    }

    public Boolean getObjectLockEnabledForBucket() {
        return objectLockEnabledForBucket;
    }

    public void setObjectLockEnabledForBucket(Boolean objectLockEnabledForBucket) {
        this.objectLockEnabledForBucket = objectLockEnabledForBucket;
    }

    public String getObjectOwnership() {
        return objectOwnership;
    }

    public void setObjectOwnership(String objectOwnership) {
        this.objectOwnership = objectOwnership;
    }
}
