package smtp.server.handler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class HandlerMapper {

	private String basePackage;
	private Map<Class<?>, Object> instances = new HashMap<>();
	private Map<String, Method> handlers = new HashMap<>();

	public HandlerMapper(String basePackage) {
		this.basePackage = basePackage;
	}

	public void initialize() {
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage(basePackage))
				.setScanners(new MethodAnnotationsScanner()));
		Set<Method> methods = reflections
				.getMethodsAnnotatedWith(SmtpHandler.class);

		for (Method method : methods) {
			SmtpHandler annotation = method
					.getDeclaredAnnotation(SmtpHandler.class);

			String[] keys = annotation.value();
			for (String key : keys) {
				handlers.put(key, method);
			}
		}
	}

	public boolean isValidKey(String key) {
		for (String k : handlers.keySet()) {
			if (k.equals(key)) {
				return true;
			}
		}
		return false;
	}

	private Object getInstanceForClassName(Class<?> clazz) {
		Object o = instances.get(clazz);
		if (o == null) {
			try {
				o = clazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			instances.put(clazz, o);
		}
		return o;
	}

	public String executeMethod(String key, Object... args) {
		Method m = handlers.get(key);
		Class<?> clazz = m.getDeclaringClass();
		try {
			return (String) m.invoke(getInstanceForClassName(clazz), args);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
