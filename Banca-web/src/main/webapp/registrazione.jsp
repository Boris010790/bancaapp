<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <%@ include file="VIEW/head.jsp" %>


    <body class="body">
        <%
         String messaggio =(String) request.getAttribute("messaggio");
         if(messaggio!=null && messaggio.length() > 0)  out.print("<script> $.simplyToast('"+messaggio+"','danger'); </script>");
        %>
        <div class="container">
            <nav class="navbar navbarBG  animated slideInLeft">
                <div class="navbar-header">
                    <a class="navbar-brand" href="./index.jsp" style="color:#FFF; font-size:1.5em;">
                        <span class="glyphicon glyphicon-cloud"></span>&nbsp;MyBank
                    </a>

                </div>
                <span style="float:right; margin-right:20px; margin-top:10px;">
                    <button class="btn btn-primary"  onclick="location.href = 'login'">
                        <span class="glyphicon glyphicon-user"></span>
                        &nbsp;Login
                    </button>
                </span>  
            </nav>



            <div class="row">
                <div class="col-md-6 col-md-offset-3 text-center">
                    <div class="panel panel-primary animated slideInRight">
                        <div class="panel-heading" style="font-weight:bold">Registrazione</div>
                        <div class="panel-body">

                            <div style="padding-top: 30px;">

                                <form action="registrazione" method="POST">
                                    <div class="form-group">
                                        <label for="exampleInputEmail1">Nome</label>
                                        <input id="nome" name="nome" type="text" class="form-control" placeholder="Nome" required/>                                </div>
                                    <div class="form-group">
                                        <label for="exampleInputEmail1">Cognome</label>
                                        <input id="cognome" name="cognome" type="text" class="form-control" placeholder="Cognome" required/>
                                    </div>
                                    <div class="form-group">
                                        <label for="exampleInputEmail1">Email</label>
                                        <input id="email" name="email" type="email" class="form-control" placeholder="Email" required/>
                                    </div>
                                    <div class="form-group">
                                        <label>Password</label>
                                        <input id="password" name="password" type="password" class="form-control" placeholder="Password" required>

                                    </div>
                                    <div class="form-group">
                                        <label>Conferma Password</label>
                                        <input id="conferma" name="conferma" type="password" class="form-control" placeholder="Conferma Password" required>

                                    </div>

                                    <div style="float:right">
                                        <button id="bottone" name="tipoLogin" type="submit"  value="standard" class="btn btn-primary" onclick="return controlloRegistrazione();">
                                            <span class="glyphicon glyphicon-pencil"></span>
                                            Registrati
                                        </button>
                                    </div>

                                </form>    
                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <%@ include file="VIEW/footer.jsp" %>


        </div>  

        <script>
            function controlloRegistrazione() {
                var nome = $("#nome").val();
                var cognome = $("#cognome").val();
                var email = $("#email").val();
                var password = $("#password").val();
                var conferma = $("#conferma").val();
                var bottone = $("#bottone").val();

                if (isEmptyField(nome)) {
                    $.simplyToast("Inserisci il nome", "danger");
                    return false;
                }
                if (isEmptyField(cognome)) {
                    $.simplyToast("Inserisci il cognome", "danger");
                    return false;
                }
                if (!isEmail(email)) {
                    $.simplyToast("Inserisci un'email valida", "danger");
                    return false;
                }
                if (isEmptyField(password)) {
                    $.simplyToast("Inserisci la password", "danger");
                    return false;
                }

                if (password.length < 6) {
                    $.simplyToast("La password deve essere almeno di 6 caratteri", "danger");
                    return false;
                }

                if (isEmptyField(conferma)) {
                    $.simplyToast("[Conferma Password] Reinserisci la password", "danger");
                    return false;
                }

                if (password != conferma) {
                    $.simplyToast("Le password non corrispondono", "danger");
                    return false;
                }

                return true;

            }

            function isEmptyField(field) {
                return  field == undefined || field.length == 0;
            }

            function isEmail(email) {
                var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
                return regex.test(email);
            }
        </script>
    </body>
</html>
