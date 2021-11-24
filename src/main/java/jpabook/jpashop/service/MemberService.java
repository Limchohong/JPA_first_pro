package jpabook.jpashop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor	//final이 붙은 필드만 생성자로 생성해줌
public class MemberService {
	
	@Autowired
	private final MemberRepository memberRepository;
	
	//== @RequiredArgsConstructor어노테이션으로 대체 ==
	//생성자 인잭션(생성시 완성이 되기 때문에 중간에 set해서 변경불가하기 때문에 좋음)
//	@Autowired	//어노테이션 없이도 생성자가 1개만 있기만 해도 됨
//	public MemberService(MemberRepository memberRepository) {
//		this.memberRepository = memberRepository;
//	}
	
	//회원가입
	@Transactional()	//따로 설정한 것은 우선권을 가짐
	public Long join(Member member) {
		
		//중복회원 검증
		validateDuplicateMember(member);
		
		memberRepository.save(member);
		return member.getId();
	}
	
	//중복회원 검증
	private void validateDuplicateMember(Member member) {
		//Exception
		//실무에서는 멀티쓰레드의 상황을 고려하여 DB에서는 member의 name을 유니크 제약조건으로 걸어두는 것이 좋음
		List<Member> findMembers = memberRepository.findByName(member.getName());
		if(!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}
	
	//회원 전체 조회
	public List<Member> findMembers(){
		return memberRepository.findAll();
	}
	
	//회원 단건 조회
	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}
}
