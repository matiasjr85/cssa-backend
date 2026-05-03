package br.com.clubedossargentos.dto;

public class HistoricoPagamentoDTO {

    private String mesReferencia;
    private boolean pago;
    private boolean renegociado;
    private String pagamentoId;

    public HistoricoPagamentoDTO(String mesReferencia, boolean pago, boolean renegociado, String pagamentoId) {
        this.mesReferencia = mesReferencia;
        this.pago = pago;
        this.renegociado = renegociado;
        this.pagamentoId = pagamentoId;
    }

    public String getMesReferencia() { return mesReferencia; }
    public boolean isPago() { return pago; }
    public boolean isRenegociado() { return renegociado; }
    public String getPagamentoId() { return pagamentoId; }
}
