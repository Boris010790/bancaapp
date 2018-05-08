
<%@page import="java.util.List"%>
<%@page import="utility.Utility"%>
<%
      utente.Utente user = (utente.Utente) session.getAttribute("utente");
     
           String classeNav = "";
           String css="";
              if(user.getTipo().equals("ADMIN")) css = "style='background-color:#5cb85c;'";
              else classeNav = "navbarBG"; 
    %>
    <nav class="navbar <%out.println(classeNav);%>" <%out.println(css);%>>
    <div class="container">
        <div class="navbar-header">

            <%

              

                if (user == null) {
                    out.println("<h1 class='text-center'>Accesso alla pagina non valido<h1>");
                    return;
                }

                List<messaggi.Messaggi> messaggiNonLetti = (List<messaggi.Messaggi>) session.getAttribute("messaggiNonLetti");
                List<messaggi.Messaggi> messaggiRicevutiHome = (List<messaggi.Messaggi>) session.getAttribute("messaggiRicevuti");
                int messNonLetti = 0;
                int messRicevuti = 0;
                if (messaggiNonLetti != null) {
                    messNonLetti = messaggiNonLetti.size();
                }
                if (messaggiRicevutiHome != null) {
                    messRicevuti = messaggiRicevutiHome.size();
                }

            %>
            <a style="cursor:default" class="navbar-brand" onclick="return false;" href="#"> Saldo:
                <%                    out.println(Utility.getFormatImportoView(user.getSaldo()));
                %><span class="glyphicon glyphicon-euro"></span>

            </a>
        </div>
        <div class="navbar-collapse collapse" style="float:right">
            <ul class="nav navbar-nav" style="font-size:1.0em;">
                <li><a class="active"  href="home">Home</a></li>
             
                <li><a class="linea" href="attivita">Attivit&agrave;</a></li>
                <li><a class="linea" href="messaggi">Messaggi <%if (messNonLetti > 0) {
                        out.print("(" + messNonLetti + ")");
                    }%></a></li>
                 <%if(user.getTipo().equals("CLIENTE")) out.println("<li><a class=\"linea\" href=\"rubrica\">Rubrica</a></li>") ;%> 
                <li><a class="linea" href="#" onclick="signOut();">Logout</a></li>
            </ul>

        </div><!--/.nav-collapse -->
    </div>
</nav>


<script>
    if (gapi != undefined) {
        gapi.load('auth2', function () {
            gapi.auth2.init();

        });
    }
    function onLoad() {
        gapi.load('auth2', function () {
            gapi.auth2.init();
        });
    }

    function signOut() {
        try {
            if (gapi == undefined)
                return;
            var auth2 = gapi.auth2.getAuthInstance();
            if (auth2 == undefined) {
                window.location.href = './home?page=logout';
            }
            auth2.signOut().then(function () {
                console.log('User signed out.');
            });
        } catch (exc) {

        }
        window.location.href = './home?page=logout';

    }
</script>
