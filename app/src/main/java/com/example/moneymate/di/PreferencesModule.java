package com.example.moneymate.di;

// UserPreferences đã có @Inject constructor và @Singleton nên Hilt tự xử lý.
// Module này được giữ lại để mở rộng sau nếu cần thêm bindings.
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class PreferencesModule {
    // No bindings needed - UserPreferences uses @Inject constructor
}
