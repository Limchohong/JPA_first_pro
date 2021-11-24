package jpabook.jpashop.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("M")	//싱글테이블이기 때문에 DB에서 저장시 구분할 때 넣는 값
@Getter
@Setter
public class Movie extends Item{

	private String director;	//감독
	private String actor;		//배우
 }
