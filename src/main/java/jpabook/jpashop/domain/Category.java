package jpabook.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Category {

	@Id
	@GeneratedValue
	@Column(name = "category_id")
	private Long id;
	
	private String name;
	
	//카테고리와 List의 N:N관계를 만들기 위한것(양방향)?
	//카테고리도 List로 Item을 가지고 Item도 List로 카테고리를 가짐
	//실무에서 ManyToMany는 사용하지 않는게 좋음 
	@ManyToMany
	@JoinTable(name = "category_item",
			joinColumns = @JoinColumn(name = "category_id"),		//중간테이블에 명시된 컬럼명	
			inverseJoinColumns = @JoinColumn(name = "item_id"))		//해당테이블에 item쪽으로 들어가는 컬럼명
	//중간테이블이 있기 때문에 Mapping을 해주어야함 객체에서는 컬렉션 vs 컬렉션이기 때문에 N:N이 가능하지만
	//관계형 DB는 컬렉션 관계를 양쪽에 가질 수 없어서 1:N으로 풀어낼 수 있는 중간테이블이 필요함
	private List<Item> items = new ArrayList<>();
	
	//self로 양방향 연관관계를 한 것 다른 entity가 아닌 이름만 같은 다른 entity를 Mapping하는 것 처럼 하면 됨
	//ManyToOne, OneToOne의 기본전략은 EAGER이기 떄문에 LAZY로 바꾸어야 성능이 좋음
	@ManyToOne(fetch = FetchType.LAZY)	
	@JoinColumn(name = "parent_id")
	//자신을 상속 , 부모
	private Category parent;					 
	
	//자식은 여러개가 될 수 있기 때문에 @OneToMany
	@OneToMany(mappedBy = "parent")	
	private List<Category> child = new ArrayList<>();
	
	//연관관계 편의 메서드 - 위치는 양쪽 중 핵심적으로 컨트롤 하는 곳에 있는게 좋음
	//원자적으로 양쪽에 값을 세팅하는 것
	public void addChildCategory(Category child) {
		this.child.add(child);	//부모컬렉션에 들어감
		child.setParent(this);	//자식에서도 부모가 누군지 바로 넣어줘야함
	}
}
