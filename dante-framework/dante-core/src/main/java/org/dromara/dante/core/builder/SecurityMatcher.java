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

package org.dromara.dante.core.builder;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>Description: 静态 Security 权限构建器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/12/13 17:37
 */
public class SecurityMatcher {

    private List<String> staticResources;
    private List<String> permitAllResources;
    private List<String> hasAuthenticatedResources;

    public List<String> getHasAuthenticatedResources() {
        return hasAuthenticatedResources;
    }

    private void setHasAuthenticatedResources(List<String> hasAuthenticatedResources) {
        this.hasAuthenticatedResources = hasAuthenticatedResources;
    }

    public List<String> getPermitAllResources() {
        return permitAllResources;
    }

    private void setPermitAllResources(List<String> permitAllResources) {
        this.permitAllResources = permitAllResources;
    }

    public List<String> getStaticResources() {
        return staticResources;
    }

    private void setStaticResources(List<String> staticResources) {
        this.staticResources = staticResources;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> staticResources;
        private List<String> permitAllResources;
        private List<String> hasAuthenticatedResources;

        protected Builder() {
            this.staticResources = new ArrayList<>();
            this.permitAllResources = new ArrayList<>();
            this.hasAuthenticatedResources = new ArrayList<>();
        }

        public Builder staticResources(List<String> staticResources) {
            this.staticResources = merge(this.staticResources, staticResources);
            return this;
        }

        public Builder permitAll(List<String> permitAllResources) {
            this.permitAllResources = merge(this.permitAllResources, permitAllResources);
            return this;
        }

        public Builder hasAuthenticated(List<String> hasAuthenticatedResources) {
            this.hasAuthenticatedResources = merge(this.hasAuthenticatedResources, hasAuthenticatedResources);
            return this;
        }

        private List<String> merge(List<String> target, List<String> source) {
            if (CollectionUtils.isEmpty(target)) {
                return source;
            }

            if (CollectionUtils.isEmpty(source)) {
                return target;
            }

            Set<String> result = new HashSet<>(target.size() + source.size());
            result.addAll(target);
            result.addAll(source);
            return new ArrayList<>(result);
        }

        public SecurityMatcher build() {
            SecurityMatcher securityMatcher = new SecurityMatcher();
            securityMatcher.setStaticResources(this.staticResources);
            securityMatcher.setPermitAllResources(this.permitAllResources);
            securityMatcher.setHasAuthenticatedResources(this.hasAuthenticatedResources);
            return securityMatcher;
        }
    }
}
