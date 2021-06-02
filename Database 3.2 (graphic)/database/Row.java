package database;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

abstract public class Row {
    public final int id;
    public Row(int id) { this.id = id; }

    @Override public String toString() {
        StringBuilder str = new StringBuilder(this.getClass().getSimpleName() + "{id=" + id);
        for(Method method: this.getClass().getMethods())
            for(Annotation annotation: method.getAnnotations())
                if(annotation.annotationType().equals(Getter.class))
                    try {
                        str.append(", ").append(((Getter) annotation).alias()).append("=").append(method.invoke(this));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
        return str.append('}').toString();
    }
}
