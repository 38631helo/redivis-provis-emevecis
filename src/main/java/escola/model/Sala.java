package escola.model;

import jakarta.persistence.*;

@Entity
@Table(name = "escola")
public class Sala {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String nomeSala;

    @Column(nullable = false)
    private int capacidade;

    @Column(nullable = false)
    private boolean isReservada;

    public Sala{};

    public String getNomeSala(){
        return nomeSala;
    }

    public int getCapacidade(){
        return capacidade;
    }

    public boolean getReservada(){
        return isReservada;
    }

    public void setNomeSala(String nomeSala){
        this.nomeSala = nomeSala;
    }

    public void setCapacidade(int capacidade){
        this.capacidade = capacidade;
    }

    public void setReservada(boolean isReservada){
        this.isReservada = isReservada;
    }
}
