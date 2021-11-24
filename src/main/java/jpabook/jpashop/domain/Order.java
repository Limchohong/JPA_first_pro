package jpabook.jpashop.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
//Member테이블과 양방향 관계
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")	//명시하지 않을 경우 클래스 명이 테이블 명이됨 여기에서는 JAP에 order예약어가 있기 때문에 명시함
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)	//다른 곳에서 createOrder가 아닌 new를 사용한 생성자 호출을 막기 위해
public class Order {

	@Id
	@GeneratedValue
	@Column(name = "order_id")
	private Long id;
	
	//Member테이블에 있어서 연관관계의 주인 (대부분 FK를 가지고 있는 쪽이 주인이 됨)
	//연관관계의 주인은 FK를 Update할 수 있다. 
	//mappedby를 가지고 있는 필드와는 다르게 여기에서는 값을 세팅할 경우 member_id가 다른 값으로 변경 됨 
	//member테이블과 order테이블의 관계는 N : 1 이기 때문에 ManyToOne
	@ManyToOne(fetch = FetchType.LAZY)	//XToOne는 LAZY전략을 사용해야함 그렇지 않으면 단건 조회 쿼리문이 여러번 실행되기 때문에 성능이 저하됨						
	@JoinColumn(name = "member_id")		//join(Mapping)할 컬럼명 명시 (FK의 컬럼명이 됨)
	private Member member;
	
	//하나의 Order는 여러개의 OrderItem을 가질 수 있음 여기에서는 Order테이블이 1, OrderItem이 N인 관계
	//연관관계의 주인이 아닌 거울(read only)이기 때문에 mappedBy(해당 객체에 명시한 필드명)을 기입해야함
	//orderItems에 데이터를 넣어두고 order를 저장하면 영속화를 한번헤 함, delete도 한번에 함
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)	
	private List<OrderItem> orderItems = new ArrayList<>();	//주문 상품
	
	
	//1:1Mapping 하나의 주문과 하나의 배송
	//1:1관계에서는 FK를 어디에 둬도 상관이 없으나 대부분 access를 많이 하는 곳에 둠
	//여기에서는 배송을 직접 조회하기보다는 order를 먼저 보고 찾기 떄문에 
	//order에 주로 FK를 둠 그렇기 때문에 order에 있는 delivery가 연관관계의 주인
	//ManyToOne, OneToOne의 기본전략은 EAGER이기 떄문에 LAZY로 바꾸어야 성능이 좋음
	//order를 저장할 때 delivery의 값도 같이 저장함, 원래라면 order, delivery를 각각 persist해야함
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;			//배송
	
	private LocalDateTime orderDate;	//주문시간 (시간과 분까지 다 있음)
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;	 		//주문상태 [ORDER, CANCEL]
	
	//연관관계 편의 메서드 - 위치는 양쪽 중 핵심적으로 컨트롤 하는 곳에 있는게 좋음
	//원자적으로 양쪽에 값을 세팅하는 것
	//양방향 연관관계 세팅시 order와 member가 있으면 member가 주문하면 listOrder에 값을 넣어주어야 하는데
	//값의 저장은 연관관계의 주인이하지만(양방향이라 왔다갔다 할 수 는 있지만) 로직을 탈 때 필요함  
	public void setMember(Member member) {	//양방향 연관관계에 다 걸림
		this.member = member;			//order가 가진 member값 세팅
		member.getOrder().add(this);	//member가 가진 order세팅
	}
	
	public void addOrderItem(OrderItem orderItem) {	//order와 OrderItem도 양방향 연관관계라 해주어야함
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}
	
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
		delivery.setOrder(this);
	}
	
	
	//==생성 메서드==//
	//생성할 때 부터 createOrder를 호출해야함
	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
		
		Order order = new Order();
		
		order.setMember(member);
		order.setDelivery(delivery);
		
		for(OrderItem orderItem : orderItems) {
			order.addOrderItem(orderItem);
		}
		
		order.setStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());
		
		return order;
	}
	
	//==핵심 비즈니스 로직==
	//주문 취소
	public void cancel() {
		
		//배송 상태가 이미 배송완료이면
		if(delivery.getStatus() == DeliveryStatus.COMP) {
			throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다");
		}
		
		this.setStatus(OrderStatus.CANCEL);	//배송상태 변경
		
		for(OrderItem orderItem : orderItems) { //반복을 돌며 상품 재고 원복
			orderItem.cancel();
		}
	}
	
	
	//==조회 로직==//
	//전체 주문 가격 조회
	public int getTotalPrice() {
		
		int totalPrice = 0;
		
		for(OrderItem orderItem : orderItems) {
			totalPrice += orderItem.getTotalPrice();
		}
		return totalPrice;
		
		//stream 또는 람다 이용시 더욱 깔끔하게 할 수 있음
		//Stream이용시 
//		return orderItems.stream()
//			.mapToInt(OrderItem::getTotalPrice)
//			.sum();
	}


}
