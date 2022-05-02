package com.projeto_venda.venda;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UsuarioController {

    User usuario = new User();
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    Produto produto = new Produto();
    ProdutoDAO produtoDAO = new ProdutoDAO();

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
        HttpSession session = request.getSession();
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
                session.setAttribute("NomeUser", usuario2.getNome());// Usuario Logista
                RequestDispatcher dispatcher = request.getRequestDispatcher("/telaLogista");
                dispatcher.forward(request, response);
            }

        }
    }

    @RequestMapping("/telaLogista")
    public void doTelaLogista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        var nome = session.getAttribute("NomeUser");
        response.getWriter().println("<html>" +
                "<body> <h2>Seja Bem Vindo a Área do Logista Sr(a) " + nome + "</h2> " +
                "<form action=/formCadastraProduto><button>Cadastre Produtos</button>" +
                "</form>" +
                "<form action=/listaProduto><button>Listar Produtos</button></form>" +
                "<form action=/logout><button>Sair</button></form>" +
                "<body>" +
                "</html>");
    }

    @RequestMapping("/formCadastraProduto")
    public void doFormCadastraProduto(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.sendRedirect("http://localhost:8080/indexCadastraProduto.html");
    }

    @RequestMapping(value = "/cadastraProduto", method = RequestMethod.POST)
    public void doCadastraProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var nome = request.getParameter("nome");
        var marca = request.getParameter("marca");
        var modelo = request.getParameter("modelo");
        var preco = Float.parseFloat(request.getParameter("preco"));

        produto.setNome(nome);
        produto.setMarca(marca);
        produto.setModelo(modelo);
        produto.setPreco(preco);
        produto.setId(produtoDAO.qtdProdutos() + 1);
        produtoDAO.addProduto(produto);
        response.sendRedirect("http://localhost:8080/telaLogista");

    }

    @RequestMapping("/logout")
    public void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("http://localhost:8080/index.html");
    }

    @RequestMapping("/listaProduto")
    public void doListaProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        ArrayList<Produto> lista_produto = new ArrayList();
        lista_produto.addAll(produtoDAO.BuscarGeral());
        var i = 0;
        response.getWriter().println("Lista de Produtos");
        response.getWriter().println("\n");
        /*
         * response.getWriter()
         * .println(
         * "<h2>Lista de Produtos</h2>" +
         * "<html>" +
         * "<body>" +
         * "<table>"+
         * "<tr>" +
         * "   <td>Nome</td>" +
         * "   <td>Marca</td>" +
         * "   <td>Modelo</td>" +
         * "   <td>Preço</td> " +
         * "   <td>Id Produto</th>" +
         * "</tr>" +
         * "</table>"+
         * "</body>"
         * + "</html>");
         */
        for (i = 0; i < lista_produto.size(); i++) {
            response.getWriter().println();
            response.getWriter().println("Nome: " + lista_produto.get(i).getNome() + "   |   " + "Marca: "
                    + lista_produto.get(i).getMarca() + "   |   " + "Modelo: " + lista_produto.get(i).getModelo()
                    + "   |   " + "Preço: " + lista_produto.get(i).getPreco() + "   |   " + "Id Produto: "
                    + lista_produto.get(i).getId());
        }
        /*
         * response.getWriter().println("\n");
         * response.getWriter().println("<html>" +
         * "<body> " +
         * "<form action=/telaLogista><button>Voltar</button>" +
         * "</form>" +
         * "<body>" +
         * "</html>");
         */
    }

    @RequestMapping("/logado")
    public void doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getAttribute("usuarioLogado");
        response.getWriter()
                .println("Nome: " + user.getNome() + ", Email: " + user.getEmail() + ", Tipo do Usuario: "
                        + user.getTipoUser());
    }
}