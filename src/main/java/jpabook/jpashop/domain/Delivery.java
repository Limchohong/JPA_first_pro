package jpabook.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

	@Id
	@GeneratedValue
	@Column(name = "delivery_id") 
	private Long id;
	
	//1:1Mapping 하나의 주문과 하나의 배송
	//1:1관계에서는 FK를 어디에 둬도 상관이 없으나 대부분 access를 많이 하는 곳에 둠
	//여기에서는 배송을 직접 조회하기보다는 order를 먼저 보고 찾기 떄문에 
	//order에 주로 FK를 둠 그렇기 때문에 order에 있는 delivery가 연관관계의 주인
	//ManyToOne, OneToOne의 기본전략은 EAGER이기 떄문에 LAZY로 바꾸어야 성능이 좋음
	@OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
	private Order order;
	
	@Embedded	//Value재활용 내장타입이기 때문에 @Embedded 명시
	private Address address;
	
	//Enum타입 명시
	@Enumerated(EnumType.STRING)	
	//EnumType.STRING : 문자열로 저장
	//EnumType.ORDINAL: 숫자(index?)로 저장 중간에 다른 상태값이 추가되는 경우 망함 웬만하면 쓰지 않음
	private DeliveryStatus status; //READY, COMP
}
