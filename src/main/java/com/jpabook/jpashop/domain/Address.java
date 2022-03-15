package com.jpabook.jpashop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
public class Address {

    private String city;
    private String street;
    private String zipcode;
    /*
        JPA 스펙상 생성해 놓은 기본 생성자
            - reflection 등을 위해 생성
            - 사용을 지양하기 위해 protected 로 구성
        Immutable 을 위해서 Setter 는 두지 않는다.
     */
    protected Address(){

    }

}
