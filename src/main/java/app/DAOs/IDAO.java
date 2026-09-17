package app.DAOs;

import java.util.Set;

public interface IDAO <T>{
    T create(T t);
    Set<T> get();
    T getByID(Long id);
    T update(T t);
    Integer delete(T t);
}
