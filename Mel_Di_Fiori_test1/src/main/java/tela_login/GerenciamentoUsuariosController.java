package tela_login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import util.SessaoUsuario;

public class GerenciamentoUsuariosController {

    @FXML
    private void abrirCadastroUsuario() {
        // ✅ VERIFICA SE O USUÁRIO É ADMIN
        if (!SessaoUsuario.isAdmin()) {
            mostrarAlertaAcessoNegado();
            return;
        }

        try {
            Node tela = FXMLLoader.load(getClass().getResource("/telas/view/TelaCadastroUsuario.fxml"));
            StackPane painel = (StackPane) ((Node) tela).getScene().lookup("#painelConteudo");
            painel.getChildren().setAll(tela);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao carregar tela de cadastro: " + e.getMessage());
        }
    }

    @FXML
    private void abrirListaUsuarios() {
        // ✅ VERIFICA SE O USUÁRIO É ADMIN
        if (!SessaoUsuario.isAdmin()) {
            mostrarAlertaAcessoNegado();
            return;
        }

        // TODO: Implementar tela de listagem de usuários
        mostrarAlertaInfo("Funcionalidade em desenvolvimento");
    }

    @FXML
    private void voltarDashboard() {
        try {
            Node tela = FXMLLoader.load(getClass().getResource("/telas/view/TelaDashboard.fxml"));
            StackPane painel = (StackPane) ((Node) tela).getScene().lookup("#painelConteudo");
            painel.getChildren().setAll(tela);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao voltar para dashboard: " + e.getMessage());
        }
    }

    private void mostrarAlertaAcessoNegado() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Acesso Negado");
        alert.setHeaderText("Permissão Insuficiente");
        alert.setContentText("Apenas usuários ADMIN podem acessar esta funcionalidade.");
        alert.showAndWait();
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Ocorreu um erro");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAlertaInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText("Funcionalidade em desenvolvimento");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}