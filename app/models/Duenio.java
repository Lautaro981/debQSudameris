package models;


import com.debmedia.utils.models.debModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="Duenios")
public class Duenio extends debModel {

    private static final Finder<Long, Duenio> finder = (Finder<Long, Duenio>) debModel.getFinder(Duenio.class);

    public static Duenio findById(Long id) {
        return finder.where().eq("id", id).findUnique();
    }

    @NotNull
    @Id
    private int id;
    @NotNull

    private String nombre;
    @NotNull
    private int edad;
    @NotNull
    private String email;


    //Metodos


    public static List<Duenio> findAll() {
        return finder.all();
    }

    public static void create(Duenio duenio) {
        duenio.save();
    }

    public static void delete(Duenio duenio) {
        duenio.delete();
    }

    public static void update(Duenio duenio) {
        duenio.update();
    }

    @OneToMany(mappedBy = "duenio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Gato> gatoList;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    ;
}

