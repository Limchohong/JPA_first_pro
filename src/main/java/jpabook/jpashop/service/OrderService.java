package jpabook.jpashop.service;



import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;

	//주문
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		
		//엔티티 조회
		Member member = memberRepository.findOne(memberId);
		Item item = itemRepository.findOne(itemId);
		
		//배송정보 생성
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());
		
		//주문상품 생성
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		
		//주문 생성
		Order order = Order.createOrder(member,delivery,orderItem);
		
		//주문 저장
		//배송과, 주문 상품은 따로 save해야 하지만 
		//order클래스에서 cascade = CascadeType.ALL 로 인해 orderItem과 Delivery는 자동으로 persist됨
		//cascade = CascadeType.AL은 주인이 privateOner일 경우에만 사용함 (다른 곳에서 참조하는 게 없을 떄)
		//delivery가 중요해서 다른 곳에서도 참조해 사용한다면 사용하면 안됨(잘못되면 order지울 때 다 지워지고 복잡하게 돌아감)
		orderRepository.save(order);
		
		return order.getId();
	}
	
	//주문 취소
	@Transactional
	public void cancleOrder(Long OrderId) {
		
		//주문 엔티티 조회
		Order order = orderRepository.findOne(OrderId);
		
		//주문 취소
		order.cancel();
	} 
	
	//주문 검색
	public List<Order> findOrders(OrderSearch orderSearch){
		return orderRepository.findAllByString(orderSearch);
	}
	
}
