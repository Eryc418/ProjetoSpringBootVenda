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

    @RequestMapping(value = "/formPaginaInicial")
    public void formPaginaInicial(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:8080/index.html");
    }

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

    @RequestMapping("/falhaLogin")
    public void falhaLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println("<html>" +
                "<body> Login inexiste, por favor cadastra-se!" +
                "<form action=/formCadastraUser><button>Cadastre-se</button>" +
                "</form>" +
                "<body>" +
                "</html>");
    }

    @RequestMapping(value = "/loginAdm")
    public void formCadastraLogista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var nome = request.getParameter("nome");
        var senha = request.getParameter("senha");
        if (nome.equals("root") && (senha.equals("admin"))) {
            response.sendRedirect("http://localhost:8080/indexCadastraLogista.html");
        } else {
            response.getWriter().println("<html>" +
                    "<body> Login Admin invalido" +
                    "<form action=/formPaginaInicial><button>Voltar a pagina inicial</button>" +
                    "</form>" +
                    "<body>" +
                    "</html>");
        }

    }

    @RequestMapping(value = "/cadastraLogista", method = RequestMethod.POST)
    public void addLogista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var nome = request.getParameter("nome");
        var senha = request.getParameter("senha");
        var email = request.getParameter("email");
        usuario.setNome(nome);
        usuario.setSenha(senha);
        usuario.setEmail(email);
        usuario.setId(usuarioDAO.qtdUsuario() + 1);
        usuario.setTipoUser(2);
        usuarioDAO.addUser(usuario);
        response.sendRedirect("http://localhost:8080/index.html");
    }

    @RequestMapping("/verificaLogin")
    public void verificaLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var email = request.getParameter("email");
        var senha = request.getParameter("senha");
        var usuario2 = new User();
        usuario2 = usuarioDAO.buscarUser(email, senha);
        if (usuario2 == null) {
            response.sendRedirect("http://localhost:8080/falhaLogin");
        } else {
            if (usuario2.getTipoUser() == 1) {// Usuário Cliente
                request.setAttribute("usuarioClienteLogado", usuario2);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/logado");
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("usuarioLogistaLogado", usuario2);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/telaLogista");
                dispatcher.forward(request, response);
            }

        }
    }

    @RequestMapping("/telaLogista")
    public void doTelaLogista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getAttribute("usuarioLogistaLogado");

    }

    @RequestMapping("/logado")
    public void doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getAttribute("usuarioLogado");
        response.getWriter()
                .println("Nome: " + user.getNome() + ", Email: " + user.getEmail() + ", Tipo do Usuario: "
                        + user.getTipoUser());
    }
}