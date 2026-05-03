package br.com.clubedossargentos.entity;

import br.com.clubedossargentos.enums.EspacoLocacao;
import br.com.clubedossargentos.enums.StatusAluguel;
import br.com.clubedossargentos.enums.TipoPagamento;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aluguais_espaco")
public class AluguelEspaco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean socio;

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    @Column(nullable = false)
    private String nomeResponsavel;

    @Column(nullable = false, length = 11)
    private String cpfResponsavel;

    @Column(nullable = false)
    private String enderecoResponsavel;

    @Column(nullable = false)
    private LocalDate dataEvento;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFim;

    @ElementCollection(targetClass = EspacoLocacao.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "aluguel_espacos_selecionados", joinColumns = @JoinColumn(name = "aluguel_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "espaco", nullable = false)
    private List<EspacoLocacao> espacosSelecionados = new ArrayList<>();

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private Integer parcelas = 1;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorPago = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoPagamento tipoPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAluguel status = StatusAluguel.PENDENTE;

    private String observacao;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    private LocalDateTime dataPagamento;

    public Long getId() { return id; }
    public Boolean getSocio() { return socio; }
    public Pessoa getPessoa() { return pessoa; }
    public String getNomeResponsavel() { return nomeResponsavel; }
    public String getCpfResponsavel() { return cpfResponsavel; }
    public String getEnderecoResponsavel() { return enderecoResponsavel; }
    public LocalDate getDataEvento() { return dataEvento; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFim() { return horaFim; }
    public List<EspacoLocacao> getEspacosSelecionados() { return espacosSelecionados; }
    public BigDecimal getValor() { return valor; }
    public Integer getParcelas() { return parcelas; }
    public BigDecimal getValorPago() { return valorPago; }
    public TipoPagamento getTipoPagamento() { return tipoPagamento; }
    public StatusAluguel getStatus() { return status; }
    public String getObservacao() { return observacao; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataPagamento() { return dataPagamento; }

    public void setId(Long id) { this.id = id; }
    public void setSocio(Boolean socio) { this.socio = socio; }
    public void setPessoa(Pessoa pessoa) { this.pessoa = pessoa; }
    public void setNomeResponsavel(String nomeResponsavel) { this.nomeResponsavel = nomeResponsavel; }
    public void setCpfResponsavel(String cpfResponsavel) { this.cpfResponsavel = cpfResponsavel; }
    public void setEnderecoResponsavel(String enderecoResponsavel) { this.enderecoResponsavel = enderecoResponsavel; }
    public void setDataEvento(LocalDate dataEvento) { this.dataEvento = dataEvento; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public void setHoraFim(LocalTime horaFim) { this.horaFim = horaFim; }
    public void setEspacosSelecionados(List<EspacoLocacao> espacosSelecionados) { this.espacosSelecionados = espacosSelecionados; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public void setParcelas(Integer parcelas) { this.parcelas = parcelas; }
    public void setValorPago(BigDecimal valorPago) { this.valorPago = valorPago; }
    public void setTipoPagamento(TipoPagamento tipoPagamento) { this.tipoPagamento = tipoPagamento; }
    public void setStatus(StatusAluguel status) { this.status = status; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
}
