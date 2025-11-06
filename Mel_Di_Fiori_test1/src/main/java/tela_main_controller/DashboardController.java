package tela_main_controller;

import dao.DashboardDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Colmeia;
import service.RegaService;

// ✅ IMPORT ADICIONADO PARA CORRIGIR O ERRO
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardController {

    @FXML private Label totalPlantasLabel;
    @FXML private Label producaoLabel;
    @FXML private Label vendasLabel;
    @FXML private Label topVendaLabel;

    @FXML private LineChart<String, Number> graficoVendas;
    @FXML private BarChart<String, Number> graficoProducao;

    // ✅ NOVOS CAMPOS ADICIONADOS:
    @FXML private VBox itensParaRegar;
    @FXML private VBox itensNaoRegadas;

    private final String[] months = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun"};
    private DashboardDAO dashboardDAO;
    private RegaService regaService; // ✅ NOVO SERVICE

    @FXML
    public void initialize() {
        dashboardDAO = new DashboardDAO();
        regaService = new RegaService(); // ✅ INICIALIZAÇÃO DO SERVICE
        
        // ✅ NOVA CHAMADA: Atualiza status de rega automaticamente
        regaService.atualizarStatusRega();
        
        carregarDadosReais();
        // ✅ NOVAS CHAMADAS:
        carregarPlantasParaRegar();
        carregarPlantasNaoRegadas();
        
        Platform.runLater(() -> {
            int total = safeParseNumber(getTextOrEmpty(totalPlantasLabel));
            int prod  = safeParseNumber(getTextOrEmpty(producaoLabel));
            int vendas = safeParseNumber(getTextOrEmpty(vendasLabel));

            populateLineChartVendas(vendas);
            populateBarChartProducao(prod);
            adjustYAxisIfNumberAxis(Math.max(Math.max(total, prod), vendas));

            applyChartColors();
        });
    }

    private void carregarDadosReais() {
        int totalPlantas = dashboardDAO.getTotalPlantas();
        int plantasAtivas = dashboardDAO.getPlantasAtivas();
        int totalClientes = dashboardDAO.getTotalClientes();
        String topVenda = dashboardDAO.getTopVenda();

        totalPlantasLabel.setText(String.valueOf(totalPlantas));
        producaoLabel.setText(plantasAtivas + " ativas");
        vendasLabel.setText(totalClientes + " clientes");
        topVendaLabel.setText(topVenda);
    }

    // ✅ NOVO MÉTODO ADICIONADO:
    private void carregarPlantasParaRegar() {
        List<Colmeia> plantasParaRegar = regaService.getPlantasParaRegar();
        itensParaRegar.getChildren().clear();
        
        if (plantasParaRegar.isEmpty()) {
            Label vazio = new Label("Nenhuma planta para regar");
            vazio.setStyle("-fx-text-fill: rgba(255,255,255,0.85); -fx-font-size: 13px;");
            itensParaRegar.getChildren().add(vazio);
        } else {
            for (Colmeia planta : plantasParaRegar) {
                itensParaRegar.getChildren().add(createPlantNode(planta, "PARA_REGAR"));
            }
        }
    }

    // ✅ NOVO MÉTODO ADICIONADO:
    private void carregarPlantasNaoRegadas() {
        List<Colmeia> plantasNaoRegadas = regaService.getPlantasNaoRegadas();
        itensNaoRegadas.getChildren().clear();
        
        if (plantasNaoRegadas.isEmpty()) {
            Label vazio = new Label("Nenhuma planta não regada");
            vazio.setStyle("-fx-text-fill: rgba(255,255,255,0.85); -fx-font-size: 13px;");
            itensNaoRegadas.getChildren().add(vazio);
        } else {
            for (Colmeia planta : plantasNaoRegadas) {
                itensNaoRegadas.getChildren().add(createPlantNode(planta, "NAO_REGADA"));
            }
        }
    }

    // ✅ MÉTODO MODIFICADO: Agora calcula dias desde a ÚLTIMA REGA e inclui botão
    private HBox createPlantNode(Colmeia planta, String status) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle(
            "-fx-background-color: rgba(255,255,255,0.06);" +
            "-fx-padding: 10;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: rgba(255,255,255,0.06);" +
            "-fx-border-width: 1;"
        );

        // ✅ NOVA LÓGICA: Cores diferentes por status
        String dotColor = status.equals("PARA_REGAR") ? "#FFA000" : "#F44336";

        Label dot = new Label("●");
        dot.setStyle("-fx-font-size: 12px; -fx-text-fill: " + dotColor + ";");

        String nameText = planta.getIdentificacao() + 
                         (planta.getLocalizacao() != null && !planta.getLocalizacao().isEmpty() ? 
                         " — " + planta.getLocalizacao() : "");
        Label name = new Label(nameText);
        name.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: 600;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // ✅ MODIFICADO: Agora calcula dias desde a ÚLTIMA REGA (não instalação)
        LocalDate dataReferencia = planta.getDataUltimaRega() != null ? 
                                  planta.getDataUltimaRega() : planta.getDataInstalacao();
        
        long dias = java.time.temporal.ChronoUnit.DAYS.between(
            dataReferencia, 
            java.time.LocalDate.now()
        );
        
        Label days = new Label(dias + (dias == 1 ? " dia" : " dias"));
        days.setStyle("-fx-text-fill: rgba(255,255,255,0.85); -fx-font-size: 11px; -fx-font-weight: 500;");

        // ✅ BOTÃO: Marcar como regada (AGORA RESETA TEMPORIZADOR)
        Button btnRegar = new Button("✓");
        btnRegar.setStyle(
            "-fx-background-color: #4CAF50; " +
            "-fx-text-fill: white; " +
            "-fx-border-radius: 4px; " +
            "-fx-background-radius: 4px; " +
            "-fx-padding: 2 6px; " +
            "-fx-font-size: 10px; " +
            "-fx-cursor: hand;"
        );
        
        btnRegar.setOnAction(e -> {
            regaService.marcarComoRegada(planta);
            carregarPlantasParaRegar();
            carregarPlantasNaoRegadas();
        });

        box.getChildren().addAll(dot, name, spacer, days, btnRegar);
        return box;
    }

    private void populateLineChartVendas(int vendasFinal) {
        graficoVendas.getData().clear();
        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("Vendas");

        int steps = months.length;
        int step = Math.max(1, Math.round(vendasFinal / (float)Math.max(1, steps)));

        for (int i = 0; i < months.length; i++) {
            int val = Math.max(0, vendasFinal - (months.length - 1 - i) * step);
            s.getData().add(new XYChart.Data<>(months[i], val));
        }
        graficoVendas.getData().add(s);
    }

    private void populateBarChartProducao(int prodFinal) {
        graficoProducao.getData().clear();
        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("Produção");

        int steps = months.length;
        int step = Math.max(1, Math.round(prodFinal / (float)Math.max(1, steps)));

        for (int i = 0; i < months.length; i++) {
            int val = Math.max(0, prodFinal - (months.length - 1 - i) * step);
            s.getData().add(new XYChart.Data<>(months[i], val));
        }
        graficoProducao.getData().add(s);
    }

    private void adjustYAxisIfNumberAxis(int maxValue) {
        if (graficoVendas.getYAxis() instanceof NumberAxis) {
            NumberAxis y = (NumberAxis) graficoVendas.getYAxis();
            double upper = Math.max(10, maxValue * 1.2);
            y.setAutoRanging(false);
            y.setLowerBound(0);
            y.setUpperBound(Math.ceil(upper));
            y.setTickUnit(Math.max(1, Math.ceil(upper / 5.0)));
        }
    }

    private void applyChartColors() {
        graficoVendas.lookupAll(".default-color0.chart-series-line")
                .forEach(n -> n.setStyle("-fx-stroke: #388E3C; -fx-stroke-width: 2px;"));
        graficoVendas.lookupAll(".default-color0.chart-line-symbol")
                .forEach(n -> n.setStyle("-fx-background-color: #388E3C, white;"));

        graficoProducao.lookupAll(".default-color0.chart-bar")
                .forEach(n -> n.setStyle("-fx-bar-fill: #66BB6A;"));
    }

    private String getTextOrEmpty(Label l) {
        return l == null || l.getText() == null ? "" : l.getText();
    }

    private int safeParseNumber(String text) {
        if (text == null) return 0;
        try {
            Matcher m = Pattern.compile("(\\d+)").matcher(text);
            if (m.find()) return Integer.parseInt(m.group(1));
        } catch (Exception ignored) {}
        return 0;
    }

    // Métodos mantidos para compatibilidade (não usados no novo sistema)
    public void setPlantasNaoRegadas(List<Plant> plantas) {
        // Mantido para compatibilidade, mas não usado mais
    }

    public static class Plant {
        public final String name;
        public final String location;
        public final int daysSinceWatered;

        public Plant(String name, String location, int daysSinceWatered) {
            this.name = name;
            this.location = location;
            this.daysSinceWatered = Math.max(0, daysSinceWatered);
        }
    }
}