package model;

import java.util.function.Function;

// Enum que aplica o padrão Strategy para o cálculo de cada variável.
public enum ParametroCalculavel {
    PT("Potência de Transmissão", "dBm", p -> p.sensibilidadeReceptor + (p.atenuacaoFibra * p.comprimentoFibra + p.perdaPorConector * p.numeroDeConectores + p.perdaPorSplitter) + p.margemDeSeguranca),
    SR("Sensibilidade do Receptor", "dBm", p -> p.potenciaTransmissao - (p.atenuacaoFibra * p.comprimentoFibra + p.perdaPorConector * p.numeroDeConectores + p.perdaPorSplitter) - p.margemDeSeguranca),
    AF("Atenuação da Fibra", "dB/km", p -> (p.potenciaTransmissao - p.sensibilidadeReceptor - p.perdaPorConector * p.numeroDeConectores - p.perdaPorSplitter - p.margemDeSeguranca) / p.comprimentoFibra),
    CF("Comprimento da Fibra", "km", p -> (p.potenciaTransmissao - p.sensibilidadeReceptor - p.perdaPorConector * p.numeroDeConectores - p.perdaPorSplitter - p.margemDeSeguranca) / p.atenuacaoFibra),
    PC("Perda por Conector", "dB", p -> (p.potenciaTransmissao - p.sensibilidadeReceptor - p.atenuacaoFibra * p.comprimentoFibra - p.perdaPorSplitter - p.margemDeSeguranca) / p.numeroDeConectores),
    NC("Quantidade de Conectores", "", p -> (p.potenciaTransmissao - p.sensibilidadeReceptor - p.atenuacaoFibra * p.comprimentoFibra - p.perdaPorSplitter - p.margemDeSeguranca) / p.perdaPorConector),
    PS("Perda por Splitter", "dB", p -> p.potenciaTransmissao - p.sensibilidadeReceptor - p.atenuacaoFibra * p.comprimentoFibra - p.perdaPorConector * p.numeroDeConectores - p.margemDeSeguranca),
    MS("Margem de Segurança", "dB", p -> p.potenciaTransmissao - p.sensibilidadeReceptor - (p.atenuacaoFibra * p.comprimentoFibra + p.perdaPorConector * p.numeroDeConectores + p.perdaPorSplitter));

    private final String descricao;
    private final String unidade;
    private final Function<ProjetoPON, Double> equacao;

    ParametroCalculavel(String descricao, String unidade, Function<ProjetoPON, Double> equacao) {
        this.descricao = descricao;
        this.unidade = unidade;
        this.equacao = equacao;
    }

    public String getDescricao() { return descricao; }
    public String getUnidade() { return unidade; }
    public double calcular(ProjetoPON projeto) {
        return equacao.apply(projeto);
    }
}