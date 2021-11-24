package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	//ItemService는 ItemRepository에 단순하게 위임만 하는 클래스
	//위임만 하는 클래스를 만들어야 필요가 있을까에 대해 고민해볼 필요는 있음
	//컨트롤러에서 바로 repository를 호출해도 된다고 함
	
	private final ItemRepository itemRepository;
	
	//상품 등록
	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}
	
	//1. 변경 감지(dirty checking) 기능 사용 _ 좋은 방법임
	@Transactional
	public void updateItem(Long itemId, String name, int price, int stockQuantity) {
		//여기에서는 findItem는 영속상태이기 때문에 dirty checking으로 인해 persist를 안해줘도 Transactional이 끝나는 시점에 commit됨
		//Transactional이 끝나는 시점에 commit되면 jpa는 flush를 날림 (영속성 컨텍스트 중에 변경된 아이를 찾아서 update쿼리를 날림)
		Item findItem = itemRepository.findOne(itemId);	//id를 기반으로 실제 db에 있는 영속상태에 있는 entity를 찾아옴
		findItem.setName(name);
		findItem.setPrice(price);
		findItem.setStockQuantity(stockQuantity);
	}
	
	//상품 단건 조회
	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}

	//상품 목록 조회
	public List<Item> findItems(){
		return itemRepository.findAll();
	}
	
}
