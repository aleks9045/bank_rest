package org.example.bank_rest.util;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class PageableFactory {

    private static final String SORT_DELIMITER = ":";
    private static final String SORTS_DELIMITER = ",";
    private static final Integer DEFAULT_PAGE = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 20;

    public static Pageable getPageable(Integer page, Integer pageSize, @Nullable String sort) {

        Sort actualSort = parseMultiSort(sort);
        boolean noSort = (actualSort == null);
        boolean noPage = (page == null);
        boolean noSize = (pageSize == null);

        // No pagination, no sort
        if (noPage && noSize && noSort) return Pageable.unpaged();

        // No pagination, only sort
        if (noPage && noSize) return Pageable.unpaged(actualSort);

        page = page != null && page >= 0 ? page : DEFAULT_PAGE;
        pageSize = pageSize != null && pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;

        // Only pagination, no sort
        if (noSort) return PageRequest.of(page, pageSize);

        return PageRequest.of(page, pageSize, actualSort);
    }

    private static Sort parseMultiSort(String sort) {
        if (sort == null) return null;
        try {
            var parts = sort.split(SORTS_DELIMITER);
            return Arrays.stream(parts)
                    .map(String::trim)
                    .map(PageableFactory::parseSort)
                    .filter(Objects::nonNull)
                    .reduce(Sort.unsorted(), Sort::and);
        } catch (Exception e) {
            return null;
        }
    }

    private static Sort parseSort(String sort) {
        try {

            var parts = sort.split(SORT_DELIMITER);

            String field = toCamelCase(parts[0].trim());
            Sort.Direction direction =
                    (parts.length > 1)
                            ? Sort.Direction.fromString(parts[1].trim())
                            : Sort.Direction.ASC;
            return Sort.by(direction, field);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param field Field in snake case
     * @return Field in camel case
     */
    private static String toCamelCase(String field) {
        var builder = new StringBuilder(field);
        var underlineIndexes = new ArrayList<Integer>();
        for (int i = 0; i < builder.length(); i++) {
            if (builder.charAt(i) == '_') {
                underlineIndexes.add(i);
            }
        }
        underlineIndexes.forEach(index ->
                builder.replace(index,
                        index + 2,
                        String.valueOf(builder.charAt(index + 1)).toUpperCase()));

        return builder.toString();
    }
}
