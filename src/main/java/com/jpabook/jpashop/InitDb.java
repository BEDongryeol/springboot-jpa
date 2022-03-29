package com.jpabook.jpashop;

import com.jpabook.jpashop.domain.*;
import com.jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/*
 * DB 초기화
 * * userA
 *   * JPA1 BOOK
 *   * JPA2 BOOK
 * * userB
 *   * Spring1 BOOK
 *   * Spring2 BOOK
 */

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager entityManager;

        public void dbInit1() {
            Member member = createMember("userA", new Address("서울시", "백제고분로", "11111"));
            entityManager.persist(member);

            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            entityManager.persist(book1);

            Book book2 = createBook("JPA1 BOOK", 20000, 100);
            entityManager.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            entityManager.persist(order);

        }

        public void dbInit2() {
            Member member = createMember("userB", new Address("순천시", "도장안길", "22222"));
            entityManager.persist(member);

            Book book1 = createBook("SPRING1 BOOK", 20000, 200);
            entityManager.persist(book1);

            Book book2 = createBook("SPRING2 BOOK", 40000, 300);
            entityManager.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            entityManager.persist(order);

        }

        private Member createMember(String userName, Address address) {
            Member member = new Member();
            member.setName(userName);
            member.setAddress(address);
            return member;
        }

        private Book createBook(String bookName, int price, int stockQuantity) {
            Book book2 = new Book();
            book2.setName(bookName);
            book2.setPrice(price);
            book2.setStockQuantity(stockQuantity);
            return book2;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }

}