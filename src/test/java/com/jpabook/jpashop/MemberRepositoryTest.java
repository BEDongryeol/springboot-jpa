package com.jpabook.jpashop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @DisplayName("Member 저장 테스트")
    @Test
    @Transactional
    @Rollback(false)
    void testMember() throws Exception {
        // given
        Member member = new Member();
        member.setUserName("memberA");

        // when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        // then
        Assertions.assertAll(
                () -> assertEquals(findMember.getId(), member.getId()),
                () -> assertEquals(findMember.getUserName(), member.getUserName())
        );
    }
}