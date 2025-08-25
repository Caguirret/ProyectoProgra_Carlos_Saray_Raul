package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    //Esta clase esta definida de manera que se pueda tener una administracion del acceso a la base de datos con el persistences
    //Esta clase esta hecha para evitar multiples instancias del EntityManagerFactory y evitar errores en el acceso a datos
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("memoriaPU");

    //al llamar la clase tenemos una unica clase y un unico EntityManagerFactory que tiene acceso a la base de datos
    public static EntityManager getEntityManager(){
        return emf.createEntityManager();
    }



}
