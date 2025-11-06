package service;

import dao.DAO;
import model.Colmeia;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RegaService {
    
    public void atualizarStatusRega() {
        DAO<Colmeia> dao = new DAO<>(Colmeia.class);
        List<Colmeia> colmeias = dao.obterTodos(1000, 0);
        
        for (Colmeia colmeia : colmeias) {
            atualizarStatusColmeia(colmeia);
        }
    }
    
    private void atualizarStatusColmeia(Colmeia colmeia) {
        LocalDate hoje = LocalDate.now();
        LocalDate dataReferencia = colmeia.getDataUltimaRega(); // ✅ AGORA USA dataUltimaRega COMO REFERÊNCIA
        
        if (dataReferencia == null) {
            // Se nunca foi regada, usa a data de instalação
            dataReferencia = colmeia.getDataInstalacao();
        }
        
        if (dataReferencia == null) return;
        
        long diasDesdeReferencia = ChronoUnit.DAYS.between(dataReferencia, hoje);
        
        if (diasDesdeReferencia >= 5) {
            // Mais de 5 dias desde a última rega - Não Regada
            if (!"NAO_REGADA".equals(colmeia.getStatusRega())) {
                colmeia.setStatusRega("NAO_REGADA");
                salvarAtualizacao(colmeia);
            }
        } else if (diasDesdeReferencia >= 3) {
            // Entre 3 e 4 dias desde a última rega - Para Regar
            if (!"PARA_REGAR".equals(colmeia.getStatusRega())) {
                colmeia.setStatusRega("PARA_REGAR");
                if (colmeia.getEntrouListaRega() == null) {
                    colmeia.setEntrouListaRega(hoje);
                }
                salvarAtualizacao(colmeia);
            }
        } else {
            // Menos de 3 dias desde a última rega - Normal
            if (!"NORMAL".equals(colmeia.getStatusRega())) {
                colmeia.setStatusRega("NORMAL");
                colmeia.setEntrouListaRega(null);
                salvarAtualizacao(colmeia);
            }
        }
    }
    
    private void salvarAtualizacao(Colmeia colmeia) {
        try {
            new DAO<>(Colmeia.class).atualizarTransacional(colmeia);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Colmeia> getPlantasParaRegar() {
        String jpql = "SELECT c FROM Colmeia c WHERE c.statusRega = 'PARA_REGAR' ORDER BY c.dataUltimaRega";
        return new DAO<>(Colmeia.class).consultar(jpql);
    }
    
    public List<Colmeia> getPlantasNaoRegadas() {
        String jpql = "SELECT c FROM Colmeia c WHERE c.statusRega = 'NAO_REGADA' ORDER BY c.dataUltimaRega";
        return new DAO<>(Colmeia.class).consultar(jpql);
    }
    
    public void marcarComoRegada(Colmeia colmeia) {
        // ✅ RESETA O TEMPORIZADOR: dataUltimaRega = HOJE
        colmeia.setDataUltimaRega(LocalDate.now());
        colmeia.setStatusRega("NORMAL");
        colmeia.setEntrouListaRega(null);
        salvarAtualizacao(colmeia);
    }
}