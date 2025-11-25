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

import cn.herodotus.engine.core.definition.domain.Dictionary;
import cn.herodotus.engine.core.foundation.founction.ListConverter;
import cn.herodotus.engine.data.core.jpa.repository.BaseJpaRepository;
import cn.herodotus.engine.data.core.jpa.service.AbstractJpaService;
import cn.herodotus.engine.logic.upms.converter.DictionaryToSysEnumConverter;
import cn.herodotus.engine.logic.upms.entity.security.SysDictionary;
import cn.herodotus.engine.logic.upms.entity.security.SysEnum;
import cn.herodotus.engine.logic.upms.repository.security.SysEnumRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>Description: SysEnumService </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/8/23 21:40
 */
@Service
public class SysEnumService extends AbstractJpaService<SysEnum, String> {

    private final SysEnumRepository sysEnumRepository;
    private final ListConverter<Dictionary, SysEnum> toSysEnums;

    public SysEnumService(SysEnumRepository sysEnumRepository) {
        this.sysEnumRepository = sysEnumRepository;
        this.toSysEnums = new DictionaryToSysEnumConverter();
    }

    @Override
    public BaseJpaRepository<SysEnum, String> getRepository() {
        return sysEnumRepository;
    }

    /**
     * 查找SysSecurityAttribute中不存在的SysAuthority
     *
     * @return SysAuthority列表
     */
    public List<SysEnum> findAllocatable() {

        // exist sql 结构示例： SELECT * FROM article WHERE EXISTS (SELECT * FROM user WHERE article.uid = user.uid)
        Specification<SysEnum> specification = (root, criteriaQuery, criteriaBuilder) -> {

            // 构造Not Exist子查询
            Subquery<SysDictionary> subQuery = criteriaQuery.subquery(SysDictionary.class);
            Root<SysDictionary> subRoot = subQuery.from(SysDictionary.class);

            // 构造Not Exist 子查询的where条件
            Predicate subPredicate = criteriaBuilder.equal(subRoot.get("dictionaryId"), root.get("enumId"));
            subQuery.where(subPredicate);

            // 构造完整的子查询语句
            //这句话不加会报错，因为他不知道你子查询要查出什么字段。就是上面示例中的子查询中的“select *”的作用
            subQuery.select(subRoot.get("dictionaryId"));

            // 构造完整SQL
            // 正确的结构参考：SELECT * FROM sys_authority sa WHERE NOT EXISTS ( SELECT * FROM sys_metadata sm WHERE sm.metadata_id = sa.authority_id )
            criteriaQuery.where(criteriaBuilder.not(criteriaBuilder.exists(subQuery)));
            return criteriaQuery.getRestriction();
        };

        return this.findAll(specification);
    }

    public List<SysEnum> storeDictionaries(List<Dictionary> dictionaries) {
        List<SysEnum> sysAuthorities = toSysEnums.convert(dictionaries);
        return saveAllAndFlush(sysAuthorities);
    }
}
