package com.jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private EntityManager em;

    /**
     * join fetch는 entity를 조회할 때만 사용할 수 있다.
     * fetch join을 사용하여도 RDB에서는 join 구문을 사용하기 때문에 이상이 없다.
     */

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                        "select new com.jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                                "from Order o " +
                                "join o.member m " +
                                "join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}
