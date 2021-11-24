package jpabook.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

	private final EntityManager em;
	
	//상품등록
	public void save(Item item) {
		
		//상품의 id가 비어있을 경우 등록(데이터 저장시 처음에는 id가 없기 때문!)
		//jpa에 저장하기 전까지는 id값이 없음, 새로 생성한 객체라는 뜻
		if(item.getId() == null) {
			em.persist(item);	//신규로 등록 하는 것이라고 생각하면됨
		}else {	
			//이미 db에 등록된걸 어디에서 가져온 것이라는 것
			//그렇지 않을 경우 병합?.. 강제 update비슷한거
			
			em.merge(item);
		}
	}
	
	//상품 단건조회
	public Item findOne(Long id) {
		return em.find(Item.class, id);
	}
	
	//상품 목록조회
	public List<Item> findAll(){
		return em.createQuery("select i from Item i",Item.class)
				.getResultList();
	}
}
