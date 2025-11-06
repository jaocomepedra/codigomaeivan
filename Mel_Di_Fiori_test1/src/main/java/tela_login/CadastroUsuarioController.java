package tela_login;

import dao.DAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Usuario;
import util.SecurityUtil;
import util.SessaoUsuario;

import java.util.List;

public class CadastroUsuarioController {

    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private PasswordField txtConfirmarSenha;
    @FXML private ComboBox<String> comboPerfil;
    @FXML private Label lblMensagem;

    @FXML
    public void initialize() {
        // ✅ VERIFICAÇÃO ROBUSTA DE PERMISSÕES
        if (SessaoUsuario.getUsuarioLogado() == null) {
            mostrarErroEFechar("Acesso não permitido. Faça login primeiro.");
            return;
        }
        
        if (!SessaoUsuario.isAdmin()) {
            mostrarErroEFechar("Apenas administradores podem cadastrar novos usuários.");
            return;
        }

        // Se chegou aqui, é ADMIN - permite cadastrar ambos os perfis
        comboPerfil.getItems().addAll("FUNCIONARIO", "ADMIN");
        comboPerfil.setValue("FUNCIONARIO");
    }

    private void mostrarErroEFechar(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de Permissão");
        alert.setHeaderText("Acesso Negado");
        alert.setContentText(mensagem);
        alert.showAndWait();
        fecharJanela();
    }

    @FXML
    private void cadastrarUsuario() {
        if (!validarCampos()) {
            return;
        }

        // ✅ VERIFICAÇÃO EXTRA DE SEGURANÇA
        if (comboPerfil.getValue().equals("ADMIN") && !SessaoUsuario.isAdmin()) {
            lblMensagem.setText("Apenas administradores podem criar outros administradores.");
            return;
        }

        try {
            // ✅ CRIPTOGRAFA A SENHA ANTES DE SALVAR
            String senhaCriptografada = SecurityUtil.hashMD5(txtSenha.getText());

            Usuario novoUsuario = new Usuario(
                txtNome.getText(),
                txtEmail.getText(),
                senhaCriptografada, // Senha criptografada
                comboPerfil.getValue()
            );

            new DAO<>(Usuario.class).incluirTransacional(novoUsuario);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Usuário cadastrado com sucesso!");
            alert.setContentText("O usuário " + comboPerfil.getValue() + " foi cadastrado e já pode fazer login no sistema.");
            alert.showAndWait();

            fecharJanela();

        } catch (Exception e) {
            e.printStackTrace();
            lblMensagem.setText("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        // ✅ VALIDAÇÃO COMPLETA DOS CAMPOS
        if (txtNome.getText().isEmpty() || txtEmail.getText().isEmpty() ||
            txtSenha.getText().isEmpty() || txtConfirmarSenha.getText().isEmpty()) {
            lblMensagem.setText("Preencha todos os campos obrigatórios.");
            return false;
        }

        if (!txtSenha.getText().equals(txtConfirmarSenha.getText())) {
            lblMensagem.setText("As senhas não coincidem.");
            return false;
        }

        if (txtSenha.getText().length() < 6) {
            lblMensagem.setText("A senha deve ter no mínimo 6 caracteres.");
            return false;
        }

        if (!txtEmail.getText().contains("@") || !txtEmail.getText().contains(".")) {
            lblMensagem.setText("Digite um e-mail válido.");
            return false;
        }

        // ✅ VERIFICA SE EMAIL JÁ EXISTE
        String jpql = "SELECT u FROM Usuario u WHERE u.email = ?1";
        List<Usuario> usuarios = new DAO<>(Usuario.class).consultar(jpql, txtEmail.getText());
        if (!usuarios.isEmpty()) {
            lblMensagem.setText("Este e-mail já está cadastrado.");
            return false;
        }

        lblMensagem.setText("");
        return true;
    }

    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }
}