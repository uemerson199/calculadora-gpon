import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.ProjetoPON;
import service.CalculadoraService;
import service.ValidacaoService;

import java.util.List;
import java.util.Objects;

public class CalculadoraPONApp extends Application {

    private final ValidacaoService validacaoService = new ValidacaoService();
    private final CalculadoraService calculadoraService = new CalculadoraService();

    private final TextField potenciaTxField = new TextField();
    private final TextField sensibilidadeRxField = new TextField();
    private final TextField atenuacaoFibraField = new TextField();
    private final TextField comprimentoFibraField = new TextField();
    private final TextField perdasConectoresField = new TextField();
    private final TextField quantidadeConectoresField = new TextField();
    private final TextField perdasSplittersField = new TextField();
    private final TextField margemSegurancaField = new TextField();
    private final TextArea resultadoArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Calculadora de Orçamento de Potência PON");

        StackPane rootPane = new StackPane();
        rootPane.setId("background-pane");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setId("main-layout");

        VBox headerBox = new VBox();
        headerBox.setId("header-box");
        headerBox.setAlignment(Pos.CENTER);
        Label titleLabel = new Label("Calculadora de Orçamento de Potência PON");
        titleLabel.setId("title-label");
        Label subtitleLabel = new Label("Preencha os parâmetros do seu projeto de rede óptica");
        subtitleLabel.setId("subtitle-label");
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);
        mainLayout.setTop(headerBox);

        HBox contentBox = new HBox(30);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20, 0, 20, 0));
        mainLayout.setCenter(contentBox);

        VBox leftColumn = new VBox(20);
        leftColumn.setId("left-column");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(18);

        // Ajuste para dar mais espaço aos labels completos
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(250); // largura suficiente para o texto completo
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(col1, col2);

        adicionarCampo(grid, 0, "📡", "Potência de Transmissão (dBm):", potenciaTxField);
        adicionarCampo(grid, 1, "⚡", "Sensibilidade de Recepção (dBm):", sensibilidadeRxField);
        adicionarCampo(grid, 2, "🛠️", "Atenuação da Fibra (dB/km):", atenuacaoFibraField);
        adicionarCampo(grid, 3, "📏", "Comprimento da Fibra (km):", comprimentoFibraField);

        HBox conectoresBox = new HBox(5, perdasConectoresField, new Label("Qtd:"), quantidadeConectoresField);
        conectoresBox.setAlignment(Pos.CENTER_LEFT);
        adicionarCampo(grid, 4, "🔌", "Perdas por Conectores (dB):", conectoresBox);

        adicionarCampo(grid, 5, "🔀", "Perdas por Splitters (dB):", perdasSplittersField);
        adicionarCampo(grid, 6, "🛡️", "Margem de Segurança (dB):", margemSegurancaField);

        Button calcularBtn = new Button("Analisar Projeto");
        calcularBtn.setPrefWidth(Double.MAX_VALUE);
        calcularBtn.setOnAction(e -> executarCalculo());

        leftColumn.getChildren().addAll(grid, calcularBtn);

        VBox rightColumn = new VBox(10);
        rightColumn.setId("right-column");
        Label resultadoLabel = new Label("Análise do Projeto");
        resultadoLabel.setId("result-title-label");

        resultadoArea.setId("result-area");
        resultadoArea.setEditable(false);
        resultadoArea.setWrapText(true);
        resultadoArea.setPrefHeight(300);
        resultadoArea.setText("Aguardando cálculo para exibir os resultados...");

        rightColumn.getChildren().addAll(resultadoLabel, resultadoArea);

        contentBox.getChildren().addAll(leftColumn, rightColumn);
        HBox.setHgrow(leftColumn, Priority.ALWAYS);
        HBox.setHgrow(rightColumn, Priority.ALWAYS);

        rootPane.getChildren().add(mainLayout);

        Scene scene = new Scene(rootPane, 950, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/resources/style.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }

    private void adicionarCampo(GridPane grid, int row, String icon, String labelText, Node... fields) {
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("icon-label");

        Label textLabel = new Label(labelText);
        textLabel.setWrapText(true); // permite quebrar texto, evitando "..."

        HBox labelBox = new HBox(5, iconLabel, textLabel);
        labelBox.setAlignment(Pos.CENTER_LEFT);

        grid.add(labelBox, 0, row);

        if (fields.length == 1) {
            grid.add(fields[0], 1, row);
        } else {
            HBox fieldBox = new HBox(5, fields);
            fieldBox.setAlignment(Pos.CENTER_LEFT);
            grid.add(fieldBox, 1, row);
        }
    }

    private void executarCalculo() {
        ProjetoPON projeto = lerEntradas();
        resultadoArea.getStyleClass().removeAll("result-success", "result-error", "result-neutral");

        List<String> errosValidacao = validacaoService.validar(projeto);
        if (!errosValidacao.isEmpty()) {
            resultadoArea.getStyleClass().add("result-error");
            resultadoArea.setText("⚠️ Foram encontrados os seguintes avisos:\n\n" + String.join("\n", errosValidacao));
            return;
        }

        String resultado = calculadoraService.calcular(projeto);

        if (resultado.contains("PROJETO VIÁVEL")) {
            resultadoArea.getStyleClass().add("result-success");
        } else {
            resultadoArea.getStyleClass().add("result-error");
        }
        resultadoArea.setText(resultado);
    }

    private ProjetoPON lerEntradas() {
        ProjetoPON projeto = new ProjetoPON();
        projeto.potenciaTransmissao = parseDouble(potenciaTxField.getText());
        projeto.sensibilidadeReceptor = parseDouble(sensibilidadeRxField.getText());
        projeto.atenuacaoFibra = parseDouble(atenuacaoFibraField.getText());
        projeto.comprimentoFibra = parseDouble(comprimentoFibraField.getText());
        projeto.perdaPorConector = parseDouble(perdasConectoresField.getText());
        projeto.numeroDeConectores = parseInt(quantidadeConectoresField.getText());
        projeto.perdaPorSplitter = parseDouble(perdasSplittersField.getText());
        projeto.margemDeSeguranca = parseDouble(margemSegurancaField.getText());
        return projeto;
    }

    private Double parseDouble(String texto) {
        try {
            return texto == null || texto.trim().isEmpty() ? null : Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInt(String texto) {
        try {
            return texto == null || texto.trim().isEmpty() ? null : Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
