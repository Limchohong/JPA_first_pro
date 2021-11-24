package jpabook.jpashop.domain;

import java.util.ArrayList;
//회원 Entity
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "member_id")	//쿼리 컬럼명을 명시하지 않을 경우 필드명으로 생성되기 때문에 컬럼명 명시s
	private Long id;									//id

	private String name;								//이름
	
	@Embedded											//내장타입을 포함하고 있다는 의미의 어노테이션 해당 클래스나, 사용시 필드 둘 중 한 군데에만 명시하면 됨
	private Address address;							//주소를 담은 객체 
	
	//하나의 회원이 여러개의 상품을 주문하기 때문에 1:N 관계 -> OneToMany
	//연관관계의 주인이 아닌 거울(read only)이기 때문에 mappedBy(해당 테이블에 명시한 필드명)을 기입해야함
	//여기에서는 order테이블에 있는 member필드에 의해 맵핑이 된 것, 읽기 전용이기 때문에 값을 넣는다고 해서 order테이블의 FK가 변하지 않음
	@OneToMany(mappedBy = "member")					
	private List<Order> order = new ArrayList<>();		//List형태의 주문 객체
	
}
