package es.codeurjc.gymapp.services;

import java.lang.reflect.*;
import java.util.Arrays;

public class DTOServices {
    @SuppressWarnings("unchecked")
    public static <T> T mergeRecords(T original, T update) {
        if (original == null) return update;
        if (update == null) return original;

        Class<?> record = original.getClass();

        if (!record.isRecord()) {
            throw new IllegalArgumentException("Only record types are supported.");
        }

        RecordComponent[] components = record.getRecordComponents();
        Object[] values = new Object[components.length];

        for (int i = 0; i < components.length; i++) {
            try {
                Method accessor = components[i].getAccessor();
                Object updatedValue = accessor.invoke(update);
                Object originalValue = accessor.invoke(original);

                values[i] = (updatedValue != null) ? updatedValue : originalValue;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Constructor<?> canonicalConstructor = record.getDeclaredConstructor(
                Arrays.stream(components).map(RecordComponent::getType).toArray(Class[]::new)
            );
            return (T) canonicalConstructor.newInstance(values);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
