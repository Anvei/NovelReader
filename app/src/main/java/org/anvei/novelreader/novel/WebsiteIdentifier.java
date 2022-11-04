package org.anvei.novelreader.novel;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;

public enum WebsiteIdentifier {
    SFACG,
    BIQUMU,
    //YUNXI,
    W147XS;

    public static @Nullable WebsiteIdentifier getIdentifier(String str) {
        str = str.toUpperCase();
        WebsiteIdentifier res = null;
        Class<?> clazz = WebsiteIdentifier.class;
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (str.equals(field.getName())) {
                try {
                    res = (WebsiteIdentifier) field.get(null);
                    break;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }
}
