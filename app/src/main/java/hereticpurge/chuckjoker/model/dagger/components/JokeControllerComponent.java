package hereticpurge.chuckjoker.dagger.components;

import dagger.Component;
import hereticpurge.chuckjoker.dagger.modules.ContextModule;
import hereticpurge.chuckjoker.dagger.modules.JokeControllerModule;
import hereticpurge.chuckjoker.dagger.scopes.JokeControllerScope;
import hereticpurge.chuckjoker.model.JokeController;

@JokeControllerScope
@Component(modules = {JokeControllerModule.class, ContextModule.class})
public interface JokeControllerComponent {

    JokeController getJokeController();
}
