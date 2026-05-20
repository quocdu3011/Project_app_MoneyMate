package com.example.moneymate.data.local.database;

import com.example.moneymate.data.local.database.dao.AccountDao;
import com.example.moneymate.data.local.database.dao.CategoryDao;
import com.example.moneymate.data.local.database.entity.AccountEntity;
import com.example.moneymate.data.local.database.entity.CategoryEntity;
import com.example.moneymate.domain.model.enums.AccountType;
import com.example.moneymate.domain.model.enums.TransactionType;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class DatabaseSeeder {

    public static void seedIfEmpty(CategoryDao categoryDao, AccountDao accountDao) {
        categoryDao.getAllCategories()
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .subscribe((categories, error) -> {
                if (error != null || categories.isEmpty()) {
                    seedCategories(categoryDao);
                }
            });

        accountDao.getAllAccounts()
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .subscribe((accounts, error) -> {
                if (error != null || accounts.isEmpty()) {
                    seedAccounts(accountDao);
                }
            });
    }

    private static void seedCategories(CategoryDao dao) {
        long now = System.currentTimeMillis();
        List<CategoryEntity> expenseCategories = Arrays.asList(
            makeCategory("Ăn uống", "ic_food", "#FF5722", TransactionType.EXPENSE, now),
            makeCategory("Di chuyển", "ic_transport", "#2196F3", TransactionType.EXPENSE, now),
            makeCategory("Mua sắm", "ic_shopping", "#9C27B0", TransactionType.EXPENSE, now),
            makeCategory("Nhà ở", "ic_home", "#795548", TransactionType.EXPENSE, now),
            makeCategory("Hóa đơn & Tiện ích", "ic_bill", "#FF9800", TransactionType.EXPENSE, now),
            makeCategory("Giải trí", "ic_entertainment", "#E91E63", TransactionType.EXPENSE, now),
            makeCategory("Giáo dục", "ic_education", "#3F51B5", TransactionType.EXPENSE, now),
            makeCategory("Sức khỏe", "ic_health", "#4CAF50", TransactionType.EXPENSE, now),
            makeCategory("Quần áo", "ic_clothes", "#607D8B", TransactionType.EXPENSE, now),
            makeCategory("Du lịch", "ic_travel", "#00BCD4", TransactionType.EXPENSE, now),
            makeCategory("Thú cưng", "ic_pet", "#8BC34A", TransactionType.EXPENSE, now),
            makeCategory("Làm đẹp", "ic_beauty", "#FF4081", TransactionType.EXPENSE, now),
            makeCategory("Khác", "ic_other", "#9E9E9E", TransactionType.EXPENSE, now)
        );

        List<CategoryEntity> incomeCategories = Arrays.asList(
            makeCategory("Lương", "ic_salary", "#4CAF50", TransactionType.INCOME, now),
            makeCategory("Thưởng", "ic_bonus", "#8BC34A", TransactionType.INCOME, now),
            makeCategory("Đầu tư", "ic_invest", "#2196F3", TransactionType.INCOME, now),
            makeCategory("Kinh doanh", "ic_business", "#FF9800", TransactionType.INCOME, now),
            makeCategory("Được tặng", "ic_gift", "#E91E63", TransactionType.INCOME, now),
            makeCategory("Thu nhập khác", "ic_other", "#9E9E9E", TransactionType.INCOME, now)
        );

        for (CategoryEntity c : expenseCategories) {
            dao.insert(c).subscribeOn(Schedulers.io()).subscribe();
        }
        for (CategoryEntity c : incomeCategories) {
            dao.insert(c).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    private static void seedAccounts(AccountDao dao) {
        long now = System.currentTimeMillis();
        AccountEntity cash = new AccountEntity();
        cash.setName("Tiền mặt");
        cash.setType(AccountType.CASH);
        cash.setBalance(0);
        cash.setIcon("ic_cash");
        cash.setColor("#4CAF50");
        cash.setDefault(true);
        cash.setCreatedAt(now);
        dao.insert(cash).subscribeOn(Schedulers.io()).subscribe();
    }

    private static CategoryEntity makeCategory(String name, String icon, String color,
                                                TransactionType type, long now) {
        CategoryEntity c = new CategoryEntity();
        c.setName(name);
        c.setIcon(icon);
        c.setColor(color);
        c.setType(type);
        c.setCreatedAt(now);
        return c;
    }
}
