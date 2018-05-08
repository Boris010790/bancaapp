<%@page import="java.util.List"%>
<%@page import="utility.Utility"%>
<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>


<html>
    <%@ include file="VIEW/head.jsp" %>



    <body class="body">

        <%
            String messaggio = (String) request.getAttribute("messaggio");
            System.out.println(messaggio + " Mess");
            if (messaggio != null && messaggio.length() > 0) {
                out.print("<script> $.simplyToast('" + messaggio + "','danger'); </script>");
                request.setAttribute("messaggio", null);
            }

            List<utente.Utente> rubrica = null;
            System.out.println("SERVLET " + session.getAttribute("rubrica"));
            if (session.getAttribute("rubrica") != null) {
                rubrica = (List<utente.Utente>) session.getAttribute("rubrica");
            }
        %>

        <div class="container">
            <%@ include file="VIEW/navbar.jsp" %>

            <div id="inviaSection" class="animated bounceInLeft">
                <form id="status" method="POST" enctype="application/x-www-form-urlencoded" action="Transfer" onchange="cambia();">  
                    <div class="panel">
                        <input style="display:none;" id="choose" name="choose" value="Email"/>

                        <div class="panel-body">
                            <div>
                                <fieldset class="text-center">
                                    
                                        <legend style=\"line-height: 3em;"><%if(user.getTipo().equals("ADMIN"))out.println("SEZIONE RICARICHE"); else out.println("SEZIONE PAGAMENTI");%></legend>


                                    <h4>
                                        <span>Rubrica &nbsp;<input  type="radio" name="radioType" value="rubrica"/></span>
                                        <span style="margin-left:5%;">Email &nbsp;<input type="radio" name="radioType" value="email" checked/></span>
                                    </h4>

                                </fieldset>
                            </div>
                            <div class="row">
                                <div class="col-xs-6 col-xs-offset-3">

                                    <span id="inputEmail" class="animated bounceIn" style="diplay:block;">
                                        <input id="email" name="emailInput" style="margin-top:10%;" class="form-control input-lg" placeholder="Digita un indirizzo email"/>
                                        <div class="checkbox text-center" <%if(user.getTipo().equals("ADMIN")) out.println("style=\"display:none;\";");%>>
                                            <label>
                                                <input name="checkbox" type="checkbox"/> Aggiungi contatto in rubrica
                                            </label>
                                        </div>
                                    </span>
                                    <span id="inputRubrica" class="animated bounceIn" style="display:none;">
                                        <div class="form-group">
                                            <select id="rubrica" name="RubricaSelect" style="margin-top:10%;" class="form-control text-center input-lg" onchange="cambia();">
                                                <option value="none"> - </option>
                                                <%                                                    for (utente.Utente u : rubrica) {
                                                        out.println("<option value='" + u.getId() + "'>" + Utility.capitalize(u.getNome() + " " + u.getCognome()) + "</option>");
                                                    }
                                                %>

                                            </select>
                                        </div>
                                    </span>
                                </div>
                            </div><!-- /.row -->
                        </div>
                    </div>


                    <div class="panel">
                        <div class="panel-body">
                            <div>
                                <fieldset class="text-center">
                                    <legend style="line-height: 3em;">Importo e Causale</legend>


                                </fieldset>
                            </div>
                            <div class="row">
                                <div class="col-xs-6">

                                    <span id="inputEmail" class="animated bounceIn" style="diplay:block;">
                                        <input id="causale"  name="causale" class="form-control input-lg" placeholder="Causale"/>
                                        <small>Motivo del pagamento. Max 25 caratteri</small>
                                    </span>               
                                </div>

                                <div class="col-xs-6">

                                    <span id="inputEmail" class="animated bounceIn" style="diplay:block;">
                                        <input id="importoPag"  name="importo" class="form-control input-lg" placeholder="Digita un importo"/>
                                        <small>Utilizza il carattere <bold>.</bold> per separare i centesimi</small>
                                    </span>               
                                </div>
                            </div><!-- /.row -->

                            <span class="col-xs-offset-4 col-xs-4" style="margin-top:5%;"><button onclick="return (importoValido('importoPag') && emailValida() && rubricaValida() && causaleValida());" type="submit" class="btn btn-bord btn-block btn-danger btn-lg">CONFERMA</button></span> 
                        </div>

                    </div>
                </form>
            </div>


            <%@ include file="VIEW/footer.jsp" %>

            <script>
                function cambia() {

                    var s = $("input[name=radioType]:checked").val();
                    $("#inputEmail").css("display", "none");
                    $("#inputRubrica").css("display", "none");

                    if (s == 'rubrica') {
                        $("#inputRubrica").css("display", "block");
                        $("#choose").val("Rubrica");
                    } else if (s == 'email') {
                        $("#inputEmail").css("display", "block");
                        $("#choose").val("Email");
                    }
                }



                function importoValido(s) {
                    var importo;
                    if (s == "importoPag")
                        importo = $("#importoPag").val();
                    else if (s == "importoDep")
                        importo = $("#importoDep").val();

                    if (isNaN(importo) || importo.length == 0 || importo == undefined) {
                        $.simplyToast("Inserisci un importo valido", "danger");
                        return false;
                    }
                    return true;
                }

                function causaleValida() {
                    var causale = $("#causale").val();
                    if (causale == undefined || causale.length < 5 || causale.length > 25) {
                        $.simplyToast("Inserisci una causale compresa tra 5 e 25 caratteri", "danger");
                        return false;
                    }
                    return true;
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
            </script>
        </div>
    </body>
</html>
