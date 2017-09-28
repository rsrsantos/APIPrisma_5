package br.com.Model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class EspelhoPonto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "bat_entrada_01" )
    private String entrada01;

    @Column(name = "bat_saida_01")
    private String saida01;

    @Column(name = "bat_entrada_02")
    private String entrada02;

    @Column(name = "bat_saida_02")
    private String saida02;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;
    
    @ManyToOne
    @JoinColumn(name = "nome_usuario_id")
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "folhaperiodo_id")
    private FolhaPeriodo folhaperiodo;
    
    @Column(name = "nome_funcionario")
    private String nome_Funcionario;
    
    private String data;
    
    private String pis;
    
    private String periodo;
    

    //Gets and Seters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public String getEntrada01() {
        return entrada01;
    }

    public void setEntrada01(String entrada01) {
        this.entrada01 = entrada01;
    }

    public String getSaida01() {
        return saida01;
    }

    public void setSaida01(String saida01) {
        this.saida01 = saida01;
    }

    public String getEntrada02() {
        return entrada02;
    }

    public void setEntrada02(String entrada02) {
        this.entrada02 = entrada02;
    }

    public String getSaida02() {
        return saida02;
    }

    public void setSaida02(String saida02) {
        this.saida02 = saida02;
    }


    public String getNome_Funcionario() {
        return nome_Funcionario;
    }

    public void setNome_Funcionario(String nome_Funcionario) {
        this.nome_Funcionario = nome_Funcionario;
    }

    public FolhaPeriodo getFolhaperiodo() {
        return folhaperiodo;
    }

    public void setFolhaperiodo(FolhaPeriodo folhaperiodo) {
        this.folhaperiodo = folhaperiodo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        return "EspelhoPonto{" + "id=" + id + ", entrada01=" + entrada01 + ", saida01=" + saida01 + ", entrada02=" + entrada02 + ", saida02=" + saida02 + ", funcionario=" + funcionario + ", usuario=" + usuario + ", folhaperiodo=" + folhaperiodo + ", nome_Funcionario=" + nome_Funcionario + ", data=" + data + ", pis=" + pis + ", periodo=" + periodo + '}';
    }
    
    
    
    
    
    
    
    
    
    

}
