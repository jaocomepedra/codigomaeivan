package dao;

import model.Colmeia;
import model.Cliente;
import java.util.List;

public class DashboardDAO {
    
    public int getTotalPlantas() {
        DAO<Colmeia> dao = new DAO<>(Colmeia.class);
        return dao.obterTodos(1000, 0).size();
    }
    
    public int getTotalClientes() {
        DAO<Cliente> dao = new DAO<>(Cliente.class);
        return dao.obterTodos(1000, 0).size();
    }
    
    public int getPlantasAtivas() {
        DAO<Colmeia> dao = new DAO<>(Colmeia.class);
        List<Colmeia> plantas = dao.obterTodos(1000, 0);
        return (int) plantas.stream()
                .filter(colmeia -> "Ativa".equals(colmeia.getStatus()))
                .count();
    }
    
    public int getClientesAtivos() {
        DAO<Cliente> dao = new DAO<>(Cliente.class);
        List<Cliente> clientes = dao.obterTodos(1000, 0);
        return (int) clientes.stream()
                .filter(cliente -> "ATIVO".equals(cliente.getStatus()))
                .count();
    }
    
    public String getTopVenda() {
        return "Rosa Vermelha";
    }
}