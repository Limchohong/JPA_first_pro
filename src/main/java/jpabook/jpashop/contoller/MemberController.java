package jpabook.jpashop.contoller;


import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	
	//회원 가입 폼 보여주기
	@GetMapping("/members/new")
	public String createForm(Model model) {
		
		model.addAttribute("memberForm", new MemberForm());
		
		return "members/createMemberForm";
	}
	
	//회원 가입
	@PostMapping("/members/new")
	public String create(@Valid MemberForm form, BindingResult result) {	
		
		//@Valid form에  있는 NotEmpty를 보고 유효성 검사를 해줌
		//오류가 있을 경우 컨트롤러에 넘어오지 않고 팅기는데 @Valid 다음에 BindingResult가 있으면
		//오류가 result에 담겨서 실행이됨
		
		if(result.hasErrors()) {	//error가 있을 경우 
			return "members/createMemberForm";
		}
		
		Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
		
		Member member = new Member();
		member.setName(form.getName());
		member.setAddress(address);
		
		memberService.join(member);
		
		return "redirect:/";
	}
	
	//회원 목록 조회
	@GetMapping("/members")
	public String list(Model model) {
//		List<Member> members = memberService.findMembers();
//		model.addAttribute("members", members);
		
		model.addAttribute("members", memberService.findMembers());
		return "members/memberList";
	}
}
