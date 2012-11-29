package play.modules.camelus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import javax.inject.Inject;

import play.Logger;
import play.Play;
import play.classloading.enhancers.ControllersEnhancer.ControllerSupport;
import play.inject.BeanSource;
import play.jobs.Job;

public class Injector {

	public static void inject(BeanSource source) {
		List<Class> classes = Play.classloader.getAssignableClasses(ControllerSupport.class);
		classes.addAll(Play.classloader.getAssignableClasses(Job.class));

		for(Class<?> clazz : classes) {
			for(Field field : clazz.getDeclaredFields()) {
				if(Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(Inject.class)) {
					Class<?> type = field.getType();
					field.setAccessible(true);
					try {
						Object o = source.getBeanOfType(type);
						if(o != null){
							field.set(null, o);
						}
					} catch(RuntimeException e) {
						Logger.error("Camelus injection runtime error.", e);
					} catch(Exception e) {
						Logger.error("Camelus injection error.", e);
					}
				}
			}
		}
	}

}
