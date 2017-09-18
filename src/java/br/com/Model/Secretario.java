package br.com.Model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;





@Entity
public class Secretario implements Serializable {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String nomeSecretario;
    
    
    
    
    
    
    // Gets and Seters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeSecretario() {
        return nomeSecretario;
    }

    public void setNomeSecretario(String nomeSecretario) {
        this.nomeSecretario = nomeSecretario;
    }
    
    
    
    
    
    
    
    //Hascode e Equals

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Secretario other = (Secretario) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
    
    
    
    
    
    
}
