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

package cn.herodotus.dante.logic.message.service;

import cn.herodotus.dante.data.jpa.repository.BaseJpaRepository;
import cn.herodotus.dante.data.jpa.service.AbstractJpaService;
import cn.herodotus.dante.logic.message.entity.Dialogue;
import cn.herodotus.dante.logic.message.entity.DialogueContact;
import cn.herodotus.dante.logic.message.entity.DialogueDetail;
import cn.herodotus.dante.logic.message.repository.DialogueContactRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: PersonalContactService </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/12/7 22:09
 */
@Service
public class DialogueContactService extends AbstractJpaService<DialogueContact, String> {

    private final DialogueContactRepository dialogueContactRepository;

    public DialogueContactService(DialogueContactRepository dialogueContactRepository) {
        this.dialogueContactRepository = dialogueContactRepository;
    }

    @Override
    public BaseJpaRepository<DialogueContact, String> getRepository() {
        return dialogueContactRepository;
    }

    public List<DialogueContact> createContact(Dialogue dialogue, DialogueDetail dialogueDetail) {
        DialogueContact contact = new DialogueContact();
        contact.setDialogue(dialogue);
        contact.setReceiverId(dialogueDetail.getReceiverId());
        contact.setSenderId(dialogueDetail.getSenderId());
        contact.setSenderName(dialogueDetail.getSenderName());
        contact.setSenderAvatar(dialogueDetail.getSenderAvatar());

        DialogueContact reverseContext = new DialogueContact();
        reverseContext.setDialogue(dialogue);
        reverseContext.setReceiverId(dialogueDetail.getSenderId());
        reverseContext.setSenderId(dialogueDetail.getReceiverId());
        reverseContext.setSenderName(dialogueDetail.getReceiverName());
        reverseContext.setSenderAvatar(dialogueDetail.getReceiverAvatar());

        List<DialogueContact> personalContacts = new ArrayList<>();
        personalContacts.add(contact);
        personalContacts.add(reverseContext);

        return this.saveAll(personalContacts);
    }

    public Page<DialogueContact> findByCondition(int pageNumber, int pageSize, String receiverId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Specification<DialogueContact> specification = (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("receiverId"), receiverId));

            Predicate[] predicateArray = new Predicate[predicates.size()];
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(predicateArray)));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createTime")));
            return criteriaQuery.getRestriction();
        };

        return this.findByPage(specification, pageable);
    }

    public void deleteByDialogueId(String dialogueId) {
        dialogueContactRepository.deleteAllByDialogueId(dialogueId);
    }

    public DialogueContact findBySenderIdAndReceiverId(String senderId, String receiverId) {
        return dialogueContactRepository.findBySenderIdAndReceiverId(senderId, receiverId).orElse(null);
    }
}
