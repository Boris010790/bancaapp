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
            }
        %>
        <div class="container">

            <nav class="navbar navbarBG">

                <div class="navbar-header">
                    <a class="navbar-brand" href="index.jsp" style="color:#FFF;font-size:1.5em;">
                        <span class="glyphicon glyphicon-cloud"></span>&nbsp;MyBank
                    </a>

                </div>
                <span style="float:right; margin-right:20px; margin-top:10px;">
                    <button class="btn btn-primary"  data-toggle="modal" onclick="location.href = './registrazione'">
                        <span class="glyphicon glyphicon-user"></span>
                        &nbsp;Registrati
                    </button>

                </span>

            </nav>



            <div class="row">
                <div class="col-md-6 col-md-offset-3 text-center">
                    <div class="panel panel-primary animated slideInRight">
                        <div class="panel-heading" style="font-weight:bold">Login</div>
                        <div class="panel-body">

                            <div style="padding-top: 30px;">

                                <form action="login" method="POST">

                                    <input style="display:none;" id="gmail" name="gmail" value="">

                                    <div class="form-group">
                                        <label for="exampleInputEmail1">Email</label>
                                        <input id="email" name="email" type="email" class="form-control" placeholder="Email" required/>
                                    </div>
                                    <div class="form-group">
                                        <label>Password</label>
                                        <input id="password" name="password" type="password" class="form-control" placeholder="Password" required>

                                    </div>

                                    <span style="float:left">
                                        <div class="g-signin2" data-onsuccess="onSignIn"></div>
                                    </span>

                                    <span style="float:right">
                                        <button id="bottone" name="tipoLogin" type="submit"  value="standard" class="btn btn-primary" onclick="return controlloLogin();">
                                            <span class="glyphicon glyphicon-pencil"></span>
                                            Accedi
                                        </button>
                                    </span>

                                </form>    
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <form style="display:none" id="hiddenForm" action="./login" method="POST">
                <input name="login" value="gmail"/>
                <input id="token" name="tokenGmail" value=""/>
                <input id="firstname" name="firstname" value=""/>
                <input id="lastname" name="lastname" value=""/>
            </form>


            <%@ include file="VIEW/footer.jsp" %>


        </div>  

        <script>
            function controlloLogin() {

                var email = $("#email").val();
                var password = $("#password").val();



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
            }

            function isEmail(email) {
                var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
                return regex.test(email);
            }

            function isEmptyField(s) {
                return s == null && s.length == 0;
            }

            function onSignIn(googleUser) {
                var profile = googleUser.getBasicProfile();
                var id_token = googleUser.getAuthResponse().id_token;

                console.log(id_token);
                $("#gmail").val(id_token);
                //if(id_token && id_token.length >5)window.location.href = './login?gmail='+id_token+'&firstname='+profile.getName()+'&lastname='+profile.getFamilyName();
                if (id_token && id_token.length > 5) {
                    $("#firstname").val(profile.getName());
                    $("#lastname").val(profile.getFamilyName());
                    $("#token").val(id_token);
                    $("#hiddenForm").submit();
                }

            }

        </script>
    </body>
</html>
