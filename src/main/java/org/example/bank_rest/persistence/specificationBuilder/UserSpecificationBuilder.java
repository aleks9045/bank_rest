package org.example.bank_rest.persistence.specificationBuilder;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.persistence.model.entity.User;
import org.example.bank_rest.persistence.model.entity.User_;
import org.example.bank_rest.persistence.model.filter.UserFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserSpecificationBuilder extends JpaSpecificationBuilder<User> {

    public Specification<User> build(UserFilter filter) {

        return Specification.<User>unrestricted()
                .and(this.equal(User_.EMAIL, filter.email()))
                .and(this.equal(User_.FIRST_NAME, filter.firstName()))
                .and(this.equal(User_.LAST_NAME, filter.lastName()));
    }

}
