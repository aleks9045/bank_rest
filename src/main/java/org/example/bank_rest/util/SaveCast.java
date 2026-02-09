package org.example.bank_rest.util;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class SaveCast {

    private SaveCast(){}

    public static List<String> toStringList(Object o) {
        if (o instanceof List<?> rawList) {
            return toStringList(rawList);
        }
        return Collections.emptyList();
    }

    public static List<String> toStringList(List<?> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .toList();
    }
}
