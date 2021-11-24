package jpabook.jpashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//해당 어노테이션은 해당 패키지와 해당 패키지 하위에 있는 모든 것을 스프링이 component스캔을 해서 자동 등록이 됨
public class JpashopApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(JpashopApplication.class, args);
	}

}
