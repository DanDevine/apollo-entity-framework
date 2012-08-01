package com.apollo.annotate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.apollo.ApolloException;
import com.apollo.Component;
import com.apollo.Entity;
import com.apollo.managers.Manager;

public abstract class ComponentInjector<T> {
	private final Class<? extends Annotation> clazz;

	public static ComponentInjector<Component> injectorOwner = new ComponentInjector<Component>(InjectFromOwner.class) {
		@Override
		Component getInjectionObject(Entity entity, Class<Component> type) {
			return entity.getComponent(type);
		}
	};
	
	public static ComponentInjector<Component> injectorWorld = new ComponentInjector<Component>(InjectFromWorld.class) {
		@Override
		Component getInjectionObject(Entity entity, Class<Component> type) {
			return entity.getWorld().getEntityManager().getSingleComponent(type);
		}
	};
	
	public static ComponentInjector<Manager> injectorManager = new ComponentInjector<Manager>(InjectManager.class) {
		@Override
		Manager getInjectionObject(Entity entity, Class<Manager> type) {
			return entity.getWorld().getManager(type);
		}
	};

	public ComponentInjector(Class<? extends Annotation> clazz) {
		this.clazz = clazz;
	}
	
	public void inject(Field field, Component component) {
		Annotation annotation = field.getAnnotation(clazz);
		if(annotation!=null && clazz.isAssignableFrom(clazz)) {
			@SuppressWarnings("unchecked")
			Class<T> type = (Class<T>)field.getType();
			T object = getInjectionObject(component.getOwner(), type);
			if(object==null) {
				//This will just inject a null object, but that is likely a problem in the code
				System.out.println("Warning! Autoinjection didn't find an object to inject! "+ field.getDeclaringClass() + " for " + field.getName());
			}
			try {
				field.setAccessible(true);
				field.set(component, object);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ApolloException("Failed to inject.", e);
			}
		}
	}
	
	abstract T getInjectionObject(Entity entity, Class<T> type);

	public ComponentInjector<Component> getInjectorOwner() {
		return injectorOwner;
	}

	public ComponentInjector<Component> getInjectorWorld() {
		return injectorWorld;
	}

}
