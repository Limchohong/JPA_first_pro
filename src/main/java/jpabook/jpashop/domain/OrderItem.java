package jpabook.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)	//다른 곳에서 createOrderItem가 아닌 new를 사용한 생성자 호출을 막기 위해
public class OrderItem {
	
	@Id	
	@GeneratedValue
	@Column(name = "order_item_id")
	private Long id;
	
	//OrderItem입장에서 item은 ManyToOne
	//ManyToOne, OneToOne의 기본전략은 EAGER이기 떄문에 LAZY로 바꾸어야 성능이 좋음
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	//하나의 Order는 여러개의 OrderItem을 가질 수 있음 여기에서는 Order테이블이 1, OrderItem이 N인 관계
	//연관관계의 주인임
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	private int orderPrice;	//주문가격
	
	private int count;		//주문수량

	//==생성 메서드==//
	public static OrderItem createOrderItem(Item item, int OrderPrice, int count) {
		//OrderItem을 생성하면서 재고 수량 감소 시킴
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setOrderPrice(OrderPrice);
		orderItem.setCount(count);
		
		item.removeStock(count);
		return orderItem;
	}

	//==비즈니스 로직==//
	//주문 취소(재고 수량을 원상복귀해줌)
	public void cancel() {
		getItem().addStock(count);
	}
	
	//==조회 로직==//
	//주문상품 전체 가격 조회
	public int getTotalPrice() {
		return getOrderPrice() * getCount();
	}
}
