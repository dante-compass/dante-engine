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

package org.dromara.dante.logic.upms.service.security;

import jakarta.persistence.criteria.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.data.jpa.repository.BaseJpaRepository;
import org.dromara.dante.data.jpa.service.AbstractJpaService;
import org.dromara.dante.logic.upms.entity.security.SysDictionary;
import org.dromara.dante.logic.upms.repository.security.SysDictionaryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Description: SysDictionaryService </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/8/23 21:45
 */
@Service
public class SysDictionaryService extends AbstractJpaService<SysDictionary, String> {

    private final SysDictionaryRepository sysDictionaryRepository;

    public SysDictionaryService(SysDictionaryRepository sysDictionaryRepository) {
        this.sysDictionaryRepository = sysDictionaryRepository;
    }

    @Override
    public BaseJpaRepository<SysDictionary, String> getRepository() {
        return sysDictionaryRepository;
    }

    public List<SysDictionary> findAllByCategory(String category) {
        return sysDictionaryRepository.findAllByCategory(category);
    }

    public Page<SysDictionary> findByCondition(int pageNumber, int pageSize, String category, String name, String label) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Specification<SysDictionary> specification = (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (ObjectUtils.isNotEmpty(category)) {
                predicates.add(criteriaBuilder.like(root.get("category"), like(category)));
            }

            if (ObjectUtils.isNotEmpty(name)) {
                predicates.add(criteriaBuilder.like(root.get("name"), like(name)));
            }

            if (ObjectUtils.isNotEmpty(label)) {
                predicates.add(criteriaBuilder.like(root.get("label"), like(label)));
            }

            Predicate[] predicateArray = new Predicate[predicates.size()];
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(predicateArray)));
            return criteriaQuery.getRestriction();
        };

        return this.findByPage(specification, pageable);
    }

    public Map<String, List<SysDictionary>> findByCategories(Set<String> categories) {
        if (CollectionUtils.isNotEmpty(categories)) {
            List<SysDictionary> items = sysDictionaryRepository.findByCategoryIn(categories);
            if (CollectionUtils.isNotEmpty(items)) {
                return items.stream().collect(Collectors.groupingBy(SysDictionary::getCategory));
            }
        }

        return Map.of();
    }
}
