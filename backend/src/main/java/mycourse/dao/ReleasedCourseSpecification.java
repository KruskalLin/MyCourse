package mycourse.dao;

import mycourse.entity.Course;
import mycourse.entity.CourseStatus;
import mycourse.entity.ReleasedCourse;
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
 * @Date: 2019/2/21
 * @Todo:
 */
public class ReleasedCourseSpecification implements Specification<ReleasedCourse> {
    private FilterRequest filter;

    private List<CourseStatus> courseStatus;

    public ReleasedCourseSpecification(FilterRequest filterRequest) {
        this.filter = filterRequest;
    }

    public ReleasedCourseSpecification(FilterRequest filter, List<CourseStatus> courseStatus) {
        this.filter = filter;
        this.courseStatus = courseStatus;
    }

    @Override
    public Predicate toPredicate(Root<ReleasedCourse> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Predicate[] p = new Predicate[predicates.size()];
        Predicate result = criteriaBuilder.and(predicates.toArray(p));
        if (!filter.getTitle().equals("")) {
            predicates.clear();
            predicates.add(criteriaBuilder.like(root.get("course").get("name"), "%" + filter.getTitle() + "%"));
            result = criteriaBuilder.and(result, criteriaBuilder.or(toPredicateArray(predicates)));
        }
        if (courseStatus != null) {
            predicates.clear();
            for (CourseStatus status: courseStatus) {
                predicates.add(criteriaBuilder.equal(root.get("courseStatus"), status));
            }
            result = criteriaBuilder.and(result, criteriaBuilder.or(toPredicateArray(predicates)));
        }
        return result;
    }
    private Predicate[] toPredicateArray(List<Predicate> predicates) {
        Predicate[] p = new Predicate[predicates.size()];
        return predicates.toArray(p);
    }
}
