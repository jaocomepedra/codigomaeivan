package tela_login;

import dao.DAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Usuario;
import util.SecurityUtil;
import util.SessaoUsuario;

import java.util.List;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblMensagem;

    @FXML
    private void fazerLogin() {
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            lblMensagem.setText("Preencha todos os campos.");
            return;
        }

        try {
            // ✅ CONSULTA O USUÁRIO PELO EMAIL (sem a senha na consulta)
            String jpql = "SELECT u FROM Usuario u WHERE u.email = ?1 AND u.status = 'ATIVO'";
            List<Usuario> usuarios = new DAO<>(Usuario.class).consultar(jpql, email);

            if (usuarios.isEmpty()) {
                lblMensagem.setText("E-mail ou senha inválidos.");
            } else {
                Usuario usuario = usuarios.get(0);
                
                // ✅ COMPARA A SENHA CRIPTOGRAFADA
                String senhaCriptografadaDigitada = SecurityUtil.hashMD5(senha);
                if (senhaCriptografadaDigitada.equals(usuario.getSenha())) {
                    // ✅ LOGIN BEM-SUCEDIDO - CONFIGURA A SESSÃO
                    SessaoUsuario.setUsuarioLogado(usuario);
                    abrirTelaPrincipal();
                } else {
                    lblMensagem.setText("E-mail ou senha inválidos.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMensagem.setText("Erro ao fazer login: " + e.getMessage());
        }
    }

    private void abrirTelaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/view/MainLayout.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Flor & Ser - Sistema de Floricultura");
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            lblMensagem.setText("Erro ao carregar a aplicação.");
        }
    }
}