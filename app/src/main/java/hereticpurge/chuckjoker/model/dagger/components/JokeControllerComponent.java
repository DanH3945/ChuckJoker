package hereticpurge.chuckjoker.model.dagger.components;

import dagger.Component;
import hereticpurge.chuckjoker.model.JokeController;
import hereticpurge.chuckjoker.model.dagger.modules.ContextModule;
import hereticpurge.chuckjoker.model.dagger.modules.JokeControllerModule;
import hereticpurge.chuckjoker.model.dagger.scopes.JokeControllerScope;

@JokeControllerScope
@Component(modules = {JokeControllerModule.class, ContextModule.class})
public interface JokeControllerComponent {

    JokeController getJokeController();
}
