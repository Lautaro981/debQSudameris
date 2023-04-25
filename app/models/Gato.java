package models;

import com.debmedia.utils.models.debModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
@Table(name="Gatos")
public class Gato extends debModel {

    private static final Finder<Long,Gato>finder = (Finder<Long,Gato>) debModel.getFinder(Gato.class);

    public static Gato findById(Long id){
        return finder.where().eq("id",id).findUnique();
    }

    @Id
    private int id;

    @NotNull
    private String nombre;
    @NotNull
    private String raza;
    @NotNull
    @Column(name="colorOjos")
    private String colorOjos;
    @NotNull
    @Column(name="colorPelo")
    private String colorPelo;
    @NotNull
    @Column(name="cantPatas")
    private int cantPatas;
    @NotNull
    private boolean muerde;



    @ManyToOne()
    @JoinColumn(name = "idDuenio")
    private Duenio duenio;





    public Duenio getDuenio() {
        return duenio;
    }

    public void setDuenio(Duenio duenio) {
        this.duenio = duenio;
    }

    public static List<Gato> findAll(){ return finder.all();}

    public static void create(Gato gato){
        gato.save();
    }

    public static void delete (Gato gato){gato.delete();}

    public static void update(Gato gato){gato.update();}

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

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getColorOjos() {
        return colorOjos;
    }

    public void setColorOjos(String colorOjos) {
        this.colorOjos = colorOjos;
    }

    public String getColorPelo() {
        return colorPelo;
    }

    public void setColorPelo(String colorPelo) {
        this.colorPelo = colorPelo;
    }

    public int getCantPatas() {
        return cantPatas;
    }

    public void setCantPatas(int cantPatas) {
        this.cantPatas = cantPatas;
    }

    public boolean isMuerde() {
        return muerde;
    }

    public void setMuerde(boolean muerde) {
        this.muerde = muerde;
    }
}