
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Consola de Mensajes para Android</title>

    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/jumbotron-narrow.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <div class="container">
      <div class="header">
        <nav>
          <ul class="nav nav-pills pull-right">
          </ul>
        </nav>
        <h3 class="text-muted">GCM CONSOLE</h3>
      </div>

      <div class="row">
    <div class="col-sm-12">
        <h2>Envio de Mensajes</h2>
    </div>
</div>
<div class="row">
    <div class="col-xs-12">
        
        <form>
            <div class="form-group">
                <label for="inputAplicacion" class="control-label">Aplicacion</label>
                    <select class="form-control input-lg" name="selAplicacion" id="selAPlicacion">
                        <option value="1">Almashopping</option>
                    </select>
            </div>
            <div class="form-group">
                <label for="inputTitulo" class="control-label">Titulo</label>
                    <input type="text" class="form-control input-lg" id="inputTitulo" placeholder="Titulo">
            </div>
            <div class="form-group">
                <label for="inputMensaje" class="control-label">Mensaje</label>
                    <textarea rows="6" class="form-control input-lg" name="inputMensaje" id="inputMensaje" cols="30" placeholder="Mensaje a enviar a dispositivos"></textarea>
            </div>

            <div class="form-group">
                <div >
                    <button type="button" id="btnEnviar" class="btn btn-primary">Enviar Notificacion</button>
                </div>
            </div>
        </form>
    </div>
</div>
        <p>&nbsp;

      <footer class="footer">
        <p>&copy; ENNOVVA 2015</p>
      </footer>

    </div> <!-- /container -->


    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="js/ie10-viewport-bug-workaround.js"></script>
    <script>
    var valida ;
    var aplicaciones;


    function EnviarMensaje(titulo,mensaje,aplicacion)
    {
        var app = parseInt(aplicacion);
        $.ajax({
            type: 'POST',
            url: 'v1/index.php/gcm/sendByApp',
            data:{idAplicacion:app,titulo:titulo,mensaje:mensaje},
            success: function (msg) {
                alert(msg);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    }

    function ValidarEnvio(titulo, mensaje) {
        
        if (titulo.length < 10 && mensaje.length < 10) {
            alert("EL titulo y el mensaje son requeridos");
            return false;
        }
        else
            return true;
    }
    $(document).on("click", "#btnEnviar", function () {
        var titulo = $("#inputTitulo").val();
        var mensaje = $("#inputMensaje").val();
        var app = $("#selAPlicacion").val();
        valida = true;
        valida = ValidarEnvio(titulo,mensaje);
        if (valida == true)
            EnviarMensaje(titulo, mensaje,app);

    })
</script>

  </body>
</html>
