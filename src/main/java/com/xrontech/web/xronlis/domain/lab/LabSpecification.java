package com.xrontech.web.xronlis.domain.lab;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class LabSpecification {

    public static Specification<Lab> filterByAttributes(
            String name,
            String branch,
            String fromDate,
            String toDate,
            Boolean status,
            Boolean delete,
            String mobile,
            String code
    ) {
//        return (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            if (name != null && !name.isEmpty()) {
//                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
//            }
//            if (branch != null && !branch.isEmpty()) {
//                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("branch")), "%" + branch.toLowerCase() + "%"));
//            }
//            if (status != null) {
//                predicates.add(criteriaBuilder.equal(root.get("status"), status));
//            }
//            if (delete != null) {
//                predicates.add(criteriaBuilder.equal(root.get("delete"), delete));
//            }
//            if (mobile != null && !mobile.isEmpty()) {
//                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("mobile")), "%" + mobile.toLowerCase() + "%"));
//            }
//            if (code != null && !code.isEmpty()) {
//                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + code.toLowerCase() + "%"));
//            }
//            // Convert fromDate and toDate to LocalDate and compare only the date part of createdAt
//            if (fromDate != null && !fromDate.isEmpty()) {
//                LocalDate fromLocalDate = LocalDate.parse(fromDate);
//                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
//                        criteriaBuilder.function("DATE", LocalDate.class, root.get("createdAt")), fromLocalDate));
//            }
//            if (toDate != null && !toDate.isEmpty()) {
//                LocalDate toLocalDate = LocalDate.parse(toDate);
//                predicates.add(criteriaBuilder.lessThanOrEqualTo(
//                        criteriaBuilder.function("DATE", LocalDate.class, root.get("createdAt")), toLocalDate));
//            }
//
//            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//        };

        /// ///////////////////////////////optimized code
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addPredicateIfNotEmpty(predicates, criteriaBuilder, root.get("name"), name);
            addPredicateIfNotEmpty(predicates, criteriaBuilder, root.get("branch"), branch);
            addPredicateIfNotEmpty(predicates, criteriaBuilder, root.get("mobile"), mobile);
            addPredicateIfNotEmpty(predicates, criteriaBuilder, root.get("code"), code);
            addPredicateIfNotNull(predicates, criteriaBuilder, root.get("status"), status);
            addPredicateIfNotNull(predicates, criteriaBuilder, root.get("delete"), delete);

            // Filter by date (comparing only the date part of createdAt)
            addDatePredicate(predicates, criteriaBuilder, root.get("createdAt"), fromDate, toDate);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

    }

    private static void addPredicateIfNotEmpty(List<Predicate> predicates,
                                               jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
                                               jakarta.persistence.criteria.Path<String> path,
                                               String value) {
        if (value != null && !value.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(path), "%" + value.toLowerCase() + "%"));
        }
    }

    private static void addPredicateIfNotNull(List<Predicate> predicates,
                                              jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
                                              jakarta.persistence.criteria.Path<Boolean> path,
                                              Boolean value) {
        if (value != null) {
            predicates.add(criteriaBuilder.equal(path, value));
        }
    }

    private static void addDatePredicate(List<Predicate> predicates,
                                         jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
                                         jakarta.persistence.criteria.Path<?> path,
                                         String fromDate, String toDate) {
        if (fromDate != null && !fromDate.isEmpty()) {
            LocalDate fromLocalDate = LocalDate.parse(fromDate);
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    criteriaBuilder.function("DATE", LocalDate.class, path), fromLocalDate));
        }
        if (toDate != null && !toDate.isEmpty()) {
            LocalDate toLocalDate = LocalDate.parse(toDate);
            predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    criteriaBuilder.function("DATE", LocalDate.class, path), toLocalDate));
        }
    }
}
