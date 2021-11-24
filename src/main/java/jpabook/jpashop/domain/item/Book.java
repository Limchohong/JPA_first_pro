package jpabook.jpashop.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B")	//싱글테이블이기 때문에 DB에서 저장시 구분할 때 넣는 값
@Getter
@Setter
public class Book extends Item{

	private String author;	//저자
	private String isbn;
}
