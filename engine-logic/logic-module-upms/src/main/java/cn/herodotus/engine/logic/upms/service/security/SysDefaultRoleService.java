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

package cn.herodotus.engine.logic.upms.service.security;

import cn.herodotus.engine.core.identity.enums.AccountCategory;
import cn.herodotus.engine.data.core.repository.BaseRepository;
import cn.herodotus.engine.data.core.service.BaseService;
import cn.herodotus.engine.logic.upms.entity.security.SysDefaultRole;
import cn.herodotus.engine.logic.upms.entity.security.SysRole;
import cn.herodotus.engine.logic.upms.repository.security.SysDefaultRoleRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 系统默认角色Service </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/7/21 16:09
 */
@Service
public class SysDefaultRoleService extends BaseService<SysDefaultRole, String> {

    private final SysDefaultRoleRepository sysDefaultRoleRepository;
    private final SysRoleService sysRoleService;

    public SysDefaultRoleService(SysDefaultRoleRepository sysDefaultRoleRepository, SysRoleService sysRoleService) {
        this.sysDefaultRoleRepository = sysDefaultRoleRepository;
        this.sysRoleService = sysRoleService;
    }

    @Override
    public BaseRepository<SysDefaultRole, String> getRepository() {
        return this.sysDefaultRoleRepository;
    }

    public SysDefaultRole findByScene(AccountCategory scene) {
        return this.sysDefaultRoleRepository.findByScene(scene);
    }


    public SysDefaultRole assign(String defaultId, String roleId) {
        SysRole sysRole = sysRoleService.findByRoleId(roleId);
        SysDefaultRole sysDefaultRole = sysDefaultRoleRepository.findByDefaultId(defaultId);
        if (ObjectUtils.isNotEmpty(sysDefaultRole) && ObjectUtils.isNotEmpty(sysRole)) {
            sysDefaultRole.setRole(sysRole);
            return sysDefaultRoleRepository.saveAndFlush(sysDefaultRole);
        }

        return null;
    }
}
