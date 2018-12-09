package hereticpurge.chuckjoker.dagger.components;

import dagger.Component;
import hereticpurge.chuckjoker.dagger.modules.ContextModule;
import hereticpurge.chuckjoker.dagger.modules.NetworkModule;
import hereticpurge.chuckjoker.model.JokeController;

@Component(modules = {NetworkModule.class, ContextModule.class})
public interface JokeControllerComponent {

    JokeController getJokeController();
}
