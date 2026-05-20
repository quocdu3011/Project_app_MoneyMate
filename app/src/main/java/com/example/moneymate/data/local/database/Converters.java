package com.example.moneymate.data.local.database;

import androidx.room.TypeConverter;

import com.example.moneymate.domain.model.enums.AccountType;
import com.example.moneymate.domain.model.enums.Frequency;
import com.example.moneymate.domain.model.enums.TransactionType;

public class Converters {
    @TypeConverter
    public static TransactionType toTransactionType(String value) {
        return value == null ? null : TransactionType.valueOf(value);
    }

    @TypeConverter
    public static String fromTransactionType(TransactionType type) {
        return type == null ? null : type.name();
    }

    @TypeConverter
    public static AccountType toAccountType(String value) {
        return value == null ? null : AccountType.valueOf(value);
    }

    @TypeConverter
    public static String fromAccountType(AccountType type) {
        return type == null ? null : type.name();
    }

    @TypeConverter
    public static Frequency toFrequency(String value) {
        return value == null ? null : Frequency.valueOf(value);
    }

    @TypeConverter
    public static String fromFrequency(Frequency frequency) {
        return frequency == null ? null : frequency.name();
    }
}
