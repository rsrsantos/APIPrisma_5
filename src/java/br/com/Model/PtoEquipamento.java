package br.com.Model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PtoEquipamento")//caso o nome da tabela seja diferente informe aqui
public class PtoEquipamento implements Serializable {

    @Id
    @Column(name = "idEquipamento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "descricao", length = 50, nullable = false)
    private String descricao;

    @Column(name = "ip", length = 20, nullable = false)
    private String ip;

    @Column(name = "porta", nullable = false)
    private String porta;

    @Column(name = "mac", length = 20)
    private String mac;

    @Column(name = "ultimonsr")
    private int ultimonsr;

    @Column(name = "localidade", nullable = false)
    private String localidade;

    @Column(name = "modelo")
    private String modelo;

    private String tipoCorte;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "tipo_corte_id")
    private TipoCorte tipocorte;

    // Gets and Seters =========================================================
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getTipoCorte() {
        return tipoCorte;
    }

    public void setTipoCorte(String tipoCorte) {
        this.tipoCorte = tipoCorte;
    }

    public String getSenhaMenu() {
        return senhaMenu;
    }

    public void setSenhaMenu(String senhaMenu) {
        this.senhaMenu = senhaMenu;
    }

    @Column(name = "senha")
    private String senhaMenu;

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getUltimonsr() {
        return ultimonsr;
    }

    public void setUltimonsr(int ultimonsr) {
        this.ultimonsr = ultimonsr;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public TipoCorte getTipocorte() {
        return tipocorte;
    }

    public void setTipocorte(TipoCorte tipocorte) {
        this.tipocorte = tipocorte;
    }

}
