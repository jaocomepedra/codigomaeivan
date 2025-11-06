package model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "colmeias")
public class Colmeia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String identificacao;

    @Column(nullable = false)
    private String localizacao;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDate dataInstalacao;

    private int numeroQuadros;

    @Column(length = 2000)
    private String observacoes;

    // ✅ NOVOS CAMPOS ADICIONADOS PARA CONTROLE DE REGA:
    @Column(name = "data_ultima_rega")
    private LocalDate dataUltimaRega;

    @Column(name = "entrou_lista_rega")
    private LocalDate entrouListaRega;

    @Column(name = "status_rega")
    private String statusRega; // "NORMAL", "PARA_REGAR", "NAO_REGADA"

    // 🔄 CONSTRUTOR VAZIO MODIFICADO
    public Colmeia() {
        this.dataUltimaRega = LocalDate.now();
        this.entrouListaRega = null;
        this.statusRega = "NORMAL";
    }

    // 🔄 CONSTRUTOR COM PARÂMETROS
    public Colmeia(String identificacao, String localizacao, String tipo, String status, LocalDate dataInstalacao,
                   int numeroQuadros, String observacoes) {
        this();
        this.identificacao = identificacao;
        this.localizacao = localizacao;
        this.tipo = tipo;
        this.status = status;
        this.dataInstalacao = dataInstalacao;
        this.numeroQuadros = numeroQuadros;
        this.observacoes = observacoes;
    }

    // ➕ GETTERS E SETTERS ORIGINAIS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataInstalacao() {
        return dataInstalacao;
    }

    public void setDataInstalacao(LocalDate dataInstalacao) {
        this.dataInstalacao = dataInstalacao;
    }

    public int getNumeroQuadros() {
        return numeroQuadros;
    }

    public void setNumeroQuadros(int numeroQuadros) {
        this.numeroQuadros = numeroQuadros;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    // ✅ NOVOS GETTERS E SETTERS ADICIONADOS
    public LocalDate getDataUltimaRega() {
        return dataUltimaRega;
    }

    public void setDataUltimaRega(LocalDate dataUltimaRega) {
        this.dataUltimaRega = dataUltimaRega;
    }

    public LocalDate getEntrouListaRega() {
        return entrouListaRega;
    }

    public void setEntrouListaRega(LocalDate entrouListaRega) {
        this.entrouListaRega = entrouListaRega;
    }

    public String getStatusRega() {
        return statusRega;
    }

    public void setStatusRega(String statusRega) {
        this.statusRega = statusRega;
    }

    @Override
    public String toString() {
        return "Colmeia [id=" + id + ", identificacao=" + identificacao + ", localizacao=" + localizacao + ", tipo="
                + tipo + ", status=" + status + ", dataInstalacao=" + dataInstalacao + ", numeroQuadros="
                + numeroQuadros + ", observacoes=" + observacoes + "]";
    }
}