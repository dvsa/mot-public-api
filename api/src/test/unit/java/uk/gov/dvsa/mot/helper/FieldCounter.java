package uk.gov.dvsa.mot.helper;

import java.lang.reflect.Field;

public class FieldCounter {

    public static int getNumberOfFieldsFromClass(Field[] classFields, Field[] superFields, Field[] superSuperFields) {
        int fieldCount = 0;

        fieldCount = getFieldCount(classFields);

        fieldCount += getFieldCount(superFields);

        fieldCount += getFieldCount(superSuperFields);

        return fieldCount;
    }

    public static int getNumberOfFieldsFromClass(Field[] classFields, Field[] superFields) {
        int fieldCount = 0;

        fieldCount = getFieldCount(classFields);

        fieldCount += getFieldCount(superFields);

        return fieldCount;
    }

    public static int getNumberOfFieldsFromClass(Field[] classFields) {
        int fieldCount = 0;

        fieldCount = getFieldCount(classFields);

        return fieldCount;
    }

    private static int getFieldCount(Field[] fields) {
        int fieldCount = 0;

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            if (!field.isSynthetic()) {
                fieldCount++;
            }
        }

        return fieldCount;
    }
}
