package se.danielmartensson.tools;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;

public class GetSetClassInformation {

	public static <T> String[] getFieldNames(T object) {
		return getFieldNames(object, new String[] {});
	}

	public static <T> String[] getFieldNames(T object, String[] avoidTheseFields) {
		Field[] fields = object.getClass().getDeclaredFields();
		String[] fieldNames = new String[fields.length - avoidTheseFields.length];
		List<String> avoidTheseNamesList = Arrays.asList(avoidTheseFields);
		int i = 0;
		for (Field field : fields) {
			String fieldName = field.getName();
			if (avoidTheseNamesList.contains(fieldName))
				continue;
			fieldNames[i] = fieldName;
			i++;
		}
		return fieldNames;
	}

	public static <T> Float[] getFieldValuesFloat(T object) {
		return getFieldValuesFloat(object, new String[] {});
	}

	public static <T> Float[] getFieldValuesFloat(T object, String[] avoidTheseFields) {
		String[] fieldNames = getFieldNames(object, avoidTheseFields);
		Float[] fieldValues = new Float[fieldNames.length];
		int i = 0;
		for (String fieldName : fieldNames) {
			try {
				Field field = object.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				fieldValues[i] = field.getFloat(object);
				i++;
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fieldValues;
	}

	public static <T> String[] getFieldValuesString(T object) {
		return getFieldValuesString(object, new String[] {});
	}

	public static <T> String[] getFieldValuesString(T object, String[] avoidTheseFields) {
		String[] fieldNames = getFieldNames(object, avoidTheseFields);
		String[] fieldValues = new String[fieldNames.length];
		int i = 0;
		for (String fieldName : fieldNames) {
			try {
				Field field = object.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				fieldValues[i] = String.valueOf(field.get(object));
				i++;
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fieldValues;
	}

	public static <T> void setFillFields(Object target, String[] dataRow, int beginsAtDataRowIndex) {
		Field[] fields = target.getClass().getDeclaredFields();
		int count = beginsAtDataRowIndex;
		for (Field field : fields) {
			String fieldType = field.getType().getSimpleName();
			field.setAccessible(true);
			setFieldValue(target, fieldType, field, dataRow[count]);
			count++;
		}
	}

	public static <T> void copyFieldsToDestination(Object source, Object target) {
		BeanUtils.copyProperties(source, target);
	}

	private static <T> void setFieldValue(Object target, String fieldType, Field field, String value) {
		try {
			switch (fieldType) {
			case "int":
				try {
					field.setInt(target, Integer.parseInt(value));
				} catch (Exception e) {
					field.setInt(target, 0);
				}
				break;
			case "String":
				try {
					field.set(target, value);
				} catch (Exception e) {
					field.set(target, "");
				}
				break;
			case "float":
				try {
					field.setFloat(target, Float.parseFloat(value));
				} catch (Exception e) {
					field.setFloat(target, 0.0f);
				}
				break;
			case "boolean":
				try {
					field.setBoolean(target, Boolean.parseBoolean(value));
				} catch (Exception e) {
					field.setBoolean(target, false);
				}
				break;
			}
		} catch (Exception e) {
			System.out.println("2 Fel på " + fieldType);
		}
	}
}
