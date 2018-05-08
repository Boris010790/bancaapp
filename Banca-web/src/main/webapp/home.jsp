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

            <div class="row" >
                <div class="col-md-2">
                    <img class="img-responsive" src="Utility/Immagini/user.png" alt="">
                </div>
                <div class="col-md-2" style="margin-top:2%;">
                    <h3>Ciao <%out.println(user.getNome());%></h3>
                </div>
                <br/>
                <div class="   <% if (user.getTipo().equals("ADMIN")) out.println("col-md-4 col-md-offset-3"); else out.println("col-md-7 col-md-offset-3");%>">
                    <div class="btn-group btn-group-justified" role="group" aria-label="..."  style="padding-right:20px;">
                        <div class="btn-group" role="group">
                            <button type="button" class="btn btn-primary btn-bord" onclick="window.location.href = './Transfer';">
                                <span class="glyphicon glyphicon-euro"></span>
                                <% if (user.getTipo().equals("ADMIN")) {
                                        out.println("Ricarica conto");
                                    } else {
                                        out.println("Nuova Operazione");
                                    } %>
                            </button>
                        </div>


                        <% if (user.getTipo().equals("CLIENTE")) {
                                out.println("<div class=\"btn-group\" role=\"group\">");
                                out.println("<button type=\"button\" class=\"btn btn-success btn-bord\" style=\"margin-left:10px;\">");
                                out.println("<span class=\"glyphicon glyphicon-arrow-down\"></span>");
                                out.println("Scarica l'app MyBank </button>  </div>");
                        }
                        %>


                    </div>   
                </div>
            </div>
            <hr class="hr"/>



            <div class="row">
                <div class="col-md-5">
                    <div class="panel panel-default" style="font-weight:bold;">
                        <div class="panel-body">
                            <span style="float:left;"><%if(user.getTipo().equals("ADMIN"))out.println("Denaro disponibile:"); else out.println("Il tuo saldo &egrave;");%></span>
                            <span style="float:right;"><a href="./attivita"> <%out.println(Utility.getFormatImportoView(user.getSaldo()));%> <span class="glyphicon glyphicon-euro"></span></a></span>
                        </div>
                    </div>

                    <div class="panel panel-default" style="font-weight:bold;"  >
                        <div class="panel-body">
                            <span style="float:left;"> <span class="glyphicon glyphicon-envelope"></span> I tuoi messaggi:</span>
                            <span style="float:right;"><a href="./messaggi"> <%
                                if (messNonLetti == 0) {
                                    out.println("Non hai ricevuto nuovi messaggi (" + messNonLetti + "/" + messRicevuti + ")");
                                } else if (messNonLetti == 1) {
                                    out.println("Hai ricevuto un nuovo messaggio (" + messNonLetti + "/" + messRicevuti + ")");
                                } else if (messNonLetti > 1) {
                                    out.println(" Hai ricevuto " + messNonLetti + " messaggi (" + messNonLetti + "/" + messRicevuti + ")");
                                }


                                    %>
                                    </div></a></span>

                        </div>

                    </div>
                    <div class="col-md-7" style="display: inline-block; vertical-align: top;">

                        <div class="panel panel-default">


                            <div class="panel-body">
                                <%                                ArrayList<attivita.Attivita> listaAttivita = (ArrayList<attivita.Attivita>) session.getAttribute("attivita");

                                %>
                                <strong>Ultime attivit&agrave;</strong><br/><br/>

                                <div class='row text-center'>
                                    <strong>
                                        <div class='row text-center'>
                                            <div class='col-xs-3'>Data</div>
                                            <div class='col-xs-6'>Causale</div>
                                            <div class='col-xs-3 text-center'>Importo</div>
                                        </div>
                                    </strong>
                                    <br/>

                                    <%                                    if (listaAttivita != null && listaAttivita.size() > 0) {

                                            for (int i = 0; i < listaAttivita.size() && i < 10; i++) {
                                                attivita.Attivita att = listaAttivita.get(i);
                                                if (att == null || att.getData() == null) {
                                                    continue;
                                                }

                                                if (att.getDestinatario().equals(user)) {
                                                    out.println("<a href='#' class='list-group-item list-group-item-success'>");
                                                } else {
                                                    out.println("<a href='#' class='list-group-item list-group-item-danger'>");
                                                }
                                                out.println("<div class='row text-center'>");
                                                out.println("<div class='col-xs-3'>" + Utility.getFormatDateView(att.getData()) + "</div>");
                                                out.println("<div class='col-xs-6'>" + att.getCausale() + "</div>");
                                                out.println("<div class='col-xs-3'>" + Utility.getFormatImportoView(att.getImporto()) + " EUR</div>");
                                                out.println("</div>");
                                                out.println("</a>");
                                            }
                                        } else {
                                            out.println("<div class='alert alert-success' role='alert'>Non ci sono attività</div>");
                                        }

                                    %>
                                </div>



                            </div>
                        </div>

                    </div>
                </div>


                <%@ include file="VIEW/footer.jsp" %>

            </div>
    </body>
</html>
