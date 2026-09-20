package app.DAOs;

import java.util.Set;

public interface IDAO <T>{
    T create(T t);
    Set<T> get();
    T getByID(Integer id);
    T update(T t);
    Integer delete(T t);
}
