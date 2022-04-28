package com.projeto_venda.venda;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UsuarioController {

    User usuario = new User();
    UsuarioDAO usuarioDAO = new UsuarioDAO();

    @RequestMapping(value = "/formCadastraUser")
    public void formCadastraUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:8080/indexCadastraUser.html");
    }

    @RequestMapping(value = "/cadastraUser", method = RequestMethod.POST)
    public void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var nome = request.getParameter("nome");
        var senha = request.getParameter("senha");
        var email = request.getParameter("email");
        usuario.setNome(nome);
        usuario.setSenha(senha);
        usuario.setEmail(email);
        usuario.setId(usuarioDAO.qtdUsuario() + 1);
        usuario.setTipoUser(1);
        usuarioDAO.addUser(usuario);
        response.sendRedirect("http://localhost:8080/index.html");
    }

    @RequestMapping("/parte2")
    public void doParte2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println("parte 2 do processamento kkk");
    }

    @RequestMapping("/parte1")
    public void doEncaminhar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher encaminhar = request.getRequestDispatcher("/parte2");
        encaminhar.forward(request, response);
    }

    @RequestMapping("/redirecionarTeste")
    public void doRedirecionamentoTeste(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println("Teste de redirect");
    }

    @RequestMapping("/redirecionar")
    public void doRedirecionamento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:8080/redirecionarTeste");
    }
}