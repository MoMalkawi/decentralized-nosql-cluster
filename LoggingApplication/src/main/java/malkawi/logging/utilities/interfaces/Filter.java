package malkawi.logging.utilities.interfaces;

public interface Filter <T> {

    boolean verify(T t);

}
