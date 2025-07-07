package service;

import model.ProjetoPON;
import java.util.ArrayList;
import java.util.List;

public class ValidacaoService {

    public List<String> validar(ProjetoPON projeto) {
        List<String> erros = new ArrayList<>();
        if (projeto.potenciaTransmissao != null && (projeto.potenciaTransmissao < 0 || projeto.potenciaTransmissao > 10))
            erros.add("Potência de transmissão (Pt) fora do intervalo típico (0 a 10 dBm).");
        if (projeto.sensibilidadeReceptor != null && (projeto.sensibilidadeReceptor < -33 || projeto.sensibilidadeReceptor > -18))
            erros.add("Sensibilidade do receptor (Sr) fora do intervalo (-33 a -18 dBm).");
        if (projeto.atenuacaoFibra != null && (projeto.atenuacaoFibra < 0.2 || projeto.atenuacaoFibra > 0.5))
            erros.add("Atenuação da fibra (Af) fora do intervalo (0.2 a 0.5 dB/km).");
        if (projeto.comprimentoFibra != null && (projeto.comprimentoFibra < 1 || projeto.comprimentoFibra > 20))
            erros.add("Comprimento da fibra (Cf) fora do intervalo (1 a 20 km).");
        if (projeto.perdaPorConector != null && (projeto.perdaPorConector < 0.2 || projeto.perdaPorConector > 1.0))
            erros.add("Perda por conector (Pc) fora do intervalo (0.2 a 1.0 dB).");
        if (projeto.numeroDeConectores != null && (projeto.numeroDeConectores < 0 || projeto.numeroDeConectores > 10))
            erros.add("Quantidade de conectores (Nc) fora do intervalo (0 a 10).");
        if (projeto.perdaPorSplitter != null && (projeto.perdaPorSplitter < 0 || projeto.perdaPorSplitter > 18))
            erros.add("Perda por splitter (Ps) fora do intervalo (0 a 18 dB).");
        if (projeto.margemDeSeguranca != null && (projeto.margemDeSeguranca < 1 || projeto.margemDeSeguranca > 6))
            erros.add("Margem de segurança (Ms) fora do intervalo (1 a 6 dB).");
        return erros;
    }
}