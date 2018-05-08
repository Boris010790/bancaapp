<%@page import="utility.Utility"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>


<html>
    <%@ include file="VIEW/head.jsp" %>

    <body class="body">

        <%
            String messaggio = (String) session.getAttribute("messaggio");
            if (messaggio != null && messaggio.length() > 0) {
                out.print("<script> $.simplyToast('" + messaggio + "','success'); </script>");
                session.setAttribute("messaggio", null);
            }
        %>

        <div class="container">
            <%@ include file="VIEW/navbar.jsp" %>


            <%
                List<rubrica.Rubrica> rubrica = (List<rubrica.Rubrica>) session.getAttribute("rubrica");
                int max = 10;
                int pagina = 1;
                Object o = request.getParameter("page");
                if (o != null) {
                    try {
                        pagina = Integer.parseInt((String) o);
                        if (pagina < 1) {
                            pagina = 1;
                        }
                    } catch (Exception f) {
                    }
                }
                String ident = null;
                o = request.getParameter("id");
                if (o != null) {
                    ident = (String) request.getParameter("id");
                    ident = ident.toUpperCase();
                    if (ident.length() > 0) {
                        for (int i = 0; i < rubrica.size();) {
                            if (!ident.equals(rubrica.get(i).getId() + "")) {
                                rubrica.remove(i);
                            } else {
                                i++;
                            }
                        }
                    }
                }

                o = request.getParameter("utente");
                String username = null;
                if (o != null) {
                    username = (String) request.getParameter("utente");
                    username = username.toUpperCase();
                    for (int i = 0; i < rubrica.size();) {
                        String contatto = rubrica.get(i).getContatto().getNome() + " " + rubrica.get(i).getContatto().getCognome();
                        if (!(contatto.contains(username))) {
                            rubrica.remove(i);
                        } else {
                            i++;
                        }
                    }
                }

            %>


            <div class="row">
                <div class="col-md-4 col-xs-12">
                    <div class="panel panel-primary text-center">
                        <div class="panel-heading">
                            <h3 class="panel-title bg-primary">Filtra per</h3>
                        </div>
                        <div class="panel-body">
                            <form class="form-inline">
                                <div class="form-group">
                                    <label ><span class="glyphicon glyphicon-user"></span></label>
                                    <input type="text" class="form-control" id="cognome" placeholder="Cognome Utente" value="<%if (username != null) {
                                            out.println(username);
                                        } %>"/>
                                </div>
                            </form>
                            <br/>
                            <form class="form-inline">
                                <div class="form-group">
                                    <label ><span class="glyphicon glyphicon-search"></span></label>
                                    <input type="text" class="form-control" id="identificatore" placeholder="ID contatto" value="<%if (ident != null) {
                                            out.println(ident);
                                        } %>"/>
                                </div>
                            </form>
                            <br/>
                            <button class="btn btn-primary" onclick="filtra();">Cerca</button>
                        </div>

                    </div>
                </div>

                <script>
                    function filtra(page) {
                        var utente = $("#cognome").val();
                        var id = $("#identificatore").val();

                        if (utente == null) {
                            utente = '';
                        }
                        if (id == null) {
                            id = '';
                        }
                        window.location.href = "./rubrica?page=" + page + "&utente=" + utente + "&id=" + id;
                    }
                </script>

                <div class="col-md-8 col-xs-12">

                    <div class="panel panel-primary text-center">
                        <div class="panel-heading">
                            <h3 class="panel-title bg-primary">Lista Attivit&agrave;</h3>
                        </div>
                        <div class="panel-body">
                            <%
                                if (rubrica != null && rubrica.size() != 0) {
                                    out.println("<div class='row text-center'>");
                                    out.println("<div class='col-xs-2 text-center'><strong>ID</strong></div>");
                                    out.println("<div class='col-xs-3 text-center'><strong>Nome</strong></div>");
                                    out.println("<div class='col-xs-3 text-center'><strong>Cognome</strong></div>");
                                    out.println("<div class='col-xs-4 text-center'></div>");
                                    out.println("</div><br/>");
                                    for (int i = max * (pagina - 1); i < rubrica.size() && i < max * (pagina - 1) + max; i++) {

                                        rubrica.Rubrica rub = rubrica.get(i);
                                        out.println("<div class='list-group-item'>");
                                        out.println("<div class='row text-center'>");
                                        out.println("<div class='col-xs-2'>" + rub.getId() + "</div>");
                                        out.println("<div id='nome"+rub.getId()+"' class='col-md-3'>" + Utility.capitalize(rub.getContatto().getNome()) + "</div>");
                                        out.println("<div  id='cognome"+rub.getId()+"' class='col-xs-3'>" + Utility.capitalize(rub.getContatto().getCognome()) + "</div>");
                                        out.println("<div class='col-xs-2 col-xs-offset-2'><button class='btn btn-rounded btn-block btn-danger' onclick='elimina("+rub.getId()+")';>Elimina</button></div>");
                                        out.println("</div>");
                                        out.println("</div>");
                                    }
                                } else {
                                    out.println("<div class='alert alert-success' role='alert'>Non risultano contatti registrati</div>");
                                }

                            %>
                        </div>
                        <div class="panel-footer">
                            <nav aria-label="Page navigation">
                                <ul class="pagination">
                                    <%                                        int j = 1;

                                        for (int i = 0; i < rubrica.size(); i += max, j++) {
                                            String active = "";

                                            if (pagina == j) {
                                                active = "class='active'";

                                            }
                                            out.println(" <li " + active + "><a onclick=filtra(" + j + ");>" + j + "</a></li>");
                                        }
                                    %>

                                </ul>
                            </nav>

                        </div>
                    </div>

                </div>


            </div>

            <div id="eliminaModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-header bg-primary">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 id="titoloMessaggio" class="modal-title  text-center">Elimina contatto</h4>
                        </div>

                        <div class="panel text-center">
                            <div class="panel-heading">
                                <h3 class="panel-title">Vuoi eliminare <strong> <span id="nomeContatto"> </span></strong> dalla lista dei contatti?</h3>
                            </div>
                        </div>
                        
                         <div class="modal-footer">
                            <form action="rubrica" method="POST">
                                <input id="bottoneRubrica" name="eliminaCont" />
                                <button type="button" class="btn btn-default" data-dismiss="modal">Chiudi</button>
                                <button type="submit"  class="btn btn-danger" name="bottoneElimina" value="bottoneElimina">Elimina Messaggio</button>
                            </form>
                        </div>
                </div>
            </div>                     

        </div>






        <script>
            function elimina(s) {
                var nome = $("#nome"+s).text();
                var cognome = $("#cognome"+s).text();
                $("#bottoneRubrica").val(s);
                $("#nomeContatto").html(nome+" "+cognome);
                $("#eliminaModal").modal("show");
            }



        </script>


        <%@ include file="VIEW/footer.jsp" %>


    </body>
</html>
