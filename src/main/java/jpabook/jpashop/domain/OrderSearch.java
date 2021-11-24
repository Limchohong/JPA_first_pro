package jpabook.jpashop.domain;


//동적쿼리를 위한 클래스
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
	
	private String memberName;		 //회원 이름
	
	private OrderStatus orderStatus; //주문 상태[ORDER, CANCEL]
}
