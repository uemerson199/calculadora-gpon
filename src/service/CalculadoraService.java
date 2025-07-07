package service;

import model.ParametroCalculavel;
import model.ProjetoPON;

public class CalculadoraService {

    public String calcular(ProjetoPON projeto) {
        ParametroCalculavel parametroACalcular = null;
        int camposNulos = 0;

        if (projeto.potenciaTransmissao == null) { parametroACalcular = ParametroCalculavel.PT; camposNulos++; }
        if (projeto.sensibilidadeReceptor == null) { parametroACalcular = ParametroCalculavel.SR; camposNulos++; }
        if (projeto.atenuacaoFibra == null) { parametroACalcular = ParametroCalculavel.AF; camposNulos++; }
        if (projeto.comprimentoFibra == null) { parametroACalcular = ParametroCalculavel.CF; camposNulos++; }
        if (projeto.perdaPorConector == null) { parametroACalcular = ParametroCalculavel.PC; camposNulos++; }
        if (projeto.numeroDeConectores == null) { parametroACalcular = ParametroCalculavel.NC; camposNulos++; }
        if (projeto.perdaPorSplitter == null) { parametroACalcular = ParametroCalculavel.PS; camposNulos++; }
        if (projeto.margemDeSeguranca == null) { parametroACalcular = ParametroCalculavel.MS; camposNulos++; }

        if (camposNulos > 1) return "⚠️ Preencha todos os campos, exceto apenas UM para ser calculado.";
        if (camposNulos == 0) return "⚠️ Deixe um campo vazio para que ele seja calculado.";

        double resultado = parametroACalcular.calcular(projeto);
        String unidade = parametroACalcular.getUnidade();

        // Formata o resultado para ter no máximo 3 casas decimais
        String resultadoFormatado = String.format("%.3f", resultado);

        return String.format("✔️ %s = %s %s", parametroACalcular.getDescricao(), resultadoFormatado, unidade);
    }
}