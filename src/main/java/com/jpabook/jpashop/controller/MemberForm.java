package com.jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

// 회원가입시 필요한 정보 받는 Form
@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    private String city;
    private String street;
    @Size(message = "우편번호 5자리를 입력해주세요", min = 5, max = 5)
    private String zipcode;
}
