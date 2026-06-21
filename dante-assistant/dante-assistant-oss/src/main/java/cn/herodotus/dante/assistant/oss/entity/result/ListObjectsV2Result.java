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

package cn.herodotus.dante.assistant.oss.entity.result;

import cn.herodotus.dante.assistant.oss.definition.result.AbstractObjectListResult;
import cn.herodotus.dante.assistant.oss.entity.domain.ObjectDomain;
import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * <p>Description: 对象列表响应结果对象实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/23 23:48
 */
public class ListObjectsV2Result extends AbstractObjectListResult {

    private List<ObjectDomain> contents;

    private Integer keyCount;

    private String continuationToken;

    private String nextContinuationToken;

    private String startAfter;

    public List<ObjectDomain> getContents() {
        return contents;
    }

    public void setContents(List<ObjectDomain> contents) {
        this.contents = contents;
    }

    public Integer getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(Integer keyCount) {
        this.keyCount = keyCount;
    }

    public String getContinuationToken() {
        return continuationToken;
    }

    public void setContinuationToken(String continuationToken) {
        this.continuationToken = continuationToken;
    }

    public String getNextContinuationToken() {
        return nextContinuationToken;
    }

    public void setNextContinuationToken(String nextContinuationToken) {
        this.nextContinuationToken = nextContinuationToken;
    }

    public String getStartAfter() {
        return startAfter;
    }

    public void setStartAfter(String startAfter) {
        this.startAfter = startAfter;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("contents", contents)
                .add("keyCount", keyCount)
                .add("continuationToken", continuationToken)
                .add("nextContinuationToken", nextContinuationToken)
                .add("startAfter", startAfter)
                .addValue(super.toString())
                .toString();
    }
}
