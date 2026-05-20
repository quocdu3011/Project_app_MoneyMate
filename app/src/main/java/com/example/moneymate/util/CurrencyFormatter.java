package com.example.moneymate.util;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyFormatter {

    public static String format(double amount, String currencyCode) {
        if ("VND".equals(currencyCode)) {
            NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
            formatter.setMaximumFractionDigits(0);
            return formatter.format(amount) + " ₫";
        }
        try {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
            formatter.setCurrency(Currency.getInstance(currencyCode));
            return formatter.format(amount);
        } catch (Exception e) {
            return String.format(Locale.US, "%.2f %s", amount, currencyCode);
        }
    }

    public static String formatVnd(double amount) {
        return format(amount, "VND");
    }

    public static String formatWithSign(double amount, String currencyCode) {
        String formatted = format(Math.abs(amount), currencyCode);
        return amount >= 0 ? "+" + formatted : "-" + formatted;
    }
}
