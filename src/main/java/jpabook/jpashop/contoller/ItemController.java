package jpabook.jpashop.contoller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;
	
	//상품등록 폼 보여주기
	@GetMapping("/items/new")
	public String createForm(Model model) {
		model.addAttribute("bookForm", new BookForm());
		return "items/createItemForm";
	}
	
	//상품등록
	@PostMapping("/items/new")
	public String create(@Valid BookForm form, BindingResult result) {
		
		if(result.hasErrors()) {
			return "items/createItemForm";
		}
		
		Book book = new Book();
		
		book.setName(form.getName());
		book.setPrice(form.getPrice());
		book.setStockQuantity(form.getStockQuantity());
		book.setAuthor(form.getAuthor());
		book.setIsbn(form.getIsbn());
		
		itemService.saveItem(book);
		
		return "redirect:/items";
	}
	
	//상품 목록 조회
	@GetMapping("/items")
	public String list(Model model) {
		List<Item> items = itemService.findItems();
		model.addAttribute("items", items);
		return "items/itemList";
	}
	
	//상품 수정폼 보여주기
	@GetMapping("/items/{itemId}/edit")
	public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
		Book item = (Book) itemService.findOne(itemId);
		
		BookForm form = new BookForm();
		form.setId(item.getId());
		form.setName(item.getName());
		form.setPrice(item.getPrice());
		form.setStockQuantity(item.getStockQuantity());
		form.setAuthor(item.getAuthor());
		form.setIsbn(item.getIsbn());
		
		model.addAttribute("itemForm", form);
		
		return "/items/updateItemForm";
	}
	
	//상품 수정처리
	@PostMapping("/items/{itemId}/edit")
	public String updateItem(@PathVariable Long itemId,
			@ModelAttribute("itemForm") BookForm form) { //@ModelAttribute("view에서 지정한 값이 담긴 객체") 
		
		
		//준영속 entity : DB에 다녀온 상태로 식별자가 있는 경우 -> (JPA가 식별할 수 있는 id를 가지고 있음) DB에 저장되고 불려온 것
		//영속성 컨텍스트가 더 이상 관리하지 않는 entity
		//변경감지 (dirty checking이 안됨)
		//준영속 entity를 수정하는 2가지 방법
		//1. 변경 감지(dirty checking) 기능 사용
		//2. 병합(merge) : 준영속 상태의 entity를 영속 상태로 변경할 때 사용하는 기능
		
		//어설프게 entity를 파라미터로 쓴것 
//		Book book = new Book();
//		book.setId(form.getId());
//		book.setAuthor(form.getAuthor());
//		book.setName(form.getName());
//		book.setPrice(form.getPrice());
//		book.setStockQuantity(form.getStockQuantity());
//		book.setAuthor(form.getAuthor());
//		book.setIsbn(form.getIsbn());
//		
//		itemService.saveItem(book);
		
		//위 코드보다 유지보수성이 더 좋음 
		itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
		return "redirect:/items";
	}
}
