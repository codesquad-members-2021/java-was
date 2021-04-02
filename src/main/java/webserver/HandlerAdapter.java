package webserver;

import annotation.RequestMapping;
import controller.Controller;
import controller.ControllerKey;
import controller.DefaultController;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HandlerAdapter {
    private static final Controller defaultController = new DefaultController();
    private static final Map<ControllerKey, Controller> controllerMap = new HashMap<>();

    public HandlerAdapter() {
        Reflections reflections = new Reflections("controller");
        Set<Class<?>> requestMappingControllers = reflections.getTypesAnnotatedWith(RequestMapping.class);
        for (Class<?> controller : requestMappingControllers) {
            RequestMapping annotation = controller.getAnnotation(RequestMapping.class);
            try {
                controllerMap.put(new ControllerKey(annotation.method(), annotation.path()), (Controller) controller.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    public Controller controller(ControllerKey key){
        return controllerMap.getOrDefault(key,defaultController);
    }
}
