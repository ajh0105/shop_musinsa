package com.musinsa.shop.item.support;

import java.util.Set;

public final class ItemCategory {

    public static final Set<String> ALLOWED = Set.of(
            "Best", "원피스", "상의", "하의", "아우터", "악세잡화", "오늘의 할인"
    );

    private ItemCategory() {
    }

    public static String normalize(String category) {
        if (category == null) {
            return null;
        }
        String trimmed = category.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        if (trimmed.equalsIgnoreCase("best")) {
            return "Best";
        }
        return trimmed;
    }

    public static boolean isValid(String category) {
        String normalized = normalize(category);
        return normalized != null && ALLOWED.contains(normalized);
    }
}