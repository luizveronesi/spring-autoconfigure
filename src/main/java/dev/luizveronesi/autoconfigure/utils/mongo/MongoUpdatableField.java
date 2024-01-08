package dev.luizveronesi.autoconfigure.utils.mongo;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

public @Data class MongoUpdatableField implements Serializable {

	private String id;

	private String name;

	private Object value;

	@JsonIgnore
	public String getStringValue() {
		if (getUnique() == null)
			return null;
		String str = getUnique().toString();
		if (str.equals("null"))
			return null;
		return str;
	}

	@JsonIgnore
	public Object getUnique() {
		if (value == null)
			return null;
		if (value.getClass().isArray())
			return ((Object[]) value)[0];
		return value;
	}

	public static void apply(MongoUpdatableField field, Object obj) {
		try {
			obj.getClass().getDeclaredField(field.getName()); // force exception if field doesn't exist
			BeanUtils.setProperty(obj, field.getName(), field.getValue());
		} catch (Exception e) {
			Object[] args = { field.getName(), field.getValue() };
			try {
				MethodUtils.invokeMethod(obj, "set", args);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
				throw new RuntimeException(ExceptionUtils.getStackTrace(e));
			}
		}
	}
}
