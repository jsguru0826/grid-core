package com.aat.application.util;

import com.aat.application.annotations.ContentDisplayedInSelect;
import com.aat.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.component.Component;
import jakarta.persistence.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

public class GlobalData {
    public static Field getPrimaryKeyField(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        // Check superclass if no field found
        return getPrimaryKeyField(clazz.getSuperclass());
    }

    public static List<String> getFieldNamesWithAnnotation(Class<? extends Annotation> annotation, Class<?> entityClass, boolean root) {
        List<String> fieldNames = new ArrayList<>();
        Map<String, Integer> fieldNameSequenceMap = new HashMap<>();
        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(annotation)) {

                if (root) {
                    if (field.getType() == String.class
                            || field.getType() == LocalDateTime.class
                            || field.getType() == Integer.class) {
                        fieldNames.add(field.getName());
                    } else {
                        fieldNames.add(field.getName() + "." + getFieldNamesWithAnnotation(annotation, field.getType(), root).get(0));
                    }
                    if (annotation.getSimpleName().equals("ContentDisplayedInSelect")) {
                        ContentDisplayedInSelect contentAnnotation = field.getAnnotation(ContentDisplayedInSelect.class);
                        if (contentAnnotation != null) {
                            fieldNameSequenceMap.put(fieldNames.get(fieldNames.size() - 1), contentAnnotation.sequence());
                        }
                    }
                }
            }
        }
        if (annotation.getSimpleName().equals("ContentDisplayedInSelect")) {
            fieldNames.sort(Comparator.comparingInt(fieldNameSequenceMap::get));
        }
        return fieldNames;
    }

    public static List<Component> findComponentsWithAttribute(Component parent, String attributeName, String attributeValue) {
        List<Component> matchingComponents = new ArrayList<>();

        parent.getChildren().forEach(child -> {
            if (attributeValue.equals(child.getElement().getAttribute(attributeName))) {
                matchingComponents.add(child);
            }
            matchingComponents.addAll(findComponentsWithAttribute(child, attributeName, attributeValue));
        });

        return matchingComponents;
    }

    public static List<Component> findComponentsWithAttribute(Component parent, String attributeName) {
        List<Component> matchingComponents = new ArrayList<>();

        parent.getChildren().forEach(child -> {
            if (child.getElement().hasAttribute(attributeName)) {
                matchingComponents.add(child);
            }

            matchingComponents.addAll(findComponentsWithAttribute(child, attributeName));
        });

        return matchingComponents;
    }

    public static String convertToStandard(String string) {
        if (string == null)
            return null;
        return string.substring(0, 1).toUpperCase()
                + string.substring(1);
    }
}