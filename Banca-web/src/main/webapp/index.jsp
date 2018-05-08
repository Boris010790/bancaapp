<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>

<%
    if (session != null) {
        utente.Utente user = (utente.Utente) session.getAttribute("utente");
        if (user != null) {
            response.sendRedirect("home.jsp");
        }
    }
%>
<html lang="en">
    <%@ include file="VIEW/head.jsp" %>
    <body  class="container-fluid noPadding hidden-sm hidden-xs">
        <div class="img img-responsive" style="background-image: url(Utility/Immagini/cover.jpg); background-repeat:no-repeat; background-size: 100%;">
            <nav class="navbar">
                <div class="navbar-header col-md-offset-1 animated slideInRight">
                    <a class="navbar-brand" href="./index.jsp">
                        <h2 class="navbar-text" style="color:#FFF; margin-top:-10px;">
                            <span class="glyphicon glyphicon-cloud"></span>
                            Blue Bank 
                        </h2>
                    </a>
                </div>
                <div class="navbar-collapse collapse animated slideInLeft">
                    <div class="navbar-form navbar-right">
                        <button class="btn btn-default"  data-toggle="modal" onclick="window.location.href = './login'">
                            <span class="glyphicon glyphicon-user"></span>
                            &nbsp;Accedi
                        </button>

                        <button class="btn btn-default" onclick="window.location.href = 'registrazione'">
                            <span class="glyphicon glyphicon-pencil"></span>
                            &nbsp;Registrati
                        </button>
                    </div>
                </div><!--/.navbar-collapse -->
            </nav>

            <hr/>
            <div class="row" >
                <div class="col-xs-12 animated slideInUp">
                    <h1 class="text-center" style="color:#FFF; font-size: 3.5em; font-family: DharmaGothicE-RegularItalic; font-weight: 900;">
                        <br/> GESTISCI LE TUE TRANSAZIONI<br/> IN MODO SICURO E VELOCE</h1>
                    <div>
                    </div>
                </div>
            </div>
            <div class="row" style="padding-top:20%;">  </div>

        </div>
        <h2 class="text-center animated slideInLeft">My Bank: la soluzione semplice e sicura per pagare e farsi pagare.</h2>
        <br/><br/>

        <div class="row text-center jumbotron" style="margin-left:0px; margin-right:0px;"> 

            <h3 style="font-weight:bold; padding-bottom: 50px;"> L'esperienza MyBank in tre passaggi. </h3>


            <div class="row" >
                <div class="col-md-4">
                    <div class="media">
                        <div class="media-left">
                            <p style="border:1px solid silver; border-radius:50%;padding:11px 18px">
                                1
                            </p>
                        </div>
                        <div class="media-body">
                            <h4 class="media-heading text-left">
                                Apri un conto MyBank gratuitamente in pochi click.
                            </h4>

                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <div class="media">
                        <div class="media-left">
                            <p style="border:1px solid silver; border-radius:50%;padding:11px 18px">
                                2
                            </p>
                        </div>
                        <div class="media-body">
                            <h4 class="media-heading text-left">
                                Accedi in modo sicuro al tuo conto bancario.
                            </h4>

                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="media">
                        <div class="media-left">
                            <p style="border:1px solid silver; border-radius:50%;padding:11px 18px">
                                3
                            </p>
                        </div>
                        <div class="media-body">
                            <h4 class="media-heading text-left">
                                Gestisci le tue transazioni con il tuo indirizzo email e la password.
                            </h4>

                        </div>
                    </div>
                </div>   
            </div>   


        </div>

        <div class="jumbotron text-center animated slideInRight" style="background-color:#337ab7; color:#FFF">
            <h2>
                Scopri perch&egrave; 200 milioni di persone usano MyBank nel mondo.
            </h2> 

            <div class="row text-left">
                <div class="col-md-4">
                    <h3 class="text-center">Pi&ugrave; sicuro e protetto</h3>
                    <hr/>
                    <p>
                        Trasferisci denaro e acquista online in tranquillit&agrave;. 
                        Per aiutare a proteggerti dalle frodi online, utilizziamo metodi di crittografia avanzati.
                    </p>
                </div>
                <div class="col-md-4">
                    <h3 class="text-center">Semplice e pratico</h3>
                    <hr/>
                    <p>
                        Con MyBank basta selezionare un utente, digitare la cifra e trasferire denaro con pochi click.
                    </p>
                </div>
                <div class="col-md-4">
                    <h3 class="text-center">Gratuito e sempre trasparente</h3>
                    <hr/>
                    <p>
                        Aprire un conto virtuale con MyBank &egrave; gratuito, cos&igrave; come &egrave; gratuito fare acquisti
                    </p>
                </div>

            </div>

        </div>


        <br/>

        <%@ include file="VIEW/footer.jsp" %>

    </body>

    <body class="hidden-lg hidden-md">
        <nav class="navbar">
            <div class="navbar-header col-md-offset-1 animated slideInRight">
                <a class="navbar-brand" href="./index.jsp">
                    <h2 class="navbar-text" style="color:#FFF; margin-top:-10px;">
                        <span class="glyphicon glyphicon-cloud"></span>
                        Blue Bank 
                    </h2>
                </a>
            </div>
            <div class="navbar-collapse collapse animated slideInLeft">
                <div class="navbar-form navbar-right">
                    <button class="btn btn-default"  data-toggle="modal" onclick="window.location.href = 'login'">
                        <span class="glyphicon glyphicon-user"></span>
                        &nbsp;Accedi
                    </button>

                    <button class="btn btn-default" onclick="window.location.href = 'registrazione'">
                        <span class="glyphicon glyphicon-pencil"></span>
                        &nbsp;Registrati
                    </button>
                </div>
            </div><!--/.navbar-collapse -->
        </nav>
    </body>
</html>
