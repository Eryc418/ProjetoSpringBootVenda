package com.projeto_venda.venda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
    Carrinho carrinho = new Carrinho();
    ArrayList<Carrinho> listaCompra = new ArrayList<>();
    Integer logIdUser = 0;

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

    @RequestMapping(value = "/loginAdm", method = RequestMethod.POST)
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

    @RequestMapping(value = "/verificaLogin", method = RequestMethod.POST)
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
                session.setAttribute("usuarioClienteLogado", usuario2.getNome());
                RequestDispatcher dispatcher = request.getRequestDispatcher("/telaCliente");
                dispatcher.forward(request, response);
            } else {
                session.setAttribute("NomeUser", usuario2.getNome());// Usuario Logista
                RequestDispatcher dispatcher = request.getRequestDispatcher("/telaLogista");
                dispatcher.forward(request, response);
            }
        }
    }

    @RequestMapping(value = "/telaCliente")
    public void doTelaCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        var nome = session.getAttribute("usuarioClienteLogado");

        ProdutoDAO produtoDAO = new ProdutoDAO();
        ArrayList<Produto> lista_produto = new ArrayList();
        lista_produto.addAll(produtoDAO.BuscarGeral());
        var i = 0;

        response.getWriter().println("<html>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1> Seja bem vindo ao portal do cliente Sr(a) " + nome + "</h1>");
        response.getWriter().println("<br>");
        response.getWriter().println("<h2> Lista de Produtos Disponiveis</h2>");
        response.getWriter().println("<table>");
        for (i = 0; i < lista_produto.size(); i++) {
            response.getWriter().println("<tr>");
            response.getWriter().println("<td>" + "Nome: " + lista_produto.get(i).getNome() + "    |    " + "</td>");
            response.getWriter().println("<td>" + "Marca: " + lista_produto.get(i).getMarca() + "    |    " + "</td>");
            response.getWriter()
                    .println("<td>" + "Modelo: " + lista_produto.get(i).getModelo() + "    |    " + "</td>");
            response.getWriter().println("<td>" + "Preço: " + lista_produto.get(i).getPreco() + "    |    " + "</td>");
            response.getWriter()
                    .println("<td>" + "Id do Produto: " + lista_produto.get(i).getId() + "    |    " + "</td>");
            response.getWriter().println("<td>");
            response.getWriter()
                    .println("<a href='/AddItemCarrinho?idProd=" + lista_produto.get(i).getId() + "'>+ ADD</a>");
            response.getWriter().println("</td>");

            response.getWriter().println("</tr>");
        }
        response.getWriter().println("</table>");
        response.getWriter().println("<form");// Botão sair
        response.getWriter().println("action=/VisualizarCarrinho");
        response.getWriter().println(">");
        response.getWriter().println("<button>");
        response.getWriter().println("Visualizar Carrinho de compras");
        response.getWriter().println("</button>");
        response.getWriter().println("</form>");// Botão sair
        response.getWriter().println("<form");// Botão sair
        response.getWriter().println("action=/logout");
        response.getWriter().println(">");
        response.getWriter().println("<button>");
        response.getWriter().println("Sair");
        response.getWriter().println("</button>");
        response.getWriter().println("</form>");// Botão sair
        response.getWriter().println("</body>");
        response.getWriter().println("<html>");

    }

    @RequestMapping("/AddItemCarrinho")
    public void doAdicionaItem(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var idProduto = Integer.parseInt(request.getParameter("idProd"));
        ProdutoDAO prodDAO = new ProdutoDAO();
        var produto = prodDAO.buscarProduto(idProduto);
        Cookie carrinhoCompras = new Cookie("carrinhoCompras", "");
        carrinhoCompras.setMaxAge(60 * 60 * 24 * 7);
        Cookie[] requestCookies = request.getCookies();
        boolean achouCarrinho = false;

        if (requestCookies != null) {
            for (var c : requestCookies) {
                if (c.getName().equals("carrinhoCompras")) {
                    achouCarrinho = true;
                    carrinhoCompras = c;
                    break;
                }
            }
        }
        Produto produtoEscolhido = null;

        if (produto != null) {
            produtoEscolhido = produto;
            if (achouCarrinho == true) {
                String value = carrinhoCompras.getValue();
                carrinhoCompras.setValue(value + produtoEscolhido.getId() + "|");
            } else {
                carrinhoCompras.setValue(produtoEscolhido.getId() + "|");
            }
        } else {
            response.addCookie(carrinhoCompras);
        }
        response.addCookie(carrinhoCompras);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/telaCliente");
        dispatcher.forward(request, response);
    }

    @RequestMapping("/VisualizarCarrinho")
    public void doVisualizarCarrinho(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie carrinhoCompras = new Cookie("carrinhoCompras", "");
        carrinhoCompras.setMaxAge(60 * 60 * 24 * 7);
        Cookie[] requestCookies = request.getCookies();
        boolean achouCarrinho = false;

        if (requestCookies != null) {
            for (var c : requestCookies) {
                if (c.getName().equals("carrinhoCompras")) {
                    achouCarrinho = true;
                    carrinhoCompras = c;
                    break;
                }
            }
        }
        ProdutoDAO produtoDAO = new ProdutoDAO();
        Produto produto = null;
        var i = 0;
        ArrayList<Produto> lista_produto = new ArrayList();
        if (achouCarrinho == true) {
            StringTokenizer tokenizer = new StringTokenizer(carrinhoCompras.getValue(), "|");
            while (tokenizer.hasMoreTokens()) {
                produto = produtoDAO.buscarProduto(Integer.parseInt(tokenizer.nextToken()));
                lista_produto.add(produto);
            }
            response.getWriter().println("<html>");
            response.getWriter().println("<body>");
            response.getWriter().println("<h1>Seja bem vindo ao seu carrinho de compras</h1>");
            response.getWriter().println("<br>");
            response.getWriter().println("<table>");
            for (i = 0; i < lista_produto.size(); i++) {
                response.getWriter().println("<tr>");
                response.getWriter()
                        .println("<td>" + "Nome: " + lista_produto.get(i).getNome() + "    |    " + "</td>");
                response.getWriter()
                        .println("<td>" + "Marca: " + lista_produto.get(i).getMarca() + "    |    " + "</td>");
                response.getWriter()
                        .println("<td>" + "Modelo: " + lista_produto.get(i).getModelo() + "    |    " + "</td>");
                response.getWriter()
                        .println("<td>" + "Preço: " + lista_produto.get(i).getPreco() + "    |    " + "</td>");
                response.getWriter()
                        .println("<td>" + "Id do Produto: " + lista_produto.get(i).getId() + "    |    " + "</td>");
                response.getWriter().println("</tr>");
            }
            response.getWriter().println("</table>");
            response.getWriter().println("<br>");
            response.getWriter().println("<form");// Botão sair
            response.getWriter().println("action=/telaCliente");
            response.getWriter().println(">");
            response.getWriter().println("<button>");
            response.getWriter().println("MenuPrincipal");
            response.getWriter().println("</button>");
            response.getWriter().println("</form>");// Botão sair
            response.getWriter().println("</body>");
            response.getWriter().println("<html>");
        } else {
            response.getWriter().println("<html>");
            response.getWriter().println("<body>");
            response.getWriter().println("<h1>Você não possui compras, caso deseje comprar vá ao menu principal</h1>");
            response.getWriter().println("<br>");
            response.getWriter().println("<form");// Botão sair
            response.getWriter().println("action=/telaCliente");
            response.getWriter().println(">");
            response.getWriter().println("<button>");
            response.getWriter().println("MenuPrincipal");
            response.getWriter().println("</button>");
            response.getWriter().println("</form>");// Botão sair
            response.getWriter().println("</body>");
            response.getWriter().println("<html>");
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

        response.getWriter().println("<html>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h2> Lista de Produtos cadastrados</h2>");
        response.getWriter().println("<table>");
        for (i = 0; i < lista_produto.size(); i++) {
            response.getWriter().println("<tr>");
            response.getWriter().println("<td>" + "Nome: " + lista_produto.get(i).getNome() + "    |    " + "</td>");
            response.getWriter().println("<td>" + "Marca: " + lista_produto.get(i).getMarca() + "    |    " + "</td>");
            response.getWriter()
                    .println("<td>" + "Modelo: " + lista_produto.get(i).getModelo() + "    |    " + "</td>");
            response.getWriter().println("<td>" + "Preço: " + lista_produto.get(i).getPreco() + "    |    " + "</td>");
            response.getWriter().println("</tr>");
        }
        response.getWriter().println("</table>");
        response.getWriter().println("</body>");
        response.getWriter().println("<html>");
    }

    @RequestMapping("/teste")
    public void doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var i = 0;
        response.getWriter().println("<html>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1> Lista de Produtos comprados</h1>");
        response.getWriter().println("<br>");
        response.getWriter().println("<h2> Lista de Produtos Disponiveis</h2>");
        response.getWriter().println("<table>");
        for (i = 0; i < listaCompra.size(); i++) {
            response.getWriter().println("<tr>");
            response.getWriter()
                    .println("<td>" + "Nome: " + listaCompra.get(i).getNomeProduto() + "    |    " + "</td>");
            response.getWriter()
                    .println("<td>" + "Id Produto: " + listaCompra.get(i).getIdProduto() + "    |    " + "</td>");
            response.getWriter()
                    .println("<td>" + "Id do Cliente: " + listaCompra.get(i).getIdUser() + "    |    " + "</td>");
            response.getWriter().println("</td>");

            response.getWriter().println("</tr>");
        }
        response.getWriter().println("</table>");
        response.getWriter().println("<br>");
        response.getWriter().println("<br>");
        response.getWriter().println("<h2> Para realizar a compra do produto digite o id</h2>");
        response.getWriter().println("<br>");
        response.getWriter().println("</form>");
        response.getWriter().println("<form");// Botão sair
        response.getWriter().println("action=/logout");
        response.getWriter().println(">");
        response.getWriter().println("<button>");
        response.getWriter().println("Sair");
        response.getWriter().println("</button>");
        response.getWriter().println("</form>");// Botão sair
        response.getWriter().println("</body>");
        response.getWriter().println("<html>");
    }
}