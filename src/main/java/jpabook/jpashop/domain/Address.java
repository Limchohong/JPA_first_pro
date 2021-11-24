package jpabook.jpashop.domain;

import javax.persistence.Embeddable;

import lombok.Getter;

//JPA의 내장타입 명시 (어딘가에 내장되어 value로 쓰일 수 있다)
@Embeddable
@Getter
public class Address {

	private String city;
	private String street;
	private String zipcode;
	
	//값 타입은 변경이 불가능하게 설계해야 한다.
	//Setter를 제거하고, 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만들어야함
	//JAP스펙상 Entity나 임베디드 타입(@Embeddable)은 자바 기본생성자(default constructor)를
	//public 또는 protected로 설정해야 한다. public보다는 protected가 더 안전하다.
	//JAP가 이러한 제약(기본생성자를 만들어두는 이유)을 두는 이유는 JPA 구현 라이브러리가 객체를 생성할 때
	//리플랙션 같은 기술을 사용할 수 있도록 지원해야 하기 때문
	
	protected Address() {	//jpa스펙상 만든 것 함부로 new로 생성하면 안된다는 것
	}
	
	public Address(String city, String street, String zipcode) {
		this.city = city;
		this.street = street;
		this.zipcode = zipcode;
	}
}
