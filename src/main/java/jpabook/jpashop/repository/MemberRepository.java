package jpabook.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor	//final이 붙은 필드만 생성자
//스프링 빈으로 등록
public class MemberRepository {
	
	//jpa의 entity매니저를 주입해줌
//	@PersistenceContext
//	private EntityManager em;
	
	//매니저팩토리를 직접 주입받을 수 있으나 대부분 사용하지 않음
//	@PersistenceUnit
//	private EntityManager emf;

	//생성자로 주입
	private final EntityManager em;

	
	//회원 저장
	public void save(Member member) {
		//영속성 컨텍스트에 맴버 객체를 넣음 트랜젝션이 commit되는 시점에 DB에 insert가 됨
		em.persist(member);	
	}
	
	//회원조회(단건조회) 
	public Member findOne(Long id) {
		//id값으로 member를 찾아서 반환 
		//Member memeber = em.find(Member.class, id);
		
		//타입, pk
		return em.find(Member.class, id);	
	}
	
	//회원조회 (리스트 조회)
	public List<Member> findAll(){
		//sql과 기능은 동일하나 살짝 다름 sql은 테이블을 대상으로 쿼리를 날리고, jpql은 entity객체를 상대로 쿼리를 날림
		//여기에서 m은 알리야스임
		return em.createQuery("select m from Member m",Member.class)	//Jpql사용 (쿼리문, 반환타입)
		.getResultList();	//member를 list로 만들어줌
	}
	
	//회원의 이름으로 회원 조회
	public List<Member> findByName(String name){
		// : name는 데이터 바인딩을 하는것
		return em.createQuery("select m from Member m where m.name = :name", Member.class)
				.setParameter("name", name)
				.getResultList();
	}
}
