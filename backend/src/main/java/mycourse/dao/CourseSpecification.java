package mycourse.dao;

import mycourse.entity.Course;
import mycourse.payloads.FilterRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/19
 * @Todo:
 */
public class CourseSpecification implements Specification<Course> {
    private FilterRequest filter;

    public CourseSpecification(FilterRequest filterRequest) {
        this.filter = filterRequest;
    }

    @Override
    public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Predicate[] p = new Predicate[predicates.size()];
        Predicate result = criteriaBuilder.and(predicates.toArray(p));
        if (!filter.getTitle().equals("")) {
            predicates.clear();
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getTitle() + "%"));
            result = criteriaBuilder.and(result, criteriaBuilder.or(toPredicateArray(predicates)));
        }
        return result;
    }
    private Predicate[] toPredicateArray(List<Predicate> predicates) {
        Predicate[] p = new Predicate[predicates.size()];
        return predicates.toArray(p);
    }
}
