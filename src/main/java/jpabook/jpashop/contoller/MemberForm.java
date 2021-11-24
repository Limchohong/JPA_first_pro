package jpabook.jpashop.contoller;

import javax.validation.constraints.NotEmpty;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {
	
	@NotEmpty(message = "회원 이름은 필수입니다.")	//값이 비어있을 경우 
	private String name;
	
	@NotEmpty(message = "도시 입력은 필수입니다.")
	private String city;
	
	@NotEmpty(message = "거리 입력은 필수입니다.")
	private String street;
	
	@NotEmpty(message = "우편번호는 필수입니다.")
	private String zipcode;
}
