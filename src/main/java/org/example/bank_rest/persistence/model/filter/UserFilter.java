package org.example.bank_rest.persistence.model.filter;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserFilter {
    private String email;
    private String firstName;
    private String lastName;
}
