package br.com.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    Funcionario funcionario;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "senha", length = 50, nullable = false)
    private String senha;

    @Column(name = "login", length = 50, nullable = false)
    private String login;

    @Column(name = "administrador")
    private String administrador;

    @Column(name = "bloqueado")
    private String bloqueado;

    private String ultimologin;

    private String ultima_alteracao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    //Gets and Seters
    public String getUltimologin() {
        return ultimologin;
    }

    public void setUltimologin(String ultimologin) {
        this.ultimologin = ultimologin;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public String getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(String bloqueado) {
        this.bloqueado = bloqueado;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
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

}
