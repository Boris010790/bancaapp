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


            <%                ArrayList<attivita.Attivita> attivita = (ArrayList<attivita.Attivita>) session.getAttribute("attivita");
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
                String ident=null;
                o = request.getParameter("id");
                if (o != null) {
                    ident = (String) request.getParameter("id");
                    ident = ident.toUpperCase();
                    if (ident.length() > 0) {
                        for (int i = 0; i < attivita.size();) {
                            if (!ident.equals(attivita.get(i).getId() + "")) {
                                attivita.remove(i);
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
                    for (int i = 0; i < attivita.size();) {
                        String mittente = attivita.get(i).getMittente().getNome() + " " + attivita.get(i).getMittente().getCognome();
                        String destinatario = attivita.get(i).getDestinatario().getNome() + " " + attivita.get(i).getDestinatario().getCognome();
                        if (!(mittente.contains(username) || destinatario.contains(username))) {
                            attivita.remove(i);
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
                                    <input type="text" class="form-control" id="cognome" placeholder="Cognome Utente" value="<%if (username != null) out.println(username); %>"/>
                                </div>
                            </form>
                            <br/>
                            <form class="form-inline">
                                <div class="form-group">
                                    <label ><span class="glyphicon glyphicon-search"></span></label>
                                    <input type="text" class="form-control" id="identificatore" placeholder="ID Transazione" value="<%if(ident != null) out.println(ident); %>"/>
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
                        window.location.href = "./attivita?page=" + page + "&utente=" + utente + "&id=" + id;
                    }
                </script>

                <div class="col-md-8 col-xs-12">

                    <div class="panel panel-primary text-center">
                        <div class="panel-heading">
                            <h3 class="panel-title bg-primary">Lista Attivit&agrave;</h3>
                        </div>
                        <div class="panel-body">
                            <%                                if (attivita != null && attivita.size() != 0) {

                                    out.println("<div class='row text-center'>");
                                    out.println("<div class='col-xs-1 text-center'><bold>ID</bold></div>");
                                    out.println("<div class='col-xs-3 hidden-xs text-center'><bold>DATA</bold></div>");
                                    out.println("<div class='col-xs-2 text-center'><bold>COGNOME</bold></div>");
                                    out.println("<div class='col-md-3 col-xs-6 text-center'><bold>CAUSALE</bold></div>");
                                    out.println("<div class='col-xs-3 text-center'><bold>IMPORTO</bold></div>");

                                    out.println("</div>");
                                    for (int i = max * (pagina - 1); i < attivita.size() && i < max * (pagina - 1) + 10; i++) {

                                        attivita.Attivita att = attivita.get(i);
                                        if (att == null || att.getData() == null) {
                                            continue;
                                        }

                                        if (att.getDestinatario().equals(user)) {
                                            out.println("<a href='#' class='list-group-item list-group-item-success'>");
                                        } else {
                                            out.println("<a href='#' class='list-group-item list-group-item-danger'>");
                                        }
                                        out.println("<div class='row text-center'>");
                                        out.println("<div class='col-xs-1'>" + att.getId() + "</div>");

                                        out.println("<div class='col-md-3 hidden-xs'>" + Utility.getFormatDateView(att.getData()) + "</div>");

                                        if (att.getDestinatario().equals(user)) {
                                            out.println("<div class='col-xs-2'>" + att.getMittente().getCognome() + "</div>");
                                        } else {
                                            out.println("<div class='col-xs-2'>" + Utility.capitalize(att.getDestinatario().getCognome()) + "</div>");
                                        }
                                        out.println("<div class='col-md-3 col-xs-6'>" + att.getCausale() + "</div>");
                                        out.println("<div class='col-xs-3'>" + Utility.getFormatImportoView(att.getImporto()) + " EUR</div>");
                                        out.println("</div>");
                                        out.println("</a>");
                                    }
                                } else {
                                    out.println("<div class='alert alert-success' role='alert'>Non risultano attivit&agrave;</div>");
                                }

                            %>
                        </div>
                        <div class="panel-footer">
                            <nav aria-label="Page navigation">
                                <ul class="pagination">
                                    <%                                        int j = 1;

                                        for (int i = 0; i < attivita.size(); i += max, j++) {
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

        </div>



    </div>
</div>

</div>
</div>


<!--
///////////////////////////////////////////
///////////////// RICARICA CONTO //////////
///////////////////////////////////////////
-->

<div id="ricarica" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="gridSystemModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title text-center" id="gridSystemModalLabel">DIGITA L'IMPORTO </h4>
            </div>
            <div class="modal-body">
                <form class="form-inline" action="operazioni" method="POST">

                    <div class="form-group col-xs-offset-3">
                        <input  style="text-align:right; font-weight:900;" class="form-control input-lg" id="importo" value="0">
                    </div>
                    <div class="form-group col-xs-offset-3">
                        <select  class="form-control input-lg" id="utente" >
                            <option>Boris</option>
                            <option>Marco Fanti</option>
                        </select>
                    </div>
                    <hr/>
                    <button type="submit" class="btn btn-success btn-lg" onclick="return isCurrency();">Ricarica</button>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<script>
    function openModalRicarica() {
        $("#ricarica").modal("show");
        $("#importo").val("0");
    }

    function isCurrency() {
        var x = $("#importo").val();
        x = x.replace(",", ".");

        if (!$.isNumeric(x) || x <= 0) {
            $.simplyToast("Inserisci un importo valido", "danger");
            return false;
        }
        return true;
    }

</script>


<%@ include file="VIEW/footer.jsp" %>

</div>
</body>
</html>
