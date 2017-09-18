package br.com.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;




@Entity
public class Horario implements Serializable {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String nome;
    private String diaSemana;
    
    private long tolerancia_antesEntrada;
    private long tolerancia_depoisEntrada;
    
    private long tolerancia_antesSaida;
    private long tolerancia_depoisSaida;
    
    private boolean compensado;
    
    //Segunda - Feira
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT1S;

    @Temporal(TemporalType.TIME)
    private Date horaSaidaT1S;
    
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT2S;
    
    @Temporal(TemporalType.TIME)
    private Date horaSaidaT2S;
    
    //Terça-Feira
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT1T;
    
    @Temporal(TemporalType.TIME)
    private Date horaSaidaT1T;
    
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT2T;
    
    @Temporal(TemporalType.TIME)
    private Date horaSaidaT2T;
    
    //Quarta-Feira
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT1Q;
    
    @Temporal(TemporalType.TIME)
    private Date horaSaidaT1Q;
    
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT2Q;
    
    @Temporal(TemporalType.TIME)
    private Date horaSaidaT2Q;
    
    //Quinta-Feira
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT1QF;
    
    @Temporal(TemporalType.TIME)
    private Date horaSaidaT1QF;
    
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT2QF;
    
    @Temporal(TemporalType.TIME)
    private Date horaSaidaT2QF;
    
    //Sexta-Feira
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT1SX;
    
    @Temporal(TemporalType.TIME)
    private Date horaSaidaT1SX;
    
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT2SX;
    
    @Temporal(TemporalType.TIME)
    private Date horaSaidaT2SX;
    
    //Sabado
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT1SA;
    
    @Temporal(TemporalType.TIME)
    private Date horaSaidaT1SA;
    
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT2SA;
    
    @Temporal(TemporalType.TIME)
    private Date horaSaidaT2SA;
    
    //Domingo
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT1D;
    
    @Temporal(TemporalType.TIME)
    private Date horaSaidaT1D;
    
    @Temporal(TemporalType.TIME)
    private Date horaEntradaT2D;
    
    @Temporal(TemporalType.TIME)
    private Date horaSaidaT2D;
    
    
    
    
    
    
    
    
   //====== Gets and Setrs

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public long getTolerancia_antesEntrada() {
        return tolerancia_antesEntrada;
    }

    public void setTolerancia_antesEntrada(long tolerancia_antesEntrada) {
        this.tolerancia_antesEntrada = tolerancia_antesEntrada;
    }

    public long getTolerancia_depoisEntrada() {
        return tolerancia_depoisEntrada;
    }

    public void setTolerancia_depoisEntrada(long tolerancia_depoisEntrada) {
        this.tolerancia_depoisEntrada = tolerancia_depoisEntrada;
    }

    public long getTolerancia_antesSaida() {
        return tolerancia_antesSaida;
    }

    public void setTolerancia_antesSaida(long tolerancia_antesSaida) {
        this.tolerancia_antesSaida = tolerancia_antesSaida;
    }

    public long getTolerancia_depoisSaida() {
        return tolerancia_depoisSaida;
    }

    public void setTolerancia_depoisSaida(long tolerancia_depoisSaida) {
        this.tolerancia_depoisSaida = tolerancia_depoisSaida;
    }

    public boolean isCompensado() {
        return compensado;
    }

    public void setCompensado(boolean compensado) {
        this.compensado = compensado;
    }

    public Date getHoraEntradaT1S() {
        return horaEntradaT1S;
    }

    public void setHoraEntradaT1S(Date horaEntradaT1S) {
        this.horaEntradaT1S = horaEntradaT1S;
    }

    public Date getHoraSaidaT1S() {
        return horaSaidaT1S;
    }

    public void setHoraSaidaT1S(Date horaSaidaT1S) {
        this.horaSaidaT1S = horaSaidaT1S;
    }

    public Date getHoraEntradaT2S() {
        return horaEntradaT2S;
    }

    public void setHoraEntradaT2S(Date horaEntradaT2S) {
        this.horaEntradaT2S = horaEntradaT2S;
    }

    public Date getHoraSaidaT2S() {
        return horaSaidaT2S;
    }

    public void setHoraSaidaT2S(Date horaSaidaT2S) {
        this.horaSaidaT2S = horaSaidaT2S;
    }

    public Date getHoraEntradaT1T() {
        return horaEntradaT1T;
    }

    public void setHoraEntradaT1T(Date horaEntradaT1T) {
        this.horaEntradaT1T = horaEntradaT1T;
    }

    public Date getHoraSaidaT1T() {
        return horaSaidaT1T;
    }

    public void setHoraSaidaT1T(Date horaSaidaT1T) {
        this.horaSaidaT1T = horaSaidaT1T;
    }

    public Date getHoraEntradaT2T() {
        return horaEntradaT2T;
    }

    public void setHoraEntradaT2T(Date horaEntradaT2T) {
        this.horaEntradaT2T = horaEntradaT2T;
    }

    public Date getHoraSaidaT2T() {
        return horaSaidaT2T;
    }

    public void setHoraSaidaT2T(Date horaSaidaT2T) {
        this.horaSaidaT2T = horaSaidaT2T;
    }

    public Date getHoraEntradaT1Q() {
        return horaEntradaT1Q;
    }

    public void setHoraEntradaT1Q(Date horaEntradaT1Q) {
        this.horaEntradaT1Q = horaEntradaT1Q;
    }

    public Date getHoraSaidaT1Q() {
        return horaSaidaT1Q;
    }

    public void setHoraSaidaT1Q(Date horaSaidaT1Q) {
        this.horaSaidaT1Q = horaSaidaT1Q;
    }

    public Date getHoraEntradaT2Q() {
        return horaEntradaT2Q;
    }

    public void setHoraEntradaT2Q(Date horaEntradaT2Q) {
        this.horaEntradaT2Q = horaEntradaT2Q;
    }

    public Date getHoraSaidaT2Q() {
        return horaSaidaT2Q;
    }

    public void setHoraSaidaT2Q(Date horaSaidaT2Q) {
        this.horaSaidaT2Q = horaSaidaT2Q;
    }

    public Date getHoraEntradaT1QF() {
        return horaEntradaT1QF;
    }

    public void setHoraEntradaT1QF(Date horaEntradaT1QF) {
        this.horaEntradaT1QF = horaEntradaT1QF;
    }

    public Date getHoraSaidaT1QF() {
        return horaSaidaT1QF;
    }

    public void setHoraSaidaT1QF(Date horaSaidaT1QF) {
        this.horaSaidaT1QF = horaSaidaT1QF;
    }

    public Date getHoraEntradaT2QF() {
        return horaEntradaT2QF;
    }

    public void setHoraEntradaT2QF(Date horaEntradaT2QF) {
        this.horaEntradaT2QF = horaEntradaT2QF;
    }

    public Date getHoraSaidaT2QF() {
        return horaSaidaT2QF;
    }

    public void setHoraSaidaT2QF(Date horaSaidaT2QF) {
        this.horaSaidaT2QF = horaSaidaT2QF;
    }

    public Date getHoraEntradaT1SX() {
        return horaEntradaT1SX;
    }

    public void setHoraEntradaT1SX(Date horaEntradaT1SX) {
        this.horaEntradaT1SX = horaEntradaT1SX;
    }

    public Date getHoraSaidaT1SX() {
        return horaSaidaT1SX;
    }

    public void setHoraSaidaT1SX(Date horaSaidaT1SX) {
        this.horaSaidaT1SX = horaSaidaT1SX;
    }

    public Date getHoraEntradaT2SX() {
        return horaEntradaT2SX;
    }

    public void setHoraEntradaT2SX(Date horaEntradaT2SX) {
        this.horaEntradaT2SX = horaEntradaT2SX;
    }

    public Date getHoraSaidaT2SX() {
        return horaSaidaT2SX;
    }

    public void setHoraSaidaT2SX(Date horaSaidaT2SX) {
        this.horaSaidaT2SX = horaSaidaT2SX;
    }

    public Date getHoraEntradaT1SA() {
        return horaEntradaT1SA;
    }

    public void setHoraEntradaT1SA(Date horaEntradaT1SA) {
        this.horaEntradaT1SA = horaEntradaT1SA;
    }

    public Date getHoraSaidaT1SA() {
        return horaSaidaT1SA;
    }

    public void setHoraSaidaT1SA(Date horaSaidaT1SA) {
        this.horaSaidaT1SA = horaSaidaT1SA;
    }

    public Date getHoraEntradaT2SA() {
        return horaEntradaT2SA;
    }

    public void setHoraEntradaT2SA(Date horaEntradaT2SA) {
        this.horaEntradaT2SA = horaEntradaT2SA;
    }

    public Date getHoraSaidaT2SA() {
        return horaSaidaT2SA;
    }

    public void setHoraSaidaT2SA(Date horaSaidaT2SA) {
        this.horaSaidaT2SA = horaSaidaT2SA;
    }

    public Date getHoraEntradaT1D() {
        return horaEntradaT1D;
    }

    public void setHoraEntradaT1D(Date horaEntradaT1D) {
        this.horaEntradaT1D = horaEntradaT1D;
    }

    public Date getHoraSaidaT1D() {
        return horaSaidaT1D;
    }

    public void setHoraSaidaT1D(Date horaSaidaT1D) {
        this.horaSaidaT1D = horaSaidaT1D;
    }

    public Date getHoraEntradaT2D() {
        return horaEntradaT2D;
    }

    public void setHoraEntradaT2D(Date horaEntradaT2D) {
        this.horaEntradaT2D = horaEntradaT2D;
    }

    public Date getHoraSaidaT2D() {
        return horaSaidaT2D;
    }

    public void setHoraSaidaT2D(Date horaSaidaT2D) {
        this.horaSaidaT2D = horaSaidaT2D;
    }
    
    
    
    
    //Has cod - Equal

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 23 * hash + Objects.hashCode(this.diaSemana);
        hash = 23 * hash + (int) (this.tolerancia_antesEntrada ^ (this.tolerancia_antesEntrada >>> 32));
        hash = 23 * hash + (int) (this.tolerancia_depoisEntrada ^ (this.tolerancia_depoisEntrada >>> 32));
        hash = 23 * hash + (int) (this.tolerancia_antesSaida ^ (this.tolerancia_antesSaida >>> 32));
        hash = 23 * hash + (int) (this.tolerancia_depoisSaida ^ (this.tolerancia_depoisSaida >>> 32));
        hash = 23 * hash + (this.compensado ? 1 : 0);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT1S);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT1S);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT2S);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT2S);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT1T);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT1T);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT2T);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT2T);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT1Q);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT1Q);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT2Q);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT2Q);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT1QF);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT1QF);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT2QF);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT2QF);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT1SX);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT1SX);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT2SX);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT2SX);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT1SA);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT1SA);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT2SA);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT2SA);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT1D);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT1D);
        hash = 23 * hash + Objects.hashCode(this.horaEntradaT2D);
        hash = 23 * hash + Objects.hashCode(this.horaSaidaT2D);
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
        final Horario other = (Horario) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.tolerancia_antesEntrada != other.tolerancia_antesEntrada) {
            return false;
        }
        if (this.tolerancia_depoisEntrada != other.tolerancia_depoisEntrada) {
            return false;
        }
        if (this.tolerancia_antesSaida != other.tolerancia_antesSaida) {
            return false;
        }
        if (this.tolerancia_depoisSaida != other.tolerancia_depoisSaida) {
            return false;
        }
        if (this.compensado != other.compensado) {
            return false;
        }
        if (!Objects.equals(this.diaSemana, other.diaSemana)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT1S, other.horaEntradaT1S)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT1S, other.horaSaidaT1S)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT2S, other.horaEntradaT2S)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT2S, other.horaSaidaT2S)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT1T, other.horaEntradaT1T)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT1T, other.horaSaidaT1T)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT2T, other.horaEntradaT2T)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT2T, other.horaSaidaT2T)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT1Q, other.horaEntradaT1Q)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT1Q, other.horaSaidaT1Q)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT2Q, other.horaEntradaT2Q)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT2Q, other.horaSaidaT2Q)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT1QF, other.horaEntradaT1QF)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT1QF, other.horaSaidaT1QF)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT2QF, other.horaEntradaT2QF)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT2QF, other.horaSaidaT2QF)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT1SX, other.horaEntradaT1SX)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT1SX, other.horaSaidaT1SX)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT2SX, other.horaEntradaT2SX)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT2SX, other.horaSaidaT2SX)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT1SA, other.horaEntradaT1SA)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT1SA, other.horaSaidaT1SA)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT2SA, other.horaEntradaT2SA)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT2SA, other.horaSaidaT2SA)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT1D, other.horaEntradaT1D)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT1D, other.horaSaidaT1D)) {
            return false;
        }
        if (!Objects.equals(this.horaEntradaT2D, other.horaEntradaT2D)) {
            return false;
        }
        if (!Objects.equals(this.horaSaidaT2D, other.horaSaidaT2D)) {
            return false;
        }
        return true;
    }
    
    
    
    

    

  
    
    
    
}
