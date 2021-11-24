package jpabook.jpashop.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")	//싱글테이블이기 때문에 DB에서 저장시 구분할 때 넣는 값
@Getter
@Setter
public class Album extends Item{

	private String artist;	//가수
	private String atc;		//기타정보
}
