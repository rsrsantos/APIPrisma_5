package br.com.Model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Funcionario")
public class Funcionario implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nome")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "funcao_id")
    private Funcao funcao;

    @Column(name = "pis")
    private String pis;

    @Column(name = "matricula")
    private String matricula;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @Temporal(TemporalType.DATE)
    @Column(name = "dataadm")
    private Date dataadm;

    @Column(name = "empresa")
    private String empresa;

    private String ultima_alteracao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "horario_id")
    private Horario horario;
    
    private Boolean secretario;

    public Funcionario() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Gets and Seters ====================================================
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

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

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Funcao getFuncao() {
        return funcao;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }

    public Date getDataadm() {
        return dataadm;
    }

    public void setDataadm(Date dataadm) {
        this.dataadm = dataadm;
    }

    public String getUltima_alteracao() {
        return ultima_alteracao;
    }

    public void setUltima_alteracao(String ultima_alteracao) {
        this.ultima_alteracao = ultima_alteracao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Boolean getSecretario() {
        return secretario;
    }

    public void setSecretario(Boolean secretario) {
        this.secretario = secretario;
    }
    
    


    //Hascode and Equals
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Funcionario other = (Funcionario) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "Funcionario{" + "id=" + id + ", nome=" + nome + '}';
//    }

    
    
    
    


}
