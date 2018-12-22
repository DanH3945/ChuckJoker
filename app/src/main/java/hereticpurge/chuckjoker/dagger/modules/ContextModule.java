package hereticpurge.chuckjoker.dagger.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import hereticpurge.chuckjoker.dagger.qualifiers.ActivityContextQualifier;

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @ActivityContextQualifier
    public Context context() {
        return context;
    }
}
