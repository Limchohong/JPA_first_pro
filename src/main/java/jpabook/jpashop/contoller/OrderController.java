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
import org.springframework.web.bind.annotation.RequestParam;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final MemberService memberService;
	private final ItemService itemService;

	//주문 폼 보여주기
	@GetMapping("/order")
	public String createForm(Model model) {
		
		List<Member> members = memberService.findMembers();
		
		List<Item> items = itemService.findItems();
		
		model.addAttribute("members", members);
		model.addAttribute("items", items);
		
		return "order/orderForm";
	}
	
	//주문하기
	//@RequestParam : form에서 submit하면 값이 담겨서 넘어옴
	@PostMapping("/order")
	public String order(@RequestParam("memberId") Long memberId, 
						@RequestParam("itemId") Long itemId,
						@RequestParam("count") int count) {
		
		//비즈니스 로직
		orderService.order(memberId, itemId, count);
		
		return "redirect:/orders";
	}
	
	//조건에 따라 주문 목록 보기
	@GetMapping("/orders")
	public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {	//orderSearch : 상품 리스트 조회 조건
		
		
		List<Order> orders = orderService.findOrders(orderSearch);

		model.addAttribute("orders", orders);
		
		return "order/orderList";
	}
	
	//주문취소
	@PostMapping("/orders/{orderId}/cancel")
	public String cancelOrder(@PathVariable("orderId") Long orderId) {
		
		orderService.cancleOrder(orderId);
		
		return "redirect:/orders";
	}
	
}
