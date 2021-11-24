package jpabook.jpashop.contoller;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

	private Long id;
	
	@NotEmpty(message = "이름은 필수 입력입니다.")
	private String name;
	
	private int price;
	
	private int stockQuantity;
	
	@NotEmpty(message = "저자는 필수 입력입니다.")
	private String author;
	
	@NotEmpty(message = "isbn은 필수 입력입니다.")
	private String isbn;
}
