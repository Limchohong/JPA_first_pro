package jpabook.jpashop.domain.item;

import java.util.ArrayList;
import java.util.List;

//구현체를 가져야 하기 떄문에 추상클래스로 함
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

@Entity
//SingleTable 전략 사용
//상속관계의 Mapping이기 때문에 상속관계 전략을 지정해야 하는데 부모 클래스에 잡아줘야함
//InheritanceType.JOINED 	   : 정규화된 스타일
//InheritanceType.SINGLE_TABLE : 하나의 테이블에 다 떄려 박는 것
//여기에서는 Album, Book, Movie가 상속받음
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

	@Id	
	@GeneratedValue
	@Column(name = "item_id")
	private Long id;

	//상속을 하기 위해 Mapping를 함 - 공통속성임
	private String name;		//상품이름
	private int price;			//상품가격
	private int stockQuantity;	//재고수량
	
	//카테고리와 List의 N:N관계를 만들기 위한것 (양방향)?
	//카테고리도 List로 Item을 가지고 Item도 List로 카테고리를 가짐
	//실무에서 ManyToMany는 사용하지 않는게 좋음 
	@ManyToMany(mappedBy = "items")
	private List<Category> categories = new ArrayList<>();
	
	//도메인 주도 설계시 비즈니스로직은 데이터를 가지고 있는 쪽에 있어야 응집력이 있고 객체지향형임
	//setter를 가지고 밖에서 계산하여 넣는 것이 아닌, 안에 필요한 것이 있는게 가장 객체지향?,,
	//==비즈니스 로직== 
	
	//재고수량 증가 
	public void addStock(int quantity) {
		this.stockQuantity += quantity; 
	}
	
	//재고수량 감소
	public void removeStock(int quantity) {
		
		int restStock = this.stockQuantity - quantity; 
		
		if(restStock < 0) {	//재고 수량이 0보다 작을 경우 예외처리
			throw new NotEnoughStockException("need more stock");	//사용자 정의 exception
		}
		
		this.stockQuantity = restStock;
	}

}
