package org.example.bank_rest.util;

import org.example.bank_rest.testUtil.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PageableFactoryTest {

    @Test
    public void testPageAndSize() {
        for (int i = 0; i < 10; i++) {
            int page = RandomGenerator.getInteger(1, 10);
            int pageSize = RandomGenerator.getInteger(1, 100);

            var pageable = PageableFactory.getPageable(page, pageSize, null);

            assertEquals(pageable.getPageNumber(), page);
            assertEquals(pageable.getPageSize(), pageSize);

        }
    }

    @Test
    public void testDefaultValues() {

        var defaultPageValue = 0;
        var defaultPageSizeValue = 20;

        var pageable = PageableFactory.getPageable(null, null, null);

        assertEquals(pageable.getPageNumber(), defaultPageValue);
        assertEquals(pageable.getPageSize(), defaultPageSizeValue);
        assertEquals(pageable.getSort(), Sort.by(Sort.Direction.ASC, "created_at"));
    }

    @Test
    public void testMixedSort() {

        var mixedSort = "created_at:asc,name:desc";
        var pageable = PageableFactory.getPageable(null, null, mixedSort);

        List<Sort.Order> orders = pageable.getSort().stream().toList();

        assertEquals(2, orders.size());

        var first = orders.get(0);
        var second = orders.get(1);

        assertEquals("created_at", first.getProperty());
        assertEquals(Sort.Direction.ASC, first.getDirection());

        assertEquals("name", second.getProperty());
        assertEquals(Sort.Direction.DESC, second.getDirection());
    }
}
