package jpabook.jpashop.repository;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

	private final EntityManager em;
	
	//주문
	public void save(Order order) {
		em.persist(order);
	}
	
	//주문 내역 단건 조회
	public Order findOne(Long id) {
		return em.find(Order.class, id);	//반환타입, pk
	}
	
	//검색용
	public List<Order> findAllByString(OrderSearch orderSearch){
		
		String jpql = "select o from Order o join o.member m";
		boolean isFirstCondition = true;

		//방법1. 무식한 방법 - 주문 상태 검색
		if(orderSearch.getOrderStatus() != null) {
			if(isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			}else {
				jpql += " and";
			}
			
			jpql +=" o.status = :status";
		}
		
		//방법1. 무식한 방법 - 회원 이름 검색
		if(StringUtils.hasText(orderSearch.getMemberName())) {	//text에 값이 있는지 확인
			if(isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			}else {
				jpql += " and";
			}
			
			jpql += " m.name like :name";
		}
		
		//방법1 이어서
		TypedQuery<Order> query = em.createQuery(jpql, Order.class)
				.setMaxResults(1000);
		
		if(orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		
		if(StringUtils.hasText(orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}
		
		return query.getResultList();
		
		//방법2. JPQL이용 - order를 조회 후 member랑 order랑 join을 한 것 
//		return em.createQuery("select o from Order o join o.member m" +
//				"where o.status = :status "+
//				"and m.name like :name", Order.class)
//				.setParameter("status", orderSearch.getOrderStatus())	//파라미터 바인딩
//				.setParameter("name", orderSearch.getMemberName())		//파라미터 바인딩
//				.setMaxResults(1000)	//최대 1000개 까지만 호출해라
//				.getResultList();
	}
	
	//JAP Criteria
	 public List<Order> findAllByCriteria(OrderSearch orderSearch){
		 CriteriaBuilder cb = em.getCriteriaBuilder();
		 CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		 Root<Order> o = cq.from(Order.class);
		 
		 //m으로 알리야스
		 Join<Object, Object> m = o.join("member", JoinType.INNER);
		 
		 List<Predicate> criteria = new ArrayList<>();
		 
		 //주문상태 검색
		 if(orderSearch.getOrderStatus() != null) {
			 Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
			 criteria.add(status);
		 }
		 
		 //회원 이름 검색
		 if(StringUtils.hasText(orderSearch.getMemberName())) {
			 Predicate name = 
					 cb.like(m.<String>get("name"), "%"+ orderSearch.getMemberName() + "%");
			 criteria.add(name);
		 }
		 
		 cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
		 TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
		 return query.getResultList();
	 }
	
	 //QueryDsl
//	 public List<Order> findAll(OrderSearch orderSearch){
//		 
//	 }
}
