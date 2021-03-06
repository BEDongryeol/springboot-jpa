package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderItem;
import com.jpabook.jpashop.domain.OrderStatus;
import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.repository.OrderSearch;
import com.jpabook.jpashop.repository.order.query.OrderFlatDto;
import com.jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import com.jpabook.jpashop.repository.order.query.OrderQueryDto;
import com.jpabook.jpashop.repository.order.query.OrderQueryRepository;
import com.jpabook.jpashop.service.query.OrderDto;
import com.jpabook.jpashop.service.query.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderQueryService orderQueryService;

     @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
         List<Order> all = orderRepository.findAllByString(new OrderSearch());
         for (Order order : all) {
             order.getMember().getName();
             order.getDelivery().getAddress();
             // Collection과 Item(One) 초기화
             List<OrderItem> orderItems = order.getOrderItems();
             orderItems.stream().forEach(o -> o.getItem().getName());
         }
         return all;
     }

     @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
         List<Order> orders = orderRepository.findAllByString(new OrderSearch());
         List<OrderDto> collect = orders.stream()
                 .map(OrderDto::new)
                 .collect(Collectors.toList());
         return collect;
     }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){
         return orderQueryService.ordersV3();
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit)
    {
        // ToOne Entity 들을 join fetch로 조회한다.
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        // LazyLoading으로 Collection 데이터를 초기화한다.
        // batchsize로 최적화
        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    // JPA로 DTO 바로 조회하기
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDto();
    }

    // DTO 조회 쿼리 최적화
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_Optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6(){
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        return flats.stream()
                .collect(
                        Collectors.groupingBy(
                                o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        Collectors.mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), Collectors.toList())))
                .entrySet()
                .stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(Collectors.toList());
     }

}
