package com.apollo.managers;

import java.util.HashMap;
import java.util.Map;

import com.apollo.Entity;

public class TagManager extends Manager {
	private Map<Tag, Entity> entityByTag;

	public TagManager() {
		entityByTag = new HashMap<Tag, Entity>();
	}

	public void register(Tag tag, Entity e) {
		entityByTag.put(tag, e);
	}

	public void unregister(Tag tag) {
		entityByTag.remove(tag);
	}

	public boolean isRegistered(Tag tag) {
		return entityByTag.containsKey(tag);
	}

	public Entity getEntity(Tag tag) {
		return entityByTag.get(tag);
	}
	
	@Override
	public void removed(Entity e) {
		entityByTag.values().remove(e);
	}
}
