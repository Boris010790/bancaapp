

<%@page import="java.util.List"%>
<%@page import="utility.Utility"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>


<html>
    <%@ include file="VIEW/head.jsp" %>

    <body class="body">

        <%
            String messaggio = (String) request.getAttribute("messaggio");
            System.out.println("MESSAGGIO JSP " + messaggio);
            if (messaggio != null && messaggio.length() > 0) {
                out.print("<script> $.simplyToast('" + messaggio + "','success'); </script>");
                request.setAttribute("messaggio", null);
            }

            List<utente.Utente> rubrica = null;
            System.out.println("SERVLET " + session.getAttribute("rubrica"));
            if (session.getAttribute("rubrica") != null) {
                rubrica = (List<utente.Utente>) session.getAttribute("rubrica");
            }

            List<messaggi.Messaggi> messaggiRicevuti = null;
            if (session.getAttribute("ricevuti") != null) {
                messaggiRicevuti = (List<messaggi.Messaggi>) session.getAttribute("ricevuti");
            }

            List<messaggi.Messaggi> messaggiInviati = null;
            if (session.getAttribute("inviati") != null) {
                messaggiInviati = (List<messaggi.Messaggi>) session.getAttribute("inviati");
            }

            //View Messaggi
            Object o = request.getParameter("messaggi");
            List<messaggi.Messaggi> messaggiFil = messaggiInviati;
            String tipoMess = "";
            if (o != null) {
                tipoMess = (String) o;
                if (tipoMess.equals("ricevuti")) {
                    messaggiFil = messaggiRicevuti;
                }
            }

            int max = 10;
            int pagina = 1;
            o = request.getParameter("page");
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
                    for (int i = 0; i < messaggiFil.size();) {
                        if (!ident.equals(messaggiFil.get(i).getId() + "")) {
                            messaggiFil.remove(i);
                        } else {
                            i++;
                        }
                    }
                }
            }

            o = request.getParameter("utente");
            String nomeUtente = (String) o;
            System.out.println("**********" + messaggiFil.size());
            if (nomeUtente != null) {
                nomeUtente = nomeUtente.toUpperCase();
            }
            if (nomeUtente != null && nomeUtente.length() > 0) {
                for (int i = 0; i < messaggiFil.size();) {

                    messaggi.Messaggi m = messaggiFil.get(i);
                    boolean isDestinatario = tipoMess != null && tipoMess.equals("ricevuti");
                    System.out.println("IsDest " + isDestinatario + " Cognome" + messaggiFil.get(i).getMittente().getCognome() + " Contains " + !messaggiFil.get(i).getMittente().getCognome().contains(nomeUtente));
                    if (isDestinatario && !messaggiFil.get(i).getDestinatario().getCognome().contains(nomeUtente)) {
                        messaggiFil.remove(i);
                    } else if (!isDestinatario && !messaggiFil.get(i).getDestinatario().getCognome().contains(nomeUtente)) {
                        messaggiFil.remove(i);
                    } else {
                        i++;
                    }

                }
            }

            System.out.println("**********FINE" + messaggiFil.size());
        %>

        <div class="container">
            <%@ include file="VIEW/navbar.jsp" %>
            <div class="row">
                <div class="col-xs-offset-10 col-xs-2">
                    <button class="btn btn-success btn-border" data-toggle="modal" data-target="#inviaMessaggio"><span class="glyphicon glyphicon-plus"></span> Invia Messaggio</button>
                </div>
            </div>
            <br/>

            <div class="row">
                <div class="col-md-12 text-center">
                    <ul class="nav nav-tabs nav-justified">
                        <li role="presentation" <% if (!tipoMess.equals("ricevuti")) {
                                out.println("class='active'");
                            }
                            %>><a href="./messaggi?messaggi=inviati">Inviati</a></li>
                        <li role="presentation" <% if (tipoMess.equals("ricevuti")) {
                                out.println("class='active'");
                            }%>><a href="./messaggi?messaggi=ricevuti">Ricevuti</a></li>
                    </ul>
                </div>
            </div>

            <br/> <br/>



            <div class="row">
                <!--Filtro-->
                <div class="col-md-4 col-xs-12">

                    <div class="panel panel-primary text-center">
                        <div class="panel-heading">
                            <h3 class="panel-title bg-primary">Filtra per</h3>
                        </div>
                        <div class="panel-body">
                            <form class="form-inline">
                                <div class="form-group">
                                    <label ><span class="glyphicon glyphicon-user"></span></label>
                                    <input type="text" class="form-control" id="cognome" placeholder="Cognome Utente" value=""/>
                                </div>
                            </form>
                            <br/>
                            <form class="form-inline">
                                <div class="form-group">
                                    <label ><span class="glyphicon glyphicon-search"></span></label>
                                    <input type="text" class="form-control" id="identificatore" placeholder="ID Transazione" value=""/>
                                </div>
                            </form>
                            <br/>
                            <button class="btn btn-primary" onclick="filtra();">Cerca</button>
                        </div>
                    </div>
                </div>
                <!--Fine Filtro-->
                <!--Lista Messaggi-->
                <div class="col-md-8 col-xs-12">

                    <div class="panel panel-primary text-center">
                        <div class="panel-heading">
                            <h3 class="panel-title bg-primary">Messaggi</h3>
                        </div>
                        <ul class="list-group">
                            <li class="list-group-item">
                                <%                            out.println("<div class='row text-center'>");
                                    out.println("<div class='col-xs-1 text-center'><bold>ID</bold></div>");
                                    out.println("<div class='col-xs-3 text-center'><bold>DATA</bold></div>");
                                    out.println("<div class='col-md-3 text-center'><bold>UTENTE</bold></div>");
                                    out.println("<div class='col-xs-5 text-center'><bold>OGGETTO</bold></div>");

                                    out.println("</div>");%>
                            </li>
                        </ul>   
                        <div class="panel-body">


                            <ul class="list-group">
                                <%
                                    if (messaggiFil.size() > 0) {
                                        for (int i = max * (pagina - 1); i < messaggiFil.size() && i < max * (pagina - 1) + 10; i++) {
                                            messaggi.Messaggi mess = messaggiFil.get(i);
                                            String nonLettoClass = "";
                                            if(!mess.isLetto() && mess.getMittente().getId() != user.getId()) nonLettoClass="list-group-item-info";
                                            out.println("<li class='list-group-item "+nonLettoClass+"' style='cursor:pointer' onclick=apriMess('" + mess.getId() + "')>");
                                            out.println("<div class='row text-center'>");
                                            out.println("<div class='col-xs-1 text-center'><bold id='messaggioId" + mess.getId() + "'>" + mess.getId() + "</bold></div>");
                                            out.println("<div class='col-xs-3 text-center'><bold id='messaggioData" + mess.getId() + "'>" + Utility.getFormatDateView(mess.getData()) + "</bold></div>");
                                            if (mess.getMittente().getId() == user.getId()) {
                                                out.println("<div class='col-md-3 text-center'><bold id='messaggioUtente" + mess.getId() + "'>" + mess.getDestinatario().getCognome() + "</bold></div>");
                                                out.println("<div id='messaggioMD" + mess.getId() + "'>mittente</div>");
                                            } else {
                                                out.println("<div class='col-md-3 text-center'><bold id='messaggioUtente" + mess.getId() + "'>" + mess.getMittente().getCognome() + "</bold></div>");
                                                out.println("<div id='messaggioMD" + mess.getId() + "'>destinatario</div>");

                                            }
                                            out.println("<div class='col-xs-5 text-center'><bold id='messaggioOggetto" + mess.getId() + "'>" + mess.getOggetto() + "</bold></div>");
                                            out.println("<div id='messaggioBody" + mess.getId() + "' style='display:none;'>" + mess.getTesto() + "</div>");
                                            out.println("</div>");
                                            out.println("</li>");

                                        }
                                    }
                                    else out.println("<div class='alert alert-success' role='alert'>Non risultano messaggi</div>");

                                %>
                            </ul>
                        </div>


                        <div class="panel-footer">
                            <nav aria-label="Page navigation">
                                <ul class="pagination">
                                    <%
                                        int j = 1;

                                        for (int i = 0; i < messaggiFil.size(); i += max, j++) {
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
                <!--Fine Lista Messaggi-->
            </div>

            <!--MODALE-->                

            <div id="leggiMessaggio" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-header bg-primary">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 id="titoloMessaggio" class="modal-title  text-center"></h4>
                        </div>

                        <div class="panel text-center">
                            <div class="panel-heading">
                                <h3 class="panel-title">Oggetto: <strong> <span id="oggettoMessaggio"> </span></strong></h3>
                            </div>
                            <hr/>
                            <div class="panel-body">
                                <div class="modal-body jumbotron">
                                    <p id="testoMessaggio"></p>
                                </div>
                            </div>
                        </div>




                        <div class="modal-footer">
                            <form action="./messaggi" method="POST">
                                <input style="display:none" name="md" id="mdMessaggio" value="mittente"/>
                                <button type="button" class="btn btn-default" data-dismiss="modal">Chiudi</button>
                                <button type="submit" id="bottoneMessaggio" class="btn btn-danger" name="elimina" value="">Elimina Messaggio</button>
                            </form>
                        </div>


                    </div>
                </div>
            </div>                  

            <!--FINE MODALE-->

            <!--MODALE-->
            <div id="inviaMessaggio" class="modal fade " tabindex="-1" role="dialog">
                <div class="modal-dialog modal-lg" role="document">
                    <form id="status" method="POST" action="./messaggi" onchange="cambia();">

                        <div class="modal-content">

                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title text-center" id="myModalLabel">Nuovo Messaggio</h4>
                            </div>
                            <div class="modal-body">
                                <div>
                                    <span class="text-center">
                                        <h4 class="text-center"><strong>Destinatario</strong></h4>
                                        <h4>
                                            <span>Rubrica &nbsp;<input  type="radio" name="radioType" value="rubrica"/></span>
                                            <span style="margin-left:5%;">Email &nbsp;<input type="radio" name="radioType" value="email" checked/></span>
                                        </h4>

                                        <div class="row">
                                            <div class="col-xs-6 col-xs-offset-3">
                                                <input style="display:none;" id="choose" name="choose" value="Email"/>

                                                <span id="inputEmail" class="animated bounceIn" style="diplay:block;">
                                                    <input id="email" name="emailInput" style="margin-top:10%;" class="form-control input-lg" placeholder="Digita un indirizzo email"/>
                                                    <div class="checkbox text-center">
                                                        <label>
                                                            <input name="checkbox" type="checkbox"/> Aggiungi contatto in rubrica
                                                        </label>
                                                    </div>
                                                </span>
                                                <span id="inputRubrica" class="animated bounceIn" style="display:none;">
                                                    <div class="form-group">
                                                        <select id="rubrica" name="RubricaSelect" style="margin-top:10%;" class="form-control text-center input-lg" onchange="cambia();">
                                                            <option value="none"> - </option>
                                                            <%
                                                                for (utente.Utente u : rubrica) {
                                                                    out.println("<option value='" + u.getId() + "'>" + Utility.capitalize(u.getNome() + " " + u.getCognome()) + "</option>");
                                                                }
                                                            %>

                                                        </select>
                                                    </div>
                                                </span>
                                            </div>
                                        </div><!-- /.row -->
                                        <hr/>
                                        <h4 class="text-center"><strong>Oggetto</strong></h4>
                                        <div class="row"> 
                                            <div class="col-md-8 col-md-offset-2">
                                                <input id="oggetto" name="oggetto" class="form-control input-lg" type="text" placeholder="Inserisci l'oggetto"/>
                                                <small>Max 25 caratteri</small>
                                            </div>
                                        </div>
                                        <hr/>
                                        <h4 class="text-center"><strong>Messaggio</strong></h4>
                                        <div class="row"> 
                                            <div class="col-md-8 col-md-offset-2 text-left">
                                                <textarea id="messaggioT" name="messaggio"  rows="6" class="form-control" type="text" placeholder="Inserisci il Messaggio">
                                                Prova
                                                </textarea>
                                                <small>Max 255 caratteri</small>
                                            </div>
                                        </div>


                                    </span>
                                </div>


                            </div>
                            <div class="modal-footer">
                                <div class="row">
                                    <div class="col-xs-2 col-md-offset-8">
                                        <button class="btn btn-success btn-block" type="submit" onclick="return (emailValida() && rubricaValida() && oggettoValido() && messaggioValido());">Invia</button>
                                    </div>
                                    <div class="col-xs-2">
                                        <button type="button" class="btn btn-danger btn-block" data-dismiss="modal" onclick="clearFields();">Annulla</button>

                                    </div>
                                </div>

                            </div>

                        </div>
                    </form>
                </div>
            </div>
            <!--FINE MODALE-->


            <%@ include file="VIEW/footer.jsp" %>

            <script>
                $("#messaggioT").val("");
                function cambia() {

                    var s = $("input[name=radioType]:checked").val();
                    $("#inputEmail").css("display", "none");
                    $("#inputRubrica").css("display", "none");

                    if (s == "rubrica") {
                        $("#inputRubrica").css("display", "block");
                        $("#choose").val("Rubrica");
                    } else if (s == 'email') {
                        $("#inputEmail").css("display", "block");
                        $("#choose").val("Email");
                    }
                }

                function clearFields() {
                    $("#messaggioT").val("");
                    $("#oggetto").val("");
                    $("#email").val("");
                    $("#rubrica").val("none");
                }

                function emailValida() {
                    var s = $("input[name=radioType]:checked").val();
                    if (s == 'rubrica')
                        return true;
                    var email = $("#email").val();
                    var email_reg_exp = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-]{2,})+\.)+([a-zA-Z0-9]{2,})+$/;
                    if (!email_reg_exp.test(email)) {
                        $.simplyToast("Inserisci una mail valida", "danger");
                        return false;
                    }
                    return true;
                }

                function rubricaValida() {
                    var s = $("input[name=radioType]:checked").val();
                    if (s == 'email')
                        return true;
                    var rubrica = $("#rubrica").val();
                    if (rubrica == undefined || rubrica == "none") {
                        $.simplyToast("Inserisci un contatto valido", "danger");
                        return false;
                    }
                    return true;
                }

                function oggettoValido() {
                    var oggetto = $("#oggetto").val();
                    if (oggetto.length < 5 || oggetto.length > 25) {
                        $.simplyToast("La lunghezza dell'oggetto deve essere compresa tra i 5 e 25 caratteri", "danger");
                        return false;
                    }
                    return true;
                }

                function messaggioValido() {
                    var mess = $("#messaggioT").val();
                    if (mess.length < 10 || mess.length > 255) {
                        $.simplyToast("La lunghezza del messaggio deve essere compresa tra i 10 e 255 caratteri", "danger");
                        return false;
                    }
                    return true;
                }

                function filtra(page) {
                    var utente = $("#cognome").val();
                    var id = $("#identificatore").val();

                    if (utente == null) {
                        utente = '';
                    }
                    if (id == null) {
                        id = '';
                    }
                    window.location.href = "./messaggi?page=" + page + "&utente=" + utente + "&id=" + id<%if (tipoMess.equals("ricevuti")) {
                            out.println("+\"&messaggi=ricevuti\"");
                        }%>;
                }
                function apriMess(id) {
                    var data = $("#messaggioData" + id).html();
                    var oggetto = $("#messaggioOggetto" + id).html();
                    var testoMess = $("#messaggioBody" + id).html();
                    var utente = $("#messaggioUtente" + id).html();
                    var ident = $("#messaggioId" + id).html();
                    var md = $("#messaggioMD" + id).text();
                    $("#titoloMessaggio").html(utente + " (" + data + ")");
                    $("#testoMessaggio").html(testoMess);
                    $("#leggiMessaggio").modal('show');
                    $("#oggettoMessaggio").html(oggetto);
                    $("#bottoneMessaggio").val(ident);
                    $("#mdMessaggio").val(md);

                }

            </script>

        </div>
    </body>
</html>

